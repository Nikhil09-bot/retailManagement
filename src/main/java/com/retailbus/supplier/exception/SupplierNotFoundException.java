package com.retailbus.supplier.exception;

/**
 * Exception thrown when supplier is not found.
 */
public class SupplierNotFoundException extends RuntimeException {

    public SupplierNotFoundException(String message) {
        super(message);
    }

    public SupplierNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

