# Git Workflow Guide for 3-Member Team

## Team Structure & Responsibilities

### Member 1: Backend Developer (Authentication & Models)

**Days 1-3**: Core authentication and data models
**Days 4-5**: Support other members with integration

### Member 2: Backend Developer (Services & Controllers)

**Days 3-7**: Business logic and REST APIs  
**Days 8-9**: Advanced features and testing

### Member 3: Frontend Developer (React UI)

**Days 5-11**: Complete React application
**Days 12**: Final integration and testing

---

## Initial Setup (Day 1 - All Members)

### Step 1: Member 1 Creates Repository

```powershell
# Member 1 only
cd C:\restaurant-stock-management
git init
git add .
git commit -m "Initial project structure with Maven and configurations"

# Create repository on GitHub (through web interface)
# Then push to GitHub
git remote add origin https://github.com/YOUR_USERNAME/restaurant-stock-management.git
git branch -M main
git push -u origin main
```

### Step 2: All Members Clone Repository

```powershell
# Member 2 and Member 3
cd C:\
git clone https://github.com/YOUR_USERNAME/restaurant-stock-management.git
cd restaurant-stock-management
```

### Step 3: All Members Create Their Branches

```powershell
# Member 1
git checkout -b feature/authentication

# Member 2
git checkout -b feature/backend-services

# Member 3
git checkout -b feature/frontend
```

---

## Day-by-Day Git Workflow

### 📅 **DAY 1-2: Member 1 - Database Models & Authentication**

#### What to Upload:

1. Entity classes (User, Role, BaseEntity)
2. Security configuration
3. UserRepository and CustomUserDetailsService
4. Basic authentication controller

#### Commands:

```powershell
# Member 1 work in feature/authentication branch
git checkout feature/authentication

# After completing entities and security config
git add backend/src/main/java/com/restaurant/stockmanagement/model/
git add backend/src/main/java/com/restaurant/stockmanagement/security/
git add backend/src/main/java/com/restaurant/stockmanagement/config/SecurityConfig.java
git commit -m "Add user entity, roles, and Spring Security configuration"
git push origin feature/authentication

# Continue with repositories
git add backend/src/main/java/com/restaurant/stockmanagement/repository/UserRepository.java
git commit -m "Add user repository with custom queries"
git push origin feature/authentication

# Create Pull Request on GitHub
# Title: "Authentication System - User Management"
# Description: "Implements user entities, Spring Security with session/cookie auth, CSRF protection"
```

---

### 📅 **DAY 3: Member 1 - Complete Core Models**

#### What to Upload:

1. Ingredient, Food, RecipeIngredient entities
2. StockTransaction, Sale, SaleItem entities
3. StockAlert entity
4. All corresponding repositories

#### Commands:

```powershell
# Member 1 continues
git add backend/src/main/java/com/restaurant/stockmanagement/model/Ingredient.java
git add backend/src/main/java/com/restaurant/stockmanagement/model/Food.java
git add backend/src/main/java/com/restaurant/stockmanagement/model/RecipeIngredient.java
git commit -m "Add core domain models: Ingredient, Food, RecipeIngredient"
git push origin feature/authentication

git add backend/src/main/java/com/restaurant/stockmanagement/model/StockTransaction.java
git add backend/src/main/java/com/restaurant/stockmanagement/model/Sale.java
git add backend/src/main/java/com/restaurant/stockmanagement/model/SaleItem.java
git add backend/src/main/java/com/restaurant/stockmanagement/model/StockAlert.java
git commit -m "Add transaction and sales models with audit trail"
git push origin feature/authentication

# Add all repositories
git add backend/src/main/java/com/restaurant/stockmanagement/repository/
git commit -m "Add JPA repositories with pagination and custom queries"
git push origin feature/authentication

# Create Pull Request
# Title: "Core Domain Models and Repositories"
```

#### Member 2 Starts Work:

```powershell
# Member 2: Pull latest main and merge into your branch
git checkout main
git pull origin main
git checkout feature/backend-services
git merge main

# Start working on DTOs and Services
```

---

### 📅 **DAY 4-5: Member 2 - DTOs and Services**

#### What to Upload:

1. All DTO classes
2. Service interfaces and implementations
3. Exception handling classes
4. Validation logic

#### Commands:

```powershell
# Member 2 working
git checkout feature/backend-services

# Add DTOs
git add backend/src/main/java/com/restaurant/stockmanagement/dto/
git commit -m "Add DTOs for all entities with validation annotations"
git push origin feature/backend-services

# Add Exception handling
git add backend/src/main/java/com/restaurant/stockmanagement/exception/
git commit -m "Add global exception handler and custom exceptions"
git push origin feature/backend-services

# Add Services (one commit per service or group)
git add backend/src/main/java/com/restaurant/stockmanagement/service/UserService.java
git add backend/src/main/java/com/restaurant/stockmanagement/service/AuthService.java
git commit -m "Add authentication and user management services"
git push origin feature/backend-services

git add backend/src/main/java/com/restaurant/stockmanagement/service/IngredientService.java
git add backend/src/main/java/com/restaurant/stockmanagement/service/FoodService.java
git commit -m "Add ingredient and food management services"
git push origin feature/backend-services
```

