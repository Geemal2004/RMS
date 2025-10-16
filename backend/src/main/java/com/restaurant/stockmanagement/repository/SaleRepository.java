package com.restaurant.stockmanagement.repository;

import com.restaurant.stockmanagement.model.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    
    Page<Sale> findByDeletedFalse(Pageable pageable);
    
    @Query("SELECT s FROM Sale s WHERE s.deleted = false AND " +
           "s.saleDate BETWEEN :startDate AND :endDate")
    List<Sale> findSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT s FROM Sale s WHERE s.deleted = false AND s.cashierUsername = :cashierUsername")
    Page<Sale> findByCashierUsername(String cashierUsername, Pageable pageable);
    
    @Query("SELECT SUM(s.totalAmount) FROM Sale s WHERE s.deleted = false AND " +
           "s.saleDate BETWEEN :startDate AND :endDate")
    Double getTotalSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT s FROM Sale s WHERE s.deleted = false AND s.branchId = :branchId")
    Page<Sale> findByBranchId(Long branchId, Pageable pageable);
}
