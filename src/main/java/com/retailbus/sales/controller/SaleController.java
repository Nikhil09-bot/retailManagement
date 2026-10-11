package com.retailbus.sales.controller;

import com.retailbus.dto.ApiResponse;
import com.retailbus.sales.dto.CreateSaleRequest;
import com.retailbus.sales.dto.SaleResponse;
import com.retailbus.sales.service.SaleInvoiceService;
import com.retailbus.sales.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * SaleController.
 * REST API endpoints for Sales/Point-of-Sale management.
 */
@Slf4j
@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;
    private final SaleInvoiceService saleInvoiceService;

    /** POST /api/sales — Create a new sale (auto-deducts inventory) */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSale(@Valid @RequestBody CreateSaleRequest request) {
        log.info("POST request to create a new sale with {} items", request.getItems().size());
        SaleResponse response = saleService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sale completed successfully", response));
    }

    /** GET /api/sales — All sales */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSales() {
        List<SaleResponse> sales = saleService.getAllSales();
        return ResponseEntity.ok(ApiResponse.success("Sales retrieved successfully", sales));
    }

    /** GET /api/sales/{id} — Sale by ID */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getSaleById(@PathVariable Long id) {
        SaleResponse response = saleService.getSaleById(id);
        return ResponseEntity.ok(ApiResponse.success("Sale retrieved successfully", response));
    }

    /** GET /api/sales/receipt/{receiptNumber} — Find by receipt number */
    @GetMapping("/receipt/{receiptNumber}")
    public ResponseEntity<ApiResponse<?>> getSaleByReceiptNumber(@PathVariable String receiptNumber) {
        SaleResponse response = saleService.getSaleByReceiptNumber(receiptNumber);
        return ResponseEntity.ok(ApiResponse.success("Sale retrieved successfully", response));
    }

    /** GET /api/sales/today — Today's sales */
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<?>> getTodaysSales() {
        List<SaleResponse> sales = saleService.getTodaysSales();
        return ResponseEntity.ok(ApiResponse.success("Today's sales retrieved successfully", sales));
    }

    /** GET /api/sales/date-range?start=&end= — By date range */
    @GetMapping("/date-range")
    public ResponseEntity<ApiResponse<?>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<SaleResponse> sales = saleService.getSalesByDateRange(start, end);
        return ResponseEntity.ok(ApiResponse.success("Sales retrieved by date range successfully", sales));
    }

    /** GET /api/sales/{id}/invoice/pdf — Download the PDF invoice/receipt for a sale */
    @GetMapping("/{id}/invoice/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) {
        log.info("GET request to download PDF invoice for sale ID: {}", id);
        byte[] pdf = saleInvoiceService.generateInvoicePdf(id);

        String filename = "invoice-sale-" + id + ".pdf";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename).build().toString())
                .body(pdf);
    }

    /** PATCH /api/sales/{id}/cancel — Cancel a sale (restores inventory) */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelSale(@PathVariable Long id) {
        log.info("PATCH request to cancel sale ID: {}", id);
        SaleResponse response = saleService.cancelSale(id);
        return ResponseEntity.ok(ApiResponse.success("Sale cancelled. Inventory restored.", response));
    }

    /** GET /api/sales/revenue/today — Today's revenue total */
    @GetMapping("/revenue/today")
    public ResponseEntity<ApiResponse<?>> getTodaysRevenue() {
        BigDecimal revenue = saleService.getTodaysRevenue();
        return ResponseEntity.ok(ApiResponse.success("Today's revenue retrieved successfully", revenue));
    }

    /** GET /api/sales/revenue/monthly?month=7&year=2026 — Monthly revenue */
    @GetMapping("/revenue/monthly")
    public ResponseEntity<ApiResponse<?>> getMonthlyRevenue(
            @RequestParam int month,
            @RequestParam int year) {
        BigDecimal revenue = saleService.getMonthlyRevenue(month, year);
        return ResponseEntity.ok(ApiResponse.success("Monthly revenue retrieved successfully", revenue));
    }
}

