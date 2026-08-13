package com.retailbus.supplier.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Supplier Response DTO.
 * Used for returning supplier data in API responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {

    private Long id;

    private String supplierCode;

    private String name;

    private String contactPerson;

    private String phone;

    private String email;

    private String address;

    private String city;

    private String state;

    private String zipcode;

    private String country;

    private String paymentTerms;

    private String taxId;

    private String notes;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

