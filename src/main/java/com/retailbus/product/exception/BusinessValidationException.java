package com.retailbus.product.exception;

/**
 * Exception thrown when business validation rules are violated.
 * For example, selling price must be greater than purchase price.
 */
public class BusinessValidationException extends RuntimeException {

    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

