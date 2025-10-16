package com.restaurant.stockmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sale_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaleItem extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;
    
    @NotNull(message = "Quantity is required")
    @Column(nullable = false)
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    @Column(nullable = false)
    private Double unitPrice;
    
    @NotNull(message = "Subtotal is required")
    @Column(nullable = false)
    private Double subtotal;
}
