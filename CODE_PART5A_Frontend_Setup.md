# CODE PART 5: Complete React Frontend Application

This includes all React components, pages, services, and configuration.

---

## File: `frontend/package.json`

```json
{
  "name": "restaurant-stock-frontend",
  "private": true,
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.20.0",
    "axios": "^1.6.2",
    "react-hook-form": "^7.48.2",
    "recharts": "^2.10.3"
  },
  "devDependencies": {
    "@types/react": "^18.2.43",
    "@types/react-dom": "^18.2.17",
    "@vitejs/plugin-react": "^4.2.1",
    "vite": "^5.0.8"
  }
}
```

---

## File: `frontend/vite.config.js`

```javascript
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  }
})
```

---

## File: `frontend/index.html`

```html
<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <link rel="icon" type="image/svg+xml" href="/vite.svg" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Restaurant Stock Management</title>
  </head>
  <body>
    <div id="root"></div>
    <script type="module" src="/src/main.jsx"></script>
  </body>
</html>
```

---

## File: `frontend/src/main.jsx`

```jsx
import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
```

---

## File: `frontend/src/App.jsx`

```jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Layout from './components/Layout/Layout';
import Login from './pages/Auth/Login';
import Signup from './pages/Auth/Signup';
import Dashboard from './pages/Dashboard/Dashboard';
import Ingredients from './pages/Ingredients/Ingredients';
import IngredientForm from './pages/Ingredients/IngredientForm';
import Foods from './pages/Foods/Foods';
import FoodForm from './pages/Foods/FoodForm';
import Sales from './pages/Sales/Sales';
import POS from './pages/Sales/POS';
import Alerts from './pages/Alerts/Alerts';
import Analytics from './pages/Analytics/Analytics';
import Users from './pages/Users/Users';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          
          <Route path="/" element={<ProtectedRoute><Layout /></ProtectedRoute>}>
            <Route index element={<Navigate to="/dashboard" replace />} />
            <Route path="dashboard" element={<Dashboard />} />
            
            <Route path="ingredients" element={<Ingredients />} />
            <Route path="ingredients/new" element={<IngredientForm />} />
            <Route path="ingredients/:id/edit" element={<IngredientForm />} />
            
            <Route path="foods" element={<Foods />} />
            <Route path="foods/new" element={<FoodForm />} />
            <Route path="foods/:id/edit" element={<FoodForm />} />
            
            <Route path="sales" element={<Sales />} />
            <Route path="pos" element={<POS />} />
            
            <Route path="alerts" element={<Alerts />} />
            <Route path="analytics" element={<Analytics />} />
            <Route path="users" element={<Users />} />
          </Route>
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;
```

---

## File: `frontend/src/contexts/AuthContext.jsx`

```jsx
import React, { createContext, useState, useContext, useEffect } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    checkAuth();
  }, []);

  const checkAuth = async () => {
    try {
      const currentUser = await authService.getCurrentUser();
      setUser(currentUser);
    } catch (error) {
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const login = async (credentials) => {
    const userData = await authService.login(credentials);
    setUser(userData);
    return userData;
  };

  const signup = async (data) => {
    const userData = await authService.signup(data);
    return userData;
  };

  const logout = async () => {
    await authService.logout();
    setUser(null);
  };

  const hasRole = (roles) => {
    if (!user || !user.roles) return false;
    return roles.some(role => user.roles.includes(role));
  };

  const value = {
    user,
    loading,
    login,
    signup,
    logout,
    hasRole
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
```

---

## File: `frontend/src/services/api.js`

```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    if (error.response?.status === 401) {
      window.location.href = '/login';
    }
    return Promise.reject(error.response?.data || error.message);
  }
);

export default api;
```

---

## File: `frontend/src/services/authService.js`

```javascript
import api from './api';

export const authService = {
  async login(credentials) {
    const response = await api.post('/auth/login', credentials);
    return response.data;
  },

  async signup(data) {
    const response = await api.post('/auth/signup', data);
    return response.data;
  },

  async logout() {
    await api.post('/auth/logout');
  },

  async getCurrentUser() {
    const response = await api.get('/auth/me');
    return response.data;
  }
};
```

---

## File: `frontend/src/services/ingredientService.js`

```javascript
import api from './api';

export const ingredientService = {
  async getAll(page = 0, size = 10, sortBy = 'name', sortDir = 'ASC') {
    const response = await api.get('/ingredients', {
      params: { page, size, sortBy, sortDir }
    });
    return response.data;
  },

  async getById(id) {
    const response = await api.get(`/ingredients/${id}`);
    return response.data;
  },

  async create(data) {
    const response = await api.post('/ingredients', data);
    return response.data;
  },

  async update(id, data) {
    const response = await api.put(`/ingredients/${id}`, data);
    return response.data;
  },

  async delete(id) {
    await api.delete(`/ingredients/${id}`);
  },

  async getLowStock() {
    const response = await api.get('/ingredients/low-stock');
    return response.data;
  },

  async getOutOfStock() {
    const response = await api.get('/ingredients/out-of-stock');
    return response.data;
  },

  async getExpiring(daysAhead = 7) {
    const response = await api.get('/ingredients/expiring', {
      params: { daysAhead }
    });
    return response.data;
  },

  async search(query, page = 0, size = 10) {
    const response = await api.get('/ingredients/search', {
      params: { query, page, size }
    });
    return response.data;
  }
};
```

---

## File: `frontend/src/services/foodService.js`

