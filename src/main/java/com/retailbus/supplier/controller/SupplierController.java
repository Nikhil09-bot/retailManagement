package com.retailbus.supplier.controller;

import com.retailbus.dto.ApiResponse;
import com.retailbus.supplier.dto.request.SupplierRequest;
import com.retailbus.supplier.dto.response.SupplierResponse;
import com.retailbus.supplier.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SupplierController.
 * REST API endpoints for Supplier management.
 */
@Slf4j
@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    /**
     * Create a new supplier.
     * POST /api/suppliers
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSupplier(@Valid @RequestBody SupplierRequest request) {
        log.info("POST request to create supplier: {}", request.getSupplierCode());
        SupplierResponse response = supplierService.createSupplier(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Supplier created successfully", response));
    }

    /**
     * Get all suppliers.
     * GET /api/suppliers
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSuppliers() {
        log.info("GET request to fetch all suppliers");
        List<SupplierResponse> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(ApiResponse.success("Suppliers retrieved successfully", suppliers));
    }

    /**
     * Get supplier by ID.
     * GET /api/suppliers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getSupplierById(@PathVariable Long id) {
        log.info("GET request to fetch supplier with ID: {}", id);
        SupplierResponse response = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier retrieved successfully", response));
    }

    /**
     * Update supplier.
     * PUT /api/suppliers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request) {
        log.info("PUT request to update supplier with ID: {}", id);
        SupplierResponse response = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully", response));
    }

    /**
     * Get all active suppliers.
     * GET /api/suppliers/active
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<?>> getActiveSuppliers() {
        log.info("GET request to fetch active suppliers");
        List<SupplierResponse> suppliers = supplierService.getActiveSuppliers();
        return ResponseEntity.ok(ApiResponse.success("Active suppliers retrieved successfully", suppliers));
    }

    /**
     * Search suppliers by name.
     * GET /api/suppliers/search?keyword=
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchSuppliers(@RequestParam String keyword) {
        log.info("GET request to search suppliers with keyword: {}", keyword);
        List<SupplierResponse> suppliers = supplierService.searchSuppliers(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", suppliers));
    }

    /**
     * Get suppliers by city.
     * GET /api/suppliers/city/{city}
     */
    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<?>> getSuppliersByCity(@PathVariable String city) {
        log.info("GET request to fetch suppliers from city: {}", city);
        List<SupplierResponse> suppliers = supplierService.getSuppliersByCity(city);
        return ResponseEntity.ok(ApiResponse.success("Suppliers retrieved by city successfully", suppliers));
    }

    /**
     * Deactivate supplier.
     * PATCH /api/suppliers/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<?>> deactivateSupplier(@PathVariable Long id) {
        log.info("PATCH request to deactivate supplier with ID: {}", id);
        SupplierResponse response = supplierService.deactivateSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deactivated successfully", response));
    }

    /**
     * Delete supplier.
     * DELETE /api/suppliers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteSupplier(@PathVariable Long id) {
        log.info("DELETE request to delete supplier with ID: {}", id);
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully", null));
    }

    /**
     * Get supplier count (for dashboard).
     * GET /api/suppliers/count
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<?>> getSupplierCount() {
        log.info("GET request to fetch supplier count");
        Long count = supplierService.getSupplierCount();
        return ResponseEntity.ok(ApiResponse.success("Supplier count retrieved successfully", count));
    }
}

