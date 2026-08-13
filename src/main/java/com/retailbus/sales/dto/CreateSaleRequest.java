package com.retailbus.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * CreateSaleRequest DTO.
 * Used when creating a new sale/transaction at the POS.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSaleRequest {

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<SaleItemRequest> items;

    /**
     * Tax rate as a decimal. e.g. 0.08 means 8% tax.
     */
    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.0", message = "Tax rate cannot be negative")
    private BigDecimal taxRate;

    /**
     * Flat discount amount in dollars.
     */
    @DecimalMin(value = "0.0", message = "Discount cannot be negative")
    private BigDecimal discountAmount;

    private String paymentMethod;

    private String notes;
}

