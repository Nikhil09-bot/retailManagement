package com.retailbus.supplier.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Supplier Request DTO.
 * Used for creating and updating suppliers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierRequest {

    @NotBlank(message = "Supplier code is required")
    @Size(max = 50, message = "Supplier code must not exceed 50 characters")
    private String supplierCode;

    @NotBlank(message = "Supplier name is required")
    @Size(min = 3, max = 200, message = "Supplier name must be between 3 and 200 characters")
    private String name;

    @Size(max = 100, message = "Contact person name must not exceed 100 characters")
    private String contactPerson;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 1000, message = "Address must not exceed 1000 characters")
    private String address;

    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;

    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;

    @Size(max = 20, message = "Zipcode must not exceed 20 characters")
    private String zipcode;

    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;

    @Size(max = 100, message = "Payment terms must not exceed 100 characters")
    private String paymentTerms;

    @Size(max = 50, message = "Tax ID must not exceed 50 characters")
    private String taxId;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;
}