```javascript
import api from './api';

export const foodService = {
  async getAll(page = 0, size = 10) {
    const response = await api.get('/foods', {
      params: { page, size }
    });
    return response.data;
  },

  async getById(id) {
    const response = await api.get(`/foods/${id}`);
    return response.data;
  },

  async create(data) {
    const response = await api.post('/foods', data);
    return response.data;
  },

  async update(id, data) {
    const response = await api.put(`/foods/${id}`, data);
    return response.data;
  },

  async delete(id) {
    await api.delete(`/foods/${id}`);
  },

  async getAvailable() {
    const response = await api.get('/foods/available');
    return response.data;
  },

  async search(query, page = 0, size = 10) {
    const response = await api.get('/foods/search', {
      params: { query, page, size }
    });
    return response.data;
  }
};
```

---

## File: `frontend/src/services/saleService.js`

```javascript
import api from './api';

export const saleService = {
  async create(data) {
    const response = await api.post('/sales', data);
    return response.data;
  },

  async getAll(page = 0, size = 10) {
    const response = await api.get('/sales', {
      params: { page, size }
    });
    return response.data;
  },

  async getById(id) {
    const response = await api.get(`/sales/${id}`);
    return response.data;
  },

  async getByDateRange(startDate, endDate) {
    const response = await api.get('/sales/date-range', {
      params: { startDate, endDate }
    });
    return response.data;
  },

  async getTotalSales(startDate, endDate) {
    const response = await api.get('/sales/total', {
      params: { startDate, endDate }
    });
    return response.data;
  }
};
```

---

## File: `frontend/src/services/alertService.js`

```javascript
import api from './api';

export const alertService = {
  async getAll(page = 0, size = 20) {
    const response = await api.get('/alerts', {
      params: { page, size }
    });
    return response.data;
  },

  async getUnacknowledged() {
    const response = await api.get('/alerts/unacknowledged');
    return response.data;
  },

  async acknowledge(id) {
    const response = await api.put(`/alerts/${id}/acknowledge`);
    return response.data;
  }
};
```

---

## File: `frontend/src/services/analyticsService.js`

```javascript
import api from './api';

export const analyticsService = {
  async getMostUsedIngredients(days = 30) {
    const response = await api.get('/analytics/most-used-ingredients', {
      params: { days }
    });
    return response.data;
  },

  async getMostSoldFoods(days = 30) {
    const response = await api.get('/analytics/most-sold-foods', {
      params: { days }
    });
    return response.data;
  },

  async getRestockRecommendations() {
    const response = await api.get('/analytics/restock-recommendations');
    return response.data;
  }
};
```

---

## File: `frontend/src/components/ProtectedRoute.jsx`

```jsx
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const ProtectedRoute = ({ children, roles }) => {
  const { user, loading } = useAuth();

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (roles && !roles.some(role => user.roles?.includes(role))) {
    return <div className="unauthorized">Unauthorized Access</div>;
  }

  return children;
};

export default ProtectedRoute;
```

---

## File: `frontend/src/components/Layout/Layout.jsx`

```jsx
import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import Sidebar from './Sidebar';
import './Layout.css';

const Layout = () => {
  return (
    <div className="layout">
      <Navbar />
      <div className="layout-container">
        <Sidebar />
        <main className="main-content">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default Layout;
```

---

## File: `frontend/src/components/Layout/Navbar.jsx`

```jsx
import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      await logout();
      navigate('/login');
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/dashboard">🍽️ Restaurant Stock</Link>
      </div>
      <div className="navbar-user">
        <span className="user-name">{user?.fullName}</span>
        <span className="user-role">{user?.roles?.join(', ')}</span>
        <button onClick={handleLogout} className="btn-logout">Logout</button>
      </div>
    </nav>
  );
};

export default Navbar;
```

---

## File: `frontend/src/components/Layout/Sidebar.jsx`

```jsx
import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './Sidebar.css';

const Sidebar = () => {
  const { hasRole } = useAuth();

  return (
    <aside className="sidebar">
      <nav className="sidebar-nav">
        <NavLink to="/dashboard" className="nav-link">
          📊 Dashboard
        </NavLink>

        <NavLink to="/ingredients" className="nav-link">
          🥬 Ingredients
        </NavLink>

        <NavLink to="/foods" className="nav-link">
          🍔 Foods Menu
        </NavLink>

        {hasRole(['ROLE_CASHIER']) && (
          <NavLink to="/pos" className="nav-link">
            💰 Point of Sale
          </NavLink>
        )}

        {hasRole(['ROLE_OWNER', 'ROLE_BRANCH_MANAGER', 'ROLE_CASHIER']) && (
          <NavLink to="/sales" className="nav-link">
            📈 Sales History
          </NavLink>
        )}

        {hasRole(['ROLE_OWNER', 'ROLE_BRANCH_MANAGER', 'ROLE_STOCK_MANAGER']) && (
          <NavLink to="/alerts" className="nav-link">
            🔔 Alerts
          </NavLink>
        )}

        {hasRole(['ROLE_OWNER', 'ROLE_BRANCH_MANAGER', 'ROLE_STOCK_MANAGER']) && (
          <NavLink to="/analytics" className="nav-link">
            📉 Analytics
          </NavLink>
        )}

        {hasRole(['ROLE_OWNER', 'ROLE_BRANCH_MANAGER']) && (
          <NavLink to="/users" className="nav-link">
            👥 Users
          </NavLink>
        )}
      </nav>
    </aside>
  );
};

export default Sidebar;
```

---

**Continue to next file for page components...**

This is Part 1 of the frontend. Would you like me to continue with the page components (Login, Dashboard, Ingredients, Foods, POS, etc.) and CSS styling?
