package com.retailbus.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * SaleItemResponse DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItemResponse {

    private Long id;
    private Long productId;
    private String productCode;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}

