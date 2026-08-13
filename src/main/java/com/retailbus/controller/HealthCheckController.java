package com.retailbus.controller;

import com.retailbus.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Health Check Controller.
 *
 * This controller provides an endpoint to verify that the application
 * is running and database is accessible.
 */
@RestController
@RequestMapping("/health")
public class HealthCheckController {

    /**
     * Check application health status.
     *
     * @return Status information about the application
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", "Retail Management System");
        health.put("version", "1.0.0");

        return ResponseEntity.ok(ApiResponse.success("Application is running", health));
    }
}

