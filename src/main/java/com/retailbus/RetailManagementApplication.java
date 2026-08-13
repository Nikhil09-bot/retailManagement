package com.retailbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for Retail Management System application.
 *
 * This Spring Boot application manages operations for independent convenience stores
 * and gas stations including inventory, sales, customers, and reporting.
 */
@SpringBootApplication
public class RetailManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(RetailManagementApplication.class, args);
    }
}

