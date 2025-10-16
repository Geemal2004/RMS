# CODE PART 5E: Complete Frontend CSS Styling

---

## File: `frontend/src/pages/Ingredients/Ingredients.css`

```css
.ingredients-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.page-header h1 {
  margin: 0;
  color: #2c3e50;
}

.search-bar {
  margin-bottom: 20px;
}

.search-bar input {
  width: 100%;
  max-width: 500px;
  padding: 12px 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
}

.table-container {
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  overflow-x: auto;
  margin-bottom: 20px;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.data-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #495057;
  position: sticky;
  top: 0;
}

.data-table tbody tr:hover {
  background: #f8f9fa;
}

.status-badge {
  padding: 5px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
}

.status-badge.in-stock {
  background: #d4edda;
  color: #155724;
}

.status-badge.low-stock {
  background: #fff3cd;
  color: #856404;
}

.status-badge.out-of-stock {
  background: #f8d7da;
  color: #721c24;
}

.actions {
  display: flex;
  gap: 10px;
}

.btn-edit {
  background: #17a2b8;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-edit:hover {
  background: #138496;
}

.btn-delete {
  background: #dc3545;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-delete:hover {
  background: #c82333;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 20px;
}

.pagination button {
  padding: 10px 20px;
}

.pagination span {
  font-weight: 600;
}
```

---

## File: `frontend/src/pages/Ingredients/IngredientForm.css`

```css
.form-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.form-page h1 {
  color: #2c3e50;
  margin-bottom: 30px;
}

.ingredient-form {
  background: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.form-group label {
  margin-bottom: 8px;
  font-weight: 600;
  color: #495057;
}

.form-group input,
.form-group select,
.form-group textarea {
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #007bff;
}

.form-actions {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

.form-actions button {
  padding: 12px 30px;
  font-size: 16px;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}
```

---

## File: `frontend/src/pages/Foods/Foods.css`

```css
.foods-page {
  padding: 20px;
}

.foods-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.food-card {
  background: white;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
}

.food-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
}

.food-image {
  width: 100%;
  height: 200px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.food-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.no-image {
  font-size: 60px;
}

.food-details {
  padding: 20px;
}

.food-details h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.food-category {
  color: #6c757d;
  font-size: 14px;
  margin-bottom: 10px;
}

.food-description {
  color: #495057;
  font-size: 14px;
  margin-bottom: 15px;
  min-height: 40px;
}

.food-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.food-price {
  font-size: 24px;
  font-weight: bold;
  color: #28a745;
}

.availability {
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 14px;
  font-weight: 600;
}

.availability.available {
  background: #d4edda;
  color: #155724;
}

.availability.unavailable {
  background: #f8d7da;
  color: #721c24;
}

.food-actions {
  display: flex;
  gap: 10px;
}

.food-actions button {
  flex: 1;
  padding: 8px;
}
```

---

## File: `frontend/src/pages/Foods/FoodForm.css`

```css
.food-form {
  background: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.recipe-section {
  margin-top: 30px;
  padding-top: 30px;
  border-top: 2px solid #eee;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  margin: 0;
  color: #2c3e50;
}

.btn-add {
  background: #28a745;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-add:hover {
  background: #218838;
}

.recipe-item {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 2fr auto;
  gap: 10px;
  margin-bottom: 15px;
  align-items: center;
}

.recipe-item select,
.recipe-item input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.btn-remove {
  background: #dc3545;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 18px;
  transition: background 0.3s;
}

.btn-remove:hover {
  background: #c82333;
}

@media (max-width: 768px) {
  .recipe-item {
    grid-template-columns: 1fr;
  }
}
```

---

## File: `frontend/src/pages/Sales/POS.css`

```css
.pos-page {
  padding: 20px;
}

.pos-page h1 {
  margin-bottom: 20px;
  color: #2c3e50;
}

.pos-container {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  height: calc(100vh - 150px);
}

.foods-section,
.cart-section {
  background: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.foods-section h2,
.cart-section h2 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #2c3e50;
}

.foods-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 15px;
  max-height: calc(100vh - 250px);
  overflow-y: auto;
}

.food-item {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  border: 2px solid transparent;
}

.food-item:hover {
  background: #e9ecef;
  border-color: #007bff;
  transform: scale(1.05);
}

.food-name {
  font-weight: 600;
  margin-bottom: 10px;
  color: #495057;
}

.food-price {
  font-size: 20px;
  color: #28a745;
  font-weight: bold;
}

.cart-section {
  display: flex;
  flex-direction: column;
}

.empty-cart {
  text-align: center;
  color: #6c757d;
  padding: 40px;
}

.cart-items {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 20px;
}

.cart-item {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
}

.item-info {
  flex: 1;
}

.item-info strong {
  display: block;
  margin-bottom: 5px;
  color: #2c3e50;
}

.item-price {
  color: #6c757d;
  font-size: 14px;
}

.item-controls {
  display: flex;
  align-items: center;
  gap: 10px;
}

.qty-btn {
  background: #007bff;
  color: white;
  border: none;
  width: 30px;
  height: 30px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 18px;
  transition: background 0.3s;
}

.qty-btn:hover {
  background: #0056b3;
}

.quantity {
  font-weight: bold;
  min-width: 30px;
  text-align: center;
}

.remove-btn {
  background: #dc3545;
  color: white;
  border: none;
  width: 30px;
  height: 30px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background 0.3s;
}

.remove-btn:hover {
  background: #c82333;
}

.item-subtotal {
  font-weight: bold;
  color: #28a745;
  min-width: 80px;
  text-align: right;
}

.cart-total {
  border-top: 2px solid #dee2e6;
  padding-top: 15px;
  margin-bottom: 15px;
}

.cart-total h3 {
  margin: 0;
  color: #28a745;
  text-align: right;
}

.btn-checkout {
  width: 100%;
  background: #28a745;
  color: white;
  border: none;
  padding: 15px;
  border-radius: 8px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-checkout:hover {
  background: #218838;
}

.btn-checkout:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

@media (max-width: 992px) {
  .pos-container {
    grid-template-columns: 1fr;
    height: auto;
  }
}
```

