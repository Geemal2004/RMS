# 🚀 QUICK START - COPY THIS!

## For Member 1 (Backend Setup - START TODAY!)

### Step 1: Install Requirements (30 min)
```powershell
# Check Java 17
java -version

# Check Maven
mvn -version

# Check MySQL
mysql --version
```

### Step 2: Create Database (5 min)
```sql
-- Open MySQL Workbench or command line
CREATE DATABASE restaurant_stock_db;
```

### Step 3: Create Backend Project in IntelliJ (10 min)
1. File → New → Project
2. Maven Project
3. Group ID: `com.restaurant`
4. Artifact ID: `stockmanagement`
5. Location: `C:\restaurant-stock-management\backend`

### Step 4: Copy Files (1 hour)
Open `CODE_PART1_DTOs_Services.md` and copy IN THIS ORDER:

1. **pom.xml** - Replace entire file
2. **application.yml** - Create in `src/main/resources/`
3. Create package: `com.restaurant.stockmanagement.model`
4. Copy ALL model files (BaseEntity, User, Role, Ingredient, Food, etc.)
5. Create package: `com.restaurant.stockmanagement.repository`
6. Copy ALL repository files
7. Create package: `com.restaurant.stockmanagement.config`
8. Copy SecurityConfig.java

### Step 5: Update Configuration (5 min)
In `application.yml`, update:
```yaml
spring:
  datasource:
    username: root        # Your MySQL username
    password: YOUR_PASSWORD_HERE  # Your MySQL password
```

### Step 6: Run Backend (2 min)
1. Right-click on `RestaurantStockManagementApplication.java`
2. Click "Run"
3. Check console - should see "Started RestaurantStockManagementApplication"
4. Open browser: http://localhost:8080/api/swagger-ui.html

### Step 7: Commit to Git (5 min)
```powershell
cd C:\restaurant-stock-management\backend
git checkout -b feature/authentication
git add .
git commit -m "Day 1: Added models, repositories, and security config"
git push origin feature/authentication
```

---

## For Member 2 (Services & Controllers - START DAY 3)

### Files to Copy (Days 3-5):
1. Open `CODE_PART1_DTOs_Services.md`
   - Copy all DTOs
   - Copy all Exceptions
   - Copy AuthService

2. Open `CODE_PART2_Services.md`
   - Copy IngredientService
   - Copy FoodService
   - Copy SaleService
   - Copy StockAlertService

3. Open `CODE_PART3_Controllers.md`
   - Copy ALL controllers
   - Copy UserService

4. Open `CODE_PART4_Advanced_Features.md`
   - Copy EmailService
   - Copy FileStorageService
   - Copy ReportService
   - Copy AnalyticsService
   - Copy all Config files

### Test with Postman:
```
POST http://localhost:8080/api/auth/signup
Body: {
  "username": "admin",
  "password": "admin123",
  "email": "admin@test.com",
  "fullName": "Admin User",
  "roles": ["ROLE_OWNER"]
}

POST http://localhost:8080/api/auth/login
Body: {
  "username": "admin",
  "password": "admin123"
}
```

---

## For Member 3 (Frontend - START DAY 5)

### Step 1: Install Node.js (if not installed)
```powershell
# Check Node version (should be 16+)
node -version
npm -version
```

### Step 2: Create React Project (5 min)
```powershell
cd C:\restaurant-stock-management
npm create vite@latest frontend -- --template react
cd frontend
```

### Step 3: Replace package.json (2 min)
1. Open `CODE_PART5A_Frontend_Setup.md`
2. Copy the entire `package.json` content
3. Replace your `frontend/package.json`
4. Run: `npm install` (this takes 2-3 minutes)

### Step 4: Copy Setup Files (30 min)
From `CODE_PART5A_Frontend_Setup.md`:
1. Copy `vite.config.js` - replace file
2. Copy `index.html` to `public/index.html`
3. Create `src/contexts/AuthContext.jsx`
4. Create `src/services/api.js`
5. Create ALL service files (authService, ingredientService, etc.)
6. Create `src/components/Layout.jsx`, `Navbar.jsx`, `Sidebar.jsx`
7. Copy `App.jsx` - replace file

### Step 5: Copy Page Components (1 hour)
1. From `CODE_PART5B`: Copy Login, Signup, Dashboard
2. From `CODE_PART5C`: Copy Ingredients, Foods, POS
3. From `CODE_PART5D`: Copy Sales, Alerts, Analytics, Users
4. From `CODE_PART5E`: Copy ALL CSS files
5. From `CODE_PART5F`: Copy index.css, App.css

