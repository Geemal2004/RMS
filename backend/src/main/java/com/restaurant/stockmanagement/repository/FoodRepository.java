package com.restaurant.stockmanagement.repository;

import com.restaurant.stockmanagement.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    
    Page<Food> findByDeletedFalse(Pageable pageable);
    
    List<Food> findByDeletedFalseAndAvailableTrue();
    
    @Query("SELECT f FROM Food f WHERE f.deleted = false AND " +
           "(LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(f.category) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Food> searchFoods(String search, Pageable pageable);
    
    List<Food> findByDeletedFalseAndCategory(String category);
    
    @Query("SELECT f FROM Food f WHERE f.deleted = false AND f.branchId = :branchId")
    Page<Food> findByBranchId(Long branchId, Pageable pageable);
}
