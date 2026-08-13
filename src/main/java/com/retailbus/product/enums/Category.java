package com.retailbus.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Product category enumeration.
 * Represents different categories of products available in the store.
 */
public enum Category {
    BEVERAGES("Beverages"),
    SNACKS("Snacks"),
    DAIRY("Dairy"),
    GROCERY("Grocery"),
    HOUSEHOLD("Household"),
    TOBACCO("Tobacco"),
    PERSONAL_CARE("Personal Care"),
    FROZEN("Frozen"),
    OTHER("Other");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Category fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalizedInput = normalize(value);

        for (Category category : values()) {
            if (normalize(category.name()).equals(normalizedInput)
                    || normalize(category.displayName).equals(normalizedInput)) {
                return category;
            }
        }

        throw new IllegalArgumentException("Unsupported category: " + value);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s_-]", "").toLowerCase();
    }
}

