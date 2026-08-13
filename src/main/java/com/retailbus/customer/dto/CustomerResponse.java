package com.retailbus.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CustomerResponse DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private BigDecimal loyaltyPoints;
    private BigDecimal totalPurchases;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

