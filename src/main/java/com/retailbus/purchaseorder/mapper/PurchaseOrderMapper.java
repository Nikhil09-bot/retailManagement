package com.retailbus.purchaseorder.mapper;

import com.retailbus.purchaseorder.dto.CreatePurchaseOrderRequest;
import com.retailbus.purchaseorder.dto.PurchaseOrderItemRequest;
import com.retailbus.purchaseorder.dto.PurchaseOrderItemResponse;
import com.retailbus.purchaseorder.dto.PurchaseOrderResponse;
import com.retailbus.purchaseorder.entity.PurchaseOrder;
import com.retailbus.purchaseorder.entity.PurchaseOrderItem;
import com.retailbus.purchaseorder.enums.PurchaseOrderStatus;
import com.retailbus.product.entity.Product;
import com.retailbus.supplier.entity.Supplier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PurchaseOrderMapper.
 * Converts between PurchaseOrder entities and DTOs.
 */
@Component
public class PurchaseOrderMapper {

    /**
     * Create PurchaseOrder entity from request.
     * Items are mapped separately after product lookup.
     */
    public PurchaseOrder toEntity(String poNumber, Supplier supplier, CreatePurchaseOrderRequest request) {
        return PurchaseOrder.builder()
                .poNumber(poNumber)
                .supplier(supplier)
                .status(PurchaseOrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)       // Calculated later
                .receivedAmount(BigDecimal.ZERO)
                .expectedDeliveryDate(request.getExpectedDeliveryDate())
                .notes(request.getNotes())
                .build();
    }

    /**
     * Create PurchaseOrderItem entity from request + resolved product.
     */
    public PurchaseOrderItem toItemEntity(PurchaseOrder purchaseOrder, Product product, PurchaseOrderItemRequest req) {
        BigDecimal lineTotal = req.getUnitPrice().multiply(BigDecimal.valueOf(req.getQuantityOrdered()));
        return PurchaseOrderItem.builder()
                .purchaseOrder(purchaseOrder)
                .product(product)
                .quantityOrdered(req.getQuantityOrdered())
                .quantityReceived(0)
                .unitPrice(req.getUnitPrice())
                .lineTotal(lineTotal)
                .build();
    }

    /**
     * Convert PurchaseOrder entity to PurchaseOrderResponse DTO.
     */
    public PurchaseOrderResponse toResponse(PurchaseOrder po) {
        List<PurchaseOrderItemResponse> itemResponses = po.getItems() == null ? List.of() :
                po.getItems().stream()
                        .map(this::toItemResponse)
                        .collect(Collectors.toList());

        return PurchaseOrderResponse.builder()
                .id(po.getId())
                .poNumber(po.getPoNumber())
                .supplierId(po.getSupplier().getId())
                .supplierName(po.getSupplier().getName())
                .status(po.getStatus())
                .totalAmount(po.getTotalAmount())
                .receivedAmount(po.getReceivedAmount())
                .expectedDeliveryDate(po.getExpectedDeliveryDate())
                .receivedDate(po.getReceivedDate())
                .notes(po.getNotes())
                .items(itemResponses)
                .createdAt(po.getCreatedAt())
                .updatedAt(po.getUpdatedAt())
                .build();
    }

    /**
     * Convert PurchaseOrderItem entity to PurchaseOrderItemResponse DTO.
     */
    public PurchaseOrderItemResponse toItemResponse(PurchaseOrderItem item) {
        return PurchaseOrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productCode(item.getProduct().getProductCode())
                .productName(item.getProduct().getName())
                .quantityOrdered(item.getQuantityOrdered())
                .quantityReceived(item.getQuantityReceived())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();
    }
}

