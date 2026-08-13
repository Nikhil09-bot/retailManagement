package com.retailbus.inventory.enums;

/**
 * Inventory transaction type enumeration.
 * Tracks the reason for inventory changes.
 */
public enum InventoryTransactionType {
    INITIAL("Initial Stock"),
    STOCK_IN("Stock Received"),
    STOCK_OUT("Stock Sold"),
    ADJUSTMENT("Stock Adjustment"),
    DAMAGE("Damaged/Loss"),
    RETURN("Customer Return"),
    PURCHASE_ORDER_RECEIVED("Purchase Order Received"),
    CANCELLED_SALE("Sale Cancelled");

    private final String description;

    InventoryTransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

