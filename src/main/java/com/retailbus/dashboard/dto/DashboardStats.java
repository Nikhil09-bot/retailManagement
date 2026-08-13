package com.retailbus.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DashboardStats DTO.
 * Aggregated statistics for the store dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStats {

    // Products
    private Long totalProducts;
    private Long activeProducts;
    private Long lowStockCount;
    private Long outOfStockCount;

    // Suppliers
    private Long totalSuppliers;
    private Long activeSuppliers;

    // Customers
    private Long totalCustomers;

    // Sales
    private Long todaysSalesCount;
    private BigDecimal todaysRevenue;
    private Long currentMonthSalesCount;
    private BigDecimal currentMonthRevenue;

    // Purchase Orders
    private Long pendingPurchaseOrders;
}

