package com.retailbus.exception;

/**
 * Custom exception for business logic violations.
 * Thrown when a business rule is violated or operation cannot be performed.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

