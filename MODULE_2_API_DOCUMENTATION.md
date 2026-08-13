# Module 2: Product Management - API Documentation

## Overview
Complete REST API for managing products in the retail store system. Supports CRUD operations, search, filtering, and inventory management.

## API Base URL
```
http://localhost:8080/api
```

## Table of Contents
1. [Product Response Format](#product-response-format)
2. [API Endpoints](#api-endpoints)
3. [Request Examples](#request-examples)
4. [Response Examples](#response-examples)
5. [Error Responses](#error-responses)

---

## Product Response Format

### Product Response Object
```json
{
  "id": 1,
  "productCode": "COCA001",
  "barcode": "049000050127",
  "name": "Coca Cola Classic 12oz",
  "description": "Classic cola beverage",
  "brand": "Coca Cola",
  "category": "BEVERAGES",
  "purchasePrice": "1.50",
  "sellingPrice": "2.99",
  "quantityInStock": 150,
  "reorderLevel": 50,
  "unit": "CAN",
  "active": true,
  "createdAt": "2026-07-12T10:30:00",
  "updatedAt": "2026-07-12T10:30:00"
}
```

### Standard API Response Wrapper
```json
{
  "success": true,
  "message": "Operation description",
  "data": {},
  "timestamp": 1689160200000
}
```

---

## API Endpoints

### 1. Create Product
**POST** `/products`

**Description:** Create a new product

**Request Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "productCode": "COCA001",
  "barcode": "049000050127",
  "name": "Coca Cola Classic 12oz",
  "description": "Classic cola beverage",
  "brand": "Coca Cola",
  "category": "BEVERAGES",
  "purchasePrice": "1.50",
  "sellingPrice": "2.99",
  "quantityInStock": 150,
  "reorderLevel": 50,
  "unit": "CAN"
}
```

**Response Status:** `201 Created`

**Response Body:**
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "productCode": "COCA001",
    "barcode": "049000050127",
    "name": "Coca Cola Classic 12oz",
    "description": "Classic cola beverage",
    "brand": "Coca Cola",
    "category": "BEVERAGES",
    "purchasePrice": "1.50",
    "sellingPrice": "2.99",
    "quantityInStock": 150,
    "reorderLevel": 50,
    "unit": "CAN",
    "active": true,
    "createdAt": "2026-07-12T10:30:00",
    "updatedAt": "2026-07-12T10:30:00"
  },
  "timestamp": 1689160200000
}
```

---

### 2. Get All Products
**GET** `/products`

**Description:** Retrieve all products (including inactive)

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Products retrieved successfully",
  "data": [
    {
      "id": 1,
      "productCode": "COCA001",
      "barcode": "049000050127",
      "name": "Coca Cola Classic 12oz",
      "description": "Classic cola beverage",
      "brand": "Coca Cola",
      "category": "BEVERAGES",
      "purchasePrice": "1.50",
      "sellingPrice": "2.99",
      "quantityInStock": 150,
      "reorderLevel": 50,
      "unit": "CAN",
      "active": true,
      "createdAt": "2026-07-12T10:30:00",
      "updatedAt": "2026-07-12T10:30:00"
    },
    {
      "id": 2,
      "productCode": "BEV002",
      "barcode": "012000009814",
      "name": "Pepsi 12oz",
      "description": "Cola soft drink",
      "brand": "Pepsi",
      "category": "BEVERAGES",
      "purchasePrice": "1.50",
      "sellingPrice": "2.99",
      "quantityInStock": 120,
      "reorderLevel": 40,
      "unit": "CAN",
      "active": true,
      "createdAt": "2026-07-12T10:35:00",
      "updatedAt": "2026-07-12T10:35:00"
    }
  ],
  "timestamp": 1689160200000
}
```

---

### 3. Get Product by ID
**GET** `/products/{id}`

**Description:** Retrieve a specific product by ID

**Path Parameters:**
- `id` (Long, required): Product ID

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Product retrieved successfully",
  "data": {
    "id": 1,
    "productCode": "COCA001",
    "barcode": "049000050127",
    "name": "Coca Cola Classic 12oz",
    "description": "Classic cola beverage",
    "brand": "Coca Cola",
    "category": "BEVERAGES",
    "purchasePrice": "1.50",
    "sellingPrice": "2.99",
    "quantityInStock": 150,
    "reorderLevel": 50,
    "unit": "CAN",
    "active": true,
    "createdAt": "2026-07-12T10:30:00",
    "updatedAt": "2026-07-12T10:30:00"
  },
  "timestamp": 1689160200000
}
```

---

### 4. Update Product
**PUT** `/products/{id}`

**Description:** Update an existing product

**Path Parameters:**
- `id` (Long, required): Product ID

**Request Body:**
```json
{
  "productCode": "COCA001",
  "barcode": "049000050127",
  "name": "Coca Cola Classic 12oz",
  "description": "Classic cola beverage - UPDATED",
  "brand": "Coca Cola",
  "category": "BEVERAGES",
  "purchasePrice": "1.60",
  "sellingPrice": "3.09",
  "quantityInStock": 200,
  "reorderLevel": 60,
  "unit": "CAN"
}
```

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Product updated successfully",
  "data": {
    "id": 1,
    "productCode": "COCA001",
    "barcode": "049000050127",
    "name": "Coca Cola Classic 12oz",
    "description": "Classic cola beverage - UPDATED",
    "brand": "Coca Cola",
    "category": "BEVERAGES",
    "purchasePrice": "1.60",
    "sellingPrice": "3.09",
    "quantityInStock": 200,
    "reorderLevel": 60,
    "unit": "CAN",
    "active": true,
    "createdAt": "2026-07-12T10:30:00",
    "updatedAt": "2026-07-12T11:00:00"
  },
  "timestamp": 1689160200000
}
```

---

### 5. Search Products
**GET** `/products/search?keyword={keyword}`

**Description:** Search products by name or brand

**Query Parameters:**
- `keyword` (String, required): Search keyword

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Search completed successfully",
  "data": [
    {
      "id": 1,
      "productCode": "COCA001",
      "barcode": "049000050127",
      "name": "Coca Cola Classic 12oz",
      "description": "Classic cola beverage",
      "brand": "Coca Cola",
      "category": "BEVERAGES",
      "purchasePrice": "1.50",
      "sellingPrice": "2.99",
      "quantityInStock": 150,
      "reorderLevel": 50,
      "unit": "CAN",
      "active": true,
      "createdAt": "2026-07-12T10:30:00",
      "updatedAt": "2026-07-12T10:30:00"
    }
  ],
  "timestamp": 1689160200000
}
```

---

### 6. Get Products by Category
**GET** `/products/category/{category}`

**Description:** Get all active products in a specific category

**Path Parameters:**
- `category` (Enum, required): BEVERAGES, SNACKS, DAIRY, GROCERY, HOUSEHOLD, TOBACCO, PERSONAL_CARE, FROZEN, OTHER

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Products retrieved by category successfully",
  "data": [
    {
      "id": 1,
      "productCode": "COCA001",
      "barcode": "049000050127",
      "name": "Coca Cola Classic 12oz",
      "description": "Classic cola beverage",
      "brand": "Coca Cola",
      "category": "BEVERAGES",
      "purchasePrice": "1.50",
      "sellingPrice": "2.99",
      "quantityInStock": 150,
      "reorderLevel": 50,
      "unit": "CAN",
      "active": true,
      "createdAt": "2026-07-12T10:30:00",
      "updatedAt": "2026-07-12T10:30:00"
    }
  ],
  "timestamp": 1689160200000
}
```

---

### 7. Get Active Products
**GET** `/products/active`

**Description:** Get all active products

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Active products retrieved successfully",
  "data": [
    {
      "id": 1,
      "productCode": "COCA001",
      "barcode": "049000050127",
      "name": "Coca Cola Classic 12oz",
      "description": "Classic cola beverage",
      "brand": "Coca Cola",
      "category": "BEVERAGES",
      "purchasePrice": "1.50",
      "sellingPrice": "2.99",
      "quantityInStock": 150,
      "reorderLevel": 50,
      "unit": "CAN",
      "active": true,
      "createdAt": "2026-07-12T10:30:00",
      "updatedAt": "2026-07-12T10:30:00"
    }
  ],
  "timestamp": 1689160200000
}
```

---

### 8. Increase Stock
**PATCH** `/products/{id}/stock/add?quantity={quantity}`

**Description:** Increase the stock quantity for a product

**Path Parameters:**
- `id` (Long, required): Product ID

**Query Parameters:**
- `quantity` (Integer, required): Quantity to add (must be positive)

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Stock increased successfully",
  "data": {
    "id": 1,
    "productCode": "COCA001",
    "barcode": "049000050127",
    "name": "Coca Cola Classic 12oz",
    "description": "Classic cola beverage",
    "brand": "Coca Cola",
    "category": "BEVERAGES",
    "purchasePrice": "1.50",
    "sellingPrice": "2.99",
    "quantityInStock": 200,
    "reorderLevel": 50,
    "unit": "CAN",
    "active": true,
    "createdAt": "2026-07-12T10:30:00",
    "updatedAt": "2026-07-12T11:05:00"
  },
  "timestamp": 1689160200000
}
```

---

### 9. Decrease Stock
**PATCH** `/products/{id}/stock/remove?quantity={quantity}`

**Description:** Decrease the stock quantity for a product

**Path Parameters:**
- `id` (Long, required): Product ID

**Query Parameters:**
- `quantity` (Integer, required): Quantity to decrease (must be positive and not exceed current stock)

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Stock decreased successfully",
  "data": {
    "id": 1,
    "productCode": "COCA001",
    "barcode": "049000050127",
    "name": "Coca Cola Classic 12oz",
    "description": "Classic cola beverage",
    "brand": "Coca Cola",
    "category": "BEVERAGES",
    "purchasePrice": "1.50",
    "sellingPrice": "2.99",
    "quantityInStock": 130,
    "reorderLevel": 50,
    "unit": "CAN",
    "active": true,
    "createdAt": "2026-07-12T10:30:00",
    "updatedAt": "2026-07-12T11:10:00"
  },
  "timestamp": 1689160200000
}
```

