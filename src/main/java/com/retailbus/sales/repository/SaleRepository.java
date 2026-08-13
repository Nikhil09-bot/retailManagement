package com.retailbus.sales.repository;

import com.retailbus.sales.entity.Sale;
import com.retailbus.sales.enums.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * SaleRepository.
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByReceiptNumber(String receiptNumber);

    List<Sale> findByStatus(SaleStatus status);

    List<Sale> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Sale s WHERE DATE(s.createdAt) = CURRENT_DATE ORDER BY s.createdAt DESC")
    List<Sale> findTodaysSales();

    Long countByStatusAndCreatedAtBetween(SaleStatus status, LocalDateTime start, LocalDateTime end);
}

