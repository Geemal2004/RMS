package com.restaurant.stockmanagement.repository;

import com.restaurant.stockmanagement.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    
    Page<Ingredient> findByDeletedFalse(Pageable pageable);
    
    @Query("SELECT i FROM Ingredient i WHERE i.deleted = false AND i.currentStock < i.minimumStock")
    List<Ingredient> findLowStockIngredients();
    
    @Query("SELECT i FROM Ingredient i WHERE i.deleted = false AND i.currentStock = 0")
    List<Ingredient> findOutOfStockIngredients();
    
    @Query("SELECT i FROM Ingredient i WHERE i.deleted = false AND i.expiryDate BETWEEN :startDate AND :endDate")
    List<Ingredient> findIngredientsByExpiryDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT i FROM Ingredient i WHERE i.deleted = false AND i.expiryDate < :date")
    List<Ingredient> findExpiredIngredients(LocalDate date);
    
    @Query("SELECT i FROM Ingredient i WHERE i.deleted = false AND " +
           "(LOWER(i.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.category) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Ingredient> searchIngredients(String search, Pageable pageable);
    
    List<Ingredient> findByDeletedFalseAndCategory(String category);
    
    @Query("SELECT i FROM Ingredient i WHERE i.deleted = false AND i.branchId = :branchId")
    Page<Ingredient> findByBranchId(Long branchId, Pageable pageable);
}
