package com.retailbus.purchaseorder.controller;

import com.retailbus.dto.ApiResponse;
import com.retailbus.purchaseorder.dto.CreatePurchaseOrderRequest;
import com.retailbus.purchaseorder.dto.PurchaseOrderResponse;
import com.retailbus.purchaseorder.enums.PurchaseOrderStatus;
import com.retailbus.purchaseorder.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PurchaseOrderController.
 * REST API endpoints for Purchase Order management.
 */
@Slf4j
@RestController
@RequestMapping("/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    /** POST /api/purchase-orders — Create PO */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPurchaseOrder(@Valid @RequestBody CreatePurchaseOrderRequest request) {
        log.info("POST request to create purchase order for supplier: {}", request.getSupplierId());
        PurchaseOrderResponse response = purchaseOrderService.createPurchaseOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Purchase order created successfully", response));
    }

    /** GET /api/purchase-orders — All POs */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllPurchaseOrders() {
        List<PurchaseOrderResponse> orders = purchaseOrderService.getAllPurchaseOrders();
        return ResponseEntity.ok(ApiResponse.success("Purchase orders retrieved successfully", orders));
    }

    /** GET /api/purchase-orders/{id} — By ID */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrderResponse response = purchaseOrderService.getPurchaseOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order retrieved successfully", response));
    }

    /** GET /api/purchase-orders/po-number/{poNumber} — By PO Number */
    @GetMapping("/po-number/{poNumber}")
    public ResponseEntity<ApiResponse<?>> getPurchaseOrderByPoNumber(@PathVariable String poNumber) {
        PurchaseOrderResponse response = purchaseOrderService.getPurchaseOrderByPoNumber(poNumber);
        return ResponseEntity.ok(ApiResponse.success("Purchase order retrieved successfully", response));
    }

    /** GET /api/purchase-orders/supplier/{supplierId} — By Supplier */
    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<ApiResponse<?>> getPurchaseOrdersBySupplier(@PathVariable Long supplierId) {
        List<PurchaseOrderResponse> orders = purchaseOrderService.getPurchaseOrdersBySupplier(supplierId);
        return ResponseEntity.ok(ApiResponse.success("Purchase orders retrieved by supplier successfully", orders));
    }

    /** GET /api/purchase-orders/status/{status} — By Status */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<?>> getPurchaseOrdersByStatus(@PathVariable PurchaseOrderStatus status) {
        List<PurchaseOrderResponse> orders = purchaseOrderService.getPurchaseOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Purchase orders retrieved by status successfully", orders));
    }

    /** GET /api/purchase-orders/date-range?start=&end= — By Date Range */
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<?>> getPurchaseOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<PurchaseOrderResponse> orders = purchaseOrderService.getPurchaseOrdersByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success("Purchase orders retrieved by date range successfully", orders));
    }

    /** PATCH /api/purchase-orders/{id}/receive — Receive PO (auto-updates inventory) */
    @PatchMapping("/{id}/receive")
    public ResponseEntity<ApiResponse<?>> receivePurchaseOrder(@PathVariable Long id) {
        log.info("PATCH request to receive purchase order ID: {}", id);
        PurchaseOrderResponse response = purchaseOrderService.receivePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order received. Inventory updated successfully.", response));
    }

    /** PATCH /api/purchase-orders/{id}/cancel — Cancel PO */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelPurchaseOrder(@PathVariable Long id) {
        log.info("PATCH request to cancel purchase order ID: {}", id);
        PurchaseOrderResponse response = purchaseOrderService.cancelPurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order cancelled successfully", response));
    }

    /** DELETE /api/purchase-orders/{id} — Delete PO */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order deleted successfully", null));
    }
}