---

### 📅 **DAY 6-7: Member 2 - Controllers and APIs**

#### What to Upload:

1. All REST controllers
2. Swagger configuration
3. Controller integration tests

#### Commands:

```powershell
# Member 2 continues

# Add Swagger config
git add backend/src/main/java/com/restaurant/stockmanagement/config/SwaggerConfig.java
git commit -m "Add Swagger/OpenAPI documentation configuration"
git push origin feature/backend-services

# Add controllers (one commit per logical group)
git add backend/src/main/java/com/restaurant/stockmanagement/controller/AuthController.java
git add backend/src/main/java/com/restaurant/stockmanagement/controller/UserController.java
git commit -m "Add authentication and user management controllers"
git push origin feature/backend-services

git add backend/src/main/java/com/restaurant/stockmanagement/controller/IngredientController.java
git add backend/src/main/java/com/restaurant/stockmanagement/controller/FoodController.java
git commit -m "Add ingredient and food CRUD controllers with pagination"
git push origin feature/backend-services

git add backend/src/main/java/com/restaurant/stockmanagement/controller/SaleController.java
git commit -m "Add sales controller with automatic stock reduction"
git push origin feature/backend-services

# Create Pull Request
# Title: "Backend Services and REST API Controllers"
```

---

### 📅 **DAY 8: Member 2 - Advanced Features**

#### What to Upload:

1. Email notification service
2. File upload service
3. Report generation (CSV/PDF)
4. Caching configuration
5. Scheduled tasks

#### Commands:

```powershell
# Member 2 continues

git add backend/src/main/java/com/restaurant/stockmanagement/service/EmailService.java
git commit -m "Add email notification service for alerts"
git push origin feature/backend-services

git add backend/src/main/java/com/restaurant/stockmanagement/service/FileStorageService.java
git add backend/src/main/java/com/restaurant/stockmanagement/controller/FileUploadController.java
git commit -m "Add file upload service for images with validation"
git push origin feature/backend-services

git add backend/src/main/java/com/restaurant/stockmanagement/service/ReportService.java
git add backend/src/main/java/com/restaurant/stockmanagement/controller/ReportController.java
git commit -m "Add CSV and PDF export functionality for reports"
git push origin feature/backend-services

git add backend/src/main/java/com/restaurant/stockmanagement/config/CacheConfig.java
git add backend/src/main/java/com/restaurant/stockmanagement/scheduler/
git commit -m "Add caching and scheduled tasks for stock alerts"
git push origin feature/backend-services

# Create Pull Request
# Title: "Advanced Features - Email, Files, Reports, Scheduling"
```

---

### 📅 **DAY 5-7: Member 3 - React Project Setup & Authentication**

#### What to Upload:

1. React project structure with Vite
2. Authentication context
3. Login/Signup pages
4. Protected routes
5. HTTP client configuration

#### Commands:

```powershell
# Member 3 working
git checkout feature/frontend

# Initial React setup
cd frontend
npm create vite@latest . -- --template react
npm install axios react-router-dom react-hook-form

# After setting up project structure
git add frontend/package.json
git add frontend/package-lock.json
git add frontend/vite.config.js
git add frontend/index.html
git commit -m "Initialize React project with Vite"
git push origin feature/frontend

# Add authentication
git add frontend/src/contexts/AuthContext.jsx
git add frontend/src/services/api.js
git add frontend/src/services/authService.js
git commit -m "Add authentication context and API services"
git push origin feature/frontend

git add frontend/src/pages/Login.jsx
git add frontend/src/pages/Signup.jsx
git add frontend/src/components/ProtectedRoute.jsx
git commit -m "Add login and signup pages with protected routes"
git push origin feature/frontend
```

---

### 📅 **DAY 8-9: Member 3 - Dashboard and Ingredient Management**

#### What to Upload:

1. Dashboard components for each role
2. Ingredient management pages
3. Stock alert components
4. Navigation and layout

#### Commands:

```powershell
# Member 3 continues

git add frontend/src/components/Layout/
git add frontend/src/components/Navigation/
git commit -m "Add layout and navigation components"
git push origin feature/frontend

git add frontend/src/pages/Dashboard/
git add frontend/src/services/dashboardService.js
git commit -m "Add role-based dashboard pages"
git push origin feature/frontend

git add frontend/src/pages/Ingredients/
git add frontend/src/services/ingredientService.js
git add frontend/src/components/IngredientForm.jsx
git add frontend/src/components/IngredientList.jsx
git commit -m "Add ingredient management with CRUD operations"
git push origin feature/frontend

git add frontend/src/components/StockAlerts/
git commit -m "Add stock alerts display and acknowledgment"
git push origin feature/frontend
```

---

### 📅 **DAY 10: Member 3 - Food Menu and Recipe Management**

#### What to Upload:

1. Food management pages
2. Recipe ingredient assignment
3. Food availability checker

