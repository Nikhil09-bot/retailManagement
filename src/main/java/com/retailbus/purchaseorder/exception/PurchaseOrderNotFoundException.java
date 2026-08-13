package com.retailbus.purchaseorder.exception;

/**
 * Exception thrown when a purchase order is not found.
 */
public class PurchaseOrderNotFoundException extends RuntimeException {

    public PurchaseOrderNotFoundException(String message) {
        super(message);
    }

    public PurchaseOrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

