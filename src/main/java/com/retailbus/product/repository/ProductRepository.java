package com.retailbus.product.repository;

import com.retailbus.product.entity.Product;
import com.retailbus.product.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ProductRepository.
 * Provides database access operations for Product entity using Spring Data JPA.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Check if a product with given product code exists.
     */
    boolean existsByProductCode(String productCode);

    /**
     * Find all active products.
     */
    List<Product> findByActiveTrue();

    /**
     * Find all products by category.
     */
    List<Product> findByCategory(Category category);

    /**
     * Search products by name (case-insensitive).
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Search products by brand (case-insensitive).
     */
    List<Product> findByBrandContainingIgnoreCase(String brand);

    /**
     * Find product by product code.
     */
    Optional<Product> findByProductCode(String productCode);

    /**
     * Find products by category and active status.
     */
    List<Product> findByCategoryAndActiveTrue(Category category);
}

