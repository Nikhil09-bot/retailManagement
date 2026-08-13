# 🏢 Module-by-Module Architecture Details

## Module 1: Project Setup & Configuration ✅

### Purpose
Establish the foundational structure and configuration for the entire application.

### Components Created
```
config/
└── SecurityConfig.java          # Spring Security & CORS setup

exception/
├── GlobalExceptionHandler.java  # Centralized exception handling
├── BusinessException.java       # Custom business exception
└── ResourceNotFoundException.java # 404 handling

dto/
└── ApiResponse.java             # Standard response wrapper

controller/
└── HealthCheckController.java   # /api/health endpoint

util/
└── [Utility classes]            # Common utilities
```

### Key Configuration
```properties
server.port=8080
server.servlet.context-path=/api
spring.datasource.url=jdbc:mysql://localhost:3306/retail_management_system
spring.jpa.hibernate.ddl-auto=update
```

### API Endpoints
```
GET /api/health  → Application health check
```

---

## Module 2: Product Management ✅

### Purpose
Manage product catalog with CRUD operations and categorization.

### Architecture
```
product/
├── entity/
│   └── Product.java             # Product model
│       ├── id (PK)
│       ├── name
│       ├── category
│       ├── price (BigDecimal)
│       ├── currentStock
│       ├── reorderLevel
│       └── createdAt
│
├── repository/
│   └── ProductRepository.java   # Data access
│       ├── findAll()
│       ├── findById(Long)
│       ├── findByCategory(String)
│       └── findByNameContaining(String)
│
├── service/
│   ├── ProductService.java      # Interface
│   └── ProductServiceImpl.java   # Implementation
│       ├── create()
│       ├── update()
│       ├── delete()
│       ├── findById()
│       ├── findAll()
│       ├── search()
│       └── validation logic
│
├── dto/
│   ├── ProductRequest.java      # Create/Update DTO
│   └── ProductResponse.java     # Response DTO
│
├── mapper/
│   └── ProductMapper.java       # Entity ↔ DTO conversion
│
├── exception/
│   ├── ProductNotFoundException.java
│   └── InvalidProductException.java
│
└── enums/
    └── ProductStatus.java       # ACTIVE, INACTIVE, DISCONTINUED
```

### API Endpoints
```
POST   /api/products                 → Create product
GET    /api/products                 → Get all
GET    /api/products/{id}            → Get by ID
PUT    /api/products/{id}            → Update
DELETE /api/products/{id}            → Delete
GET    /api/products/category/{cat}  → Filter by category
GET    /api/products/search          → Search by name
```

### Database Table
```sql
CREATE TABLE products (
  product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(100),
  price DECIMAL(10,2) NOT NULL,
  current_stock INT DEFAULT 0,
  reorder_level INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_category (category),
  INDEX idx_name (name)
);
```

---

## Module 3: Inventory Management ✅

### Purpose
Track inventory levels, history, and manage adjustments with complete audit trail.

### Architecture
```
inventory/
├── entity/
│   └── InventoryHistory.java    # Audit trail model
│       ├── id (PK)
│       ├── product (FK to Product)
│       ├── qtyChange
│       ├── type (enum)
│       ├── referenceId
│       └── timestamp
│
├── enums/
│   └── InventoryTransactionType.java
│       ├── STOCK_IN (Purchase received)
│       ├── STOCK_OUT (Sale completed)
│       ├── ADJUSTMENT (Manual correction)
│       ├── DAMAGE
│       ├── LOSS
│       ├── RETURN
│       ├── TRANSFER
│       └── INITIAL_SETUP
│
├── repository/
│   └── InventoryHistoryRepository.java
│       ├── findByProductId(Long)
│       ├── findByType(TransactionType)
│       ├── findByDateRange(LocalDateTime, LocalDateTime)
│       ├── findByReferenceId(String)
│       └── countLowStock()
│
├── service/
│   ├── InventoryService.java
│   └── InventoryServiceImpl.java
│       ├── getInventoryStatus()      # All products stock levels
│       ├── getLowStockProducts()     # Below reorder level
│       ├── getOutOfStockProducts()   # Zero stock
│       ├── adjustStock()             # Manual adjustment
│       ├── deductStock()             # On sale
│       ├── addStock()                # On PO received
│       ├── getHistory()              # Audit trail
│       ├── findTransactionsByType()
│       ├── findByDateRange()
│       └── getLowStockCount()
│
├── dto/
│   ├── InventoryAdjustmentRequest.java
│   ├── InventoryHistoryResponse.java
│   └── InventoryStatusResponse.java
│
├── mapper/
│   └── InventoryMapper.java
│
└── exception/
    ├── InsufficientStockException.java
    ├── InventoryNotFoundException.java
    └── InvalidAdjustmentException.java
```

