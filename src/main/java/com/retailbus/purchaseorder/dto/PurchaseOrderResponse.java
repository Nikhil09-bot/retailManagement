package com.retailbus.purchaseorder.dto;

import com.retailbus.purchaseorder.enums.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PurchaseOrderResponse DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderResponse {

    private Long id;
    private String poNumber;
    private Long supplierId;
    private String supplierName;
    private PurchaseOrderStatus status;
    private BigDecimal totalAmount;
    private BigDecimal receivedAmount;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime receivedDate;
    private String notes;
    private List<PurchaseOrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

