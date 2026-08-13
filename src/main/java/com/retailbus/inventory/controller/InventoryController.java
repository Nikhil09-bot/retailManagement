package com.retailbus.inventory.controller;

import com.retailbus.dto.ApiResponse;
import com.retailbus.inventory.dto.request.StockAdjustmentRequest;
import com.retailbus.inventory.dto.response.InventoryHistoryResponse;
import com.retailbus.inventory.dto.response.LowStockProductResponse;
import com.retailbus.inventory.enums.InventoryTransactionType;
import com.retailbus.inventory.service.InventoryService;
import com.retailbus.product.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * InventoryController.
 * REST API endpoints for inventory management.
 */
@Slf4j
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    /**
     * Get inventory status for all products.
     * GET /api/inventory/status
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<?>> getInventoryStatus() {
        log.info("GET request to fetch inventory status");
        List<ProductResponse> response = inventoryService.getInventoryStatus();
        return ResponseEntity.ok(ApiResponse.success("Inventory status retrieved successfully", response));
    }

    /**
     * Get low stock products.
     * GET /api/inventory/low-stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<?>> getLowStockProducts() {
        log.info("GET request to fetch low stock products");
        List<LowStockProductResponse> response = inventoryService.getLowStockProducts();
        return ResponseEntity.ok(ApiResponse.success("Low stock products retrieved successfully", response));
    }

    /**
     * Get out-of-stock products.
     * GET /api/inventory/out-of-stock
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<ApiResponse<?>> getOutOfStockProducts() {
        log.info("GET request to fetch out-of-stock products");
        List<ProductResponse> response = inventoryService.getOutOfStockProducts();
        return ResponseEntity.ok(ApiResponse.success("Out-of-stock products retrieved successfully", response));
    }

    /**
     * Adjust stock for a product (manual adjustment).
     * POST /api/inventory/adjust
     */
    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<?>> adjustStock(@Valid @RequestBody StockAdjustmentRequest request) {
        log.info("POST request to adjust stock for product ID: {}", request.getProductId());
        InventoryHistoryResponse response = inventoryService.adjustStock(request);
        return ResponseEntity.ok(ApiResponse.success("Stock adjusted successfully", response));
    }

    /**
     * Get inventory history for a product.
     * GET /api/inventory/history/{productId}
     */
    @GetMapping("/history/{productId}")
    public ResponseEntity<ApiResponse<?>> getProductInventoryHistory(@PathVariable Long productId) {
        log.info("GET request to fetch inventory history for product ID: {}", productId);
        List<InventoryHistoryResponse> response = inventoryService.getProductInventoryHistory(productId);
        return ResponseEntity.ok(ApiResponse.success("Inventory history retrieved successfully", response));
    }

    /**
     * Get inventory transactions by type.
     * GET /api/inventory/transactions?type=STOCK_IN
     */
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<?>> getTransactionHistory(@RequestParam InventoryTransactionType type) {
        log.info("GET request to fetch transaction history for type: {}", type);
        List<InventoryHistoryResponse> response = inventoryService.getTransactionHistory(type);
        return ResponseEntity.ok(ApiResponse.success("Transaction history retrieved successfully", response));
    }

    /**
     * Get inventory history by date range.
     * GET /api/inventory/history-range?start=2026-07-01T00:00:00&end=2026-07-31T23:59:59
     */
    @GetMapping("/history-range")
    public ResponseEntity<ApiResponse<?>> getInventoryHistoryByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("GET request to fetch inventory history between {} and {}", start, end);
        List<InventoryHistoryResponse> response = inventoryService.getInventoryHistoryByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success("Inventory history retrieved successfully", response));
    }

    /**
     * Get transactions by reference (PO, Sale ID, etc).
     * GET /api/inventory/transactions-by-reference?reference=PO-001
     */
    @GetMapping("/transactions-by-reference")
    public ResponseEntity<ApiResponse<?>> getTransactionsByReference(@RequestParam String reference) {
        log.info("GET request to fetch transactions for reference: {}", reference);
        List<InventoryHistoryResponse> response = inventoryService.getTransactionsByReference(reference);
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", response));
    }

    /**
     * Get low stock count (for dashboard).
     * GET /api/inventory/low-stock/count
     */
    @GetMapping("/low-stock/count")
    public ResponseEntity<ApiResponse<?>> getLowStockCount() {
        log.info("GET request to fetch low stock count");
        Long count = inventoryService.getLowStockCount();
        return ResponseEntity.ok(ApiResponse.success("Low stock count retrieved successfully", count));
    }

    /**
     * Get out-of-stock count (for dashboard).
     * GET /api/inventory/out-of-stock/count
     */
    @GetMapping("/out-of-stock/count")
    public ResponseEntity<ApiResponse<?>> getOutOfStockCount() {
        log.info("GET request to fetch out-of-stock count");
        Long count = inventoryService.getOutOfStockCount();
        return ResponseEntity.ok(ApiResponse.success("Out-of-stock count retrieved successfully", count));
    }
}