### API Endpoints
```
GET    /api/inventory/status                           → All inventory status
GET    /api/inventory/low-stock                        → Products below reorder
GET    /api/inventory/out-of-stock                     → Zero stock products
GET    /api/inventory/low-stock/count                  → Count low stock
GET    /api/inventory/out-of-stock/count               → Count out of stock
POST   /api/inventory/adjust                           → Manual adjustment
GET    /api/inventory/history/{productId}              → Product history
GET    /api/inventory/transactions?type=STOCK_IN       → By type
GET    /api/inventory/history-range?start=&end=        → Date range
GET    /api/inventory/transactions-by-reference?ref=   → By reference ID
```

### Database Table
```sql
CREATE TABLE inventory_history (
  history_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  product_id BIGINT NOT NULL,
  qty_change INT NOT NULL,
  type VARCHAR(50) NOT NULL,
  reference_id VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (product_id) REFERENCES products(product_id),
  INDEX idx_product (product_id),
  INDEX idx_type (type),
  INDEX idx_date (created_at),
  INDEX idx_reference (reference_id)
);
```

### Key Business Logic
- **Deduct Stock**: Called when Sale is completed
- **Add Stock**: Called when Purchase Order is received
- **Auto Audit Trail**: Every change logged with type & reference
- **Low Stock Detection**: Compare current_stock with reorder_level

---

## Module 4: Supplier Management ✅

### Purpose
Manage supplier information with CRUD operations and advanced filtering.

### Architecture
```
supplier/
├── entity/
│   └── Supplier.java            # Supplier model
│       ├── id (PK)
│       ├── name
│       ├── contactPerson
│       ├── phone
│       ├── email
│       ├── city
│       ├── address
│       ├── paymentTerms
│       ├── taxId
│       ├── active (soft delete)
│       └── createdAt
│
├── repository/
│   └── SupplierRepository.java
│       ├── findAllActive()          # Where active = true
│       ├── findByNameContaining()   # Search
│       ├── findByCity()             # Filter by city
│       ├── findByEmail()
│       └── countActive()
│
├── service/
│   ├── SupplierService.java
│   └── SupplierServiceImpl.java
│       ├── create()                 # Validation
│       ├── update()
│       ├── delete()                 # Soft delete (set active=false)
│       ├── findById()
│       ├── findAll()                # Only active
│       ├── search()
│       ├── filterByCity()
│       ├── deactivate()
│       └── reactivate()
│
├── dto/
│   ├── SupplierRequest.java
│   ├── SupplierResponse.java
│   └── SupplierSearchResponse.java
│
├── mapper/
│   └── SupplierMapper.java
│
└── exception/
    ├── SupplierNotFoundException.java
    ├── SupplierAlreadyExistsException.java
    ├── DuplicateEmailException.java
    └── InvalidSupplierException.java
```

### API Endpoints
```
POST   /api/suppliers                    → Create supplier
GET    /api/suppliers                    → Get all active
GET    /api/suppliers/{id}               → Get by ID
PUT    /api/suppliers/{id}               → Update
DELETE /api/suppliers/{id}               → Soft delete
GET    /api/suppliers/active             → Only active
GET    /api/suppliers/search?keyword=    → Search name/email
GET    /api/suppliers/city/{city}        → Filter by city
PATCH  /api/suppliers/{id}/deactivate    → Deactivate
```

### Database Table
```sql
CREATE TABLE suppliers (
  supplier_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  contact_person VARCHAR(255),
  phone VARCHAR(20),
  email VARCHAR(100) UNIQUE,
  city VARCHAR(100),
  address VARCHAR(255),
  payment_terms VARCHAR(100),
  tax_id VARCHAR(50),
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_active (active),
  INDEX idx_city (city),
  INDEX idx_email (email)
);
```

### Key Business Logic
- **Soft Delete**: `active = false` (data preserved)
- **Search**: Case-insensitive on name/email
- **Filter**: Only return active suppliers by default

---

## Module 5: Purchase Order Management ⏳ (60% - Entities & Repos Done)

### Purpose
Manage purchase orders from suppliers with automatic inventory updates on receipt.

