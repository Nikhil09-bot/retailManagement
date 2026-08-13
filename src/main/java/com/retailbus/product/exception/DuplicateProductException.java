package com.retailbus.product.exception;

/**
 * Exception thrown when attempting to create a product with a duplicate product code.
 */
public class DuplicateProductException extends RuntimeException {

    public DuplicateProductException(String message) {
        super(message);
    }

    public DuplicateProductException(String message, Throwable cause) {
        super(message, cause);
    }
}

