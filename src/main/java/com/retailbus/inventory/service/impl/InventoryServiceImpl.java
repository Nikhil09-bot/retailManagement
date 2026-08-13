package com.retailbus.inventory.service.impl;

import com.retailbus.inventory.dto.request.StockAdjustmentRequest;
import com.retailbus.inventory.dto.response.InventoryHistoryResponse;
import com.retailbus.inventory.dto.response.LowStockProductResponse;
import com.retailbus.inventory.entity.InventoryHistory;
import com.retailbus.inventory.enums.InventoryTransactionType;
import com.retailbus.inventory.exception.InventoryException;
import com.retailbus.inventory.mapper.InventoryMapper;
import com.retailbus.inventory.repository.InventoryHistoryRepository;
import com.retailbus.inventory.service.InventoryService;
import com.retailbus.product.dto.response.ProductResponse;
import com.retailbus.product.entity.Product;
import com.retailbus.product.exception.ProductNotFoundException;
import com.retailbus.product.mapper.ProductMapper;
import com.retailbus.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * InventoryService Implementation.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;
    private final InventoryHistoryRepository inventoryHistoryRepository;
    private final InventoryMapper inventoryMapper;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getInventoryStatus() {
        log.info("Fetching inventory status for all active products");
        return productRepository.findByActiveTrue().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LowStockProductResponse> getLowStockProducts() {
        log.info("Fetching low stock products");
        return productRepository.findByActiveTrue().stream()
                .filter(product -> product.getQuantityInStock() <= product.getReorderLevel())
                .map(inventoryMapper::toLowStockResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getOutOfStockProducts() {
        log.info("Fetching out-of-stock products");
        return productRepository.findByActiveTrue().stream()
                .filter(product -> product.getQuantityInStock() == 0)
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryHistoryResponse adjustStock(StockAdjustmentRequest request) {
        log.info("Adjusting stock for product ID: {} by quantity: {}",
                request.getProductId(), request.getAdjustmentQuantity());

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", request.getProductId());
                    return new ProductNotFoundException("Product not found with ID: " + request.getProductId());
                });

        // Validate that adjustment won't make stock negative
        int newStock = product.getQuantityInStock() + request.getAdjustmentQuantity();
        if (newStock < 0) {
            log.error("Adjustment would result in negative stock. Current: {}, Adjustment: {}",
                    product.getQuantityInStock(), request.getAdjustmentQuantity());
            throw new InventoryException("Adjustment would result in negative stock. Current: " +
                    product.getQuantityInStock() + ", Requested adjustment: " + request.getAdjustmentQuantity());
        }

        return recordTransaction(
                product.getId(),
                InventoryTransactionType.ADJUSTMENT,
                request.getAdjustmentQuantity(),
                null,
                request.getReason() + (request.getNotes() != null ? " - " + request.getNotes() : "")
        );
    }

    @Override
    public InventoryHistoryResponse recordTransaction(Long productId, InventoryTransactionType type,
                                                       Integer quantityChanged, String reference, String notes) {
        log.info("Recording inventory transaction: Product ID: {}, Type: {}, Quantity: {}",
                productId, type, quantityChanged);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));

        int quantityBefore = product.getQuantityInStock();
        int quantityAfter = quantityBefore + quantityChanged;

        // Prevent negative stock
        if (quantityAfter < 0) {
            log.error("Transaction would result in negative stock. Before: {}, Change: {}",
                    quantityBefore, quantityChanged);
            throw new InventoryException("Insufficient stock for this transaction");
        }

        // Update product stock
        product.setQuantityInStock(quantityAfter);
        productRepository.save(product);

        // Record transaction history
        InventoryHistory history = InventoryHistory.builder()
                .product(product)
                .transactionType(type)
                .quantityChanged(quantityChanged)
                .quantityBefore(quantityBefore)
                .quantityAfter(quantityAfter)
                .reference(reference)
                .notes(notes)
                .build();

        InventoryHistory savedHistory = inventoryHistoryRepository.save(history);
        log.info("Transaction recorded successfully. Product stock updated from {} to {}",
                quantityBefore, quantityAfter);

        return inventoryMapper.toHistoryResponse(savedHistory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponse> getProductInventoryHistory(Long productId) {
        log.info("Fetching inventory history for product ID: {}", productId);

        // Verify product exists
        if (!productRepository.existsById(productId)) {
            log.error("Product not found with ID: {}", productId);
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }

        return inventoryHistoryRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(inventoryMapper::toHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponse> getTransactionHistory(InventoryTransactionType type) {
        log.info("Fetching transaction history for type: {}", type);
        return inventoryHistoryRepository.findByTransactionTypeOrderByCreatedAtDesc(type).stream()
                .map(inventoryMapper::toHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponse> getInventoryHistoryByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching inventory history between {} and {}", start, end);
        return inventoryHistoryRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end).stream()
                .map(inventoryMapper::toHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryHistoryResponse> getTransactionsByReference(String reference) {
        log.info("Fetching transactions for reference: {}", reference);
        return inventoryHistoryRepository.findByReferenceOrderByCreatedAtDesc(reference).stream()
                .map(inventoryMapper::toHistoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getLowStockCount() {
        log.info("Counting low stock products");
        return productRepository.findByActiveTrue().stream()
                .filter(product -> product.getQuantityInStock() <= product.getReorderLevel())
                .count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long getOutOfStockCount() {
        log.info("Counting out-of-stock products");
        return productRepository.findByActiveTrue().stream()
                .filter(product -> product.getQuantityInStock() == 0)
                .count();
    }
}

