package com.retailbus.inventory.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Stock Adjustment Request DTO.
 * Used for manual inventory adjustments (damage, loss, correction).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAdjustmentRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Adjustment quantity is required")
    private Integer adjustmentQuantity;  // Can be positive or negative

    @NotBlank(message = "Reason is required")
    private String reason;  // e.g., "Damage", "Correction", "Loss", etc.

    private String notes;
}