---

## File: `frontend/src/pages/Sales/Sales.css`

```css
.sales-page {
  padding: 20px;
}

.sales-page h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.filters {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 20px;
  display: flex;
  gap: 15px;
  align-items: flex-end;
}

.filter-group {
  display: flex;
  flex-direction: column;
}

.filter-group label {
  margin-bottom: 5px;
  font-weight: 600;
  color: #495057;
}

.filter-group input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.total-sales {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
  border-radius: 10px;
  margin-bottom: 20px;
  text-align: center;
}

.total-sales h3 {
  margin: 0;
  font-size: 28px;
}

.btn-view {
  background: #17a2b8;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-view:hover {
  background: #138496;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 10px;
  max-width: 600px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-content h2 {
  margin-top: 0;
  color: #2c3e50;
}

.modal-content h3 {
  margin-top: 20px;
  color: #28a745;
  text-align: right;
}

.items-table {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

.items-table th,
.items-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.items-table th {
  background: #f8f9fa;
  font-weight: 600;
}

@media (max-width: 768px) {
  .filters {
    flex-direction: column;
  }
}
```

---

## File: `frontend/src/pages/Alerts/Alerts.css`

```css
.alerts-page {
  padding: 20px;
}

.alerts-page h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.alert-filters {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.alert-filters button {
  padding: 10px 20px;
  border: 2px solid #ddd;
  background: white;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s;
  font-weight: 600;
}

.alert-filters button:hover {
  border-color: #007bff;
  color: #007bff;
}

.alert-filters button.active {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.no-alerts {
  background: white;
  padding: 60px;
  text-align: center;
  border-radius: 10px;
  font-size: 24px;
  color: #28a745;
}

.alerts-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.alert-card {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
  gap: 20px;
  border-left: 5px solid;
}

.alert-card.alert-warning {
  border-left-color: #ffc107;
}

.alert-card.alert-danger {
  border-left-color: #dc3545;
}

.alert-card.alert-info {
  border-left-color: #17a2b8;
}

.alert-icon {
  font-size: 40px;
}

.alert-content {
  flex: 1;
}

.alert-content h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.alert-message {
  color: #495057;
  margin: 5px 0;
}

.alert-date {
  color: #6c757d;
  font-size: 14px;
  margin: 5px 0 0 0;
}

.btn-acknowledge {
  background: #28a745;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-acknowledge:hover {
  background: #218838;
}
```

---

## File: `frontend/src/pages/Analytics/Analytics.css`

```css
.analytics-page {
  padding: 20px;
}

.analytics-page h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.period-selector {
  background: white;
  padding: 15px 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.period-selector label {
  font-weight: 600;
  color: #495057;
}

.period-selector select {
  padding: 8px 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
}

.charts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.chart-container {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.chart-container h2 {
  margin-top: 0;
  color: #2c3e50;
  font-size: 18px;
  margin-bottom: 20px;
}

.recommendations-section {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.recommendations-section h2 {
  margin-top: 0;
  color: #2c3e50;
  margin-bottom: 20px;
}

.recommendation {
  font-weight: 600;
  color: #dc3545;
}

@media (max-width: 768px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
}
```

---

## File: `frontend/src/pages/Users/Users.css`

```css
.users-page {
  padding: 20px;
}

.users-page h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.role-badge {
  display: inline-block;
  background: #007bff;
  color: white;
  padding: 3px 10px;
  border-radius: 12px;
  font-size: 12px;
  margin-right: 5px;
  font-weight: 600;
}

.status-badge.active {
  background: #d4edda;
  color: #155724;
}

.status-badge.inactive {
  background: #f8d7da;
  color: #721c24;
}

.btn-activate {
  background: #28a745;
  color: white;
  border: none;
  padding: 6px 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-activate:hover {
  background: #218838;
}

.btn-deactivate {
  background: #ffc107;
  color: #212529;
  border: none;
  padding: 6px 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-deactivate:hover {
  background: #e0a800;
}
```

---

**Continue with global CSS files...**
