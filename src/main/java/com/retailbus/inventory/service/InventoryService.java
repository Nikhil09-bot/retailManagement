package com.retailbus.inventory.service;

import com.retailbus.inventory.dto.request.StockAdjustmentRequest;
import com.retailbus.inventory.dto.response.InventoryHistoryResponse;
import com.retailbus.inventory.dto.response.LowStockProductResponse;
import com.retailbus.inventory.enums.InventoryTransactionType;
import com.retailbus.product.dto.response.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * InventoryService Interface.
 * Defines inventory management operations.
 */
public interface InventoryService {

    /**
     * Get inventory status for all active products.
     */
    List<ProductResponse> getInventoryStatus();

    /**
     * Get products below or at reorder level.
     */
    List<LowStockProductResponse> getLowStockProducts();

    /**
     * Get out-of-stock products (quantity = 0).
     */
    List<ProductResponse> getOutOfStockProducts();

    /**
     * Perform manual stock adjustment (damage, loss, correction).
     */
    InventoryHistoryResponse adjustStock(StockAdjustmentRequest request);

    /**
     * Record an inventory transaction (internal use).
     */
    InventoryHistoryResponse recordTransaction(Long productId, InventoryTransactionType type,
                                               Integer quantityChanged, String reference, String notes);

    /**
     * Get inventory history for a product.
     */
    List<InventoryHistoryResponse> getProductInventoryHistory(Long productId);

    /**
     * Get inventory transactions of a specific type.
     */
    List<InventoryHistoryResponse> getTransactionHistory(InventoryTransactionType type);

    /**
     * Get inventory transactions within date range.
     */
    List<InventoryHistoryResponse> getInventoryHistoryByDateRange(LocalDateTime start, LocalDateTime end);

    /**
     * Get inventory transactions by reference (PO, Sale ID, etc).
     */
    List<InventoryHistoryResponse> getTransactionsByReference(String reference);

    /**
     * Get low stock count for dashboard.
     */
    Long getLowStockCount();

    /**
     * Get out-of-stock count for dashboard.
     */
    Long getOutOfStockCount();
}

