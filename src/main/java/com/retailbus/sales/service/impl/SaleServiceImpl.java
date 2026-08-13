package com.retailbus.sales.service.impl;

import com.retailbus.inventory.enums.InventoryTransactionType;
import com.retailbus.inventory.exception.InventoryException;
import com.retailbus.inventory.service.InventoryService;
import com.retailbus.product.entity.Product;
import com.retailbus.product.exception.ProductNotFoundException;
import com.retailbus.product.repository.ProductRepository;
import com.retailbus.sales.dto.CreateSaleRequest;
import com.retailbus.sales.dto.SaleItemRequest;
import com.retailbus.sales.dto.SaleResponse;
import com.retailbus.sales.entity.Sale;
import com.retailbus.sales.entity.SaleItem;
import com.retailbus.sales.enums.SaleStatus;
import com.retailbus.sales.exception.InvalidSaleOperationException;
import com.retailbus.sales.exception.SaleNotFoundException;
import com.retailbus.sales.mapper.SaleMapper;
import com.retailbus.sales.repository.SaleItemRepository;
import com.retailbus.sales.repository.SaleRepository;
import com.retailbus.sales.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SaleService Implementation.
 * Handles POS transactions, inventory deduction, and sale cancellation.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final SaleMapper saleMapper;

    @Override
    public SaleResponse createSale(CreateSaleRequest request) {
        log.info("Creating new sale with {} items", request.getItems().size());

        // Build items, validate stock and compute subtotal
        BigDecimal subtotal = BigDecimal.ZERO;
        List<SaleItemData> itemDataList = new ArrayList<>();

        for (SaleItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product not found with ID: " + itemReq.getProductId()));

            if (!product.getActive()) {
                throw new InvalidSaleOperationException(
                        "Product is not active: " + product.getName());
            }

            if (product.getQuantityInStock() < itemReq.getQuantity()) {
                throw new InventoryException(
                        "Insufficient stock for product: " + product.getName() +
                        ". Available: " + product.getQuantityInStock() +
                        ", Requested: " + itemReq.getQuantity());
            }

            BigDecimal lineTotal = product.getSellingPrice()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            subtotal = subtotal.add(lineTotal);
            itemDataList.add(new SaleItemData(product, itemReq.getQuantity(), product.getSellingPrice(), lineTotal));
        }

        BigDecimal discountAmount = request.getDiscountAmount() != null ? request.getDiscountAmount() : BigDecimal.ZERO;
        BigDecimal discountedSubtotal = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = discountedSubtotal.multiply(request.getTaxRate()).setScale(2, RoundingMode.HALF_UP);
        BigDecimal grandTotal = discountedSubtotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        String receiptNumber = generateReceiptNumber();

        Sale sale = Sale.builder()
                .receiptNumber(receiptNumber)
                .status(SaleStatus.COMPLETED)
                .subtotal(subtotal.setScale(2, RoundingMode.HALF_UP))
                .taxAmount(taxAmount)
                .discountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP))
                .grandTotal(grandTotal)
                .paymentMethod(request.getPaymentMethod())
                .notes(request.getNotes())
                .build();

        Sale savedSale = saleRepository.save(sale);

        // Save items and deduct inventory
        List<SaleItem> saleItems = new ArrayList<>();
        for (SaleItemData data : itemDataList) {
            SaleItem saleItem = SaleItem.builder()
                    .sale(savedSale)
                    .product(data.product)
                    .quantity(data.quantity)
                    .unitPrice(data.unitPrice)
                    .lineTotal(data.lineTotal)
                    .build();
            saleItems.add(saleItem);

            // Deduct inventory — use negative quantity change for stock-out
            inventoryService.recordTransaction(
                    data.product.getId(),
                    InventoryTransactionType.STOCK_OUT,
                    -data.quantity,
                    null,
                    "Sold - Receipt: " + receiptNumber
            );
        }

        saleItemRepository.saveAll(saleItems);
        savedSale.setItems(saleItems);

        log.info("Sale created with receipt number: {}. Grand total: ${}", receiptNumber, grandTotal);
        return saleMapper.toResponse(savedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getAllSales() {
        log.info("Fetching all sales");
        return saleRepository.findAll().stream()
                .map(saleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getSaleById(Long id) {
        log.info("Fetching sale with ID: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with ID: " + id));
        return saleMapper.toResponse(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public SaleResponse getSaleByReceiptNumber(String receiptNumber) {
        log.info("Fetching sale with receipt number: {}", receiptNumber);
        Sale sale = saleRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with receipt number: " + receiptNumber));
        return saleMapper.toResponse(sale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getTodaysSales() {
        log.info("Fetching today's sales");
        return saleRepository.findTodaysSales().stream()
                .map(saleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SaleResponse> getSalesByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching sales between {} and {}", start, end);
        return saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end).stream()
                .map(saleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SaleResponse cancelSale(Long id) {
        log.info("Cancelling sale with ID: {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException("Sale not found with ID: " + id));

        if (sale.getStatus() == SaleStatus.CANCELLED) {
            throw new InvalidSaleOperationException("Sale is already cancelled. Receipt: " + sale.getReceiptNumber());
        }

        // Restore inventory for each item
        for (SaleItem item : sale.getItems()) {
            inventoryService.recordTransaction(
                    item.getProduct().getId(),
                    InventoryTransactionType.CANCELLED_SALE,
                    item.getQuantity(),
                    null,
                    "Sale cancelled - Receipt: " + sale.getReceiptNumber()
            );
        }

        sale.setStatus(SaleStatus.CANCELLED);
        Sale savedSale = saleRepository.save(sale);

        log.info("Sale cancelled. Receipt: {}. Inventory restored for {} items.",
                sale.getReceiptNumber(), sale.getItems().size());
        return saleMapper.toResponse(savedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTodaysRevenue() {
        log.info("Calculating today's revenue");
        return saleRepository.findTodaysSales().stream()
                .filter(sale -> sale.getStatus() == SaleStatus.COMPLETED)
                .map(Sale::getGrandTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getMonthlyRevenue(int month, int year) {
        log.info("Calculating revenue for {}/{}", month, year);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end).stream()
                .filter(sale -> sale.getStatus() == SaleStatus.COMPLETED)
                .map(Sale::getGrandTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTodaysSalesCount() {
        log.info("Counting today's sales");
        return (long) saleRepository.findTodaysSales().stream()
                .filter(s -> s.getStatus() == SaleStatus.COMPLETED)
                .count();
    }

    // ── Private Helpers ────────────────────────────────────────────────────────

    private String generateReceiptNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return "REC-" + datePart;
    }

    /** Inner helper record to carry computed item data. */
    private record SaleItemData(Product product, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {}
}