---

### 10. Disable Product
**PATCH** `/products/{id}/disable`

**Description:** Disable a product (soft delete, marks as inactive)

**Path Parameters:**
- `id` (Long, required): Product ID

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Product disabled successfully",
  "data": {
    "id": 1,
    "productCode": "COCA001",
    "barcode": "049000050127",
    "name": "Coca Cola Classic 12oz",
    "description": "Classic cola beverage",
    "brand": "Coca Cola",
    "category": "BEVERAGES",
    "purchasePrice": "1.50",
    "sellingPrice": "2.99",
    "quantityInStock": 150,
    "reorderLevel": 50,
    "unit": "CAN",
    "active": false,
    "createdAt": "2026-07-12T10:30:00",
    "updatedAt": "2026-07-12T11:15:00"
  },
  "timestamp": 1689160200000
}
```

---

### 11. Delete Product
**DELETE** `/products/{id}`

**Description:** Permanently delete a product

**Path Parameters:**
- `id` (Long, required): Product ID

**Response Status:** `200 OK`

**Response Body:**
```json
{
  "success": true,
  "message": "Product deleted successfully",
  "data": null,
  "timestamp": 1689160200000
}
```

---

## Error Responses

### 400 Bad Request - Validation Error
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "productCode": "Product code is required",
    "brand": "Brand is required",
    "sellingPrice": "Selling price must be greater than 0"
  },
  "timestamp": 1689160200000
}
```

