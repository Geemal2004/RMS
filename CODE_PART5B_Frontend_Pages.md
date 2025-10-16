# CODE PART 5B: Frontend Pages - Authentication & Dashboard

---

## File: `frontend/src/pages/Auth/Login.jsx`

```jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './Auth.css';

const Login = () => {
  const [formData, setFormData] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await login(formData);
      navigate('/dashboard');
    } catch (err) {
      setError(err.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1>🍽️ Restaurant Stock Management</h1>
        <h2>Login</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
              placeholder="Enter username"
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder="Enter password"
            />
          </div>

          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        <p className="auth-link">
          Don't have an account? <Link to="/signup">Sign up</Link>
        </p>
      </div>
    </div>
  );
};

export default Login;
```

---

## File: `frontend/src/pages/Auth/Signup.jsx`

```jsx
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import './Auth.css';

const Signup = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    fullName: '',
    phoneNumber: '',
    roles: ['ROLE_CASHIER']
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { signup } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleRoleChange = (e) => {
    setFormData({ ...formData, roles: [e.target.value] });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await signup(formData);
      alert('Registration successful! Please login.');
      navigate('/login');
    } catch (err) {
      setError(err.message || 'Registration failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1>🍽️ Restaurant Stock Management</h1>
        <h2>Sign Up</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
              minLength="3"
              placeholder="Enter username"
            />
          </div>

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="Enter email"
            />
          </div>

          <div className="form-group">
            <label>Full Name</label>
            <input
              type="text"
              name="fullName"
              value={formData.fullName}
              onChange={handleChange}
              required
              placeholder="Enter full name"
            />
          </div>

          <div className="form-group">
            <label>Phone Number</label>
            <input
              type="tel"
              name="phoneNumber"
              value={formData.phoneNumber}
              onChange={handleChange}
              placeholder="Enter phone number"
            />
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              minLength="6"
              placeholder="Enter password (min 6 characters)"
            />
          </div>

          <div className="form-group">
            <label>Role</label>
            <select onChange={handleRoleChange} value={formData.roles[0]}>
              <option value="ROLE_CASHIER">Cashier</option>
              <option value="ROLE_CHEF">Chef</option>
              <option value="ROLE_STOCK_MANAGER">Stock Manager</option>
              <option value="ROLE_BRANCH_MANAGER">Branch Manager</option>
              <option value="ROLE_OWNER">Owner</option>
            </select>
          </div>

          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Registering...' : 'Sign Up'}
          </button>
        </form>

        <p className="auth-link">
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  );
};

export default Signup;
```

---

## File: `frontend/src/pages/Dashboard/Dashboard.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { ingredientService } from '../../services/ingredientService';
import { foodService } from '../../services/foodService';
import { alertService } from '../../services/alertService';
import './Dashboard.css';

const Dashboard = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState({
    lowStock: 0,
    outOfStock: 0,
    expiring: 0,
    alerts: 0,
    availableFoods: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const [lowStock, outOfStock, expiring, alerts, availableFoods] = await Promise.all([
        ingredientService.getLowStock(),
        ingredientService.getOutOfStock(),
        ingredientService.getExpiring(7),
        alertService.getUnacknowledged(),
        foodService.getAvailable()
      ]);

      setStats({
        lowStock: lowStock.length,
        outOfStock: outOfStock.length,
        expiring: expiring.length,
        alerts: alerts.length,
        availableFoods: availableFoods.length
      });
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div className="loading">Loading dashboard...</div>;

  return (
    <div className="dashboard">
      <h1>Welcome, {user?.fullName}!</h1>
      <p className="dashboard-subtitle">
        Role: {user?.roles?.join(', ')}
      </p>

      <div className="stats-grid">
        <div className="stat-card low-stock">
          <h3>Low Stock</h3>
          <p className="stat-number">{stats.lowStock}</p>
          <p className="stat-label">Ingredients below minimum</p>
        </div>

        <div className="stat-card out-of-stock">
          <h3>Out of Stock</h3>
          <p className="stat-number">{stats.outOfStock}</p>
          <p className="stat-label">Ingredients depleted</p>
        </div>

        <div className="stat-card expiring">
          <h3>Expiring Soon</h3>
          <p className="stat-number">{stats.expiring}</p>
          <p className="stat-label">Within 7 days</p>
        </div>

        <div className="stat-card alerts">
          <h3>Active Alerts</h3>
          <p className="stat-number">{stats.alerts}</p>
          <p className="stat-label">Unacknowledged</p>
        </div>

        <div className="stat-card available-foods">
          <h3>Available Foods</h3>
          <p className="stat-number">{stats.availableFoods}</p>
          <p className="stat-label">Ready to serve</p>
        </div>
      </div>

      <div className="dashboard-info">
        <div className="info-card">
          <h3>Quick Actions</h3>
          <ul>
            <li>✅ Check stock levels regularly</li>
            <li>📧 Review email notifications</li>
            <li>📊 Monitor usage patterns</li>
            <li>🔔 Acknowledge alerts promptly</li>
          </ul>
        </div>

        <div className="info-card">
          <h3>System Status</h3>
          <p>🟢 All systems operational</p>
          <p>📅 Last updated: {new Date().toLocaleString()}</p>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
```

---

## File: `frontend/src/pages/Auth/Auth.css`

```css
.auth-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.auth-card {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 450px;
}

.auth-card h1 {
  text-align: center;
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}

.auth-card h2 {
  text-align: center;
  color: #666;
  margin-bottom: 30px;
  font-size: 20px;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-weight: 600;
  color: #333;
  font-size: 14px;
}

.form-group input,
.form-group select {
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.3s;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #667eea;
}

.btn-primary {
  padding: 14px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s;
}

.btn-primary:hover {
  transform: translateY(-2px);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-message {
  background: #fee;
  color: #c33;
  padding: 12px;
  border-radius: 8px;
  border-left: 4px solid #c33;
  font-size: 14px;
}

.auth-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.auth-link a {
  color: #667eea;
  text-decoration: none;
  font-weight: 600;
}

.auth-link a:hover {
  text-decoration: underline;
}
```

---

## File: `frontend/src/pages/Dashboard/Dashboard.css`

```css
.dashboard {
  padding: 30px;
}

.dashboard h1 {
  font-size: 32px;
  color: #333;
  margin-bottom: 8px;
}

.dashboard-subtitle {
  color: #666;
  font-size: 16px;
  margin-bottom: 30px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-left: 4px solid #667eea;
  transition: transform 0.2s;
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.stat-card h3 {
  font-size: 14px;
  color: #666;
  margin-bottom: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.stat-number {
  font-size: 48px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.stat-label {
  font-size: 13px;
  color: #999;
  margin-top: 8px;
}

.stat-card.low-stock {
  border-left-color: #ff9800;
}

.stat-card.out-of-stock {
  border-left-color: #f44336;
}

.stat-card.expiring {
  border-left-color: #ff5722;
}

.stat-card.alerts {
  border-left-color: #e91e63;
}

.stat-card.available-foods {
  border-left-color: #4caf50;
}

.dashboard-info {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.info-card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.info-card h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 16px;
}

.info-card ul {
  list-style: none;
  padding: 0;
}

.info-card li {
  padding: 8px 0;
  color: #666;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
  font-size: 18px;
  color: #666;
}
```

---

**Continue to next file for Ingredients, Foods, Sales, and other pages...**
