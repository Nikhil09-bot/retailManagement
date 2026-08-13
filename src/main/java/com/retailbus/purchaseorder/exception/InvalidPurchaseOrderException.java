package com.retailbus.purchaseorder.exception;

/**
 * Exception thrown when an invalid purchase order operation is performed.
 */
public class InvalidPurchaseOrderException extends RuntimeException {

    public InvalidPurchaseOrderException(String message) {
        super(message);
    }

    public InvalidPurchaseOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}

