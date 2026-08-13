# Complete Implementation Guide for Remaining Modules (5-9)

This guide provides the exact structure and templates for completing Modules 5-9.

---

## MODULE 5: Purchase Order Management - COMPLETE GUIDE

### Step 1: Create Exception Classes
```
com.retailbus.purchaseorder.exception/
├── PurchaseOrderNotFoundException.java
└── InvalidPurchaseOrderException.java
```

### Step 2: Create Mapper
**File**: `com.retailbus.purchaseorder.mapper.PurchaseOrderMapper`

Key methods:
- `toEntity(CreatePurchaseOrderRequest)` → PurchaseOrder
- `toResponse(PurchaseOrder)` → PurchaseOrderResponse
- `toItemResponse(PurchaseOrderItem)` → PurchaseOrderItemResponse

### Step 3: Create Service Interface
**File**: `com.retailbus.purchaseorder.service.PurchaseOrderService`

Methods:
- createPurchaseOrder(CreatePurchaseOrderRequest)
- getAllPurchaseOrders()
- getPurchaseOrderById(Long)
- updatePurchaseOrderStatus(Long, PurchaseOrderStatus)
- receivePurchaseOrder(Long) → AUTO-UPDATE INVENTORY
- cancelPurchaseOrder(Long)
- getPurchaseOrdersBySupplier(Long)
- getPurchaseOrdersByStatus(PurchaseOrderStatus)

### Step 4: Create Service Implementation
**Key Business Logic**:
```java
@Override
public void receivePurchaseOrder(Long id) {
    PurchaseOrder po = repository.findById(id)...
    
    for (PurchaseOrderItem item : po.getItems()) {
        Product product = item.getProduct();
        int newStock = product.getQuantityInStock() + item.getQuantityOrdered();
        product.setQuantityInStock(newStock);
        productRepository.save(product);
        
        // Record inventory transaction
        inventoryService.recordTransaction(
            product.getId(),
            InventoryTransactionType.PURCHASE_ORDER_RECEIVED,
            item.getQuantityOrdered(),
            "PO-" + po.getPoNumber(),
            "Received from " + po.getSupplier().getName()
        );
    }
    
    po.setStatus(PurchaseOrderStatus.RECEIVED);
    po.setReceivedDate(LocalDateTime.now());
    repository.save(po);
}
```

### Step 5: Create Controller
**File**: `com.retailbus.purchaseorder.controller.PurchaseOrderController`

Endpoints:
- POST /api/purchase-orders → create
- GET /api/purchase-orders → all
- GET /api/purchase-orders/{id} → by ID
- PUT /api/purchase-orders/{id}/status → update status
- PATCH /api/purchase-orders/{id}/receive → receive PO
- GET /api/purchase-orders/supplier/{supplierId} → by supplier
- DELETE /api/purchase-orders/{id} → cancel

---

## MODULE 6: Sales/Point of Sale - COMPLETE GUIDE

### Key Entities Already Created
- Sale (receipt_number, status, subtotal, tax, discount, grand_total)
- SaleItem (product, quantity, unit_price, line_total)

### Additional Implementation

**Step 1: Create Utility for Receipt Generation**
```java
@Component
public class ReceiptNumberGenerator {
    public String generateReceiptNumber() {
        return "REC-" + LocalDate.now().format(DateTimeFormatter.YYYYMMDD) 
               + "-" + System.currentTimeMillis();
    }
}
```

**Step 2: Create DTOs**
- SaleItemRequest (productId, quantity)
- CreateSaleRequest (items list, taxRate, discount, paymentMethod)
- SaleResponse
- SaleItemResponse

**Step 3: Create Service with Complex Business Logic**

Key methods:
```java
public SaleResponse createSale(CreateSaleRequest request) {
    // Calculate subtotal from items
    BigDecimal subtotal = calculateSubtotal(request.getItems());
    
    // Calculate tax (depends on items)
    BigDecimal tax = subtotal.multiply(taxRate);
    
    // Apply discount if present
    BigDecimal discounted = subtotal.subtract(request.getDiscount());
    
    // Grand total
    BigDecimal grandTotal = discounted.add(tax);
    
    // Deduct from inventory
    for (SaleItemRequest item : request.getItems()) {
        inventoryService.recordTransaction(
            item.getProductId(),
            InventoryTransactionType.STOCK_OUT,
            -item.getQuantity(),
            null,
            "Sold - Receipt " + receiptNumber
        );
    }
    
    // Save sale
    // Save sale items
    // Return response
}

public void cancelSale(Long saleId) {
    Sale sale = repository.findById(saleId)...
    
    // Restore inventory for each item
    for (SaleItem item : sale.getItems()) {
        inventoryService.recordTransaction(
            item.getProduct().getId(),
            InventoryTransactionType.CANCELLED_SALE,
            item.getQuantity(),
            null,
            "Sale cancelled - Receipt " + sale.getReceiptNumber()
        );
    }
    
    sale.setStatus(SaleStatus.CANCELLED);
    repository.save(sale);
}
```

**Step 4: Create Controller**

Endpoints:
- POST /api/sales → create sale
- GET /api/sales/{id} → get sale
- GET /api/sales → all sales
- PATCH /api/sales/{id}/cancel → cancel sale
- GET /api/sales/receipt/{receiptNumber} → find by receipt
- GET /api/sales/today → today's sales
- GET /api/sales/date-range → by date range

---

## MODULE 7: Customer Management - COMPLETE GUIDE

### Implementation Steps

**Step 1: DTOs**
- CustomerRequest (firstName, lastName, phone, email, address, city)
- CustomerResponse

