package com.retailbus.product.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Unit of measurement enumeration.
 * Represents different units in which products are measured and sold.
 */
public enum Unit {
    PIECE("Piece"),
    PACK("Pack"),
    BOX("Box"),
    BOTTLE("Bottle"),
    CAN("Can"),
    LITER("Liter"),
    KG("Kilogram"),
    GRAM("Gram");

    private final String displayName;

    Unit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Unit fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String normalizedInput = normalize(value);

        for (Unit unit : values()) {
            if (normalize(unit.name()).equals(normalizedInput)
                    || normalize(unit.displayName).equals(normalizedInput)) {
                return unit;
            }
        }

        throw new IllegalArgumentException("Unsupported unit: " + value);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s_-]", "").toLowerCase();
    }
}

