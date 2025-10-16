# CODE PART 4: Advanced Features (Beyond CRUD)

This includes Email notifications, File uploads, Report generation, Caching, and Scheduled tasks.

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/service/EmailService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.model.AlertType;
import com.restaurant.stockmanagement.model.Ingredient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendStockAlertEmail(Ingredient ingredient, AlertType alertType, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(fromEmail); // In production, send to stock managers
            mailMessage.setSubject("Stock Alert: " + alertType.name() + " - " + ingredient.getName());
            mailMessage.setText(message + "\n\nPlease take necessary action.");

            mailSender.send(mailMessage);
        } catch (Exception e) {
            // Log error but don't fail the transaction
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("Welcome to Restaurant Stock Management System");
            mailMessage.setText("Hello " + username + ",\n\n" +
                    "Welcome to our Restaurant Stock Management System!\n\n" +
                    "You can now log in and start managing your inventory.\n\n" +
                    "Best regards,\n" +
                    "Restaurant Management Team");

            mailSender.send(mailMessage);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }

    @Async
    public void sendLowStockNotification(String toEmail, String ingredientName, double currentStock, String unit) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(toEmail);
            mailMessage.setSubject("Low Stock Alert: " + ingredientName);
            mailMessage.setText("The ingredient " + ingredientName + " is running low.\n\n" +
                    "Current stock: " + currentStock + " " + unit + "\n\n" +
                    "Please reorder soon to avoid stockouts.\n\n" +
                    "Best regards,\n" +
                    "Stock Management System");

            mailSender.send(mailMessage);
        } catch (Exception e) {
            System.err.println("Failed to send low stock notification: " + e.getMessage());
        }
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/service/FileStorageService.java`

```java
package com.restaurant.stockmanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir:uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Validate file
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            // Check if file contains invalid characters
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Invalid file path: " + originalFileName);
            }

            // Validate file type (images only)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Only image files are allowed");
            }

            // Validate file size (max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new RuntimeException("File size exceeds maximum limit of 5MB");
            }

            // Generate unique filename
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Copy file to the target location
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return newFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName, ex);
        }
    }

    public Path loadFile(String fileName) {
        return fileStorageLocation.resolve(fileName).normalize();
    }

    public void deleteFile(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + fileName, ex);
        }
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/FileUploadController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.dto.ApiResponse;
import com.restaurant.stockmanagement.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
@Tag(name = "File Upload", description = "File upload and download endpoints")
@SecurityRequirement(name = "session")
public class FileUploadController {

