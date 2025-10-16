package com.restaurant.stockmanagement.repository;

import com.restaurant.stockmanagement.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    
    List<RecipeIngredient> findByFoodId(Long foodId);
    
    List<RecipeIngredient> findByIngredientId(Long ingredientId);
    
    @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.food.id = :foodId AND ri.deleted = false")
    List<RecipeIngredient> findActiveRecipesByFoodId(Long foodId);
    
    void deleteByFoodIdAndIngredientId(Long foodId, Long ingredientId);
}
