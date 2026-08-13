package com.retailbus.supplier.exception;

/**
 * Exception thrown when duplicate supplier code is detected.
 */
public class DuplicateSupplierException extends RuntimeException {

    public DuplicateSupplierException(String message) {
        super(message);
    }

    public DuplicateSupplierException(String message, Throwable cause) {
        super(message, cause);
    }
}

