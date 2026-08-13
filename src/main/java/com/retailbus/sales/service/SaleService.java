package com.retailbus.sales.service;

import com.retailbus.sales.dto.CreateSaleRequest;
import com.retailbus.sales.dto.SaleResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * SaleService Interface.
 */
public interface SaleService {

    /**
     * Create a new sale. Deducts inventory automatically.
     */
    SaleResponse createSale(CreateSaleRequest request);

    List<SaleResponse> getAllSales();

    SaleResponse getSaleById(Long id);

    SaleResponse getSaleByReceiptNumber(String receiptNumber);

    List<SaleResponse> getTodaysSales();

    List<SaleResponse> getSalesByDateRange(LocalDateTime start, LocalDateTime end);

    /**
     * Cancel a sale. Restores inventory automatically.
     */
    SaleResponse cancelSale(Long id);

    /**
     * Get today's total revenue (for dashboard).
     */
    BigDecimal getTodaysRevenue();

    /**
     * Get monthly revenue (for dashboard/reports).
     */
    BigDecimal getMonthlyRevenue(int month, int year);

    Long getTodaysSalesCount();
}

