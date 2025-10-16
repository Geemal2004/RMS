# COMPLETE CODE GENERATION GUIDE

This document contains ALL the code you need to create for the project. Copy each section exactly as shown into IntelliJ IDEA.

## PART 1: DTOs (Data Transfer Objects)

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/LoginRequest.java`

```java
package com.restaurant.stockmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/SignupRequest.java`

```java
package com.restaurant.stockmanagement.dto;

import com.restaurant.stockmanagement.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    private String phoneNumber;
    private Long branchId;
    private Set<Role> roles;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/UserResponse.java`

```java
package com.restaurant.stockmanagement.dto;

import com.restaurant.stockmanagement.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Long branchId;
    private Set<Role> roles;
    private Boolean active;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/IngredientDTO.java`

```java
package com.restaurant.stockmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class IngredientDTO {
    private Long id;

    @NotBlank(message = "Ingredient name is required")
    private String name;

    private String description;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Current stock is required")
    private Double currentStock;

    @NotNull(message = "Minimum stock is required")
    private Double minimumStock;

    private Double reorderLevel;
    private LocalDate expiryDate;
    private Double costPerUnit;
    private Long branchId;
    private String category;
    private String imageUrl;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/FoodDTO.java`

```java
package com.restaurant.stockmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FoodDTO {
    private Long id;

    @NotBlank(message = "Food name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    private Double price;

    private Boolean available;
    private String category;
    private String imageUrl;
    private Integer preparationTime;
    private Long branchId;
    private List<RecipeIngredientDTO> recipeIngredients;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/RecipeIngredientDTO.java`

```java
package com.restaurant.stockmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecipeIngredientDTO {
    private Long id;

    @NotNull(message = "Food ID is required")
    private Long foodId;

    @NotNull(message = "Ingredient ID is required")
    private Long ingredientId;

    private String ingredientName;
    private String ingredientUnit;

    @NotNull(message = "Quantity is required")
    private Double quantity;

    private String notes;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/SaleRequest.java`

```java
package com.restaurant.stockmanagement.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SaleRequest {
    @NotEmpty(message = "Sale items cannot be empty")
    private List<SaleItemDTO> items;

    @NotNull(message = "Total amount is required")
    private Double totalAmount;

    private String paymentMethod;
    private String notes;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/SaleItemDTO.java`

```java
package com.restaurant.stockmanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SaleItemDTO {
    @NotNull(message = "Food ID is required")
    private Long foodId;

    private String foodName;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    private Double unitPrice;

    private Double subtotal;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/SaleResponse.java`

```java
package com.restaurant.stockmanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleResponse {
    private Long id;
    private LocalDateTime saleDate;
    private Double totalAmount;
    private String cashierUsername;
    private Long branchId;
    private String paymentMethod;
    private String notes;
    private List<SaleItemDTO> items;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/StockAlertDTO.java`

```java
package com.restaurant.stockmanagement.dto;

import com.restaurant.stockmanagement.model.AlertType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockAlertDTO {
    private Long id;
    private Long ingredientId;
    private String ingredientName;
    private AlertType alertType;
    private String message;
    private Boolean acknowledged;
    private LocalDateTime acknowledgedAt;
    private String acknowledgedBy;
    private LocalDateTime createdAt;
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/dto/ApiResponse.java`

```java
package com.restaurant.stockmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private Boolean success;
    private String message;
    private Object data;

    public static ApiResponse success(String message) {
        return new ApiResponse(true, message, null);
    }

    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, message, null);
    }
}
```

---

## PART 2: Exception Handling

### File: `backend/src/main/java/com/restaurant/stockmanagement/exception/ResourceNotFoundException.java`

```java
package com.restaurant.stockmanagement.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/exception/DuplicateResourceException.java`

```java
package com.restaurant.stockmanagement.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/exception/InsufficientStockException.java`

```java
package com.restaurant.stockmanagement.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
```

### File: `backend/src/main/java/com/restaurant/stockmanagement/exception/GlobalExceptionHandler.java`

```java
package com.restaurant.stockmanagement.exception;

import com.restaurant.stockmanagement.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse> handleInsufficientStock(InsufficientStockException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Invalid username or password"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(false, "Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("An error occurred: " + ex.getMessage()));
    }
}
```

---

## PART 3: Services (Business Logic)

### File: `backend/src/main/java/com/restaurant/stockmanagement/service/AuthService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.dto.LoginRequest;
import com.restaurant.stockmanagement.dto.SignupRequest;
import com.restaurant.stockmanagement.dto.UserResponse;
import com.restaurant.stockmanagement.exception.DuplicateResourceException;
import com.restaurant.stockmanagement.model.User;
import com.restaurant.stockmanagement.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBranchId(request.getBranchId());
        user.setRoles(request.getRoles());
        user.setActive(true);

        User savedUser = userRepository.save(user);
        return mapToUserResponse(savedUser);
    }

    public UserResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserResponse(user);
    }

    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
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

### File: `backend/src/main/java/com/restaurant/stockmanagement/service/IngredientService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.dto.IngredientDTO;
import com.restaurant.stockmanagement.exception.ResourceNotFoundException;
import com.restaurant.stockmanagement.model.Ingredient;
import com.restaurant.stockmanagement.repository.IngredientRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Cacheable("ingredients")
    public Page<IngredientDTO> getAllIngredients(Pageable pageable) {
        return ingredientRepository.findByDeletedFalse(pageable)
                .map(this::mapToDTO);
    }

    @Cacheable(value = "ingredient", key = "#id")
    public IngredientDTO getIngredientById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", id));

        if (ingredient.getDeleted()) {
            throw new ResourceNotFoundException("Ingredient", "id", id);
        }

        return mapToDTO(ingredient);
    }

    @Transactional
    @CacheEvict(value = {"ingredients", "ingredient"}, allEntries = true)
    public IngredientDTO createIngredient(IngredientDTO dto) {
        Ingredient ingredient = mapToEntity(dto);
        ingredient.setDeleted(false);
        Ingredient saved = ingredientRepository.save(ingredient);
        return mapToDTO(saved);
    }

    @Transactional
    @CacheEvict(value = {"ingredients", "ingredient"}, allEntries = true)
    public IngredientDTO updateIngredient(Long id, IngredientDTO dto) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", id));

        ingredient.setName(dto.getName());
        ingredient.setDescription(dto.getDescription());
        ingredient.setUnit(dto.getUnit());
        ingredient.setCurrentStock(dto.getCurrentStock());
        ingredient.setMinimumStock(dto.getMinimumStock());
        ingredient.setReorderLevel(dto.getReorderLevel());
        ingredient.setExpiryDate(dto.getExpiryDate());
        ingredient.setCostPerUnit(dto.getCostPerUnit());
        ingredient.setCategory(dto.getCategory());
        ingredient.setImageUrl(dto.getImageUrl());

        Ingredient updated = ingredientRepository.save(ingredient);
        return mapToDTO(updated);
    }

    @Transactional
    @CacheEvict(value = {"ingredients", "ingredient"}, allEntries = true)
    public void deleteIngredient(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingredient", "id", id));

        ingredient.setDeleted(true);
        ingredientRepository.save(ingredient);
    }

    public List<IngredientDTO> getLowStockIngredients() {
        return ingredientRepository.findLowStockIngredients().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<IngredientDTO> getOutOfStockIngredients() {
        return ingredientRepository.findOutOfStockIngredients().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<IngredientDTO> getExpiringIngredients(int daysAhead) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(daysAhead);
        return ingredientRepository.findIngredientsByExpiryDateBetween(startDate, endDate).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Page<IngredientDTO> searchIngredients(String search, Pageable pageable) {
        return ingredientRepository.searchIngredients(search, pageable)
                .map(this::mapToDTO);
    }

    private IngredientDTO mapToDTO(Ingredient ingredient) {
        IngredientDTO dto = new IngredientDTO();
        dto.setId(ingredient.getId());
        dto.setName(ingredient.getName());
        dto.setDescription(ingredient.getDescription());
        dto.setUnit(ingredient.getUnit());
        dto.setCurrentStock(ingredient.getCurrentStock());
        dto.setMinimumStock(ingredient.getMinimumStock());
        dto.setReorderLevel(ingredient.getReorderLevel());
        dto.setExpiryDate(ingredient.getExpiryDate());
        dto.setCostPerUnit(ingredient.getCostPerUnit());
        dto.setBranchId(ingredient.getBranchId());
        dto.setCategory(ingredient.getCategory());
        dto.setImageUrl(ingredient.getImageUrl());
        return dto;
    }

    private Ingredient mapToEntity(IngredientDTO dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        ingredient.setDescription(dto.getDescription());
        ingredient.setUnit(dto.getUnit());
        ingredient.setCurrentStock(dto.getCurrentStock());
        ingredient.setMinimumStock(dto.getMinimumStock());
        ingredient.setReorderLevel(dto.getReorderLevel());
        ingredient.setExpiryDate(dto.getExpiryDate());
        ingredient.setCostPerUnit(dto.getCostPerUnit());
        ingredient.setBranchId(dto.getBranchId());
        ingredient.setCategory(dto.getCategory());
        ingredient.setImageUrl(dto.getImageUrl());
        return ingredient;
    }
}
```

I'll continue with more service files in the next part. Would you like me to:

1. Continue creating ALL remaining backend files (Services, Controllers)?
2. Create ALL frontend React files?
3. Provide a condensed ZIP download instruction?

The complete application has approximately **80-100 files**. Should I continue generating them all, or would you prefer a different approach?
