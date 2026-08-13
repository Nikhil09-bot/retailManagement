# Module 2: Product Management - Quick Reference Guide

## 🚀 Quick Start (5 minutes)

### 1. Setup Database
```sql
-- Run schema
mysql -u root -p retail_management_system < MODULE_2_SQL_SCHEMA.sql

-- Load sample data (optional)
mysql -u root -p retail_management_system < MODULE_2_SAMPLE_DATA.sql
```

### 2. Start Application
```bash
cd C:\Users\user\OneDrive\Desktop\RetailBus\RetailBussiness
mvn clean install
mvn spring-boot:run
```

### 3. Test Health Check
```bash
curl http://localhost:8080/api/health
```

### 4. Test Product API
```bash
# Create a product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productCode": "TEST001",
    "name": "Test Product",
    "brand": "TestBrand",
    "category": "BEVERAGES",
    "purchasePrice": 1.00,
    "sellingPrice": 2.00,
    "quantityInStock": 100,
    "reorderLevel": 20,
    "unit": "BOTTLE"
  }'

# Get all products
curl http://localhost:8080/api/products

# Search products
curl http://localhost:8080/api/products/search?keyword=Test
```

---

## 📚 API Endpoints Reference

```
BASE URL: http://localhost:8080/api

CREATE       POST   /products                              201
READ ALL     GET    /products                              200
READ ONE     GET    /products/{id}                         200
UPDATE       PUT    /products/{id}                         200
DELETE       DELETE /products/{id}                         200
SEARCH       GET    /products/search?keyword=              200
BY CATEGORY  GET    /products/category/{category}          200
ACTIVE ONLY  GET    /products/active                       200
DISABLE      PATCH  /products/{id}/disable                 200
ADD STOCK    PATCH  /products/{id}/stock/add?quantity=     200
REMOVE STOCK PATCH  /products/{id}/stock/remove?quantity=  200
```

---

## 🗂️ Directory Structure

```
src/main/java/com/retailbus/product/
│
├── 📄 controller/
│   └── ProductController.java              # REST endpoints
│
├── 🔧 service/
│   ├── ProductService.java                 # Interface
│   └── impl/ProductServiceImpl.java         # Implementation
│
├── 💾 repository/
│   └── ProductRepository.java              # Database access
│
├── 📦 entity/
│   └── Product.java                        # JPA entity
│
├── 🗺️ mapper/
│   └── ProductMapper.java                  # DTO conversions
│
├── 📨 dto/
│   ├── request/ProductRequest.java         # Create/Update DTO
│   └── response/ProductResponse.java       # Response DTO
│
├── 📋 enums/
│   ├── Category.java                       # Product categories
│   └── Unit.java                           # Units of measurement
│
└── ⚠️ exception/
    ├── ProductNotFoundException.java
    ├── DuplicateProductException.java
    ├── InvalidStockOperationException.java
    └── BusinessValidationException.java
```

---

## 🔑 Key Classes

### Product Entity
```java
@Entity
@Table(name = "products")
public class Product {
    private Long id;                    // Primary key
    private String productCode;         // Unique code
    private String name;
    private String brand;
    private Category category;          // ENUM
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Integer quantityInStock;
    private Integer reorderLevel;
    private Unit unit;                  // ENUM
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### ProductService Interface
```java
ProductResponse createProduct(ProductRequest request);
ProductResponse updateProduct(Long id, ProductRequest request);
List<ProductResponse> getAllProducts();
ProductResponse getProductById(Long id);
List<ProductResponse> searchProducts(String keyword);
List<ProductResponse> getProductsByCategory(Category category);
List<ProductResponse> getActiveProducts();
ProductResponse disableProduct(Long id);
void deleteProduct(Long id);
ProductResponse increaseStock(Long id, Integer quantity);
ProductResponse decreaseStock(Long id, Integer quantity);
```

### ProductController
```java
@RestController
@RequestMapping("/products")
public class ProductController {
    // 11 endpoints mapping to ProductService methods
}
```

---

## 📊 Categories (Use These Enums)

```java
Category.BEVERAGES        // Drinks, juice, etc.
Category.SNACKS          // Chips, candy, etc.
Category.DAIRY           // Milk, cheese, etc.
Category.GROCERY         // Bread, pasta, etc.
Category.HOUSEHOLD       // Cleaners, paper, etc.
Category.TOBACCO         // Cigarettes, etc.
Category.PERSONAL_CARE   // Soap, shampoo, etc.
Category.FROZEN          // Ice cream, frozen food
Category.OTHER           // Miscellaneous
```

## 📦 Units (Use These Enums)

```java
Unit.PIECE      // Individual items
Unit.PACK       // Packages
Unit.BOX        // Boxes
Unit.BOTTLE     // Bottles
Unit.CAN        // Cans
Unit.LITER      // Liters
Unit.KG         // Kilograms
Unit.GRAM       // Grams
```

---

## ✅ Sample Request/Response

### Create Product Request
```json
{
  "productCode": "COCA001",
  "barcode": "049000050127",
  "name": "Coca Cola Classic 12oz",
  "description": "Classic cola beverage",
  "brand": "Coca Cola",
  "category": "BEVERAGES",
  "purchasePrice": 1.50,
  "sellingPrice": 2.99,
  "quantityInStock": 150,
  "reorderLevel": 50,
  "unit": "CAN"
}
```

### Success Response
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "productCode": "COCA001",
    "name": "Coca Cola Classic 12oz",
    "category": "BEVERAGES",
    "purchasePrice": "1.50",
    "sellingPrice": "2.99",
    "quantityInStock": 150,
    "active": true,
    "createdAt": "2026-07-12T10:30:00",
    "updatedAt": "2026-07-12T10:30:00"
  },
  "timestamp": 1689160200000
}
```