### Architecture
```
purchaseorder/
├── entity/
│   ├── PurchaseOrder.java       # Main PO
│   │   ├── id (PK)
│   │   ├── poNumber (unique)
│   │   ├── supplier (FK)
│   │   ├── poDate
│   │   ├── expectedDeliveryDate
│   │   ├── totalAmount (BigDecimal)
│   │   ├── status (enum)
│   │   ├── notes
│   │   └── items (OneToMany)
│   │
│   └── PurchaseOrderItem.java   # Line items
│       ├── id (PK)
│       ├── purchaseOrder (FK)
│       ├── product (FK)
│       ├── quantity
│       ├── unitPrice (BigDecimal)
│       └── subtotal
│
├── enums/
│   └── PurchaseOrderStatus.java
│       ├── PENDING
│       ├── RECEIVED (All items received)
│       ├── PARTIALLY_RECEIVED
│       ├── CANCELLED
│       └── ON_HOLD
│
├── repository/
│   ├── PurchaseOrderRepository.java      (✅ DONE)
│   │   ├── findByStatus()
│   │   ├── findBySupplier()
│   │   ├── findByDateRange()
│   │   └── findPendingOrders()
│   │
│   └── PurchaseOrderItemRepository.java  (✅ DONE)
│       ├── findByPurchaseOrder()
│       └── findByProduct()
│
├── service/                            (❌ NEEDED)
│   ├── PurchaseOrderService.java
│   └── PurchaseOrderServiceImpl.java
│       ├── createPurchaseOrder()
│       ├── receivePurchaseOrder()     ◄─── Key: Auto-update inventory
│       ├── partiallyReceive()
│       ├── cancelPurchaseOrder()
│       ├── findById()
│       ├── findAll()
│       ├── findByStatus()
│       ├── findByDateRange()
│       └── updatePurchaseOrder()
│
├── dto/                                (✅ DONE)
│   ├── CreatePurchaseOrderRequest.java
│   ├── PurchaseOrderItemRequest.java
│   ├── PurchaseOrderResponse.java
│   └── PurchaseOrderItemResponse.java
│
├── mapper/                             (❌ NEEDED)
│   └── PurchaseOrderMapper.java
│
├── controller/                         (❌ NEEDED)
│   └── PurchaseOrderController.java
│       ├── POST create
│       ├── GET all
│       ├── GET {id}
│       ├── PATCH {id}/receive
│       ├── GET /status/{status}
│       └── DELETE {id}
│
└── exception/                          (❌ NEEDED)
    ├── PurchaseOrderNotFoundException.java
    ├── InvalidPurchaseOrderException.java
    └── CannotReceivePurchaseOrder.java
```

### Key Business Logic (Service)
```
receivePurchaseOrder(poId) {
    1. Get PurchaseOrder
    2. Validate status (must be PENDING or PARTIALLY_RECEIVED)
    3. For each PurchaseOrderItem:
       - inventoryService.addStock(productId, quantity, "PO_" + poId)
       - inventoryHistoryRepository.save(record)
       - Update item status
    4. Set PurchaseOrder status to RECEIVED
    5. Save all changes (one transaction)
    6. Return updated PO
}
```

### API Endpoints (To Build)
```
POST   /api/purchase-orders                    → Create
GET    /api/purchase-orders                    → Get all
GET    /api/purchase-orders/{id}               → Get by ID
PUT    /api/purchase-orders/{id}               → Update
PATCH  /api/purchase-orders/{id}/receive       → Mark as received
GET    /api/purchase-orders?status=PENDING     → Filter by status
GET    /api/purchase-orders?supplier=123       → Filter by supplier
DELETE /api/purchase-orders/{id}               → Cancel
```

---

## Module 6: Sales/Point of Sale ⏳ (50% - Entities & Repos Done)

### Purpose
Manage sales transactions with automatic inventory deduction, tax calculation, and receipt generation.

