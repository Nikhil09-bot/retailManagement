package com.retailbus.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Inventory History Response DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryHistoryResponse {

    private Long id;

    private Long productId;

    private String productCode;

    private String productName;

    private String transactionType;

    private Integer quantityChanged;

    private Integer quantityBefore;

    private Integer quantityAfter;

    private String reference;

    private String notes;

    private LocalDateTime createdAt;
}