### Error Response
```json
{
  "success": false,
  "message": "Product code already exists: COCA001",
  "data": null,
  "timestamp": 1689160200000
}
```

---

## 🚨 Error Codes

| HTTP | Error | Meaning |
|------|-------|---------|
| 201 | Created | Product created successfully |
| 200 | OK | Operation successful |
| 400 | Bad Request | Validation error or business rule violation |
| 404 | Not Found | Product not found |
| 409 | Conflict | Duplicate product code |
| 500 | Server Error | Unexpected error |

---

## 🧪 Run Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProductServiceImplTest

# Run with coverage
mvn test jacoco:report
```

---

## 📖 Import Postman Collection

1. Open Postman
2. Click **Import**
3. Select **MODULE_2_POSTMAN_COLLECTION.json**
4. All 11 endpoints will be pre-configured
5. Start testing!

---

## 💡 Common Operations

### Create Product
```bash
POST /products
Content-Type: application/json
{product request JSON}
```

### Search for Product
```bash
GET /products/search?keyword=Coca
```

### Get Products by Category
```bash
GET /products/category/BEVERAGES
```

### Increase Stock
```bash
PATCH /products/1/stock/add?quantity=50
```

### Disable Product
```bash
PATCH /products/1/disable
```

---

## 🔍 Debugging Tips

### Enable SQL Logging
Edit `application.properties`:
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### View API Docs
Visit: `http://localhost:8080/api/swagger-ui.html`

### Check Database
```sql
mysql -u root -p retail_management_system
SELECT * FROM products;
SELECT COUNT(*) FROM products;
SELECT * FROM products WHERE active = 1;
```

---

## 📝 Validation Rules

| Field | Rule |
|-------|------|
| productCode | Required, unique, max 50 chars |
| name | Required, 3-200 chars |
| brand | Required, max 100 chars |
| category | Required enum |
| purchasePrice | Required, >= 0 |
| sellingPrice | Required, > purchasePrice |
| quantityInStock | Required, >= 0 |
| reorderLevel | Required, >= 0 |
| unit | Required enum |

---

## 🎯 What Each Component Does

| Component | Responsibility |
|-----------|-----------------|
| **Entity** | Database model with constraints |
| **DTO** | API request/response structure |
| **Mapper** | Convert between Entity and DTO |
| **Repository** | Database queries |
| **Service** | Business logic and validation |
| **Controller** | REST endpoints and routing |
| **Exception** | Error handling and messages |

---

## 📊 Database Schema

**Table: products**

```sql
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    barcode VARCHAR(100),
    name VARCHAR(200) NOT NULL,
    description TEXT,
    brand VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    purchase_price DECIMAL(10,2) NOT NULL,
    selling_price DECIMAL(10,2) NOT NULL,
    quantity_in_stock INT NOT NULL,
    reorder_level INT NOT NULL,
    unit VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP AUTO DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP AUTO DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_product_code (product_code),
    INDEX idx_category (category),
    INDEX idx_active (active)
);
```

---

## 🔗 Related Documentation

| File | Purpose |
|------|---------|
| `MODULE_2_API_DOCUMENTATION.md` | Full API reference with examples |
| `MODULE_2_SQL_SCHEMA.sql` | Database schema |
| `MODULE_2_SAMPLE_DATA.sql` | Sample products for testing |
| `MODULE_2_POSTMAN_COLLECTION.json` | Postman collection |
| `MODULE_2_IMPLEMENTATION_SUMMARY.md` | Technical details |
| `MODULE_2_COMPLETION_CHECKLIST.md` | Complete feature list |

---

## 🚀 Next Steps

1. ✅ Module 2 is **COMPLETE** and production-ready
2. ⏳ Module 3 (Inventory Management) - Ready for instruction
3. 📋 Module 4+ - Will follow same pattern

---

## 📞 Quick Troubleshooting

**Port 8080 in use?**
```bash
netstat -ano | findstr :8080
taskkill /PID <pid> /F
```

**Database connection failed?**
- Check MySQL is running
- Verify credentials in `application.properties`
- Ensure database exists: `CREATE DATABASE retail_management_system;`

**Tests failing?**
- Run: `mvn clean test`
- Check Java version: `java -version` (should be 17+)

**API not responding?**
- Check app started: Look for "Tomcat started on port 8080"
- Verify health: `curl http://localhost:8080/api/health`

---

**Module 2: Product Management**  
✅ Complete & Production Ready  
📅 July 12, 2026  
v2.0.0