### Architecture
```
sales/
├── entity/
│   ├── Sale.java                # Main sale
│   │   ├── id (PK)
│   │   ├── receiptNumber (unique)
│   │   ├── saleDate
│   │   ├── customer (FK, nullable)
│   │   ├── totalAmount (BigDecimal)
│   │   ├── taxAmount (BigDecimal)
│   │   ├── discount (BigDecimal)
│   │   ├── paymentMethod (enum)
│   │   ├── status (enum)
│   │   └── items (OneToMany)
│   │
│   └── SaleItem.java            # Line items
│       ├── id (PK)
│       ├── sale (FK)
│       ├── product (FK)
│       ├── quantity
│       ├── unitPrice (BigDecimal)
│       └── lineTotal (BigDecimal)
│
├── enums/
│   ├── SaleStatus.java
│   │   ├── COMPLETED
│   │   ├── CANCELLED
│   │   ├── REFUNDED
│   │   └── PENDING
│   │
│   └── PaymentMethod.java
│       ├── CASH
│       ├── CARD
│       ├── CHECK
│       └── MOBILE_PAYMENT
│
├── repository/
│   ├── SaleRepository.java              (✅ DONE)
│   │   ├── findTodaySales()
│   │   ├── findByDateRange()
│   │   ├── findByReceiptNumber()
│   │   ├── findByCustomerId()
│   │   └── calculateDailySales()
│   │
│   └── SaleItemRepository.java          (✅ DONE)
│       ├── findBySale()
│       └── findByProduct()
│
├── service/                            (❌ NEEDED)
│   ├── SalesService.java
│   └── SalesServiceImpl.java
│       ├── createSale()                ◄─── Key: Auto-deduct inventory
│       ├── cancelSale()                ◄─── Key: Restore inventory
│       ├── findById()
│       ├── findTodaySales()
│       ├── findByDateRange()
│       ├── findByReceiptNumber()
│       ├── calculateTotalWithTax()
│       ├── validateStock()
│       └── generateReceiptNumber()
│
├── dto/                                (❌ NEEDED)
│   ├── CreateSaleRequest.java
│   ├── SaleItemRequest.java
│   ├── SaleResponse.java
│   ├── SaleItemResponse.java
│   └── ReceiptResponse.java
│
├── mapper/                             (❌ NEEDED)
│   └── SaleMapper.java
│
├── controller/                         (❌ NEEDED)
│   └── SalesController.java
│       ├── POST create
│       ├── GET all
│       ├── GET {id}
│       ├── GET /today
│       ├── GET /date-range
│       ├── GET /receipt/{number}
│       ├── PATCH {id}/cancel
│       └── GET /report
│
└── exception/                          (❌ NEEDED)
    ├── SaleNotFoundException.java
    ├── InsufficientStockForSale.java
    ├── InvalidSaleException.java
    └── CannotCancelSale.java
```

### Key Business Logic (Service)
```
createSale(saleRequest) {
    1. Validate all items exist
    2. Validate sufficient stock for all items
    3. Calculate tax (amount * TAX_RATE)
    4. Calculate total (subtotal + tax - discount)
    5. Generate receipt number (unique)
    6. Save Sale record
    7. For each SaleItem:
       - Save SaleItem
       - inventoryService.deductStock(productId, quantity, "SALE_" + receiptNum)
       - inventoryHistoryRepository.save(record)
    8. Transaction commits (all or nothing)
    9. Return SaleResponse with receipt
}

cancelSale(saleId) {
    1. Get Sale (must be COMPLETED)
    2. For each SaleItem:
       - inventoryService.addStock(productId, quantity, "CANCEL_" + receiptNum)
       - inventoryHistoryRepository.save(record)
    3. Set Sale status to CANCELLED
    4. Save changes (one transaction)
    5. Return updated Sale
}
```

### API Endpoints (To Build)
```
POST   /api/sales                        → Create sale (with items)
GET    /api/sales                        → Get all
GET    /api/sales/{id}                   → Get by ID
PATCH  /api/sales/{id}/cancel            → Cancel sale (restore inventory)
GET    /api/sales/today                  → Today's sales
GET    /api/sales/date-range?start=&end= → Date range sales
GET    /api/sales/receipt/{number}       → Find by receipt number
GET    /api/sales/report                 → Daily/monthly report
```

---

## Module 7: Customer Management ⏳ (40% - Entity & Repo Done)

### Purpose
Manage customer information with loyalty tracking and purchase history.

