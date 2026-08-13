package com.retailbus.sales.exception;

/**
 * Exception thrown when a sale is not found.
 */
public class SaleNotFoundException extends RuntimeException {

    public SaleNotFoundException(String message) {
        super(message);
    }

    public SaleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

