package com.retailbus.product.dto.response;

import com.retailbus.product.enums.Category;
import com.retailbus.product.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product Response DTO.
 * Used for returning product data in API responses.
 * Never exposes sensitive internal data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String productCode;

    private String barcode;

    private String name;

    private String description;

    private String brand;

    private Category category;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    private Integer quantityInStock;

    private Integer reorderLevel;

    private Unit unit;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

