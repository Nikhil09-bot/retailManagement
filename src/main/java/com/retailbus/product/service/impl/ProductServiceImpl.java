package com.retailbus.product.service.impl;

import com.retailbus.product.dto.request.ProductRequest;
import com.retailbus.product.dto.response.ProductResponse;
import com.retailbus.product.entity.Product;
import com.retailbus.product.enums.Category;
import com.retailbus.product.exception.*;
import com.retailbus.product.mapper.ProductMapper;
import com.retailbus.product.repository.ProductRepository;
import com.retailbus.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductService Implementation.
 * Implements business logic for Product management.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product with code: {}", request.getProductCode());

        // Validation: Check for duplicate product code
        if (productRepository.existsByProductCode(request.getProductCode())) {
            log.warn("Duplicate product code: {}", request.getProductCode());
            throw new DuplicateProductException("Product code already exists: " + request.getProductCode());
        }

        // Validation: Selling price must be greater than purchase price
        if (request.getSellingPrice().compareTo(request.getPurchasePrice()) <= 0) {
            log.warn("Invalid pricing: selling price must be greater than purchase price");
            throw new BusinessValidationException("Selling price must be greater than purchase price");
        }

        // Convert DTO to Entity and save
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);

        log.info("Product created successfully with ID: {}", savedProduct.getId());
        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);

        // Fetch product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with ID: " + id);
                });

        // Validation: Check for duplicate product code (if changed)
        if (!product.getProductCode().equals(request.getProductCode()) &&
                productRepository.existsByProductCode(request.getProductCode())) {
            log.warn("Duplicate product code during update: {}", request.getProductCode());
            throw new DuplicateProductException("Product code already exists: " + request.getProductCode());
        }

        // Validation: Selling price must be greater than purchase price
        if (request.getSellingPrice().compareTo(request.getPurchasePrice()) <= 0) {
            log.warn("Invalid pricing during update: selling price must be greater than purchase price");
            throw new BusinessValidationException("Selling price must be greater than purchase price");
        }

        // Update entity
        Product updatedProduct = productMapper.updateEntity(product, request);
        Product savedProduct = productRepository.save(updatedProduct);

        log.info("Product updated successfully with ID: {}", id);
        return productMapper.toResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with ID: " + id);
                });
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String keyword) {
        log.info("Searching products with keyword: {}", keyword);
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        products.addAll(productRepository.findByBrandContainingIgnoreCase(keyword));

        // Remove duplicates
        return products.stream()
                .distinct()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Category category) {
        log.info("Fetching products for category: {}", category);
        return productRepository.findByCategoryAndActiveTrue(category).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActiveProducts() {
        log.info("Fetching all active products");
        return productRepository.findByActiveTrue().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse disableProduct(Long id) {
        log.info("Disabling product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with ID: " + id);
                });

        product.setActive(false);
        Product updatedProduct = productRepository.save(product);

        log.info("Product disabled successfully with ID: {}", id);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with ID: " + id);
                });

        productRepository.delete(product);
        log.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    public ProductResponse increaseStock(Long id, Integer quantity) {
        log.info("Increasing stock for product ID: {} by quantity: {}", id, quantity);

        // Validation: Quantity must be positive
        if (quantity == null || quantity < 0) {
            log.error("Invalid quantity for stock increase: {}", quantity);
            throw new InvalidStockOperationException("Quantity must be positive");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with ID: " + id);
                });

        product.setQuantityInStock(product.getQuantityInStock() + quantity);
        Product updatedProduct = productRepository.save(product);

        log.info("Stock increased successfully for product ID: {}", id);
        return productMapper.toResponse(updatedProduct);
    }

    @Override
    public ProductResponse decreaseStock(Long id, Integer quantity) {
        log.info("Decreasing stock for product ID: {} by quantity: {}", id, quantity);

        // Validation: Quantity must be positive
        if (quantity == null || quantity < 0) {
            log.error("Invalid quantity for stock decrease: {}", quantity);
            throw new InvalidStockOperationException("Quantity must be positive");
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new ProductNotFoundException("Product not found with ID: " + id);
                });

        // Validation: Cannot decrease stock below zero
        int newStock = product.getQuantityInStock() - quantity;
        if (newStock < 0) {
            log.error("Insufficient stock. Current: {}, Requested: {}", product.getQuantityInStock(), quantity);
            throw new InvalidStockOperationException(
                    "Insufficient stock. Current quantity: " + product.getQuantityInStock() +
                    ", Requested to decrease: " + quantity
            );
        }

        product.setQuantityInStock(newStock);
        Product updatedProduct = productRepository.save(product);

        log.info("Stock decreased successfully for product ID: {}", id);
        return productMapper.toResponse(updatedProduct);
    }
}