### Architecture
```
customer/
├── entity/
│   └── Customer.java            # Customer model
│       ├── id (PK)
│       ├── name
│       ├── phone
│       ├── email
│       ├── address
│       ├── city
│       ├── loyaltyPoints (int)
│       ├── totalPurchaseAmount (BigDecimal)
│       ├── createdAt
│       └── lastPurchaseDate
│
├── repository/
│   └── CustomerRepository.java          (✅ DONE)
│       ├── findByPhone()
│       ├── findByEmail()
│       ├── findByNameContaining()
│       ├── findByCity()
│       └── findByLoyaltyPointsGreaterThan()
│
├── service/                            (❌ NEEDED)
│   ├── CustomerService.java
│   └── CustomerServiceImpl.java
│       ├── createCustomer()
│       ├── updateCustomer()
│       ├── deleteCustomer()
│       ├── findById()
│       ├── findAll()
│       ├── searchByName()
│       ├── filterByCity()
│       ├── addLoyaltyPoints()
│       ├── findTopCustomers()
│       └── validateEmail()
│
├── dto/                                (❌ NEEDED)
│   ├── CustomerRequest.java
│   ├── CustomerResponse.java
│   └── CustomerSummary.java
│
├── mapper/                             (❌ NEEDED)
│   └── CustomerMapper.java
│
├── controller/                         (❌ NEEDED)
│   └── CustomerController.java
│       ├── POST create
│       ├── GET all
│       ├── GET {id}
│       ├── PUT {id}
│       ├── DELETE {id}
│       ├── GET /search?keyword=
│       ├── GET /city/{city}
│       └── GET /loyalty-points
│
└── exception/                          (❌ NEEDED)
    ├── CustomerNotFoundException.java
    ├── DuplicateEmailException.java
    ├── InvalidCustomerException.java
    └── EmailAlreadyExistsException.java
```

### Database Table
```sql
CREATE TABLE customers (
  customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(100) UNIQUE,
  address VARCHAR(255),
  city VARCHAR(100),
  loyalty_points INT DEFAULT 0,
  total_purchase_amount DECIMAL(12,2) DEFAULT 0,
  last_purchase_date DATETIME,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_email (email),
  INDEX idx_phone (phone),
  INDEX idx_name (name)
);
```

### API Endpoints (To Build)
```
POST   /api/customers                   → Create
GET    /api/customers                   → Get all
GET    /api/customers/{id}              → Get by ID
PUT    /api/customers/{id}              → Update
DELETE /api/customers/{id}              → Delete
GET    /api/customers/search?keyword=   → Search by name/email
GET    /api/customers/city/{city}       → Filter by city
PATCH  /api/customers/{id}/loyalty      → Add loyalty points
GET    /api/customers/top-customers     → Top spenders
```

---

## Module 8: Dashboard APIs ⏳ (0% - Planned)

### Purpose
Provide aggregate statistics and KPIs for business intelligence.

### Architecture
```
dashboard/
├── service/
│   ├── DashboardService.java
│   └── DashboardServiceImpl.java
│       ├── getTotalProducts()
│       ├── getActiveProducts()
│       ├── getLowStockCount()
│       ├── getOutOfStockCount()
│       ├── getTotalSuppliers()
│       ├── getActiveSuppliers()
│       ├── getTotalCustomers()
│       ├── getTodaySales()
│       ├── getMonthlySales()
│       ├── getMonthlyRevenue()
│       ├── getTopSellingProducts()
│       ├── getSalesGrowth()
│       └── getDashboardSummary()
│
├── dto/
│   ├── DashboardSummary.java
│   ├── SalesSummary.java
│   ├── InventorySummary.java
│   ├── TopSellingProduct.java
│   └── KPIMetric.java
│
└── controller/
    └── DashboardController.java
        ├── GET /api/dashboard/summary
        ├── GET /api/dashboard/sales-today
        ├── GET /api/dashboard/revenue-month
        ├── GET /api/dashboard/inventory-status
        ├── GET /api/dashboard/top-products
        ├── GET /api/dashboard/supplier-stats
        ├── GET /api/dashboard/customer-stats
        └── GET /api/dashboard/growth-metrics
```

### Database Queries (No new tables needed)
```sql
-- Total products
SELECT COUNT(*) FROM products;

-- Low stock count
SELECT COUNT(*) FROM products WHERE current_stock <= reorder_level;

-- Today's sales
SELECT SUM(total_amount) FROM sales WHERE DATE(sale_date) = TODAY();

-- Monthly revenue
SELECT SUM(total_amount) FROM sales WHERE MONTH(sale_date) = MONTH(NOW());

-- Top selling products
SELECT product_id, SUM(quantity) as total_qty
FROM sale_items
WHERE DATE(created_at) >= DATE_SUB(NOW(), INTERVAL 30 DAY)
GROUP BY product_id
ORDER BY total_qty DESC
LIMIT 10;
```

---

## Module 9: Reports APIs ⏳ (0% - Planned)

