package com.retailbus.dashboard.service;

import com.retailbus.customer.repository.CustomerRepository;
import com.retailbus.dashboard.dto.DashboardStats;
import com.retailbus.inventory.service.InventoryService;
import com.retailbus.product.repository.ProductRepository;
import com.retailbus.purchaseorder.enums.PurchaseOrderStatus;
import com.retailbus.purchaseorder.repository.PurchaseOrderRepository;
import com.retailbus.sales.enums.SaleStatus;
import com.retailbus.sales.repository.SaleRepository;
import com.retailbus.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * DashboardService.
 * Aggregates statistics from all modules for the store dashboard.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DashboardService {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final SaleRepository saleRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    /**
     * Get all dashboard statistics in a single response.
     */
    public DashboardStats getDashboardStats() {
        log.info("Building dashboard statistics");

        // Today's date range
        LocalDateTime now = LocalDateTime.now();
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime monthStart = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime monthEnd = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        // Sales counts
        long todaysSalesCount = saleRepository.findTodaysSales().stream()
                .filter(s -> s.getStatus() == SaleStatus.COMPLETED).count();

        BigDecimal todaysRevenue = saleRepository.findTodaysSales().stream()
                .filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                .map(s -> s.getGrandTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long monthSalesCount = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(monthStart, monthEnd)
                .stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED).count();

        BigDecimal monthRevenue = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(monthStart, monthEnd)
                .stream().filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                .map(s -> s.getGrandTotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingPOs = purchaseOrderRepository.findByStatus(PurchaseOrderStatus.PENDING).size();

        return DashboardStats.builder()
                .totalProducts(productRepository.count())
                .activeProducts((long) productRepository.findByActiveTrue().size())
                .lowStockCount(inventoryService.getLowStockCount())
                .outOfStockCount(inventoryService.getOutOfStockCount())
                .totalSuppliers(supplierRepository.count())
                .activeSuppliers((long) supplierRepository.findByActiveTrue().size())
                .totalCustomers(customerRepository.count())
                .todaysSalesCount(todaysSalesCount)
                .todaysRevenue(todaysRevenue)
                .currentMonthSalesCount(monthSalesCount)
                .currentMonthRevenue(monthRevenue)
                .pendingPurchaseOrders(pendingPOs)
                .build();
    }
}

