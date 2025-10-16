package com.restaurant.stockmanagement.model;

public enum TransactionType {
    PURCHASE,      // Stock added via purchase
    SALE,          // Stock reduced via sale
    WASTE,         // Stock removed due to expiry/damage
    ADJUSTMENT,    // Manual stock adjustment
    RETURN,        // Return from supplier
    TRANSFER       // Transfer between branches
}
