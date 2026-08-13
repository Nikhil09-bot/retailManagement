package com.retailbus.product.exception;

/**
 * Exception thrown when an invalid stock operation is attempted.
 * For example, decreasing stock below zero.
 */
public class InvalidStockOperationException extends RuntimeException {

    public InvalidStockOperationException(String message) {
        super(message);
    }

    public InvalidStockOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

