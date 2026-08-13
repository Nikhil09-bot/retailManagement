package com.retailbus.product.service;

import com.retailbus.product.dto.request.ProductRequest;
import com.retailbus.product.dto.response.ProductResponse;
import com.retailbus.product.enums.Category;

import java.util.List;

/**
 * ProductService Interface.
 * Defines business logic operations for Product management.
 */
public interface ProductService {

    /**
     * Create a new product.
     *
     * @param request Product details
     * @return Created product response
     * @throws DuplicateProductException if product code already exists
     * @throws BusinessValidationException if selling price <= purchase price
     */
    ProductResponse createProduct(ProductRequest request);

    /**
     * Update an existing product.
     *
     * @param id Product ID
     * @param request Updated product details
     * @return Updated product response
     * @throws ProductNotFoundException if product not found
     * @throws DuplicateProductException if product code is already used by another product
     * @throws BusinessValidationException if selling price <= purchase price
     */
    ProductResponse updateProduct(Long id, ProductRequest request);

    /**
     * Get all products (including inactive).
     *
     * @return List of all products
     */
    List<ProductResponse> getAllProducts();

    /**
     * Get product by ID.
     *
     * @param id Product ID
     * @return Product response
     * @throws ProductNotFoundException if product not found
     */
    ProductResponse getProductById(Long id);

    /**
     * Search products by name or brand.
     *
     * @param keyword Search keyword
     * @return List of matching products
     */
    List<ProductResponse> searchProducts(String keyword);

    /**
     * Get products by category.
     *
     * @param category Product category
     * @return List of products in category
     */
    List<ProductResponse> getProductsByCategory(Category category);

    /**
     * Get all active products.
     *
     * @return List of active products
     */
    List<ProductResponse> getActiveProducts();

    /**
     * Disable a product (soft delete).
     *
     * @param id Product ID
     * @return Updated product response
     * @throws ProductNotFoundException if product not found
     */
    ProductResponse disableProduct(Long id);

    /**
     * Delete a product permanently.
     *
     * @param id Product ID
     * @throws ProductNotFoundException if product not found
     */
    void deleteProduct(Long id);

    /**
     * Increase product stock.
     *
     * @param id Product ID
     * @param quantity Quantity to add
     * @return Updated product response
     * @throws ProductNotFoundException if product not found
     * @throws InvalidStockOperationException if quantity is negative
     */
    ProductResponse increaseStock(Long id, Integer quantity);

    /**
     * Decrease product stock.
     *
     * @param id Product ID
     * @param quantity Quantity to remove
     * @return Updated product response
     * @throws ProductNotFoundException if product not found
     * @throws InvalidStockOperationException if quantity is negative or would result in negative stock
     */
    ProductResponse decreaseStock(Long id, Integer quantity);
}

