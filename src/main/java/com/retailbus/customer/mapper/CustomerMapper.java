package com.retailbus.customer.mapper;

import com.retailbus.customer.dto.CustomerRequest;
import com.retailbus.customer.dto.CustomerResponse;
import com.retailbus.customer.entity.Customer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * CustomerMapper.
 * Converts between Customer entity and DTOs.
 */
@Component
public class CustomerMapper {

    /**
     * Convert CustomerRequest to Customer entity.
     */
    public Customer toEntity(CustomerRequest request) {
        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .notes(request.getNotes())
                .loyaltyPoints(BigDecimal.ZERO)
                .totalPurchases(BigDecimal.ZERO)
                .build();
    }

    /**
     * Convert Customer entity to CustomerResponse DTO.
     */
    public CustomerResponse toResponse(Customer customer) {
        String fullName = customer.getFirstName() +
                (customer.getLastName() != null ? " " + customer.getLastName() : "");

        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .fullName(fullName)
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .city(customer.getCity())
                .loyaltyPoints(customer.getLoyaltyPoints())
                .totalPurchases(customer.getTotalPurchases())
                .notes(customer.getNotes())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    /**
     * Update existing Customer entity with new request data.
     */
    public Customer updateEntity(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setCity(request.getCity());
        customer.setNotes(request.getNotes());
        return customer;
    }
}

