package com.retailbus.sales.exception;

/**
 * Exception thrown when an invalid sale operation is attempted.
 * e.g. cancelling an already-cancelled sale.
 */
public class InvalidSaleOperationException extends RuntimeException {

    public InvalidSaleOperationException(String message) {
        super(message);
    }

    public InvalidSaleOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}

