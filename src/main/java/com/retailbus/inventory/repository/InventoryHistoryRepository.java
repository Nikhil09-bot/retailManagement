package com.retailbus.inventory.repository;

import com.retailbus.inventory.entity.InventoryHistory;
import com.retailbus.inventory.enums.InventoryTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * InventoryHistory Repository.
 */
@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Long> {

    /**
     * Find inventory history for a product.
     */
    List<InventoryHistory> findByProductIdOrderByCreatedAtDesc(Long productId);

    /**
     * Find inventory transactions of a specific type.
     */
    List<InventoryHistory> findByTransactionTypeOrderByCreatedAtDesc(InventoryTransactionType type);

    /**
     * Find inventory transactions within a date range.
     */
    List<InventoryHistory> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    /**
     * Find by reference (e.g., PO number, Sale ID).
     */
    List<InventoryHistory> findByReferenceOrderByCreatedAtDesc(String reference);
}

