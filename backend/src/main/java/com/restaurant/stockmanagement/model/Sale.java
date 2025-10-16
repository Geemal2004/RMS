package com.restaurant.stockmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Sale extends BaseEntity {
    
    @NotNull(message = "Sale date is required")
    @Column(nullable = false)
    private LocalDateTime saleDate;
    
    @NotNull(message = "Total amount is required")
    @Column(nullable = false)
    private Double totalAmount;
    
    @Column(nullable = false)
    private String cashierUsername;
    
    @Column(name = "branch_id")
    private Long branchId;
    
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items = new ArrayList<>();
    
    private String paymentMethod; // CASH, CARD, ONLINE
    
    private String notes;
}
