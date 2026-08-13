package com.retailbus.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Low Stock Product Response DTO.
 * Shows products that are at or below reorder level.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LowStockProductResponse {

    private Long productId;

    private String productCode;

    private String productName;

    private String brand;

    private String category;

    private Integer quantityInStock;

    private Integer reorderLevel;

    private Integer quantityToReorder;
}