### Step 6: Run Frontend (2 min)
```powershell
npm run dev
```
Open: http://localhost:5173

---

## 📋 DAILY CHECKLIST

### Member 1 - Day 1 (Today!)
- [ ] Install Java 17, MySQL 8.0, IntelliJ
- [ ] Create database
- [ ] Create backend project
- [ ] Copy models (10 files)
- [ ] Copy repositories (8 files)
- [ ] Copy SecurityConfig
- [ ] Copy application.yml
- [ ] Update MySQL password
- [ ] Run backend - verify it starts
- [ ] Commit to `feature/authentication`

### Member 1 - Day 2
- [ ] Help Member 2 with setup
- [ ] Review and test models
- [ ] Add any missing configurations
- [ ] Commit fixes

### Member 2 - Day 3
- [ ] Pull latest code
- [ ] Copy DTOs (5 files)
- [ ] Copy Exceptions (2 files)
- [ ] Copy AuthService
- [ ] Test signup and login with Postman
- [ ] Commit to `feature/backend-services`

### Member 2 - Days 4-5
- [ ] Copy all remaining services
- [ ] Test each service with Postman
- [ ] Fix any bugs
- [ ] Commit daily

### Member 2 - Days 6-7
- [ ] Copy all controllers
- [ ] Copy advanced features
- [ ] Test all API endpoints
- [ ] Check Swagger UI
- [ ] Commit to `feature/backend-services`

### Member 3 - Day 5
- [ ] Install Node.js and VS Code
- [ ] Create React project
- [ ] Copy package.json and install
- [ ] Copy all setup files
- [ ] Run `npm run dev`
- [ ] See login page
- [ ] Commit to `feature/frontend`

### Member 3 - Days 6-9
- [ ] Copy authentication pages
- [ ] Copy Dashboard
- [ ] Copy Ingredients pages
- [ ] Copy Foods pages
- [ ] Copy POS system
- [ ] Test each page
- [ ] Commit daily

### Member 3 - Days 10-11
- [ ] Copy Sales, Alerts, Analytics, Users
- [ ] Copy all CSS files
- [ ] Test responsive design
- [ ] Fix styling issues
- [ ] Commit to `feature/frontend`

### All Members - Days 11-12
- [ ] Merge all branches to `develop`
- [ ] Integration testing
- [ ] Fix bugs together
- [ ] Test all workflows
- [ ] Prepare for deployment

### All Members - Day 13
- [ ] Final testing
- [ ] Merge to `main`
- [ ] Update README
- [ ] Celebrate! 🎉

---

## 🆘 EMERGENCY CONTACTS

### Backend Won't Start?
1. Check MySQL is running
2. Check `application.yml` has correct password
3. Run `mvn clean install`
4. Check console for error messages
5. Google the error message

### Frontend Won't Start?
1. Delete `node_modules` folder
2. Run `npm install` again
3. Check Node version: `node -v` (should be 16+)
4. Run `npm run dev` again

### Can't Connect Frontend to Backend?
1. Check backend is running (http://localhost:8080)
2. Check `vite.config.js` has proxy configuration
3. Check browser console for CORS errors
4. Check `api.js` has `withCredentials: true`

---

## 📞 WHAT TO DO RIGHT NOW

### Member 1: START THESE COMMANDS NOW!

```powershell
# 1. Check installations
java -version
mvn -version
mysql --version

# 2. Create database
mysql -u root -p
# Enter password, then run:
CREATE DATABASE restaurant_stock_db;
exit

# 3. Open IntelliJ IDEA
# Create new Maven project
# Group ID: com.restaurant
# Artifact ID: stockmanagement

# 4. Open this location in File Explorer:
cd C:\restaurant-stock-management

# 5. Open CODE_PART1_DTOs_Services.md
# Start copying files!
```

### Member 2 & 3: WAIT FOR DAY 3 & 5
- Review the code documentation files
- Install required software
- Prepare your development environment

---

## ✅ SUCCESS INDICATORS

You're on track if:
- ✅ Member 1: Backend runs by end of Day 1
- ✅ Member 2: Can signup/login by end of Day 3
- ✅ Member 3: See login page by end of Day 5
- ✅ All: Complete integration by Day 11

---

## 🎯 REMEMBER

1. **Follow the order** - Don't skip steps
2. **Copy exactly** - Don't modify code initially
3. **Test frequently** - After each file group
4. **Commit daily** - Don't lose work
5. **Communicate** - Help each other
6. **Stay calm** - All code is ready!

---

**YOU'VE GOT THIS! START CODING NOW! 🚀**

*All code is in CODE_PART files - just copy and paste!*
