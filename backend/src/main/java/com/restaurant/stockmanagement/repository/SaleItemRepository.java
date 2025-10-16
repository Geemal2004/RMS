package com.restaurant.stockmanagement.repository;

import com.restaurant.stockmanagement.model.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {
    
    List<SaleItem> findBySaleId(Long saleId);
    
    @Query("SELECT si FROM SaleItem si WHERE si.sale.saleDate BETWEEN :startDate AND :endDate")
    List<SaleItem> findSaleItemsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT si.food.id, si.food.name, SUM(si.quantity), SUM(si.subtotal) " +
           "FROM SaleItem si WHERE si.sale.deleted = false AND " +
           "si.sale.saleDate BETWEEN :startDate AND :endDate " +
           "GROUP BY si.food.id, si.food.name ORDER BY SUM(si.quantity) DESC")
    List<Object[]> findMostSoldFoods(LocalDateTime startDate, LocalDateTime endDate);
}
