# CODE PART 5F: Global CSS & Application Entry Files

---

## File: `frontend/src/index.css`

```css
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
    'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
    sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background: #f0f2f5;
  color: #212529;
}

code {
  font-family: source-code-pro, Menlo, Monaco, Consolas, 'Courier New',
    monospace;
}

/* Scrollbar Styling */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: #555;
}

/* Loading Spinner */
.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #6c757d;
}

.loading::after {
  content: '...';
  animation: dots 1.5s infinite;
}

@keyframes dots {
  0%, 20% { content: '.'; }
  40% { content: '..'; }
  60%, 100% { content: '...'; }
}
```

---

## File: `frontend/src/App.css`

```css
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Common Button Styles */
.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
  text-decoration: none;
  display: inline-block;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.btn-primary:disabled {
  background: #6c757d;
  cursor: not-allowed;
  transform: none;
}

.btn-secondary {
  background: #6c757d;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-secondary:hover {
  background: #5a6268;
}

.btn-secondary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Form Input Styles */
input, select, textarea {
  font-family: inherit;
}

input:focus, select:focus, textarea:focus {
  outline: none;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.2);
}

/* Card Styles */
.card {
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  padding: 20px;
}

/* Alert/Notification Styles */
.alert {
  padding: 15px 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.alert-success {
  background: #d4edda;
  color: #155724;
  border-left: 4px solid #28a745;
}

.alert-error {
  background: #f8d7da;
  color: #721c24;
  border-left: 4px solid #dc3545;
}

.alert-warning {
  background: #fff3cd;
  color: #856404;
  border-left: 4px solid #ffc107;
}

.alert-info {
  background: #d1ecf1;
  color: #0c5460;
  border-left: 4px solid #17a2b8;
}

/* Responsive Design */
@media (max-width: 768px) {
  .btn-primary,
  .btn-secondary {
    width: 100%;
    padding: 14px 24px;
  }
}
```

---

## File: `frontend/public/index.html`

```html
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <link rel="icon" href="/favicon.ico" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="theme-color" content="#000000" />
    <meta
      name="description"
      content="Restaurant Stock Management System - Manage your restaurant inventory, food menu, sales, and analytics"
    />
    <title>Restaurant Stock Management</title>
  </head>
  <body>
    <noscript>You need to enable JavaScript to run this app.</noscript>
    <div id="root"></div>
  </body>
</html>
```

---

## File: `frontend/src/main.jsx`

```jsx
import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './index.css';

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
```

---

## Additional Important Files

### File: `frontend/src/services/userService.js`

```javascript
import api from './api';

export const userService = {
  getAll: async () => {
    const response = await api.get('/users');
    return response.data;
  },

  getById: async (id) => {
    const response = await api.get(`/users/${id}`);
    return response.data;
  },

  activate: async (id) => {
    const response = await api.put(`/users/${id}/activate`);
    return response.data;
  },

  deactivate: async (id) => {
    const response = await api.put(`/users/${id}/deactivate`);
    return response.data;
  },

  delete: async (id) => {
    await api.delete(`/users/${id}`);
  }
};
```

---

## File: `frontend/.gitignore`

```
# Dependencies
node_modules/
/.pnp
.pnp.js

# Testing
/coverage

# Production
/dist
/build

# Misc
.DS_Store
.env.local
.env.development.local
.env.test.local
.env.production.local

npm-debug.log*
yarn-debug.log*
yarn-error.log*
pnpm-debug.log*

# IDE
.vscode/
.idea/
*.swp
*.swo
*~

# OS
Thumbs.db
```

---

## Backend Application Properties (Update with Email Configuration)

### File: `backend/src/main/resources/application.yml`

