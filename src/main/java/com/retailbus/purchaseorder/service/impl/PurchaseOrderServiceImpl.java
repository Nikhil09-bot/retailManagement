package com.retailbus.purchaseorder.service.impl;

import com.retailbus.inventory.enums.InventoryTransactionType;
import com.retailbus.inventory.service.InventoryService;
import com.retailbus.product.entity.Product;
import com.retailbus.product.exception.ProductNotFoundException;
import com.retailbus.product.repository.ProductRepository;
import com.retailbus.purchaseorder.dto.CreatePurchaseOrderRequest;
import com.retailbus.purchaseorder.dto.PurchaseOrderItemRequest;
import com.retailbus.purchaseorder.dto.PurchaseOrderResponse;
import com.retailbus.purchaseorder.entity.PurchaseOrder;
import com.retailbus.purchaseorder.entity.PurchaseOrderItem;
import com.retailbus.purchaseorder.enums.PurchaseOrderStatus;
import com.retailbus.purchaseorder.exception.InvalidPurchaseOrderException;
import com.retailbus.purchaseorder.exception.PurchaseOrderNotFoundException;
import com.retailbus.purchaseorder.mapper.PurchaseOrderMapper;
import com.retailbus.purchaseorder.repository.PurchaseOrderItemRepository;
import com.retailbus.purchaseorder.repository.PurchaseOrderRepository;
import com.retailbus.purchaseorder.service.PurchaseOrderService;
import com.retailbus.supplier.entity.Supplier;
import com.retailbus.supplier.exception.SupplierNotFoundException;
import com.retailbus.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PurchaseOrderService Implementation.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final PurchaseOrderMapper purchaseOrderMapper;

    @Override
    public PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request) {
        log.info("Creating purchase order for supplier ID: {}", request.getSupplierId());

        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with ID: " + request.getSupplierId()));

        String poNumber = generatePoNumber();

        PurchaseOrder po = purchaseOrderMapper.toEntity(poNumber, supplier, request);
        PurchaseOrder savedPo = purchaseOrderRepository.save(po);

        // Build and save items; accumulate total
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PurchaseOrderItem> items = new ArrayList<>();

        for (PurchaseOrderItemRequest itemReq : request.getItems()) {
            if (itemReq.getQuantityOrdered() == null || itemReq.getQuantityOrdered() <= 0) {
                throw new InvalidPurchaseOrderException("Quantity must be positive for product ID: " + itemReq.getProductId());
            }
            if (itemReq.getUnitPrice() == null || itemReq.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidPurchaseOrderException("Unit price must be positive for product ID: " + itemReq.getProductId());
            }
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + itemReq.getProductId()));

            PurchaseOrderItem item = purchaseOrderMapper.toItemEntity(savedPo, product, itemReq);
            items.add(item);
            totalAmount = totalAmount.add(item.getLineTotal());
        }

        purchaseOrderItemRepository.saveAll(items);
        savedPo.setTotalAmount(totalAmount);
        savedPo.setItems(items);
        PurchaseOrder finalPo = purchaseOrderRepository.save(savedPo);

        log.info("Purchase order created with PO number: {}", finalPo.getPoNumber());
        return purchaseOrderMapper.toResponse(finalPo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getAllPurchaseOrders() {
        log.info("Fetching all purchase orders");
        return purchaseOrderRepository.findAll().stream()
                .map(purchaseOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getPurchaseOrderById(Long id) {
        log.info("Fetching purchase order with ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase order not found with ID: " + id));
        return purchaseOrderMapper.toResponse(po);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getPurchaseOrderByPoNumber(String poNumber) {
        log.info("Fetching purchase order with PO number: {}", poNumber);
        PurchaseOrder po = purchaseOrderRepository.findByPoNumber(poNumber)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase order not found with PO number: " + poNumber));
        return purchaseOrderMapper.toResponse(po);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getPurchaseOrdersBySupplier(Long supplierId) {
        log.info("Fetching purchase orders for supplier ID: {}", supplierId);
        return purchaseOrderRepository.findBySupplierId(supplierId).stream()
                .map(purchaseOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getPurchaseOrdersByStatus(PurchaseOrderStatus status) {
        log.info("Fetching purchase orders with status: {}", status);
        return purchaseOrderRepository.findByStatus(status).stream()
                .map(purchaseOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> getPurchaseOrdersByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching purchase orders between {} and {}", start, end);
        return purchaseOrderRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end).stream()
                .map(purchaseOrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseOrderResponse receivePurchaseOrder(Long id) {
        log.info("Receiving purchase order with ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase order not found with ID: " + id));

        if (po.getStatus() != PurchaseOrderStatus.PENDING) {
            throw new InvalidPurchaseOrderException(
                    "Only PENDING purchase orders can be received. Current status: " + po.getStatus());
        }

        // Update inventory for each item
        BigDecimal receivedTotal = BigDecimal.ZERO;
        for (PurchaseOrderItem item : po.getItems()) {
            inventoryService.recordTransaction(
                    item.getProduct().getId(),
                    InventoryTransactionType.PURCHASE_ORDER_RECEIVED,
                    item.getQuantityOrdered(),
                    "PO-" + po.getPoNumber(),
                    "Received from supplier: " + po.getSupplier().getName()
            );
            item.setQuantityReceived(item.getQuantityOrdered());
            receivedTotal = receivedTotal.add(item.getLineTotal());
        }

        po.setStatus(PurchaseOrderStatus.RECEIVED);
        po.setReceivedDate(LocalDateTime.now());
        po.setReceivedAmount(receivedTotal);

        PurchaseOrder savedPo = purchaseOrderRepository.save(po);
        log.info("Purchase order {} received. Inventory updated for {} items.",
                po.getPoNumber(), po.getItems().size());
        return purchaseOrderMapper.toResponse(savedPo);
    }

    @Override
    public PurchaseOrderResponse cancelPurchaseOrder(Long id) {
        log.info("Cancelling purchase order with ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase order not found with ID: " + id));

        if (po.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new InvalidPurchaseOrderException("Cannot cancel a RECEIVED purchase order.");
        }

        po.setStatus(PurchaseOrderStatus.CANCELLED);
        PurchaseOrder savedPo = purchaseOrderRepository.save(po);

        log.info("Purchase order {} cancelled.", po.getPoNumber());
        return purchaseOrderMapper.toResponse(savedPo);
    }

    @Override
    public void deletePurchaseOrder(Long id) {
        log.info("Deleting purchase order with ID: {}", id);
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Purchase order not found with ID: " + id));

        if (po.getStatus() == PurchaseOrderStatus.RECEIVED) {
            throw new InvalidPurchaseOrderException("Cannot delete a RECEIVED purchase order.");
        }

        purchaseOrderRepository.delete(po);
        log.info("Purchase order deleted successfully with ID: {}", id);
    }

    // ── Private Helpers ────────────────────────────────────────────────────────

    private String generatePoNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = purchaseOrderRepository.count() + 1;
        return "PO-" + datePart + "-" + String.format("%04d", count);
    }
}

