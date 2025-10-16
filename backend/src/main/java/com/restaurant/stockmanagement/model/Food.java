package com.restaurant.stockmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "foods")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Food extends BaseEntity {
    
    @NotBlank(message = "Food name is required")
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @NotNull(message = "Price is required")
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    private String category; // Appetizer, Main Course, Dessert, Beverage, etc.
    
    private String imageUrl;
    
    private Integer preparationTime; // in minutes
    
    @Column(name = "branch_id")
    private Long branchId;
    
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> saleItems = new ArrayList<>();
}