```yaml
spring:
  application:
    name: restaurant-stock-management
    
  datasource:
    url: jdbc:mysql://localhost:3306/restaurant_stock_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password: your_password_here
    driver-class-name: com.mysql.cj.jdbc.Driver
    
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
        
  servlet:
    multipart:
      enabled: true
      max-file-size: 5MB
      max-request-size: 5MB
      
  cache:
    type: simple
    
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000

server:
  port: 8080
  servlet:
    context-path: /api
    session:
      cookie:
        http-only: true
        secure: false
        same-site: lax
        max-age: 86400

file:
  upload-dir: ./uploads

logging:
  level:
    com.restaurant.stockmanagement: DEBUG
    org.springframework.security: DEBUG
    org.hibernate.SQL: DEBUG
```

---

## Environment Configuration Instructions

### Backend Configuration:

1. **Database Setup:**
   - Install MySQL 8.0+
   - Create database: `CREATE DATABASE restaurant_stock_db;`
   - Update `application.yml` with your MySQL credentials

2. **Email Configuration (Optional):**
   - For Gmail: Enable 2FA and create App Password
   - Update `spring.mail.username` and `spring.mail.password` in `application.yml`
   - Or disable email features by commenting out `@Async` methods

3. **File Upload Directory:**
   - Create `uploads` folder in project root or update `file.upload-dir` path

### Frontend Configuration:

1. **Environment Variables (.env file):**

```env
VITE_API_URL=http://localhost:8080/api
```

---

## Complete README Addition

Add this section to your existing `README.md`:

### Project Structure Overview

```
restaurant-stock-management/
├── backend/
│   ├── src/main/java/com/restaurant/stockmanagement/
│   │   ├── model/           # Entity classes (User, Ingredient, Food, etc.)
│   │   ├── repository/      # Spring Data JPA repositories
│   │   ├── service/         # Business logic layer
│   │   ├── controller/      # REST API endpoints
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── exception/      # Custom exceptions
│   │   └── config/         # Security, Swagger, Cache, etc.
│   ├── src/main/resources/
│   │   └── application.yml  # Application configuration
│   └── pom.xml             # Maven dependencies
│
├── frontend/
│   ├── public/
│   │   └── index.html
│   ├── src/
│   │   ├── components/     # Reusable components (Layout, Navbar, etc.)
│   │   ├── contexts/       # React contexts (AuthContext)
│   │   ├── pages/          # Page components (Dashboard, Ingredients, etc.)
│   │   ├── services/       # API service modules
│   │   ├── App.jsx         # Main app component with routing
│   │   ├── main.jsx        # Entry point
│   │   └── index.css       # Global styles
│   ├── package.json
│   └── vite.config.js
│
└── uploads/                # File upload directory (created automatically)
```

---

## API Endpoints Reference

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration
- `POST /api/auth/logout` - User logout
- `GET /api/auth/current` - Get current user

### Ingredients
- `GET /api/ingredients?page=0&size=10` - Get all ingredients (paginated)
- `GET /api/ingredients/{id}` - Get ingredient by ID
- `POST /api/ingredients` - Create ingredient (STOCK_MANAGER, OWNER)
- `PUT /api/ingredients/{id}` - Update ingredient
- `DELETE /api/ingredients/{id}` - Delete ingredient
- `GET /api/ingredients/search?query={query}` - Search ingredients
- `GET /api/ingredients/low-stock` - Get low stock ingredients

### Foods
- `GET /api/foods?page=0&size=10` - Get all foods (paginated)
- `GET /api/foods/{id}` - Get food by ID
- `POST /api/foods` - Create food (CHEF, OWNER)
- `PUT /api/foods/{id}` - Update food
- `DELETE /api/foods/{id}` - Delete food
- `GET /api/foods/available` - Get available foods
- `GET /api/foods/{id}/check-availability` - Check food availability

### Sales
- `GET /api/sales?page=0&size=10` - Get all sales (paginated)
- `GET /api/sales/{id}` - Get sale by ID
- `POST /api/sales` - Create sale (CASHIER, OWNER)
- `GET /api/sales/date-range?start={date}&end={date}` - Get sales by date range

