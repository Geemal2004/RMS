package com.restaurant.stockmanagement.model;

public enum Role {
    ROLE_OWNER,          // Admin - Full access
    ROLE_BRANCH_MANAGER, // Admin - Branch level access
    ROLE_CASHIER,        // User - Sales operations
    ROLE_CHEF,           // User - Kitchen operations
    ROLE_STOCK_MANAGER   // User - Stock management operations
}
