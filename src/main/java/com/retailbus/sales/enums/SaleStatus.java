package com.retailbus.sales.enums;

/**
 * Sale Status.
 */
public enum SaleStatus {
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    RETURNED("Returned");

    private final String description;

    SaleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

