package com.restaurant.stockmanagement.model;

public enum AlertType {
    LOW_STOCK,      // Stock below minimum threshold
    OUT_OF_STOCK,   // Stock is zero
    EXPIRING_SOON,  // Stock expiring within configured days
    EXPIRED         // Stock has expired
}
