# CODE PART 2: Remaining Services

Copy each file exactly as shown into IntelliJ IDEA.

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/service/FoodService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.dto.FoodDTO;
import com.restaurant.stockmanagement.dto.RecipeIngredientDTO;
import com.restaurant.stockmanagement.exception.ResourceNotFoundException;
import com.restaurant.stockmanagement.model.Food;
import com.restaurant.stockmanagement.model.Ingredient;
import com.restaurant.stockmanagement.model.RecipeIngredient;
import com.restaurant.stockmanagement.repository.FoodRepository;
import com.restaurant.stockmanagement.repository.IngredientRepository;
import com.restaurant.stockmanagement.repository.RecipeIngredientRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;

    public FoodService(FoodRepository foodRepository,
                      RecipeIngredientRepository recipeIngredientRepository,
                      IngredientRepository ingredientRepository) {
        this.foodRepository = foodRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Cacheable("foods")
    public Page<FoodDTO> getAllFoods(Pageable pageable) {
        return foodRepository.findByDeletedFalse(pageable)
                .map(this::mapToDTO);
    }

    @Cacheable(value = "food", key = "#id")
    public FoodDTO getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", "id", id));

        if (food.getDeleted()) {
            throw new ResourceNotFoundException("Food", "id", id);
        }

        return mapToDTO(food);
    }

    @Transactional
    @CacheEvict(value = {"foods", "food"}, allEntries = true)
    public FoodDTO createFood(FoodDTO dto) {
        Food food = mapToEntity(dto);
        food.setDeleted(false);

        // Check if food can be made with current stock
        food.setAvailable(checkFoodAvailability(dto.getRecipeIngredients()));

        Food saved = foodRepository.save(food);

        // Save recipe ingredients
        if (dto.getRecipeIngredients() != null) {
            for (RecipeIngredientDTO riDto : dto.getRecipeIngredients()) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setFood(saved);
                ri.setIngredient(ingredientRepository.findById(riDto.getIngredientId())
                        .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", riDto.getIngredientId())));
                ri.setQuantity(riDto.getQuantity());
                ri.setNotes(riDto.getNotes());
                recipeIngredientRepository.save(ri);
            }
        }

        return mapToDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"foods", "food"}, allEntries = true)
    public FoodDTO updateFood(Long id, FoodDTO dto) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", "id", id));

        food.setName(dto.getName());
        food.setDescription(dto.getDescription());
        food.setPrice(dto.getPrice());
        food.setCategory(dto.getCategory());
        food.setImageUrl(dto.getImageUrl());
        food.setPreparationTime(dto.getPreparationTime());

        // Update availability
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByFoodId(id);
        List<RecipeIngredientDTO> recipeDTOs = recipeIngredients.stream()
                .map(this::mapRecipeToDTO)
                .collect(Collectors.toList());
        food.setAvailable(checkFoodAvailability(recipeDTOs));

        Food updated = foodRepository.save(food);
        return mapToDTO(updated);
    }

    @Transactional
    @CacheEvict(value = {"foods", "food"}, allEntries = true)
    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food", "id", id));

        food.setDeleted(true);
        foodRepository.save(food);
    }

    public List<FoodDTO> getAvailableFoods() {
        return foodRepository.findByDeletedFalseAndAvailableTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Page<FoodDTO> searchFoods(String search, Pageable pageable) {
        return foodRepository.searchFoods(search, pageable)
                .map(this::mapToDTO);
    }

    @Transactional
    public void updateFoodAvailability(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException("Food", "id", foodId));

        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByFoodId(foodId);
        List<RecipeIngredientDTO> recipeDTOs = recipeIngredients.stream()
                .map(this::mapRecipeToDTO)
                .collect(Collectors.toList());

        boolean available = checkFoodAvailability(recipeDTOs);
        food.setAvailable(available);
        foodRepository.save(food);
    }

    private boolean checkFoodAvailability(List<RecipeIngredientDTO> recipeIngredients) {
        if (recipeIngredients == null || recipeIngredients.isEmpty()) {
            return true;
        }

        for (RecipeIngredientDTO ri : recipeIngredients) {
            Ingredient ingredient = ingredientRepository.findById(ri.getIngredientId())
                    .orElse(null);

            if (ingredient == null || ingredient.getCurrentStock() < ri.getQuantity()) {
                return false;
            }
        }

        return true;
    }

    private FoodDTO mapToDTO(Food food) {
        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setDescription(food.getDescription());
        dto.setPrice(food.getPrice());
        dto.setAvailable(food.getAvailable());
        dto.setCategory(food.getCategory());
        dto.setImageUrl(food.getImageUrl());
        dto.setPreparationTime(food.getPreparationTime());
        dto.setBranchId(food.getBranchId());

        // Load recipe ingredients
        List<RecipeIngredient> recipes = recipeIngredientRepository.findByFoodId(food.getId());
        dto.setRecipeIngredients(recipes.stream()
                .map(this::mapRecipeToDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private RecipeIngredientDTO mapRecipeToDTO(RecipeIngredient ri) {
        RecipeIngredientDTO dto = new RecipeIngredientDTO();
        dto.setId(ri.getId());
        dto.setFoodId(ri.getFood().getId());
        dto.setIngredientId(ri.getIngredient().getId());
        dto.setIngredientName(ri.getIngredient().getName());
        dto.setIngredientUnit(ri.getIngredient().getUnit());
        dto.setQuantity(ri.getQuantity());
        dto.setNotes(ri.getNotes());
        return dto;
    }

    private Food mapToEntity(FoodDTO dto) {
        Food food = new Food();
        food.setName(dto.getName());
        food.setDescription(dto.getDescription());
        food.setPrice(dto.getPrice());
        food.setCategory(dto.getCategory());
        food.setImageUrl(dto.getImageUrl());
        food.setPreparationTime(dto.getPreparationTime());
        food.setBranchId(dto.getBranchId());
        return food;
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/service/SaleService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.dto.SaleItemDTO;
import com.restaurant.stockmanagement.dto.SaleRequest;
import com.restaurant.stockmanagement.dto.SaleResponse;
import com.restaurant.stockmanagement.exception.InsufficientStockException;
import com.restaurant.stockmanagement.exception.ResourceNotFoundException;
import com.restaurant.stockmanagement.model.*;
import com.restaurant.stockmanagement.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final FoodRepository foodRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final IngredientRepository ingredientRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final FoodService foodService;

    public SaleService(SaleRepository saleRepository,
                      SaleItemRepository saleItemRepository,
                      FoodRepository foodRepository,
                      RecipeIngredientRepository recipeIngredientRepository,
                      IngredientRepository ingredientRepository,
                      StockTransactionRepository stockTransactionRepository,
                      FoodService foodService) {
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.foodRepository = foodRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.stockTransactionRepository = stockTransactionRepository;
        this.foodService = foodService;
    }

    @Transactional
    public SaleResponse createSale(SaleRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String cashierUsername = auth.getName();

        // Validate stock availability for all items
        validateStockAvailability(request.getItems());

        // Create sale
        Sale sale = new Sale();
        sale.setSaleDate(LocalDateTime.now());
        sale.setTotalAmount(request.getTotalAmount());
        sale.setCashierUsername(cashierUsername);
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setNotes(request.getNotes());
        sale.setDeleted(false);

        Sale savedSale = saleRepository.save(sale);

        // Create sale items and deduct stock
        List<SaleItem> saleItems = new ArrayList<>();
        for (SaleItemDTO itemDTO : request.getItems()) {
            Food food = foodRepository.findById(itemDTO.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Food", "id", itemDTO.getFoodId()));

            SaleItem saleItem = new SaleItem();
            saleItem.setSale(savedSale);
            saleItem.setFood(food);
            saleItem.setQuantity(itemDTO.getQuantity());
            saleItem.setUnitPrice(itemDTO.getUnitPrice());
            saleItem.setSubtotal(itemDTO.getQuantity() * itemDTO.getUnitPrice());
            saleItem.setDeleted(false);

            saleItems.add(saleItemRepository.save(saleItem));

            // Deduct ingredients from stock
            deductIngredientsFromStock(food.getId(), itemDTO.getQuantity(), cashierUsername);

            // Update food availability
            foodService.updateFoodAvailability(food.getId());
        }

        savedSale.setItems(saleItems);

        return mapToResponse(savedSale);
    }

    private void validateStockAvailability(List<SaleItemDTO> items) {
        for (SaleItemDTO item : items) {
            List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByFoodId(item.getFoodId());

            for (RecipeIngredient ri : recipeIngredients) {
                double requiredQuantity = ri.getQuantity() * item.getQuantity();
                Ingredient ingredient = ri.getIngredient();

                if (ingredient.getCurrentStock() < requiredQuantity) {
                    throw new InsufficientStockException(
                        String.format("Insufficient stock for ingredient: %s. Required: %.2f %s, Available: %.2f %s",
                            ingredient.getName(), requiredQuantity, ingredient.getUnit(),
                            ingredient.getCurrentStock(), ingredient.getUnit())
                    );
                }
            }
        }
    }

    private void deductIngredientsFromStock(Long foodId, int quantity, String performedBy) {
        List<RecipeIngredient> recipeIngredients = recipeIngredientRepository.findByFoodId(foodId);

        for (RecipeIngredient ri : recipeIngredients) {
            Ingredient ingredient = ri.getIngredient();
            double deductAmount = ri.getQuantity() * quantity;

            double previousStock = ingredient.getCurrentStock();
            double newStock = previousStock - deductAmount;
            ingredient.setCurrentStock(newStock);
            ingredientRepository.save(ingredient);

            // Create stock transaction
            StockTransaction transaction = new StockTransaction();
            transaction.setIngredient(ingredient);
            transaction.setType(TransactionType.SALE);
            transaction.setQuantity(deductAmount);
            transaction.setPreviousStock(previousStock);
            transaction.setNewStock(newStock);
            transaction.setReason("Sale - " + ri.getFood().getName());
            transaction.setPerformedBy(performedBy);
            transaction.setDeleted(false);
            stockTransactionRepository.save(transaction);
        }
    }

    public Page<SaleResponse> getAllSales(Pageable pageable) {
        return saleRepository.findByDeletedFalse(pageable)
                .map(this::mapToResponse);
    }

    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", "id", id));
        return mapToResponse(sale);
    }

    public List<SaleResponse> getSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findSalesBetweenDates(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Double getTotalSalesBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        Double total = saleRepository.getTotalSalesBetweenDates(startDate, endDate);
        return total != null ? total : 0.0;
    }

    private SaleResponse mapToResponse(Sale sale) {
        SaleResponse response = new SaleResponse();
        response.setId(sale.getId());
        response.setSaleDate(sale.getSaleDate());
        response.setTotalAmount(sale.getTotalAmount());
        response.setCashierUsername(sale.getCashierUsername());
        response.setBranchId(sale.getBranchId());
        response.setPaymentMethod(sale.getPaymentMethod());
        response.setNotes(sale.getNotes());

        List<SaleItem> items = saleItemRepository.findBySaleId(sale.getId());
        response.setItems(items.stream()
                .map(this::mapSaleItemToDTO)
                .collect(Collectors.toList()));

        return response;
    }

    private SaleItemDTO mapSaleItemToDTO(SaleItem item) {
        SaleItemDTO dto = new SaleItemDTO();
        dto.setFoodId(item.getFood().getId());
        dto.setFoodName(item.getFood().getName());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/service/StockAlertService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.dto.StockAlertDTO;
import com.restaurant.stockmanagement.exception.ResourceNotFoundException;
import com.restaurant.stockmanagement.model.AlertType;
import com.restaurant.stockmanagement.model.Ingredient;
import com.restaurant.stockmanagement.model.StockAlert;
import com.restaurant.stockmanagement.repository.IngredientRepository;
import com.restaurant.stockmanagement.repository.StockAlertRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockAlertService {

    private final StockAlertRepository alertRepository;
    private final IngredientRepository ingredientRepository;
    private final EmailService emailService;

    public StockAlertService(StockAlertRepository alertRepository,
                            IngredientRepository ingredientRepository,
                            EmailService emailService) {
        this.alertRepository = alertRepository;
        this.ingredientRepository = ingredientRepository;
        this.emailService = emailService;
    }

    public Page<StockAlertDTO> getAllAlerts(Pageable pageable) {
        return alertRepository.findByDeletedFalse(pageable)
                .map(this::mapToDTO);
    }

    public List<StockAlertDTO> getUnacknowledgedAlerts() {
        return alertRepository.findUnacknowledgedAlerts().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StockAlertDTO acknowledgeAlert(Long alertId) {
        StockAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alert", "id", alertId));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        alert.setAcknowledged(true);
        alert.setAcknowledgedAt(LocalDateTime.now());
        alert.setAcknowledgedBy(username);

        StockAlert updated = alertRepository.save(alert);
        return mapToDTO(updated);
    }

    @Transactional
    public void generateAlerts() {
        // Low stock alerts
        List<Ingredient> lowStockIngredients = ingredientRepository.findLowStockIngredients();
        for (Ingredient ingredient : lowStockIngredients) {
            createAlertIfNotExists(ingredient, AlertType.LOW_STOCK,
                    String.format("Low stock alert: %s is below minimum threshold. Current: %.2f %s, Minimum: %.2f %s",
                            ingredient.getName(), ingredient.getCurrentStock(), ingredient.getUnit(),
                            ingredient.getMinimumStock(), ingredient.getUnit()));
        }

        // Out of stock alerts
        List<Ingredient> outOfStockIngredients = ingredientRepository.findOutOfStockIngredients();
        for (Ingredient ingredient : outOfStockIngredients) {
            createAlertIfNotExists(ingredient, AlertType.OUT_OF_STOCK,
                    String.format("Out of stock: %s has zero stock", ingredient.getName()));
        }

        // Expiring soon alerts (within 7 days)
        LocalDate now = LocalDate.now();
        LocalDate sevenDaysLater = now.plusDays(7);
        List<Ingredient> expiringIngredients = ingredientRepository
                .findIngredientsByExpiryDateBetween(now, sevenDaysLater);
        for (Ingredient ingredient : expiringIngredients) {
            createAlertIfNotExists(ingredient, AlertType.EXPIRING_SOON,
                    String.format("Expiring soon: %s will expire on %s",
                            ingredient.getName(), ingredient.getExpiryDate()));
        }

        // Expired alerts
        List<Ingredient> expiredIngredients = ingredientRepository.findExpiredIngredients(now);
        for (Ingredient ingredient : expiredIngredients) {
            createAlertIfNotExists(ingredient, AlertType.EXPIRED,
                    String.format("Expired: %s expired on %s",
                            ingredient.getName(), ingredient.getExpiryDate()));
        }
    }

    private void createAlertIfNotExists(Ingredient ingredient, AlertType alertType, String message) {
        List<StockAlert> existingAlerts = alertRepository
                .findByDeletedFalseAndIngredientIdAndAlertType(ingredient.getId(), alertType);

        boolean hasUnacknowledged = existingAlerts.stream()
                .anyMatch(alert -> !alert.getAcknowledged());

        if (!hasUnacknowledged) {
            StockAlert alert = new StockAlert();
            alert.setIngredient(ingredient);
            alert.setAlertType(alertType);
            alert.setMessage(message);
            alert.setAcknowledged(false);
            alert.setBranchId(ingredient.getBranchId());
            alert.setDeleted(false);

            alertRepository.save(alert);

            // Send email notification
            emailService.sendStockAlertEmail(ingredient, alertType, message);
        }
    }

    private StockAlertDTO mapToDTO(StockAlert alert) {
        StockAlertDTO dto = new StockAlertDTO();
        dto.setId(alert.getId());
        dto.setIngredientId(alert.getIngredient().getId());
        dto.setIngredientName(alert.getIngredient().getName());
        dto.setAlertType(alert.getAlertType());
        dto.setMessage(alert.getMessage());
        dto.setAcknowledged(alert.getAcknowledged());
        dto.setAcknowledgedAt(alert.getAcknowledgedAt());
        dto.setAcknowledgedBy(alert.getAcknowledgedBy());
        dto.setCreatedAt(alert.getCreatedAt());
        return dto;
    }
}
```

This completes Part 2 with the critical service layer files. Would you like me to continue with:

- Part 3: All Controllers
- Part 4: Advanced Features (Email, File Upload, Reports, Scheduling)
- Part 5: Complete Frontend React Application

Let me know and I'll generate the next parts!