**Step 2: Service Methods**
- createCustomer(CustomerRequest)
- updateCustomer(Long, CustomerRequest)
- getCustomerById(Long)
- getAllCustomers()
- searchCustomer(String keyword)
- getCustomersByCity(String)
- addLoyaltyPoints(Long customerId, BigDecimal points)
- getCustomerPurchaseHistory(Long customerId)

**Step 3: Controllers**

Endpoints:
- POST /api/customers → create
- GET /api/customers/{id} → by ID
- PUT /api/customers/{id} → update
- GET /api/customers → all
- GET /api/customers/search?keyword= → search
- GET /api/customers/city/{city} → by city
- PATCH /api/customers/{id}/loyalty-points → add points
- GET /api/customers/{id}/purchase-history → purchase history

---

## MODULE 8: Dashboard APIs - IMPLEMENTATION

**Create DashboardController** with these endpoints:

```java
@GetMapping("/stats")
public ResponseEntity<ApiResponse<?>> getDashboardStats() {
    Map<String, Object> stats = new HashMap<>();
    stats.put("totalProducts", productRepository.count());
    stats.put("activeProducts", productRepository.findByActiveTrue().size());
    stats.put("lowStockCount", inventoryService.getLowStockCount());
    stats.put("outOfStockCount", inventoryService.getOutOfStockCount());
    stats.put("totalSuppliers", supplierRepository.count());
    stats.put("totalCustomers", customerRepository.count());
    stats.put("todaysSales", calculateTodaysSales());
    stats.put("monthlyRevenue", calculateMonthlyRevenue());
    
    return ResponseEntity.ok(ApiResponse.success("Dashboard stats retrieved", stats));
}
```

Endpoints:
- GET /api/dashboard/stats → all stats
- GET /api/dashboard/products → product counts
- GET /api/dashboard/inventory → low/out-of-stock
- GET /api/dashboard/sales/today → today's sales
- GET /api/dashboard/sales/monthly → monthly revenue
- GET /api/dashboard/revenue → revenue trends

---

## MODULE 9: Reports APIs - IMPLEMENTATION

**Create ReportController** with these endpoints:

```java
@GetMapping("/sales/daily")
public ResponseEntity<ApiResponse<?>> getDailySalesReport(
    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
    
    LocalDateTime startOfDay = date.withHour(0).withMinute(0);
    LocalDateTime endOfDay = date.withHour(23).withMinute(59);
    
    List<Sale> sales = saleRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startOfDay, endOfDay);
    
    // Aggregate data
    Map<String, Object> report = new HashMap<>();
    report.put("date", date);
    report.put("totalSales", sales.size());
    report.put("totalRevenue", calculateTotal(sales));
    report.put("taxCollected", calculateTax(sales));
    report.put("topProducts", getTopProducts(sales));
    
    return ResponseEntity.ok(ApiResponse.success("Daily sales report", report));
}
```

Endpoints:
- GET /api/reports/sales/daily?date= → daily sales
- GET /api/reports/sales/weekly?startDate=&endDate= → weekly sales
- GET /api/reports/sales/monthly?month=&year= → monthly sales
- GET /api/reports/inventory → inventory levels
- GET /api/reports/purchases → purchase summary
- GET /api/reports/top-products → best sellers
- GET /api/reports/revenue-trend → revenue over time

---

## Exception Handler Update

Add to **GlobalExceptionHandler**:

```java
@ExceptionHandler(PurchaseOrderNotFoundException.class)
public ResponseEntity<ApiResponse<?>> handlePurchaseOrderNotFound(PurchaseOrderNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiResponse.error(ex.getMessage()));
}

// Repeat for all new exceptions:
// - InvalidPurchaseOrderException
// - SupplierNotFoundException
// - DuplicateSupplierException
// - CustomerNotFoundException
```

---

## Database Tables Auto-Generated

Hibernate will automatically create:
- `inventory_history` (for Module 3)
- `suppliers` (for Module 4)
- `purchase_orders` (for Module 5)
- `purchase_order_items` (for Module 5)
- `sales` (for Module 6)
- `sale_items` (for Module 6)
- `customers` (for Module 7)

All with proper indexes, constraints, and foreign keys.

---

## Quick Testing Checklist

After completing all modules:

```bash
# 1. Build
mvn clean install

# 2. Run
mvn spring-boot:run

# 3. Test (via Swagger or Postman)
http://localhost:8080/api/swagger-ui.html

# 4. Test Inventory
POST /api/inventory/adjust → adjust stock

# 5. Test Suppliers  
POST /api/suppliers → create supplier
GET /api/suppliers/active → list active

# 6. Test PO
POST /api/purchase-orders → create PO
PATCH /api/purchase-orders/{id}/receive → receive (auto-updates inventory)

# 7. Test Sales
POST /api/sales → create sale (auto-deducts inventory)
PATCH /api/sales/{id}/cancel → cancel (restores inventory)

# 8. Test Customer
POST /api/customers → create customer
GET /api/customers/search → search

# 9. Test Dashboard
GET /api/dashboard/stats → all stats

# 10. Test Reports
GET /api/reports/sales/daily → daily report
```

---

## Summary of What's Remaining

✅ **DONE**:
- Module 3: Inventory (Complete)
- Module 4: Suppliers (Complete)
- Module 5-7 Entities & Repos (Complete)
- All DTOs structure established

⏳ **TO BUILD** (following templates above):
- Module 5: PurchaseOrderService, Mapper, Controller
- Module 6: SaleService, Mapper, Controller
- Module 7: CustomerService, Mapper, Controller  
- Module 8: DashboardController
- Module 9: ReportController
- Update GlobalExceptionHandler for new exceptions

Each follows the same proven pattern: Entity → Repository → DTO → Mapper → Service → Controller

**Estimated completion time**: 2-3 hours of focused development