    private final FileStorageService fileStorageService;

    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'CHEF', 'STOCK_MANAGER')")
    @Operation(summary = "Upload image file")
    public ResponseEntity<ApiResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        
        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("fileUrl", "/api/files/download/" + fileName);
        
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", response));
    }

    @GetMapping("/download/{fileName:.+}")
    @Operation(summary = "Download/view uploaded file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.loadFile(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, 
                                "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }

    @DeleteMapping("/{fileName:.+}")
    @PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER')")
    @Operation(summary = "Delete uploaded file")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable String fileName) {
        fileStorageService.deleteFile(fileName);
        return ResponseEntity.ok(ApiResponse.success("File deleted successfully"));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/service/ReportService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.model.Ingredient;
import com.restaurant.stockmanagement.model.Sale;
import com.restaurant.stockmanagement.repository.IngredientRepository;
import com.restaurant.stockmanagement.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private final IngredientRepository ingredientRepository;
    private final SaleRepository saleRepository;

    public ReportService(IngredientRepository ingredientRepository, 
                        SaleRepository saleRepository) {
        this.ingredientRepository = ingredientRepository;
        this.saleRepository = saleRepository;
    }

    public byte[] generateInventoryReportCSV() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // CSV Header
        writer.println("ID,Name,Category,Unit,Current Stock,Minimum Stock,Cost Per Unit,Expiry Date,Status");

        // CSV Data
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.getDeleted()) {
                String status = getIngredientStatus(ingredient);
                writer.printf("%d,%s,%s,%s,%.2f,%.2f,%.2f,%s,%s%n",
                        ingredient.getId(),
                        escapeCSV(ingredient.getName()),
                        escapeCSV(ingredient.getCategory()),
                        ingredient.getUnit(),
                        ingredient.getCurrentStock(),
                        ingredient.getMinimumStock(),
                        ingredient.getCostPerUnit(),
                        ingredient.getExpiryDate() != null ? ingredient.getExpiryDate().toString() : "N/A",
                        status);
            }
        }

        writer.flush();
        return outputStream.toByteArray();
    }

    public byte[] generateSalesReportCSV(LocalDateTime startDate, LocalDateTime endDate) {
        List<Sale> sales = saleRepository.findSalesBetweenDates(startDate, endDate);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // CSV Header
        writer.println("ID,Date,Cashier,Total Amount,Payment Method,Items Count");

        // CSV Data
        double totalRevenue = 0.0;
        for (Sale sale : sales) {
            if (!sale.getDeleted()) {
                writer.printf("%d,%s,%s,%.2f,%s,%d%n",
                        sale.getId(),
                        sale.getSaleDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        sale.getCashierUsername(),
                        sale.getTotalAmount(),
                        sale.getPaymentMethod() != null ? sale.getPaymentMethod() : "N/A",
                        sale.getItems().size());
                totalRevenue += sale.getTotalAmount();
            }
        }

        // Summary
        writer.println();
        writer.printf("Total Sales Count,%d%n", sales.size());
        writer.printf("Total Revenue,%.2f%n", totalRevenue);

        writer.flush();
        return outputStream.toByteArray();
    }

    public byte[] generateLowStockReportCSV() {
        List<Ingredient> ingredients = ingredientRepository.findLowStockIngredients();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // CSV Header
        writer.println("ID,Name,Category,Current Stock,Minimum Stock,Unit,Reorder Level,Status");

        // CSV Data
        for (Ingredient ingredient : ingredients) {
            String status = ingredient.getCurrentStock() == 0 ? "OUT OF STOCK" : "LOW STOCK";
            writer.printf("%d,%s,%s,%.2f,%.2f,%s,%.2f,%s%n",
                    ingredient.getId(),
                    escapeCSV(ingredient.getName()),
                    escapeCSV(ingredient.getCategory()),
                    ingredient.getCurrentStock(),
                    ingredient.getMinimumStock(),
                    ingredient.getUnit(),
                    ingredient.getReorderLevel() != null ? ingredient.getReorderLevel() : 0.0,
                    status);
        }

        writer.flush();
        return outputStream.toByteArray();
    }

    private String getIngredientStatus(Ingredient ingredient) {
        if (ingredient.getCurrentStock() == 0) {
            return "OUT OF STOCK";
        } else if (ingredient.getCurrentStock() < ingredient.getMinimumStock()) {
            return "LOW STOCK";
        } else {
            return "OK";
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/ReportController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports", description = "Report generation and export endpoints")
@SecurityRequirement(name = "session")
@PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER')")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/inventory/csv")
    @Operation(summary = "Export inventory report as CSV")
    public ResponseEntity<byte[]> exportInventoryReport() {
        byte[] csvData = reportService.generateInventoryReportCSV();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "inventory-report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    @GetMapping("/sales/csv")
    @Operation(summary = "Export sales report as CSV")
    public ResponseEntity<byte[]> exportSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        byte[] csvData = reportService.generateSalesReportCSV(startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "sales-report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }

    @GetMapping("/low-stock/csv")
    @Operation(summary = "Export low stock report as CSV")
    public ResponseEntity<byte[]> exportLowStockReport() {
        byte[] csvData = reportService.generateLowStockReportCSV();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "low-stock-report.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/config/CacheConfig.java`

```java
package com.restaurant.stockmanagement.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public ConcurrentMapCacheManager cacheManager() {
        return new ConcurrentMapCacheManager("ingredients", "ingredient", "foods", "food");
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/config/SwaggerConfig.java`

```java
package com.restaurant.stockmanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Restaurant Stock Management API")
                        .version("1.0.0")
                        .description("REST API for Restaurant Stock Management System with role-based access control")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("support@restaurant.com")))
                .components(new Components()
                        .addSecuritySchemes("session", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JSESSIONID")));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/scheduler/StockAlertScheduler.java`

```java
package com.restaurant.stockmanagement.scheduler;

import com.restaurant.stockmanagement.service.StockAlertService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockAlertScheduler {

    private final StockAlertService stockAlertService;

    public StockAlertScheduler(StockAlertService stockAlertService) {
        this.stockAlertService = stockAlertService;
    }

    // Run every 6 hours
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000)
    public void generateStockAlerts() {
        System.out.println("Running scheduled stock alert generation...");
        stockAlertService.generateAlerts();
        System.out.println("Stock alerts generated successfully");
    }

    // Run every day at 8 AM
    @Scheduled(cron = "0 0 8 * * *")
    public void dailyStockCheck() {
        System.out.println("Running daily stock check at 8 AM...");
        stockAlertService.generateAlerts();
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/service/AnalyticsService.java`

```java
package com.restaurant.stockmanagement.service;

import com.restaurant.stockmanagement.repository.SaleItemRepository;
import com.restaurant.stockmanagement.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final StockTransactionRepository transactionRepository;
    private final SaleItemRepository saleItemRepository;

    public AnalyticsService(StockTransactionRepository transactionRepository,
                          SaleItemRepository saleItemRepository) {
        this.transactionRepository = transactionRepository;
        this.saleItemRepository = saleItemRepository;
    }

    public Map<String, Object> getMostUsedIngredients(int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<Object[]> results = transactionRepository.findMostUsedIngredients(startDate, endDate);

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("period", days + " days");
        analytics.put("startDate", startDate);
        analytics.put("endDate", endDate);
        analytics.put("mostUsedIngredients", results);

        return analytics;
    }

    public Map<String, Object> getMostSoldFoods(int days) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);

        List<Object[]> results = saleItemRepository.findMostSoldFoods(startDate, endDate);

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("period", days + " days");
        analytics.put("startDate", startDate);
        analytics.put("endDate", endDate);
        analytics.put("mostSoldFoods", results);

        return analytics;
    }

    public Map<String, Object> getRestockRecommendations() {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(30);

        List<Object[]> usageData = transactionRepository.findMostUsedIngredients(startDate, endDate);

        Map<String, Object> recommendations = new HashMap<>();
        recommendations.put("basedOnLast30Days", true);
        recommendations.put("recommendations", usageData);

        return recommendations;
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/controller/AnalyticsController.java`

```java
package com.restaurant.stockmanagement.controller;

import com.restaurant.stockmanagement.dto.ApiResponse;
import com.restaurant.stockmanagement.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/analytics")
@Tag(name = "Analytics", description = "Analytics and insights endpoints")
@SecurityRequirement(name = "session")
@PreAuthorize("hasAnyRole('OWNER', 'BRANCH_MANAGER', 'STOCK_MANAGER')")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/most-used-ingredients")
    @Operation(summary = "Get most used ingredients in specified period")
    public ResponseEntity<ApiResponse> getMostUsedIngredients(
            @RequestParam(defaultValue = "30") int days) {
        
        Map<String, Object> analytics = analyticsService.getMostUsedIngredients(days);
        return ResponseEntity.ok(ApiResponse.success("Analytics retrieved successfully", analytics));
    }

    @GetMapping("/most-sold-foods")
    @Operation(summary = "Get most sold food items in specified period")
    public ResponseEntity<ApiResponse> getMostSoldFoods(
            @RequestParam(defaultValue = "30") int days) {
        
        Map<String, Object> analytics = analyticsService.getMostSoldFoods(days);
        return ResponseEntity.ok(ApiResponse.success("Analytics retrieved successfully", analytics));
    }

    @GetMapping("/restock-recommendations")
    @Operation(summary = "Get restock recommendations based on usage patterns")
    public ResponseEntity<ApiResponse> getRestockRecommendations() {
        Map<String, Object> recommendations = analyticsService.getRestockRecommendations();
        return ResponseEntity.ok(ApiResponse.success("Recommendations generated", recommendations));
    }
}
```

---

## File: `backend/src/main/java/com/restaurant/stockmanagement/config/AsyncConfig.java`

```java
package com.restaurant.stockmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {
    // Enables @Async annotation for asynchronous email sending
}
```

---

**Next Steps:**
1. Copy all advanced feature files to your project
2. Configure email settings in `application.yml` with your Gmail credentials
3. Create `uploads` directory in project root for file storage
4. Test email notifications, file uploads, and report downloads
5. Proceed to CODE_PART5 for the complete React frontend
