package com.restaurant.stockmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ingredient extends BaseEntity {
    
    @NotBlank(message = "Ingredient name is required")
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @NotBlank(message = "Unit of measurement is required")
    @Column(nullable = false)
    private String unit; // kg, g, l, ml, pieces, etc.
    
    @NotNull(message = "Current stock is required")
    @Column(nullable = false)
    private Double currentStock = 0.0;
    
    @NotNull(message = "Minimum stock threshold is required")
    @Column(nullable = false)
    private Double minimumStock;
    
    private Double reorderLevel;
    
    private LocalDate expiryDate;
    
    @Column(nullable = false)
    private Double costPerUnit = 0.0;
    
    @Column(name = "branch_id")
    private Long branchId;
    
    private String category; // Vegetables, Meat, Dairy, Spices, etc.
    
    private String imageUrl;
    
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockTransaction> transactions = new ArrayList<>();
    
    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
}
