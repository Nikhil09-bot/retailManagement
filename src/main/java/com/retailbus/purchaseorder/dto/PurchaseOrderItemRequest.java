package com.retailbus.purchaseorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * PurchaseOrderItemRequest DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderItemRequest {
    private Long productId;
    private Integer quantityOrdered;
    private BigDecimal unitPrice;
}

