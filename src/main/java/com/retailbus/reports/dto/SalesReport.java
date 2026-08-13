package com.retailbus.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * SalesReport DTO.
 * Contains aggregated sales data for a reporting period.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReport {

    private String period;          // "Daily", "Weekly", "Monthly"
    private String periodLabel;     // "2026-07-13", "Week 28", "July 2026"

    private Long totalTransactions;
    private Long completedTransactions;
    private Long cancelledTransactions;

    private BigDecimal totalRevenue;
    private BigDecimal totalTax;
    private BigDecimal totalDiscount;
    private BigDecimal netRevenue;

    private BigDecimal averageTransactionValue;

    private List<Map<String, Object>> topSellingProducts;
    private List<Map<String, Object>> salesByDay;
}

