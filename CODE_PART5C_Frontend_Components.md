# CODE PART 5C: Frontend Pages - Ingredients, Foods, Sales

---

## File: `frontend/src/pages/Ingredients/Ingredients.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ingredientService } from '../../services/ingredientService';
import './Ingredients.css';

const Ingredients = () => {
  const [ingredients, setIngredients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    fetchIngredients();
  }, [page, search]);

  const fetchIngredients = async () => {
    try {
      setLoading(true);
      const response = search
        ? await ingredientService.search(search, page, 10)
        : await ingredientService.getAll(page, 10);
      
      setIngredients(response.content);
      setTotalPages(response.totalPages);
    } catch (error) {
      console.error('Error fetching ingredients:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this ingredient?')) {
      try {
        await ingredientService.delete(id);
        fetchIngredients();
      } catch (error) {
        alert('Error deleting ingredient');
      }
    }
  };

  const getStockStatus = (ingredient) => {
    if (ingredient.currentStock === 0) return 'out-of-stock';
    if (ingredient.currentStock < ingredient.minimumStock) return 'low-stock';
    return 'in-stock';
  };

  return (
    <div className="ingredients-page">
      <div className="page-header">
        <h1>Ingredients Management</h1>
        <Link to="/ingredients/new" className="btn-primary">
          + Add Ingredient
        </Link>
      </div>

      <div className="search-bar">
        <input
          type="text"
          placeholder="Search ingredients..."
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setPage(0);
          }}
        />
      </div>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <>
          <div className="table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Category</th>
                  <th>Current Stock</th>
                  <th>Min Stock</th>
                  <th>Unit</th>
                  <th>Cost/Unit</th>
                  <th>Expiry Date</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {ingredients.map((ingredient) => (
                  <tr key={ingredient.id}>
                    <td>{ingredient.name}</td>
                    <td>{ingredient.category || 'N/A'}</td>
                    <td>{ingredient.currentStock}</td>
                    <td>{ingredient.minimumStock}</td>
                    <td>{ingredient.unit}</td>
                    <td>${ingredient.costPerUnit?.toFixed(2) || '0.00'}</td>
                    <td>{ingredient.expiryDate || 'N/A'}</td>
                    <td>
                      <span className={`status-badge ${getStockStatus(ingredient)}`}>
                        {getStockStatus(ingredient).replace('-', ' ')}
                      </span>
                    </td>
                    <td className="actions">
                      <button
                        onClick={() => navigate(`/ingredients/${ingredient.id}/edit`)}
                        className="btn-edit"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDelete(ingredient.id)}
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
    </div>
  );
};

export default Ingredients;
```

---

## File: `frontend/src/pages/Ingredients/IngredientForm.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { ingredientService } from '../../services/ingredientService';
import './IngredientForm.css';

const IngredientForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    unit: 'kg',
    currentStock: 0,
    minimumStock: 0,
    reorderLevel: 0,
    costPerUnit: 0,
    category: '',
    expiryDate: ''
  });

  useEffect(() => {
    if (id) {
      fetchIngredient();
    }
  }, [id]);

  const fetchIngredient = async () => {
    try {
      const data = await ingredientService.getById(id);
      setFormData(data);
    } catch (error) {
      alert('Error loading ingredient');
      navigate('/ingredients');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      if (id) {
        await ingredientService.update(id, formData);
        alert('Ingredient updated successfully');
      } else {
        await ingredientService.create(formData);
        alert('Ingredient created successfully');
      }
      navigate('/ingredients');
    } catch (error) {
      alert('Error saving ingredient');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-page">
      <h1>{id ? 'Edit Ingredient' : 'Add New Ingredient'}</h1>

      <form onSubmit={handleSubmit} className="ingredient-form">
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
              placeholder="e.g., Vegetables, Dairy, Meat"
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
            <label>Unit *</label>
            <select name="unit" value={formData.unit} onChange={handleChange} required>
              <option value="kg">Kilogram (kg)</option>
              <option value="g">Gram (g)</option>
              <option value="l">Liter (l)</option>
              <option value="ml">Milliliter (ml)</option>
              <option value="pieces">Pieces</option>
              <option value="dozen">Dozen</option>
            </select>
          </div>

          <div className="form-group">
            <label>Current Stock *</label>
            <input
              type="number"
              name="currentStock"
              value={formData.currentStock}
              onChange={handleChange}
              min="0"
              step="0.01"
              required
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Minimum Stock *</label>
            <input
              type="number"
              name="minimumStock"
              value={formData.minimumStock}
              onChange={handleChange}
              min="0"
              step="0.01"
              required
            />
          </div>

          <div className="form-group">
            <label>Reorder Level</label>
            <input
              type="number"
              name="reorderLevel"
              value={formData.reorderLevel}
              onChange={handleChange}
              min="0"
              step="0.01"
            />
          </div>
        </div>

        <div className="form-row">
          <div className="form-group">
            <label>Cost per Unit</label>
            <input
              type="number"
              name="costPerUnit"
              value={formData.costPerUnit}
              onChange={handleChange}
              min="0"
              step="0.01"
            />
          </div>

          <div className="form-group">
            <label>Expiry Date</label>
            <input
              type="date"
              name="expiryDate"
              value={formData.expiryDate}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Saving...' : 'Save Ingredient'}
          </button>
          <button
            type="button"
            onClick={() => navigate('/ingredients')}
            className="btn-secondary"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default IngredientForm;
```

---

## File: `frontend/src/pages/Foods/Foods.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { foodService } from '../../services/foodService';
import './Foods.css';

const Foods = () => {
  const [foods, setFoods] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    fetchFoods();
  }, [page]);

  const fetchFoods = async () => {
    try {
      setLoading(true);
      const response = await foodService.getAll(page, 10);
      setFoods(response.content);
      setTotalPages(response.totalPages);
    } catch (error) {
      console.error('Error fetching foods:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this food item?')) {
      try {
        await foodService.delete(id);
        fetchFoods();
      } catch (error) {
        alert('Error deleting food item');
      }
    }
  };

  return (
    <div className="foods-page">
      <div className="page-header">
        <h1>Food Menu Management</h1>
        <Link to="/foods/new" className="btn-primary">
          + Add Food Item
        </Link>
      </div>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <>
          <div className="foods-grid">
            {foods.map((food) => (
              <div key={food.id} className="food-card">
                <div className="food-image">
                  {food.imageUrl ? (
                    <img src={food.imageUrl} alt={food.name} />
                  ) : (
                    <div className="no-image">🍽️</div>
                  )}
                </div>
                <div className="food-details">
                  <h3>{food.name}</h3>
                  <p className="food-category">{food.category || 'Uncategorized'}</p>
                  <p className="food-description">{food.description}</p>
                  <div className="food-info">
                    <span className="food-price">${food.price.toFixed(2)}</span>
                    <span className={`availability ${food.available ? 'available' : 'unavailable'}`}>
                      {food.available ? '✅ Available' : '❌ Unavailable'}
                    </span>
                  </div>
                  <div className="food-actions">
                    <button
                      onClick={() => navigate(`/foods/${food.id}/edit`)}
                      className="btn-edit"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(food.id)}
                      className="btn-delete"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}
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
    </div>
  );
};

export default Foods;
```

---

## File: `frontend/src/pages/Sales/POS.jsx`

```jsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { foodService } from '../../services/foodService';
import { saleService } from '../../services/saleService';
import './POS.css';

const POS = () => {
  const [availableFoods, setAvailableFoods] = useState([]);
  const [cart, setCart] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchAvailableFoods();
  }, []);

  const fetchAvailableFoods = async () => {
    try {
      const foods = await foodService.getAvailable();
      setAvailableFoods(foods);
    } catch (error) {
      console.error('Error fetching foods:', error);
    }
  };

  const addToCart = (food) => {
    const existingItem = cart.find((item) => item.foodId === food.id);
    
    if (existingItem) {
      setCart(
        cart.map((item) =>
          item.foodId === food.id
            ? { ...item, quantity: item.quantity + 1, subtotal: (item.quantity + 1) * item.unitPrice }
            : item
        )
      );
    } else {
      setCart([
        ...cart,
        {
          foodId: food.id,
          foodName: food.name,
          quantity: 1,
          unitPrice: food.price,
          subtotal: food.price
        }
      ]);
    }
  };

  const removeFromCart = (foodId) => {
    setCart(cart.filter((item) => item.foodId !== foodId));
  };

  const updateQuantity = (foodId, quantity) => {
    if (quantity <= 0) {
      removeFromCart(foodId);
      return;
    }

    setCart(
      cart.map((item) =>
        item.foodId === foodId
          ? { ...item, quantity, subtotal: quantity * item.unitPrice }
          : item
      )
    );
  };

  const getTotalAmount = () => {
    return cart.reduce((sum, item) => sum + item.subtotal, 0);
  };

  const handleCheckout = async () => {
    if (cart.length === 0) {
      alert('Cart is empty');
      return;
    }

    const saleData = {
      items: cart,
      totalAmount: getTotalAmount(),
      paymentMethod: 'CASH'
    };

    setLoading(true);
    try {
      await saleService.create(saleData);
      alert('Sale completed successfully!');
      setCart([]);
      fetchAvailableFoods(); // Refresh available foods
    } catch (error) {
      alert('Error processing sale: ' + (error.message || 'Unknown error'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="pos-page">
      <h1>Point of Sale</h1>

      <div className="pos-container">
        <div className="foods-section">
          <h2>Available Foods</h2>
          <div className="foods-list">
            {availableFoods.map((food) => (
              <div key={food.id} className="food-item" onClick={() => addToCart(food)}>
                <div className="food-name">{food.name}</div>
                <div className="food-price">${food.price.toFixed(2)}</div>
              </div>
            ))}
          </div>
        </div>

        <div className="cart-section">
          <h2>Cart</h2>
          {cart.length === 0 ? (
            <p className="empty-cart">Cart is empty</p>
          ) : (
            <>
              <div className="cart-items">
                {cart.map((item) => (
                  <div key={item.foodId} className="cart-item">
                    <div className="item-info">
                      <strong>{item.foodName}</strong>
                      <div className="item-price">${item.unitPrice.toFixed(2)}</div>
                    </div>
                    <div className="item-controls">
                      <button
                        onClick={() => updateQuantity(item.foodId, item.quantity - 1)}
                        className="qty-btn"
                      >
                        -
                      </button>
                      <span className="quantity">{item.quantity}</span>
                      <button
                        onClick={() => updateQuantity(item.foodId, item.quantity + 1)}
                        className="qty-btn"
                      >
                        +
                      </button>
                      <button
                        onClick={() => removeFromCart(item.foodId)}
                        className="remove-btn"
                      >
                        ✕
                      </button>
                    </div>
                    <div className="item-subtotal">${item.subtotal.toFixed(2)}</div>
                  </div>
                ))}
              </div>

              <div className="cart-total">
                <h3>Total: ${getTotalAmount().toFixed(2)}</h3>
              </div>

              <button
                onClick={handleCheckout}
                className="btn-checkout"
                disabled={loading}
              >
                {loading ? 'Processing...' : 'Complete Sale'}
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default POS;
```

---

**Continue to final parts with CSS styling and remaining components...**
