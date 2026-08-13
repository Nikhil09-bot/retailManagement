package com.retailbus.purchaseorder.service;

import com.retailbus.purchaseorder.dto.CreatePurchaseOrderRequest;
import com.retailbus.purchaseorder.dto.PurchaseOrderResponse;
import com.retailbus.purchaseorder.enums.PurchaseOrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PurchaseOrderService Interface.
 */
public interface PurchaseOrderService {

    PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request);

    List<PurchaseOrderResponse> getAllPurchaseOrders();

    PurchaseOrderResponse getPurchaseOrderById(Long id);

    PurchaseOrderResponse getPurchaseOrderByPoNumber(String poNumber);

    List<PurchaseOrderResponse> getPurchaseOrdersBySupplier(Long supplierId);

    List<PurchaseOrderResponse> getPurchaseOrdersByStatus(PurchaseOrderStatus status);

    List<PurchaseOrderResponse> getPurchaseOrdersByDateRange(LocalDateTime start, LocalDateTime end);

    /**
     * Receive PO — updates inventory for all items.
     */
    PurchaseOrderResponse receivePurchaseOrder(Long id);

    /**
     * Cancel a PENDING purchase order.
     */
    PurchaseOrderResponse cancelPurchaseOrder(Long id);

    void deletePurchaseOrder(Long id);
}