#### Commands:

```powershell
# Member 3 continues

git add frontend/src/pages/Foods/
git add frontend/src/services/foodService.js
git add frontend/src/components/FoodForm.jsx
git add frontend/src/components/FoodList.jsx
git commit -m "Add food menu management"
git push origin feature/frontend

git add frontend/src/components/RecipeBuilder/
git add frontend/src/services/recipeService.js
git commit -m "Add recipe ingredient assignment with measurements"
git push origin feature/frontend

git add frontend/src/components/FoodAvailability/
git commit -m "Add food availability checker based on stock"
git push origin feature/frontend
```

---

### 📅 **DAY 11: Member 3 - Sales POS and Analytics**

#### What to Upload:

1. Cashier POS interface
2. Sales history
3. Analytics dashboard
4. Report generation UI

#### Commands:

```powershell
# Member 3 continues

git add frontend/src/pages/Sales/
git add frontend/src/components/POS/
git add frontend/src/services/saleService.js
git commit -m "Add cashier POS with automatic stock reduction"
git push origin feature/frontend

git add frontend/src/pages/Analytics/
git add frontend/src/components/Charts/
git add frontend/src/services/analyticsService.js
git commit -m "Add analytics dashboard with usage patterns"
git push origin feature/frontend

git add frontend/src/components/Reports/
git commit -m "Add report generation UI with CSV/PDF export"
git push origin feature/frontend

# Create Pull Request
# Title: "Complete Frontend Application"
```

---

### 📅 **DAY 12: Integration and Testing (All Members)**

#### Member 1 & 2: Backend Testing

```powershell
# Add tests
git checkout feature/backend-services
git add backend/src/test/
git commit -m "Add unit and integration tests"
git push origin feature/backend-services
```

#### Member 3: Frontend Polish

```powershell
# Add styling and polish
git checkout feature/frontend
git add frontend/src/styles/
git add frontend/src/components/
git commit -m "Add styling and UI polish"
git push origin feature/frontend
```

#### All Members: Merge to Main

```powershell
# After all PRs are reviewed and approved
git checkout main
git pull origin main

# Deploy and test together
```

---

### 📅 **DAY 13: Final Touches and Documentation**

#### All Members:

```powershell
# Member 1: Update README with screenshots
git checkout main
git pull
# Add screenshots
git add README.md
git add screenshots/
git commit -m "Add documentation and screenshots"
git push origin main

# Member 2: Final bug fixes
# Create hotfix branch if needed
git checkout -b hotfix/bug-description
# Fix bugs
git add .
git commit -m "Fix: description of bug fix"
git push origin hotfix/bug-description
# Create PR and merge

# Member 3: Final UI adjustments
# Follow same hotfix process
```

---

## Best Practices

### Commit Message Format

```
Type: Brief description

- Detailed point 1
- Detailed point 2

Type can be: Add, Update, Fix, Remove, Refactor
```

### Before Pushing

```powershell
# Always pull latest changes first
git pull origin main

# Check what you're committing
git status
git diff

# Test locally before pushing
mvn clean install  # Backend
npm run build      # Frontend
```

### Resolving Merge Conflicts

```powershell
# If you encounter conflicts
git checkout main
git pull origin main
git checkout your-branch
git merge main

# Fix conflicts in VS Code or IntelliJ
# Then:
git add .
git commit -m "Resolve merge conflicts"
git push origin your-branch
```

### Daily Standup (Communication)

Each member should update team daily:

1. What did I complete yesterday?
2. What will I work on today?
3. Any blockers?

---

## Emergency Procedures

### Accidentally Committed to Main

```powershell
git reset --soft HEAD~1  # Undo commit, keep changes
git stash               # Save changes
git checkout -b feature/your-fix
git stash pop
git add .
git commit -m "Your message"
git push origin feature/your-fix
```

### Lost Local Changes

```powershell
git reflog  # Find your lost commit
git checkout <commit-hash>
git checkout -b recovery-branch
```

---

## Final Submission Checklist

- [ ] All features implemented and tested
- [ ] All tests passing
- [ ] README.md complete with screenshots
- [ ] Database seeds added
- [ ] API documentation complete (Swagger)
- [ ] No sensitive data in repository
- [ ] All branches merged to main
- [ ] Application runs on fresh clone
- [ ] Demo video/screenshots added

---

## GitHub Repository Structure

```
main branch (protected)
├── feature/authentication (Member 1)
├── feature/backend-services (Member 2)
├── feature/frontend (Member 3)
└── hotfix/* (as needed)
```

## Pull Request Template

```markdown
## Description

Brief description of changes

## Type of Change

- [ ] New feature
- [ ] Bug fix
- [ ] Documentation update

## Testing

- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed

## Screenshots

(if applicable)

## Checklist

- [ ] Code follows project style
- [ ] Self-review completed
- [ ] Comments added for complex logic
- [ ] Documentation updated
```

---

**Remember**: Communication is key! Use GitHub Issues, PR comments, and team chat to stay coordinated.
