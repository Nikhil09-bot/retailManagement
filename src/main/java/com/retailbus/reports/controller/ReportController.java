package com.retailbus.reports.controller;

import com.retailbus.dto.ApiResponse;
import com.retailbus.reports.dto.SalesReport;
import com.retailbus.reports.dto.TopSellingProduct;
import com.retailbus.reports.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ReportController.
 * REST API endpoints for business reports.
 */
@Slf4j
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * GET /api/reports/sales/daily?date=2026-07-13 — Daily sales report
     */
    @GetMapping("/sales/daily")
    public ResponseEntity<ApiResponse<?>> getDailySalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        log.info("GET request for daily sales report: {}", targetDate);
        SalesReport report = reportService.getDailySalesReport(targetDate);
        return ResponseEntity.ok(ApiResponse.success("Daily sales report generated", report));
    }

    /**
     * GET /api/reports/sales/weekly?weekStart=2026-07-07 — Weekly sales report
     */
    @GetMapping("/sales/weekly")
    public ResponseEntity<ApiResponse<?>> getWeeklySalesReport(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        LocalDate startDate = weekStart != null ? weekStart : LocalDate.now();
        log.info("GET request for weekly sales report starting: {}", startDate);
        SalesReport report = reportService.getWeeklySalesReport(startDate);
        return ResponseEntity.ok(ApiResponse.success("Weekly sales report generated", report));
    }

    /**
     * GET /api/reports/sales/monthly?month=7&year=2026 — Monthly sales report
     */
    @GetMapping("/sales/monthly")
    public ResponseEntity<ApiResponse<?>> getMonthlySalesReport(
            @RequestParam(defaultValue = "0") int month,
            @RequestParam(defaultValue = "0") int year) {
        int targetMonth = month > 0 ? month : LocalDate.now().getMonthValue();
        int targetYear = year > 0 ? year : LocalDate.now().getYear();
        log.info("GET request for monthly sales report: {}/{}", targetMonth, targetYear);
        SalesReport report = reportService.getMonthlySalesReport(targetMonth, targetYear);
        return ResponseEntity.ok(ApiResponse.success("Monthly sales report generated", report));
    }

    /**
     * GET /api/reports/top-products?start=&end=&limit=10 — Top-selling products
     */
    @GetMapping("/top-products")
    public ResponseEntity<ApiResponse<?>> getTopSellingProducts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "10") int limit) {

        LocalDateTime from = start != null ? start.atStartOfDay() :
                LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime to = end != null ? end.atTime(23, 59, 59) : LocalDateTime.now();

        log.info("GET request for top {} selling products from {} to {}", limit, from, to);
        List<TopSellingProduct> products = reportService.getTopSellingProducts(from, to, limit);
        return ResponseEntity.ok(ApiResponse.success("Top selling products report generated", products));
    }

    /**
     * GET /api/reports/inventory — Inventory report
     */
    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<?>> getInventoryReport() {
        log.info("GET request for inventory report");
        Map<String, Object> report = reportService.getInventoryReport();
        return ResponseEntity.ok(ApiResponse.success("Inventory report generated", report));
    }

    /**
     * GET /api/reports/revenue-trend?months=6 — Revenue trend (last N months)
     */
    @GetMapping("/revenue-trend")
    public ResponseEntity<ApiResponse<?>> getRevenueTrend(
            @RequestParam(defaultValue = "6") int months) {
        log.info("GET request for revenue trend over {} months", months);
        List<Map<String, Object>> trend = reportService.getRevenueTrend(months);
        return ResponseEntity.ok(ApiResponse.success("Revenue trend generated", trend));
    }
}

