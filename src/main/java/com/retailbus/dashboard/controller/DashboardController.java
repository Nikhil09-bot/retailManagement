package com.retailbus.dashboard.controller;

import com.retailbus.customer.repository.CustomerRepository;
import com.retailbus.dashboard.dto.DashboardStats;
import com.retailbus.dashboard.service.DashboardService;
import com.retailbus.dto.ApiResponse;
import com.retailbus.inventory.service.InventoryService;
import com.retailbus.product.repository.ProductRepository;
import com.retailbus.sales.service.SaleService;
import com.retailbus.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * DashboardController.
 * REST API endpoints for dashboard statistics.
 */
@Slf4j
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;
    private final SupplierRepository supplierRepository;
    private final CustomerRepository customerRepository;
    private final SaleService saleService;

    /**
     * GET /api/dashboard/stats — All stats in one call
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<?>> getDashboardStats() {
        log.info("GET request for full dashboard stats");
        DashboardStats stats = dashboardService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved successfully", stats));
    }

    /**
     * GET /api/dashboard/products — Product summary
     */
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<?>> getProductSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("total", productRepository.count());
        summary.put("active", productRepository.findByActiveTrue().size());
        summary.put("lowStock", inventoryService.getLowStockCount());
        summary.put("outOfStock", inventoryService.getOutOfStockCount());
        return ResponseEntity.ok(ApiResponse.success("Product summary retrieved successfully", summary));
    }

    /**
     * GET /api/dashboard/inventory — Inventory summary
     */
    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<?>> getInventorySummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("lowStockCount", inventoryService.getLowStockCount());
        summary.put("outOfStockCount", inventoryService.getOutOfStockCount());
        summary.put("lowStockProducts", inventoryService.getLowStockProducts());
        return ResponseEntity.ok(ApiResponse.success("Inventory summary retrieved successfully", summary));
    }

    /**
     * GET /api/dashboard/sales/today — Today's sales stats
     */
    @GetMapping("/sales/today")
    public ResponseEntity<ApiResponse<?>> getTodaysSalesSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("count", saleService.getTodaysSalesCount());
        summary.put("revenue", saleService.getTodaysRevenue());
        return ResponseEntity.ok(ApiResponse.success("Today's sales summary retrieved successfully", summary));
    }

    /**
     * GET /api/dashboard/sales/monthly?month=7&year=2026 — Monthly stats
     */
    @GetMapping("/sales/monthly")
    public ResponseEntity<ApiResponse<?>> getMonthlySalesSummary(
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int month,
            @org.springframework.web.bind.annotation.RequestParam(defaultValue = "0") int year) {

        int targetMonth = month > 0 ? month : java.time.LocalDate.now().getMonthValue();
        int targetYear = year > 0 ? year : java.time.LocalDate.now().getYear();

        Map<String, Object> summary = new HashMap<>();
        summary.put("month", targetMonth);
        summary.put("year", targetYear);
        summary.put("revenue", saleService.getMonthlyRevenue(targetMonth, targetYear));
        return ResponseEntity.ok(ApiResponse.success("Monthly sales summary retrieved successfully", summary));
    }

    /**
     * GET /api/dashboard/suppliers — Supplier summary
     */
    @GetMapping("/suppliers")
    public ResponseEntity<ApiResponse<?>> getSupplierSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("total", supplierRepository.count());
        summary.put("active", supplierRepository.findByActiveTrue().size());
        return ResponseEntity.ok(ApiResponse.success("Supplier summary retrieved successfully", summary));
    }

    /**
     * GET /api/dashboard/customers — Customer summary
     */
    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<?>> getCustomerSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("total", customerRepository.count());
        return ResponseEntity.ok(ApiResponse.success("Customer summary retrieved successfully", summary));
    }
}

