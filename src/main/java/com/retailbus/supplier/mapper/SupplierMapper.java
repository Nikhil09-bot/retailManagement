package com.retailbus.supplier.mapper;

import com.retailbus.supplier.dto.request.SupplierRequest;
import com.retailbus.supplier.dto.response.SupplierResponse;
import com.retailbus.supplier.entity.Supplier;
import org.springframework.stereotype.Component;

/**
 * SupplierMapper.
 * Converts between Supplier Entity and DTOs.
 */
@Component
public class SupplierMapper {

    /**
     * Convert SupplierRequest to Supplier Entity.
     */
    public Supplier toEntity(SupplierRequest request) {
        return Supplier.builder()
                .supplierCode(request.getSupplierCode())
                .name(request.getName())
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipcode(request.getZipcode())
                .country(request.getCountry())
                .paymentTerms(request.getPaymentTerms())
                .taxId(request.getTaxId())
                .notes(request.getNotes())
                .active(true)
                .build();
    }

    /**
     * Convert Supplier Entity to SupplierResponse DTO.
     */
    public SupplierResponse toResponse(Supplier supplier) {
        return SupplierResponse.builder()
                .id(supplier.getId())
                .supplierCode(supplier.getSupplierCode())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .phone(supplier.getPhone())
                .email(supplier.getEmail())
                .address(supplier.getAddress())
                .city(supplier.getCity())
                .state(supplier.getState())
                .zipcode(supplier.getZipcode())
                .country(supplier.getCountry())
                .paymentTerms(supplier.getPaymentTerms())
                .taxId(supplier.getTaxId())
                .notes(supplier.getNotes())
                .active(supplier.getActive())
                .createdAt(supplier.getCreatedAt())
                .updatedAt(supplier.getUpdatedAt())
                .build();
    }

    /**
     * Update Supplier Entity with SupplierRequest data.
     */
    public Supplier updateEntity(Supplier supplier, SupplierRequest request) {
        supplier.setSupplierCode(request.getSupplierCode());
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setCity(request.getCity());
        supplier.setState(request.getState());
        supplier.setZipcode(request.getZipcode());
        supplier.setCountry(request.getCountry());
        supplier.setPaymentTerms(request.getPaymentTerms());
        supplier.setTaxId(request.getTaxId());
        supplier.setNotes(request.getNotes());
        return supplier;
    }
}