### Stock Alerts
- `GET /api/alerts` - Get all alerts
- `GET /api/alerts/unacknowledged` - Get unacknowledged alerts
- `GET /api/alerts/type/{type}` - Get alerts by type
- `PUT /api/alerts/{id}/acknowledge` - Acknowledge alert

### Analytics
- `GET /api/analytics/most-used?days=30` - Most used ingredients
- `GET /api/analytics/most-sold?days=30` - Most sold foods
- `GET /api/analytics/restock-recommendations` - Restock recommendations

### Reports
- `GET /api/reports/inventory/csv` - Export inventory as CSV
- `GET /api/reports/sales/csv?start={date}&end={date}` - Export sales as CSV
- `GET /api/reports/low-stock/csv` - Export low stock report as CSV

### Users (OWNER, BRANCH_MANAGER only)
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `PUT /api/users/{id}/activate` - Activate user
- `PUT /api/users/{id}/deactivate` - Deactivate user
- `DELETE /api/users/{id}` - Delete user

---

## Testing the Application

### Backend Testing:
1. Use **Postman** or **Swagger UI** (http://localhost:8080/api/swagger-ui.html)
2. First, login to get session cookie
3. All subsequent requests will use the session automatically

### Frontend Testing:
1. Start with login page (http://localhost:5173)
2. Use test credentials created via signup
3. Navigate through different pages based on role

### Test User Roles:
- **OWNER**: Full access to all features
- **BRANCH_MANAGER**: Manage inventory and view reports
- **STOCK_MANAGER**: Manage ingredients only
- **CHEF**: Manage food menu
- **CASHIER**: Create sales at POS

---

## Common Issues & Solutions

### Backend Issues:

1. **Database Connection Error:**
   - Ensure MySQL is running
   - Check credentials in `application.yml`
   - Verify database exists

2. **Port Already in Use:**
   - Change `server.port` in `application.yml`
   - Or kill process: `netstat -ano | findstr :8080` then `taskkill /PID <PID> /F`

3. **Email Sending Error:**
   - Configure valid SMTP credentials
   - Or disable email by removing `@Async` and email calls

### Frontend Issues:

1. **CORS Error:**
   - Ensure backend allows `http://localhost:5173`
   - Check `SecurityConfig` CORS configuration

2. **Session Not Persisting:**
   - Check cookie settings in browser
   - Ensure `withCredentials: true` in axios

3. **Build Errors:**
   - Run `npm install` again
   - Clear node_modules and reinstall

---

## Next Steps After Setup

1. **Day 1-2:** Set up backend, test APIs with Postman
2. **Day 3-5:** Implement and test backend services
3. **Day 6-9:** Develop frontend components
4. **Day 10-11:** Integration testing
5. **Day 12:** Bug fixes and polish
6. **Day 13:** Final testing and deployment preparation

---

## Deployment Considerations

### Backend:
- Use production database (not localhost)
- Set `spring.jpa.hibernate.ddl-auto=validate` in production
- Enable HTTPS and set `server.servlet.session.cookie.secure=true`
- Use environment variables for sensitive data

### Frontend:
- Update `VITE_API_URL` to production backend URL
- Build: `npm run build`
- Deploy `dist` folder to hosting (Vercel, Netlify, etc.)

---

## Support & Maintenance

### Logging:
- Backend logs in console and file (configure in `application.yml`)
- Frontend logs in browser console

### Monitoring:
- Use Spring Boot Actuator for health checks
- Monitor database connections and performance

### Backup:
- Regular database backups
- Keep uploaded files backed up

---

**🎉 ALL CODE GENERATION COMPLETE! 🎉**

You now have:
- ✅ Complete Backend (Models, Repositories, Services, Controllers)
- ✅ Complete Frontend (All Pages, Components, Styles)
- ✅ Configuration Files
- ✅ Documentation
- ✅ Git Workflow Guide

**Next Action:** Follow `GIT_WORKFLOW.md` starting from Day 1!
