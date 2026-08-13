package com.retailbus.sales.dto;

import com.retailbus.sales.enums.SaleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * SaleResponse DTO.
 * Represents a completed sale/transaction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleResponse {

    private Long id;
    private String receiptNumber;
    private SaleStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal grandTotal;
    private String paymentMethod;
    private String notes;
    private List<SaleItemResponse> items;
    private LocalDateTime createdAt;
}

