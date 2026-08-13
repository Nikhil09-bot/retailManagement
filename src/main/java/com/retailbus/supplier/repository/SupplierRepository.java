package com.retailbus.supplier.repository;

import com.retailbus.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SupplierRepository.
 * Provides database access operations for Supplier entity.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    /**
     * Check if supplier with given supplier code exists.
     */
    boolean existsBySupplierCode(String supplierCode);

    /**
     * Find supplier by supplier code.
     */
    Optional<Supplier> findBySupplierCode(String supplierCode);

    /**
     * Find all active suppliers.
     */
    List<Supplier> findByActiveTrue();

    /**
     * Search suppliers by name (case-insensitive).
     */
    List<Supplier> findByNameContainingIgnoreCase(String name);

    /**
     * Search suppliers by city.
     */
    List<Supplier> findByCityContainingIgnoreCase(String city);
}

