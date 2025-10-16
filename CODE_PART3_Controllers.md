# CODE PART 3: REST API Controllers

Copy each file exactly as shown into IntelliJ IDEA at the specified path.

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/AuthController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.dto.ApiResponse;
import com.restaurant.stockmanagement.dto.LoginRequest;
import com.restaurant.stockmanagement.dto.SignupRequest;
import com.restaurant.stockmanagement.dto.UserResponse;
import com.restaurant.stockmanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication and user management endpoints")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    public ResponseEntity<ApiResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse user = authService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", user));
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request,
                                             HttpServletRequest httpRequest) {
        UserResponse user = authService.login(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", user));
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(ApiResponse.success("Logout successful"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user information")
    public ResponseEntity<ApiResponse> getCurrentUser() {
        UserResponse user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @GetMapping("/csrf")
    @Operation(summary = "Get CSRF token")
    public ResponseEntity<ApiResponse> getCsrfToken() {
        return ResponseEntity.ok(ApiResponse.success("CSRF endpoint active"));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/IngredientController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.dto.ApiResponse;
import com.restaurant.stockmanagement.dto.IngredientDTO;
import com.restaurant.stockmanagement.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@Tag(name = "Ingredients", description = "Ingredient management endpoints")
@SecurityRequirement(name = "session")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    @Operation(summary = "Get all ingredients with pagination and sorting")
    public ResponseEntity<ApiResponse> getAllIngredients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<IngredientDTO> ingredients = ingredientService.getAllIngredients(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Ingredients retrieved successfully", ingredients));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ingredient by ID")
    public ResponseEntity<ApiResponse> getIngredientById(@PathVariable Long id) {
        IngredientDTO ingredient = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ApiResponse.success("Ingredient retrieved successfully", ingredient));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER')")
    @Operation(summary = "Create new ingredient")
    public ResponseEntity<ApiResponse> createIngredient(@Valid @RequestBody IngredientDTO ingredientDTO) {
        IngredientDTO created = ingredientService.createIngredient(ingredientDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ingredient created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER')")
    @Operation(summary = "Update ingredient")
    public ResponseEntity<ApiResponse> updateIngredient(
            @PathVariable Long id,
            @Valid @RequestBody IngredientDTO ingredientDTO) {
        IngredientDTO updated = ingredientService.updateIngredient(id, ingredientDTO);
        return ResponseEntity.ok(ApiResponse.success("Ingredient updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER')")
    @Operation(summary = "Delete ingredient (soft delete)")
    public ResponseEntity<ApiResponse> deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return ResponseEntity.ok(ApiResponse.success("Ingredient deleted successfully"));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER')")
    @Operation(summary = "Get ingredients with low stock")
    public ResponseEntity<ApiResponse> getLowStockIngredients() {
        List<IngredientDTO> ingredients = ingredientService.getLowStockIngredients();
        return ResponseEntity.ok(ApiResponse.success("Low stock ingredients retrieved", ingredients));
    }

    @GetMapping("/out-of-stock")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER')")
    @Operation(summary = "Get out of stock ingredients")
    public ResponseEntity<ApiResponse> getOutOfStockIngredients() {
        List<IngredientDTO> ingredients = ingredientService.getOutOfStockIngredients();
        return ResponseEntity.ok(ApiResponse.success("Out of stock ingredients retrieved", ingredients));
    }

    @GetMapping("/expiring")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER', 'CHEF')")
    @Operation(summary = "Get expiring ingredients")
    public ResponseEntity<ApiResponse> getExpiringIngredients(
            @RequestParam(defaultValue = "7") int daysAhead) {
        List<IngredientDTO> ingredients = ingredientService.getExpiringIngredients(daysAhead);
        return ResponseEntity.ok(ApiResponse.success("Expiring ingredients retrieved", ingredients));
    }

    @GetMapping("/search")
    @Operation(summary = "Search ingredients")
    public ResponseEntity<ApiResponse> searchIngredients(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<IngredientDTO> ingredients = ingredientService.searchIngredients(query, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved", ingredients));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/FoodController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.dto.ApiResponse;
import com.restaurant.stockmanagement.dto.FoodDTO;
import com.restaurant.stockmanagement.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foods")
@Tag(name = "Foods", description = "Food menu management endpoints")
@SecurityRequirement(name = "session")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    @Operation(summary = "Get all foods with pagination")
    public ResponseEntity<ApiResponse> getAllFoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FoodDTO> foods = foodService.getAllFoods(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Foods retrieved successfully", foods));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get food by ID")
    public ResponseEntity<ApiResponse> getFoodById(@PathVariable Long id) {
        FoodDTO food = foodService.getFoodById(id);
        return ResponseEntity.ok(ApiResponse.success("Food retrieved successfully", food));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'CHEF')")
    @Operation(summary = "Create new food item")
    public ResponseEntity<ApiResponse> createFood(@Valid @RequestBody FoodDTO foodDTO) {
        FoodDTO created = foodService.createFood(foodDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Food created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'CHEF')")
    @Operation(summary = "Update food item")
    public ResponseEntity<ApiResponse> updateFood(
            @PathVariable Long id,
            @Valid @RequestBody FoodDTO foodDTO) {
        FoodDTO updated = foodService.updateFood(id, foodDTO);
        return ResponseEntity.ok(ApiResponse.success("Food updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER')")
    @Operation(summary = "Delete food item (soft delete)")
    public ResponseEntity<ApiResponse> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return ResponseEntity.ok(ApiResponse.success("Food deleted successfully"));
    }

    @GetMapping("/available")
    @Operation(summary = "Get available foods based on stock")
    public ResponseEntity<ApiResponse> getAvailableFoods() {
        List<FoodDTO> foods = foodService.getAvailableFoods();
        return ResponseEntity.ok(ApiResponse.success("Available foods retrieved", foods));
    }

    @GetMapping("/search")
    @Operation(summary = "Search foods")
    public ResponseEntity<ApiResponse> searchFoods(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<FoodDTO> foods = foodService.searchFoods(query, pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Search results retrieved", foods));
    }

    @PutMapping("/{id}/update-availability")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER', 'CHEF')")
    @Operation(summary = "Update food availability based on current stock")
    public ResponseEntity<ApiResponse> updateFoodAvailability(@PathVariable Long id) {
        foodService.updateFoodAvailability(id);
        return ResponseEntity.ok(ApiResponse.success("Food availability updated"));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/SaleController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.dto.ApiResponse;
import com.restaurant.stockmanagement.dto.SaleRequest;
import com.restaurant.stockmanagement.dto.SaleResponse;
import com.restaurant.stockmanagement.service.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sales")
@Tag(name = "Sales", description = "Sales and POS endpoints")
@SecurityRequirement(name = "session")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CASHIER')")
    @Operation(summary = "Create new sale (automatically deducts stock)")
    public ResponseEntity<ApiResponse> createSale(@Valid @RequestBody SaleRequest request) {
        SaleResponse sale = saleService.createSale(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sale completed successfully", sale));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'CASHIER')")
    @Operation(summary = "Get all sales with pagination")
    public ResponseEntity<ApiResponse> getAllSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "saleDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC") 
                ? Sort.by(sortBy).descending() 
                : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<SaleResponse> sales = saleService.getAllSales(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Sales retrieved successfully", sales));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'CASHIER')")
    @Operation(summary = "Get sale by ID")
    public ResponseEntity<ApiResponse> getSaleById(@PathVariable Long id) {
        SaleResponse sale = saleService.getSaleById(id);
        return ResponseEntity.ok(ApiResponse.success("Sale retrieved successfully", sale));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER')")
    @Operation(summary = "Get sales within date range")
    public ResponseEntity<ApiResponse> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        List<SaleResponse> sales = saleService.getSalesBetweenDates(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Sales retrieved successfully", sales));
    }

    @GetMapping("/total")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER')")
    @Operation(summary = "Get total sales amount for date range")
    public ResponseEntity<ApiResponse> getTotalSales(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Double total = saleService.getTotalSalesBetweenDates(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Total sales calculated", total));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/StockAlertController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.dto.ApiResponse;
import com.restaurant.stockmanagement.dto.StockAlertDTO;
import com.restaurant.stockmanagement.service.StockAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@Tag(name = "Stock Alerts", description = "Stock alert management endpoints")
@SecurityRequirement(name = "session")
@PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER')")
public class StockAlertController {

    private final StockAlertService alertService;

    public StockAlertController(StockAlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    @Operation(summary = "Get all alerts with pagination")
    public ResponseEntity<ApiResponse> getAllAlerts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<StockAlertDTO> alerts = alertService.getAllAlerts(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Alerts retrieved successfully", alerts));
    }

    @GetMapping("/unacknowledged")
    @Operation(summary = "Get unacknowledged alerts")
    public ResponseEntity<ApiResponse> getUnacknowledgedAlerts() {
        List<StockAlertDTO> alerts = alertService.getUnacknowledgedAlerts();
        return ResponseEntity.ok(ApiResponse.success("Unacknowledged alerts retrieved", alerts));
    }

    @PutMapping("/{id}/acknowledge")
    @Operation(summary = "Acknowledge an alert")
    public ResponseEntity<ApiResponse> acknowledgeAlert(@PathVariable Long id) {
        StockAlertDTO alert = alertService.acknowledgeAlert(id);
        return ResponseEntity.ok(ApiResponse.success("Alert acknowledged successfully", alert));
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER')")
    @Operation(summary = "Manually trigger alert generation")
    public ResponseEntity<ApiResponse> generateAlerts() {
        alertService.generateAlerts();
        return ResponseEntity.ok(ApiResponse.success("Alerts generated successfully"));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/UserController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.dto.ApiResponse;
import com.restaurant.stockmanagement.dto.UserResponse;
import com.restaurant.stockmanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "session")
@PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER')")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users with pagination")
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponse> users = userService.getAllUsers(pageable);
        
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Activate user account")
    public ResponseEntity<ApiResponse> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User activated successfully"));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Deactivate user account")
    public ResponseEntity<ApiResponse> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deactivated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    @Operation(summary = "Delete user (soft delete)")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/service/UserService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.dto.UserResponse;
import com.restaurant.stockmanagement.exception.ResourceNotFoundException;
import com.restaurant.stockmanagement.model.User;
import com.restaurant.stockmanagement.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findByDeletedFalse(pageable)
                .map(this::mapToResponse);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToResponse(user);
    }

    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setDeleted(true);
        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFullName(),
            user.getPhoneNumber(),
            user.getBranchId(),
            user.getRoles(),
            user.getActive()
        );
    }
}
```

---

**Next Steps:**
1. Copy all these controller files to your backend project
2. Test each endpoint using Swagger UI at `http://localhost:8080/api/swagger-ui.html`
3. Proceed to CODE_PART4 for advanced features (Email, File Upload, Reports)
