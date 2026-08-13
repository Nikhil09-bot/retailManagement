package com.retailbus.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * TopSellingProduct DTO.
 * Represents a product in a top-sellers report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopSellingProduct {

    private Long productId;
    private String productCode;
    private String productName;
    private String brand;
    private String category;
    private Integer totalQuantitySold;
    private BigDecimal totalRevenue;
}