### Purpose
Generate detailed business reports and analytics.

### Architecture
```
reports/
├── service/
│   ├── ReportService.java
│   └── ReportServiceImpl.java
│       ├── getDailySalesReport()
│       ├── getWeeklySalesReport()
│       ├── getMonthlySalesReport()
│       ├── getInventoryReport()
│       ├── getPurchaseReport()
│       ├── getSupplierReport()
│       ├── getBestSellingProducts()
│       ├── getRevenueByCategory()
│       ├── getRevenueByPaymentMethod()
│       └── getCustomerReport()
│
├── dto/
│   ├── SalesReport.java
│   ├── InventoryReport.java
│   ├── PurchaseReport.java
│   ├── ProductAnalysis.java
│   ├── RevenueBreakdown.java
│   └── ExportFormat.java (PDF, CSV, Excel)
│
└── controller/
    └── ReportController.java
        ├── GET /api/reports/sales-daily
        ├── GET /api/reports/sales-weekly
        ├── GET /api/reports/sales-monthly
        ├── GET /api/reports/inventory
        ├── GET /api/reports/purchases
        ├── GET /api/reports/suppliers
        ├── GET /api/reports/best-sellers
        ├── GET /api/reports/revenue-analysis
        └── GET /api/reports/export?format=PDF
```

### Report Types
- **Sales Reports**: Daily, weekly, monthly breakdowns
- **Inventory Reports**: Current status, movements, adjustments
- **Purchase Reports**: Orders received, pending, by supplier
- **Analysis**: Top products, revenue trends, customer segments
- **Export Formats**: JSON (API), CSV (spreadsheet), PDF (document)

---

## Cross-Module Interactions

### Data Flow Between Modules

```
PRODUCT MASTER
      ↓
      ├─→ INVENTORY (track stock per product)
      ├─→ PURCHASE ORDERS (order from suppliers)
      └─→ SALES (sell to customers)
           ├─→ Auto-deduct inventory
           └─→ Record in sales module

SUPPLIER MASTER
      ↓
      └─→ PURCHASE ORDERS (create POs from suppliers)
           └─→ When received → Auto-add to inventory

CUSTOMER MASTER
      ↓
      └─→ SALES (link customer to sale)
           └─→ Update loyalty points

INVENTORY
      ↓
      ├─→ DASHBOARD (low stock alerts)
      └─→ REPORTS (inventory analysis)

SALES
      ├─→ DASHBOARD (daily/monthly sales)
      ├─→ CUSTOMER (update loyalty & purchase total)
      └─→ REPORTS (sales analysis)
```

---

## Database Schema Overview

```
Products (Master)
    ↓ 1-Many
    ├─→ SaleItems
    ├─→ PurchaseOrderItems
    └─→ InventoryHistory

Suppliers (Master)
    ↓ 1-Many
    └─→ PurchaseOrders

PurchaseOrders (Master)
    ├─ Supplier (Many-1)
    ├─ Status
    └─ 1-Many
       └─→ PurchaseOrderItems → Products

Sales (Master)
    ├─ Customer (Many-1)
    ├─ Receipt Number (Unique)
    ├─ Status
    └─ 1-Many
       └─→ SaleItems → Products

Customers (Master)
    ├─ Email (Unique)
    ├─ Loyalty Points
    └─ 1-Many (reverse)
       ← Sales

InventoryHistory (Audit)
    ├─ Product (Many-1)
    ├─ Transaction Type
    ├─ Reference ID (to PO/Sale)
    └─ Timestamp
```

---

## Completion Roadmap

### ✅ COMPLETE
- Module 1: Setup
- Module 2: Products
- Module 3: Inventory
- Module 4: Suppliers

### 🟡 IN PROGRESS
- Module 5: Purchase Orders (need service/controller) - 2 hours
- Module 6: Sales/POS (need service/controller) - 3 hours
- Module 7: Customers (need service/controller) - 2 hours

### 🟠 PLANNED
- Module 8: Dashboard - 2 hours
- Module 9: Reports - 2 hours

**Estimated Remaining**: 11 hours

---

**Architecture Documents**:
- ARCHITECTURE.md - Main architecture overview
- ARCHITECTURE_DIAGRAMS.md - Visual diagrams
- ARCHITECTURE_QUICK_REFERENCE.md - Quick lookup
- MODULE_ARCHITECTURE.md - This file (module details)

---

Last Updated: July 13, 2026  
Status: 71% Complete - Foundations Solid

