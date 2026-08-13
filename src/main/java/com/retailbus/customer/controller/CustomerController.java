package com.retailbus.customer.controller;

import com.retailbus.dto.ApiResponse;
import com.retailbus.customer.dto.CustomerRequest;
import com.retailbus.customer.dto.CustomerResponse;
import com.retailbus.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * CustomerController.
 * REST API endpoints for Customer management.
 */
@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    /** POST /api/customers — Create */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        log.info("POST request to create customer: {}", request.getFirstName());
        CustomerResponse response = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully", response));
    }

    /** GET /api/customers — All customers */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponse.success("Customers retrieved successfully", customers));
    }

    /** GET /api/customers/{id} — By ID */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCustomerById(@PathVariable Long id) {
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully", response));
    }

    /** PUT /api/customers/{id} — Update */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateCustomer(
            @PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        log.info("PUT request to update customer ID: {}", id);
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", response));
    }

    /** GET /api/customers/phone/{phone} — Find by phone */
    @GetMapping("/phone/{phone}")
    public ResponseEntity<ApiResponse<?>> getCustomerByPhone(@PathVariable String phone) {
        CustomerResponse response = customerService.getCustomerByPhone(phone);
        return ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully", response));
    }

    /** GET /api/customers/search?keyword= — Search */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchCustomers(@RequestParam String keyword) {
        List<CustomerResponse> customers = customerService.searchCustomers(keyword);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", customers));
    }

    /** GET /api/customers/city/{city} — By city */
    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse<?>> getCustomersByCity(@PathVariable String city) {
        List<CustomerResponse> customers = customerService.getCustomersByCity(city);
        return ResponseEntity.ok(ApiResponse.success("Customers retrieved by city successfully", customers));
    }

    /** PATCH /api/customers/{id}/loyalty-points?points=10 — Add loyalty points */
    @PatchMapping("/{id}/loyalty-points")
    public ResponseEntity<ApiResponse<?>> addLoyaltyPoints(
            @PathVariable Long id, @RequestParam BigDecimal points) {
        log.info("PATCH request to add {} loyalty points to customer ID: {}", points, id);
        CustomerResponse response = customerService.addLoyaltyPoints(id, points);
        return ResponseEntity.ok(ApiResponse.success("Loyalty points added successfully", response));
    }

    /** PATCH /api/customers/{id}/record-purchase?amount=25.99 — Record a purchase */
    @PatchMapping("/{id}/record-purchase")
    public ResponseEntity<ApiResponse<?>> recordPurchase(
            @PathVariable Long id, @RequestParam BigDecimal amount) {
        log.info("PATCH request to record purchase of ${} for customer ID: {}", amount, id);
        CustomerResponse response = customerService.recordPurchase(id, amount);
        return ResponseEntity.ok(ApiResponse.success("Purchase recorded successfully", response));
    }

    /** DELETE /api/customers/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
    }

    /** GET /api/customers/count — For dashboard */
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<?>> getCustomerCount() {
        Long count = customerService.getCustomerCount();
        return ResponseEntity.ok(ApiResponse.success("Customer count retrieved successfully", count));
    }
}

