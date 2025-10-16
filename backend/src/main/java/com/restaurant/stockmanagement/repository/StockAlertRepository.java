package com.restaurant.stockmanagement.repository;

import com.restaurant.stockmanagement.model.AlertType;
import com.restaurant.stockmanagement.model.StockAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAlertRepository extends JpaRepository<StockAlert, Long> {
    
    Page<StockAlert> findByDeletedFalse(Pageable pageable);
    
    @Query("SELECT sa FROM StockAlert sa WHERE sa.deleted = false AND sa.acknowledged = false")
    List<StockAlert> findUnacknowledgedAlerts();
    
    Page<StockAlert> findByDeletedFalseAndAcknowledgedFalse(Pageable pageable);
    
    List<StockAlert> findByDeletedFalseAndIngredientIdAndAlertType(Long ingredientId, AlertType alertType);
    
    @Query("SELECT sa FROM StockAlert sa WHERE sa.deleted = false AND sa.branchId = :branchId AND sa.acknowledged = false")
    List<StockAlert> findUnacknowledgedAlertsByBranch(Long branchId);
}
