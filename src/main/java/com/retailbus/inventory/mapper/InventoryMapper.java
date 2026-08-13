package com.retailbus.inventory.mapper;

import com.retailbus.inventory.dto.response.InventoryHistoryResponse;
import com.retailbus.inventory.dto.response.LowStockProductResponse;
import com.retailbus.inventory.entity.InventoryHistory;
import com.retailbus.product.entity.Product;
import org.springframework.stereotype.Component;

/**
 * InventoryMapper.
 * Converts between Inventory entities and DTOs.
 */
@Component
public class InventoryMapper {

    /**
     * Convert InventoryHistory entity to response DTO.
     */
    public InventoryHistoryResponse toHistoryResponse(InventoryHistory history) {
        return InventoryHistoryResponse.builder()
                .id(history.getId())
                .productId(history.getProduct().getId())
                .productCode(history.getProduct().getProductCode())
                .productName(history.getProduct().getName())
                .transactionType(history.getTransactionType().name())
                .quantityChanged(history.getQuantityChanged())
                .quantityBefore(history.getQuantityBefore())
                .quantityAfter(history.getQuantityAfter())
                .reference(history.getReference())
                .notes(history.getNotes())
                .createdAt(history.getCreatedAt())
                .build();
    }

    /**
     * Convert Product entity to LowStockProductResponse DTO.
     */
    public LowStockProductResponse toLowStockResponse(Product product) {
        int quantityToReorder = product.getReorderLevel() - product.getQuantityInStock();
        return LowStockProductResponse.builder()
                .productId(product.getId())
                .productCode(product.getProductCode())
                .productName(product.getName())
                .brand(product.getBrand())
                .category(product.getCategory().name())
                .quantityInStock(product.getQuantityInStock())
                .reorderLevel(product.getReorderLevel())
                .quantityToReorder(quantityToReorder > 0 ? quantityToReorder : 0)
                .build();
    }
}

