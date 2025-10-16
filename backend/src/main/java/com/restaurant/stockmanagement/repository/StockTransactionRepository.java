package com.restaurant.stockmanagement.repository;

import com.restaurant.stockmanagement.model.StockTransaction;
import com.restaurant.stockmanagement.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    
    Page<StockTransaction> findByDeletedFalse(Pageable pageable);
    
    List<StockTransaction> findByIngredientIdAndDeletedFalse(Long ingredientId);
    
    Page<StockTransaction> findByIngredientIdAndDeletedFalse(Long ingredientId, Pageable pageable);
    
    @Query("SELECT st FROM StockTransaction st WHERE st.deleted = false AND " +
           "st.createdAt BETWEEN :startDate AND :endDate")
    List<StockTransaction> findTransactionsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT st FROM StockTransaction st WHERE st.deleted = false AND " +
           "st.ingredient.id = :ingredientId AND st.type = :type")
    List<StockTransaction> findByIngredientAndType(Long ingredientId, TransactionType type);
    
    @Query("SELECT st.ingredient.id, COUNT(st), SUM(st.quantity) FROM StockTransaction st " +
           "WHERE st.deleted = false AND st.type = 'SALE' AND " +
           "st.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY st.ingredient.id ORDER BY COUNT(st) DESC")
    List<Object[]> findMostUsedIngredients(LocalDateTime startDate, LocalDateTime endDate);
}
