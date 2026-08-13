package com.retailbus.reports.service;

import com.retailbus.inventory.service.InventoryService;
import com.retailbus.product.dto.response.ProductResponse;
import com.retailbus.product.mapper.ProductMapper;
import com.retailbus.product.repository.ProductRepository;
import com.retailbus.reports.dto.SalesReport;
import com.retailbus.reports.dto.TopSellingProduct;
import com.retailbus.sales.entity.Sale;
import com.retailbus.sales.entity.SaleItem;
import com.retailbus.sales.enums.SaleStatus;
import com.retailbus.sales.repository.SaleItemRepository;
import com.retailbus.sales.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ReportService.
 * Generates various business reports.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final ProductMapper productMapper;

    /**
     * Generate a daily sales report for a specific date.
     */
    public SalesReport getDailySalesReport(LocalDate date) {
        log.info("Generating daily sales report for {}", date);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        List<Sale> sales = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
        return buildSalesReport(sales, "Daily", date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                start, end);
    }

    /**
     * Generate a weekly sales report (Mon–Sun of the week containing the given date).
     */
    public SalesReport getWeeklySalesReport(LocalDate weekStartDate) {
        log.info("Generating weekly sales report starting {}", weekStartDate);
        // Normalise to the Monday of that week
        LocalDate monday = weekStartDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate sunday = monday.plusDays(6);

        LocalDateTime start = monday.atStartOfDay();
        LocalDateTime end = sunday.atTime(23, 59, 59);

        String label = "Week of " + monday.format(DateTimeFormatter.ofPattern("MMM dd")) +
                " – " + sunday.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));

        List<Sale> sales = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
        return buildSalesReport(sales, "Weekly", label, start, end);
    }

    /**
     * Generate a monthly sales report.
     */
    public SalesReport getMonthlySalesReport(int month, int year) {
        log.info("Generating monthly sales report for {}/{}", month, year);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        String label = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));

        List<Sale> sales = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
        return buildSalesReport(sales, "Monthly", label, start, end);
    }

    /**
     * Get top-selling products in a date range.
     */
    public List<TopSellingProduct> getTopSellingProducts(LocalDateTime start, LocalDateTime end, int limit) {
        log.info("Getting top {} selling products from {} to {}", limit, start, end);

        List<Sale> sales = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end)
                .stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                .collect(Collectors.toList());

        // Aggregate by product
        Map<Long, TopSellingProduct> productMap = new HashMap<>();
        for (Sale sale : sales) {
            if (sale.getItems() == null) continue;
            for (SaleItem item : sale.getItems()) {
                Long productId = item.getProduct().getId();
                TopSellingProduct tsp = productMap.getOrDefault(productId, TopSellingProduct.builder()
                        .productId(productId)
                        .productCode(item.getProduct().getProductCode())
                        .productName(item.getProduct().getName())
                        .brand(item.getProduct().getBrand())
                        .category(item.getProduct().getCategory().name())
                        .totalQuantitySold(0)
                        .totalRevenue(BigDecimal.ZERO)
                        .build());

                tsp.setTotalQuantitySold(tsp.getTotalQuantitySold() + item.getQuantity());
                tsp.setTotalRevenue(tsp.getTotalRevenue().add(item.getLineTotal()));
                productMap.put(productId, tsp);
            }
        }

        return productMap.values().stream()
                .sorted(Comparator.comparingInt(TopSellingProduct::getTotalQuantitySold).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get current inventory report.
     */
    public Map<String, Object> getInventoryReport() {
        log.info("Generating inventory report");

        Map<String, Object> report = new HashMap<>();
        report.put("totalProducts", productRepository.count());
        report.put("activeProducts", productRepository.findByActiveTrue().size());
        report.put("lowStockCount", inventoryService.getLowStockCount());
        report.put("outOfStockCount", inventoryService.getOutOfStockCount());
        report.put("lowStockProducts", inventoryService.getLowStockProducts());
        report.put("outOfStockProducts", productRepository.findByActiveTrue().stream()
                .filter(p -> p.getQuantityInStock() == 0)
                .map(productMapper::toResponse)
                .collect(Collectors.toList()));
        return report;
    }

    /**
     * Get revenue trend — monthly breakdown for the last N months.
     */
    public List<Map<String, Object>> getRevenueTrend(int months) {
        log.info("Generating revenue trend for last {} months", months);
        List<Map<String, Object>> trend = new ArrayList<>();

        for (int i = months - 1; i >= 0; i--) {
            YearMonth ym = YearMonth.now().minusMonths(i);
            LocalDateTime start = ym.atDay(1).atStartOfDay();
            LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

            BigDecimal revenue = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end)
                    .stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                    .map(Sale::getGrandTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long count = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end)
                    .stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED).count();

            Map<String, Object> entry = new HashMap<>();
            entry.put("month", ym.format(DateTimeFormatter.ofPattern("MMM yyyy")));
            entry.put("revenue", revenue);
            entry.put("salesCount", count);
            trend.add(entry);
        }
        return trend;
    }

    // ── Private Helpers ────────────────────────────────────────────────────────

    private SalesReport buildSalesReport(List<Sale> sales, String period, String label,
                                          LocalDateTime start, LocalDateTime end) {
        long total = sales.size();
        long completed = sales.stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED).count();
        long cancelled = sales.stream().filter(s -> s.getStatus() == SaleStatus.CANCELLED).count();

        BigDecimal revenue = sales.stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                .map(Sale::getGrandTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal tax = sales.stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                .map(Sale::getTaxAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discount = sales.stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                .map(Sale::getDiscountAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal netRevenue = revenue.subtract(tax);

        BigDecimal avg = completed > 0
                ? revenue.divide(BigDecimal.valueOf(completed), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        List<TopSellingProduct> topProducts = getTopSellingProducts(start, end, 10);
        List<Map<String, Object>> topProductMaps = topProducts.stream().map(tp -> {
            Map<String, Object> m = new HashMap<>();
            m.put("productId", tp.getProductId());
            m.put("productName", tp.getProductName());
            m.put("brand", tp.getBrand());
            m.put("totalQuantitySold", tp.getTotalQuantitySold());
            m.put("totalRevenue", tp.getTotalRevenue());
            return m;
        }).collect(Collectors.toList());

        return SalesReport.builder()
                .period(period)
                .periodLabel(label)
                .totalTransactions(total)
                .completedTransactions(completed)
                .cancelledTransactions(cancelled)
                .totalRevenue(revenue)
                .totalTax(tax)
                .totalDiscount(discount)
                .netRevenue(netRevenue)
                .averageTransactionValue(avg)
                .topSellingProducts(topProductMaps)
                .build();
    }
}

