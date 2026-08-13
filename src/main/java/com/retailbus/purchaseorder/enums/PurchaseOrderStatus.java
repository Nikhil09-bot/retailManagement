package com.retailbus.purchaseorder.enums;

/**
 * Purchase Order Status.
 */
public enum PurchaseOrderStatus {
    PENDING("Pending"),
    RECEIVED("Received"),
    PARTIALLY_RECEIVED("Partially Received"),
    CANCELLED("Cancelled");

    private final String description;

    PurchaseOrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

