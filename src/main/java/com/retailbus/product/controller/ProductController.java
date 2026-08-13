package com.retailbus.product.controller;

import com.retailbus.dto.ApiResponse;
import com.retailbus.product.dto.request.ProductRequest;
import com.retailbus.product.dto.response.ProductResponse;
import com.retailbus.product.enums.Category;
import com.retailbus.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController.
 * REST API endpoints for Product management.
 */
@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Create a new product.
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createProduct(@Valid @RequestBody ProductRequest request) {
        log.info("POST request to create product: {}", request.getProductCode());
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", response));
    }

    /**
     * Get all products.
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        log.info("GET request to fetch all products");
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    /**
     * Get product by ID.
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getProductById(@PathVariable Long id) {
        log.info("GET request to fetch product with ID: {}", id);
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", response));
    }

    /**
     * Update product.
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        log.info("PUT request to update product with ID: {}", id);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", response));
    }

    /**
     * Delete product.
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id) {
        log.info("DELETE request to delete product with ID: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    /**
     * Disable product (soft delete).
     * PATCH /api/products/{id}/disable
     */
    @PatchMapping("/{id}/disable")
    public ResponseEntity<ApiResponse<?>> disableProduct(@PathVariable Long id) {
        log.info("PATCH request to disable product with ID: {}", id);
        ProductResponse response = productService.disableProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product disabled successfully", response));
    }

    /**
     * Increase product stock.
     * PATCH /api/products/{id}/stock/add
     */
    @PatchMapping("/{id}/stock/add")
    public ResponseEntity<ApiResponse<?>> increaseStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        log.info("PATCH request to increase stock for product ID: {} by: {}", id, quantity);
        ProductResponse response = productService.increaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock increased successfully", response));
    }

    /**
     * Decrease product stock.
     * PATCH /api/products/{id}/stock/remove
     */
    @PatchMapping("/{id}/stock/remove")
    public ResponseEntity<ApiResponse<?>> decreaseStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        log.info("PATCH request to decrease stock for product ID: {} by: {}", id, quantity);
        ProductResponse response = productService.decreaseStock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Stock decreased successfully", response));
    }

    /**
     * Search products by keyword.
     * GET /api/products/search?keyword=...
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchProducts(@RequestParam String keyword) {
        log.info("GET request to search products with keyword: {}", keyword);
        List<ProductResponse> products = productService.searchProducts(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", products));
    }

    /**
     * Get products by category.
     * GET /api/products/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<?>> getProductsByCategory(@PathVariable Category category) {
        log.info("GET request to fetch products for category: {}", category);
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved by category successfully", products));
    }

    /**
     * Get all active products.
     * GET /api/products/active
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<?>> getActiveProducts() {
        log.info("GET request to fetch all active products");
        List<ProductResponse> products = productService.getActiveProducts();
        return ResponseEntity.ok(ApiResponse.success("Active products retrieved successfully", products));
    }
}

