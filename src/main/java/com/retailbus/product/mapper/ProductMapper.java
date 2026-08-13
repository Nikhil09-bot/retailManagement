package com.retailbus.product.mapper;

import com.retailbus.product.dto.request.ProductRequest;
import com.retailbus.product.dto.response.ProductResponse;
import com.retailbus.product.entity.Product;
import org.springframework.stereotype.Component;

/**
 * ProductMapper.
 * Converts between Product Entity, ProductRequest DTO, and ProductResponse DTO.
 */
@Component
public class ProductMapper {

    /**
     * Convert ProductRequest to Product Entity.
     * Sets default values for database fields (active=true, timestamps handled by @CreationTimestamp).
     */
    public Product toEntity(ProductRequest request) {
        return Product.builder()
                .productCode(request.getProductCode())
                .barcode(request.getBarcode())
                .name(request.getName())
                .description(request.getDescription())
                .brand(request.getBrand())
                .category(request.getCategory())
                .purchasePrice(request.getPurchasePrice())
                .sellingPrice(request.getSellingPrice())
                .quantityInStock(request.getQuantityInStock())
                .reorderLevel(request.getReorderLevel())
                .unit(request.getUnit())
                .active(true)  // New products are active by default
                .build();
    }

    /**
     * Convert Product Entity to ProductResponse DTO.
     */
    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .barcode(product.getBarcode())
                .name(product.getName())
                .description(product.getDescription())
                .brand(product.getBrand())
                .category(product.getCategory())
                .purchasePrice(product.getPurchasePrice())
                .sellingPrice(product.getSellingPrice())
                .quantityInStock(product.getQuantityInStock())
                .reorderLevel(product.getReorderLevel())
                .unit(product.getUnit())
                .active(product.getActive())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    /**
     * Update Product Entity with ProductRequest data.
     * Used for partial updates (PUT requests).
     */
    public Product updateEntity(Product product, ProductRequest request) {
        product.setProductCode(request.getProductCode());
        product.setBarcode(request.getBarcode());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setBrand(request.getBrand());
        product.setCategory(request.getCategory());
        product.setPurchasePrice(request.getPurchasePrice());
        product.setSellingPrice(request.getSellingPrice());
        product.setQuantityInStock(request.getQuantityInStock());
        product.setReorderLevel(request.getReorderLevel());
        product.setUnit(request.getUnit());
        return product;
    }
}

