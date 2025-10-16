package com.restaurant.stockmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StockAlert extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType alertType;
    
    @Column(nullable = false)
    private String message;
    
    @Column(nullable = false)
    private Boolean acknowledged = false;
    
    private LocalDateTime acknowledgedAt;
    
    private String acknowledgedBy;
    
    @Column(name = "branch_id")
    private Long branchId;
}
