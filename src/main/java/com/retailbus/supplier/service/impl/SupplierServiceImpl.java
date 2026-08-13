package com.retailbus.supplier.service.impl;

import com.retailbus.supplier.dto.request.SupplierRequest;
import com.retailbus.supplier.dto.response.SupplierResponse;
import com.retailbus.supplier.entity.Supplier;
import com.retailbus.supplier.exception.DuplicateSupplierException;
import com.retailbus.supplier.exception.SupplierNotFoundException;
import com.retailbus.supplier.mapper.SupplierMapper;
import com.retailbus.supplier.repository.SupplierRepository;
import com.retailbus.supplier.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SupplierService Implementation.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponse createSupplier(SupplierRequest request) {
        log.info("Creating new supplier with code: {}", request.getSupplierCode());

        // Check for duplicate supplier code
        if (supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
            log.warn("Duplicate supplier code: {}", request.getSupplierCode());
            throw new DuplicateSupplierException("Supplier code already exists: " + request.getSupplierCode());
        }

        Supplier supplier = supplierMapper.toEntity(request);
        Supplier savedSupplier = supplierRepository.save(supplier);

        log.info("Supplier created successfully with ID: {}", savedSupplier.getId());
        return supplierMapper.toResponse(savedSupplier);
    }

    @Override
    public SupplierResponse updateSupplier(Long id, SupplierRequest request) {
        log.info("Updating supplier with ID: {}", id);

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Supplier not found with ID: {}", id);
                    return new SupplierNotFoundException("Supplier not found with ID: " + id);
                });

        // Check for duplicate supplier code (if changed)
        if (!supplier.getSupplierCode().equals(request.getSupplierCode()) &&
                supplierRepository.existsBySupplierCode(request.getSupplierCode())) {
            log.warn("Duplicate supplier code during update: {}", request.getSupplierCode());
            throw new DuplicateSupplierException("Supplier code already exists: " + request.getSupplierCode());
        }

        Supplier updatedSupplier = supplierMapper.updateEntity(supplier, request);
        Supplier savedSupplier = supplierRepository.save(updatedSupplier);

        log.info("Supplier updated successfully with ID: {}", id);
        return supplierMapper.toResponse(savedSupplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        log.info("Fetching all suppliers");
        return supplierRepository.findAll().stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        log.info("Fetching supplier with ID: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Supplier not found with ID: {}", id);
                    return new SupplierNotFoundException("Supplier not found with ID: " + id);
                });
        return supplierMapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getActiveSuppliers() {
        log.info("Fetching all active suppliers");
        return supplierRepository.findByActiveTrue().stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> searchSuppliers(String keyword) {
        log.info("Searching suppliers with keyword: {}", keyword);
        return supplierRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierResponse> getSuppliersByCity(String city) {
        log.info("Fetching suppliers from city: {}", city);
        return supplierRepository.findByCityContainingIgnoreCase(city).stream()
                .map(supplierMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierResponse deactivateSupplier(Long id) {
        log.info("Deactivating supplier with ID: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Supplier not found with ID: {}", id);
                    return new SupplierNotFoundException("Supplier not found with ID: " + id);
                });

        supplier.setActive(false);
        Supplier updatedSupplier = supplierRepository.save(supplier);

        log.info("Supplier deactivated successfully with ID: {}", id);
        return supplierMapper.toResponse(updatedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        log.info("Deleting supplier with ID: {}", id);
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Supplier not found with ID: {}", id);
                    return new SupplierNotFoundException("Supplier not found with ID: " + id);
                });

        supplierRepository.delete(supplier);
        log.info("Supplier deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getSupplierCount() {
        log.info("Counting suppliers");
        return supplierRepository.count();
    }
}

