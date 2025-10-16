package com.restaurant.stockmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StockTransaction extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
    
    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @NotNull(message = "Quantity is required")
    @Column(nullable = false)
    private Double quantity;
    
    @Column(nullable = false)
    private Double previousStock;
    
    @Column(nullable = false)
    private Double newStock;
    
    private String reason;
    
    private String performedBy;
    
    @Column(name = "branch_id")
    private Long branchId;
}
