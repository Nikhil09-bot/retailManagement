package com.retailbus.supplier.service;

import com.retailbus.supplier.dto.request.SupplierRequest;
import com.retailbus.supplier.dto.response.SupplierResponse;

import java.util.List;

/**
 * SupplierService Interface.
 * Defines supplier management operations.
 */
public interface SupplierService {

    /**
     * Create a new supplier.
     */
    SupplierResponse createSupplier(SupplierRequest request);

    /**
     * Update an existing supplier.
     */
    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    /**
     * Get all suppliers (including inactive).
     */
    List<SupplierResponse> getAllSuppliers();

    /**
     * Get supplier by ID.
     */
    SupplierResponse getSupplierById(Long id);

    /**
     * Get all active suppliers.
     */
    List<SupplierResponse> getActiveSuppliers();

    /**
     * Search suppliers by name.
     */
    List<SupplierResponse> searchSuppliers(String keyword);

    /**
     * Search suppliers by city.
     */
    List<SupplierResponse> getSuppliersByCity(String city);

    /**
     * Deactivate a supplier.
     */
    SupplierResponse deactivateSupplier(Long id);

    /**
     * Delete a supplier.
     */
    void deleteSupplier(Long id);

    /**
     * Get supplier count for dashboard.
     */
    Long getSupplierCount();
}

