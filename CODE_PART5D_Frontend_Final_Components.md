# CODE PART 5D: Frontend Pages - Foods Form, Sales, Alerts, Analytics, Users

---

## File: `frontend/src/pages/Foods/FoodForm.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { foodService } from '../../services/foodService';
import { ingredientService } from '../../services/ingredientService';
import './FoodForm.css';

const FoodForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [ingredients, setIngredients] = useState([]);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    category: '',
    price: 0,
    preparationTime: 0
  });
  const [recipe, setRecipe] = useState([]);

  useEffect(() => {
    fetchIngredients();
    if (id) {
      fetchFood();
    }
  }, [id]);

  const fetchIngredients = async () => {
    try {
      const response = await ingredientService.getAll(0, 100);
      setIngredients(response.content);
    } catch (error) {
      console.error('Error fetching ingredients:', error);
    }
  };

  const fetchFood = async () => {
    try {
      const data = await foodService.getById(id);
      setFormData({
        name: data.name,
        description: data.description,
        category: data.category,
        price: data.price,
        preparationTime: data.preparationTime
      });
      setRecipe(data.recipeIngredients || []);
    } catch (error) {
      alert('Error loading food item');
      navigate('/foods');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const addRecipeItem = () => {
    setRecipe([
      ...recipe,
      { ingredientId: '', quantity: 0, unit: 'kg', notes: '' }
    ]);
  };

  const removeRecipeItem = (index) => {
    setRecipe(recipe.filter((_, i) => i !== index));
  };

  const updateRecipeItem = (index, field, value) => {
    setRecipe(
      recipe.map((item, i) =>
        i === index ? { ...item, [field]: value } : item
      )
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const payload = {
      ...formData,
      recipeIngredients: recipe
    };

    try {
      if (id) {
        await foodService.update(id, payload);
        alert('Food item updated successfully');
      } else {
        await foodService.create(payload);
        alert('Food item created successfully');
      }
      navigate('/foods');
    } catch (error) {
      alert('Error saving food item');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-page">
      <h1>{id ? 'Edit Food Item' : 'Add New Food Item'}</h1>

      <form onSubmit={handleSubmit} className="food-form">
        <div className="form-row">
          <div className="form-group">
            <label>Name *</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Category</label>
            <input
              type="text"
              name="category"
              value={formData.category}
              onChange={handleChange}
              placeholder="e.g., Main Course, Dessert, Beverage"
            />
          </div>
        </div>

        <div className="form-group">
          <label>Description</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="3"
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Price *</label>
            <input
              type="number"
              name="price"
              value={formData.price}
              onChange={handleChange}
              min="0"
              step="0.01"
              required
            />
          </div>

          <div className="form-group">
            <label>Preparation Time (minutes)</label>
            <input
              type="number"
              name="preparationTime"
              value={formData.preparationTime}
              onChange={handleChange}
              min="0"
            />
          </div>
        </div>

        <div className="recipe-section">
          <div className="section-header">
            <h3>Recipe Ingredients</h3>
            <button type="button" onClick={addRecipeItem} className="btn-add">
              + Add Ingredient
            </button>
          </div>

          {recipe.map((item, index) => (
            <div key={index} className="recipe-item">
              <select
                value={item.ingredientId}
                onChange={(e) => updateRecipeItem(index, 'ingredientId', e.target.value)}
                required
              >
                <option value="">Select Ingredient</option>
                {ingredients.map((ing) => (
                  <option key={ing.id} value={ing.id}>
                    {ing.name} ({ing.unit})
                  </option>
                ))}
              </select>

              <input
                type="number"
                placeholder="Quantity"
                value={item.quantity}
                onChange={(e) => updateRecipeItem(index, 'quantity', e.target.value)}
                min="0"
                step="0.01"
                required
              />

              <select
                value={item.unit}
                onChange={(e) => updateRecipeItem(index, 'unit', e.target.value)}
                required
              >
                <option value="kg">kg</option>
                <option value="g">g</option>
                <option value="l">l</option>
                <option value="ml">ml</option>
                <option value="pieces">pieces</option>
              </select>

              <input
                type="text"
                placeholder="Notes (optional)"
                value={item.notes}
                onChange={(e) => updateRecipeItem(index, 'notes', e.target.value)}
              />

              <button
                type="button"
                onClick={() => removeRecipeItem(index)}
                className="btn-remove"
              >
                ✕
              </button>
            </div>
          ))}
        </div>

        <div className="form-actions">
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Saving...' : 'Save Food Item'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/foods')}
            className="btn-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default FoodForm;
```

---

## File: `frontend/src/pages/Sales/Sales.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { saleService } from '../../services/saleService';
import './Sales.css';

const Sales = () => {
  const [sales, setSales] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [selectedSale, setSelectedSale] = useState(null);

  useEffect(() => {
    fetchSales();
  }, [page, startDate, endDate]);

  const fetchSales = async () => {
    try {
      setLoading(true);
      const response = startDate && endDate
        ? await saleService.getByDateRange(startDate, endDate, page, 10)
        : await saleService.getAll(page, 10);
      
      setSales(response.content);
      setTotalPages(response.totalPages);
    } catch (error) {
      console.error('Error fetching sales:', error);
    } finally {
      setLoading(false);
    }
  };

  const viewDetails = (sale) => {
    setSelectedSale(sale);
  };

  const closeModal = () => {
    setSelectedSale(null);
  };

  const getTotalSales = () => {
    return sales.reduce((sum, sale) => sum + sale.totalAmount, 0);
  };

  return (
    <div className="sales-page">
      <h1>Sales History</h1>

      <div className="filters">
        <div className="filter-group">
          <label>Start Date</label>
          <input
            type="date"
            value={startDate}
            onChange={(e) => {
              setStartDate(e.target.value);
              setPage(0);
            }}
          />
        </div>
        <div className="filter-group">
          <label>End Date</label>
          <input
            type="date"
            value={endDate}
            onChange={(e) => {
              setEndDate(e.target.value);
              setPage(0);
            }}
          />
        </div>
        <button onClick={fetchSales} className="btn-primary">
          Apply Filter
        </button>
      </div>

      <div className="total-sales">
        <h3>Total Sales: ${getTotalSales().toFixed(2)}</h3>
      </div>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <>
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Cashier</th>
                  <th>Items</th>
                  <th>Total Amount</th>
                  <th>Payment</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {sales.map((sale) => (
                  <tr key={sale.id}>
                    <td>{new Date(sale.saleDate).toLocaleString()}</td>
                    <td>{sale.cashierName}</td>
                    <td>{sale.items?.length || 0}</td>
                    <td>${sale.totalAmount.toFixed(2)}</td>
                    <td>{sale.paymentMethod}</td>
                    <td>
                      <button
                        onClick={() => viewDetails(sale)}
                        className="btn-view"
                      >
                        View Details
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="pagination">
            <button
              onClick={() => setPage(page - 1)}
              disabled={page === 0}
              className="btn-secondary"
            >
              Previous
            </button>
            <span>
              Page {page + 1} of {totalPages}
            </span>
            <button
              onClick={() => setPage(page + 1)}
              disabled={page >= totalPages - 1}
              className="btn-secondary"
            >
              Next
            </button>
          </div>
        </>
      )}

      {selectedSale && (
        <div className="modal-overlay" onClick={closeModal}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h2>Sale Details</h2>
            <p><strong>Date:</strong> {new Date(selectedSale.saleDate).toLocaleString()}</p>
            <p><strong>Cashier:</strong> {selectedSale.cashierName}</p>
            <p><strong>Payment Method:</strong> {selectedSale.paymentMethod}</p>

            <h3>Items</h3>
            <table className="items-table">
              <thead>
                <tr>
                  <th>Food</th>
                  <th>Quantity</th>
                  <th>Unit Price</th>
                  <th>Subtotal</th>
                </tr>
              </thead>
              <tbody>
                {selectedSale.items?.map((item, index) => (
                  <tr key={index}>
                    <td>{item.foodName}</td>
                    <td>{item.quantity}</td>
                    <td>${item.unitPrice.toFixed(2)}</td>
                    <td>${item.subtotal.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>

            <h3>Total: ${selectedSale.totalAmount.toFixed(2)}</h3>

            <button onClick={closeModal} className="btn-secondary">
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default Sales;
```

---

## File: `frontend/src/pages/Alerts/Alerts.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { alertService } from '../../services/alertService';
import './Alerts.css';

const Alerts = () => {
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL');

  useEffect(() => {
    fetchAlerts();
  }, [filter]);

  const fetchAlerts = async () => {
    try {
      setLoading(true);
      const data = filter === 'ALL'
        ? await alertService.getUnacknowledged()
        : await alertService.getByType(filter);
      
      setAlerts(data);
    } catch (error) {
      console.error('Error fetching alerts:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleAcknowledge = async (id) => {
    try {
      await alertService.acknowledge(id);
      fetchAlerts();
    } catch (error) {
      alert('Error acknowledging alert');
    }
  };

  const getAlertClass = (type) => {
    switch (type) {
      case 'LOW_STOCK':
        return 'alert-warning';
      case 'OUT_OF_STOCK':
        return 'alert-danger';
      case 'EXPIRING_SOON':
        return 'alert-info';
      case 'EXPIRED':
        return 'alert-danger';
      default:
        return '';
    }
  };

  const getAlertIcon = (type) => {
    switch (type) {
      case 'LOW_STOCK':
        return '⚠️';
      case 'OUT_OF_STOCK':
        return '❌';
      case 'EXPIRING_SOON':
        return '🕐';
      case 'EXPIRED':
        return '☠️';
      default:
        return '📌';
    }
  };

  return (
    <div className="alerts-page">
      <h1>Stock Alerts</h1>

      <div className="alert-filters">
        <button
          onClick={() => setFilter('ALL')}
          className={filter === 'ALL' ? 'active' : ''}
        >
          All Alerts
        </button>
        <button
          onClick={() => setFilter('LOW_STOCK')}
          className={filter === 'LOW_STOCK' ? 'active' : ''}
        >
          Low Stock
        </button>
        <button
          onClick={() => setFilter('OUT_OF_STOCK')}
          className={filter === 'OUT_OF_STOCK' ? 'active' : ''}
        >
          Out of Stock
        </button>
        <button
          onClick={() => setFilter('EXPIRING_SOON')}
          className={filter === 'EXPIRING_SOON' ? 'active' : ''}
        >
          Expiring Soon
        </button>
        <button
          onClick={() => setFilter('EXPIRED')}
          className={filter === 'EXPIRED' ? 'active' : ''}
        >
          Expired
        </button>
      </div>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : alerts.length === 0 ? (
        <div className="no-alerts">✅ No alerts at this time</div>
      ) : (
        <div className="alerts-list">
          {alerts.map((alert) => (
            <div key={alert.id} className={`alert-card ${getAlertClass(alert.alertType)}`}>
              <div className="alert-icon">{getAlertIcon(alert.alertType)}</div>
              <div className="alert-content">
                <h3>{alert.ingredientName}</h3>
                <p className="alert-message">{alert.message}</p>
                <p className="alert-date">
                  {new Date(alert.createdAt).toLocaleString()}
                </p>
              </div>
              {!alert.acknowledged && (
                <button
                  onClick={() => handleAcknowledge(alert.id)}
                  className="btn-acknowledge"
                >
                  Acknowledge
                </button>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Alerts;
```

---

## File: `frontend/src/pages/Analytics/Analytics.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { analyticsService } from '../../services/analyticsService';
import {
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import './Analytics.css';

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884d8', '#82ca9d'];

const Analytics = () => {
  const [mostUsed, setMostUsed] = useState([]);
  const [mostSold, setMostSold] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const [days, setDays] = useState(30);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAnalytics();
  }, [days]);

  const fetchAnalytics = async () => {
    try {
      setLoading(true);
      const [usedData, soldData, recsData] = await Promise.all([
        analyticsService.getMostUsedIngredients(days),
        analyticsService.getMostSoldFoods(days),
        analyticsService.getRestockRecommendations()
      ]);

      setMostUsed(usedData);
      setMostSold(soldData);
      setRecommendations(recsData);
    } catch (error) {
      console.error('Error fetching analytics:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="analytics-page">
      <h1>Analytics Dashboard</h1>

      <div className="period-selector">
        <label>Time Period:</label>
        <select value={days} onChange={(e) => setDays(Number(e.target.value))}>
          <option value={7}>Last 7 Days</option>
          <option value={30}>Last 30 Days</option>
          <option value={90}>Last 90 Days</option>
        </select>
      </div>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <>
          <div className="charts-grid">
            <div className="chart-container">
              <h2>Most Used Ingredients</h2>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={mostUsed}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="usageCount" fill="#8884d8" name="Usage Count" />
                </BarChart>
              </ResponsiveContainer>
            </div>

            <div className="chart-container">
              <h2>Most Sold Foods</h2>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={mostSold}
                    dataKey="salesCount"
                    nameKey="name"
                    cx="50%"
                    cy="50%"
                    outerRadius={100}
                    label
                  >
                    {mostSold.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </div>
          </div>

          <div className="recommendations-section">
            <h2>Restock Recommendations</h2>
            <div className="table-container">
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Ingredient</th>
                    <th>Current Stock</th>
                    <th>Min Stock</th>
                    <th>Usage Rate</th>
                    <th>Recommendation</th>
                  </tr>
                </thead>
                <tbody>
                  {recommendations.map((rec) => (
                    <tr key={rec.ingredientId}>
                      <td>{rec.ingredientName}</td>
                      <td>{rec.currentStock} {rec.unit}</td>
                      <td>{rec.minimumStock} {rec.unit}</td>
                      <td>{rec.averageDailyUsage?.toFixed(2)} {rec.unit}/day</td>
                      <td className="recommendation">
                        {rec.recommendedOrderQuantity > 0
                          ? `Order ${rec.recommendedOrderQuantity} ${rec.unit}`
                          : 'Stock OK'}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default Analytics;
```

---

## File: `frontend/src/pages/Users/Users.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { userService } from '../../services/userService';
import './Users.css';

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const data = await userService.getAll();
      setUsers(data);
    } catch (error) {
      console.error('Error fetching users:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleToggleActive = async (userId, currentStatus) => {
    try {
      if (currentStatus) {
        await userService.deactivate(userId);
      } else {
        await userService.activate(userId);
      }
      fetchUsers();
    } catch (error) {
      alert('Error updating user status');
    }
  };

  const handleDelete = async (userId) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await userService.delete(userId);
        fetchUsers();
      } catch (error) {
        alert('Error deleting user');
      }
    }
  };

  return (
    <div className="users-page">
      <h1>User Management</h1>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Username</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Roles</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.username}</td>
                  <td>{user.fullName}</td>
                  <td>{user.email}</td>
                  <td>
                    {user.roles?.map((role) => (
                      <span key={role} className="role-badge">
                        {role.replace('ROLE_', '')}
                      </span>
                    ))}
                  </td>
                  <td>
                    <span className={`status-badge ${user.active ? 'active' : 'inactive'}`}>
                      {user.active ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td className="actions">
                    <button
                      onClick={() => handleToggleActive(user.id, user.active)}
                      className={user.active ? 'btn-deactivate' : 'btn-activate'}
                    >
                      {user.active ? 'Deactivate' : 'Activate'}
                    </button>
                    <button
                      onClick={() => handleDelete(user.id)}
                      className="btn-delete"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default Users;
```

---

**Continue to CSS files next...**