### 400 Bad Request - Business Rule Violation
```json
{
  "success": false,
  "message": "Selling price must be greater than purchase price",
  "data": null,
  "timestamp": 1689160200000
}
```

### 409 Conflict - Duplicate Product Code
```json
{
  "success": false,
  "message": "Product code already exists: COCA001",
  "data": null,
  "timestamp": 1689160200000
}
```

### 400 Bad Request - Invalid Stock Operation
```json
{
  "success": false,
  "message": "Insufficient stock. Current quantity: 50, Requested to decrease: 100",
  "data": null,
  "timestamp": 1689160200000
}
```

### 404 Not Found
```json
{
  "success": false,
  "message": "Product not found with ID: 999",
  "data": null,
  "timestamp": 1689160200000
}
```

---

## Category Enums
```
BEVERAGES
SNACKS
DAIRY
GROCERY
HOUSEHOLD
TOBACCO
PERSONAL_CARE
FROZEN
OTHER
```

## Unit Enums
```
PIECE
PACK
BOX
BOTTLE
CAN
LITER
KG
GRAM
```

---

## Validation Rules

| Field | Rule | Error Message |
|-------|------|---------------|
| productCode | Required, Max 50 chars, Unique | "Product code is required" |
| name | Required, Min 3, Max 200 chars | "Product name is required" |
| brand | Required, Max 100 chars | "Brand is required" |
| category | Required | "Category is required" |
| purchasePrice | Required, >= 0 | "Purchase price must be >= 0" |
| sellingPrice | Required, > 0, > purchasePrice | "Selling price must be > 0" |
| quantityInStock | Required, >= 0 | "Quantity cannot be negative" |
| reorderLevel | Required, >= 0 | "Reorder level cannot be negative" |
| unit | Required | "Unit is required" |

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK - Success |
| 201 | Created - Resource created successfully |
| 400 | Bad Request - Validation or business logic error |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Duplicate resource |
| 500 | Internal Server Error |

---

## Testing with cURL

### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"productCode":"COCA001","barcode":"049000050127","name":"Coca Cola","description":"Cola","brand":"Coca Cola","category":"BEVERAGES","purchasePrice":1.50,"sellingPrice":2.99,"quantityInStock":150,"reorderLevel":50,"unit":"CAN"}'
```

### Get All Products
```bash
curl http://localhost:8080/api/products
```

### Search Products
```bash
curl http://localhost:8080/api/products/search?keyword=Coca
```

### Increase Stock
```bash
curl -X PATCH "http://localhost:8080/api/products/1/stock/add?quantity=50"
```

---

## Postman Import

1. Open Postman
2. Click Import
3. Choose the `MODULE_2_POSTMAN_COLLECTION.json` file
4. All endpoints will be automatically configured

---


