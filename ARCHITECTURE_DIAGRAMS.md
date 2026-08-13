# 🏗️ Retail Management System - Visual Architecture

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           CLIENT LAYER                                       │
│  ┌──────────────────┬──────────────────┬──────────────────┐                 │
│  │ Web Browsers     │  Mobile Apps     │  Desktop Clients │                 │
│  └────────┬─────────┴────────┬─────────┴────────┬─────────┘                 │
│           │                  │                  │                            │
│           └──────────────────┼──────────────────┘                            │
│                              │ REST Calls (HTTP/JSON)                        │
└──────────────────────────────┼────────────────────────────────────────────────┘
                               │
                               │ :8080/api
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                                      │
│                      (REST Controllers)                                      │
│                                                                              │
│  ┌────────────────┬─────────────────┬────────────────┬─────────────────┐  │
│  │ Product        │ Inventory       │ Supplier       │ Sales           │  │
│  │ Controller     │ Controller      │ Controller     │ Controller      │  │
│  │ (/api/         │ (/api/          │ (/api/         │ (/api/          │  │
│  │ products)      │ inventory)      │ suppliers)     │ sales)          │  │
│  └────────────────┴─────────────────┴────────────────┴─────────────────┘  │
│  ┌────────────────┬─────────────────┬────────────────┬─────────────────┐  │
│  │ Customer       │ Purchase Order  │ Dashboard      │ Reports         │  │
│  │ Controller     │ Controller      │ Controller     │ Controller      │  │
│  │ (/api/         │ (/api/          │ (/api/         │ (/api/          │  │
│  │ customers)     │ purchase-orders)│ dashboard)     │ reports)        │  │
│  └────────────────┴─────────────────┴────────────────┴─────────────────┘  │
│                                                                              │
│  Request Validation → ApiResponse Formatting → Exception Handling           │
└──────────────────────────────┬───────────────────────────────────────────────┘
                               │ Service Calls
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      BUSINESS LOGIC LAYER                                    │
│                      (Service Interfaces & Implementations)                 │
│                                                                              │
│  ┌────────────────┬─────────────────┬────────────────┬─────────────────┐  │
│  │ ProductService │ InventoryService│ SupplierService│ SalesService    │  │
│  │                │                 │                │                 │  │
│  │ • CRUD         │ • Track stock   │ • CRUD         │ • Create sale   │  │
│  │ • Search       │ • Adjustments   │ • Search       │ • Deduct stock  │  │
│  │ • Validation   │ • Low stock     │ • Validation   │ • Receipt gen   │  │
│  └────────────────┴─────────────────┴────────────────┴─────────────────┘  │
│  ┌────────────────┬─────────────────┬────────────────┬─────────────────┐  │
│  │ CustomerService│ PurchaseOrderSvc│ DashboardService│ ReportService  │  │
│  │                │                 │                │                 │  │
│  │ • CRUD         │ • Create PO     │ • Aggregate    │ • Generate      │  │
│  │ • Search       │ • Track status  │ • Statistics   │   reports       │  │
│  │ • Loyalty pts  │ • Auto-add inv  │ • KPIs         │ • Analysis      │  │
│  └────────────────┴─────────────────┴────────────────┴─────────────────┘  │
│                                                                              │
│  @Transactional → Business Rules → Data Validation → Orchestration        │
└──────────────────────────────┬───────────────────────────────────────────────┘
                               │ Repository Calls
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      DATA ACCESS LAYER                                       │
│                      (Spring Data JPA Repositories)                         │
│                                                                              │
│  ┌────────────────┬─────────────────┬────────────────┬─────────────────┐  │
│  │ ProductRepo    │ InventoryRepo   │ SupplierRepo   │ SalesRepo       │  │
│  │                │                 │                │                 │  │
│  │ • findById()   │ • findByType()  │ • findByCity() │ • findToday()   │  │
│  │ • save()       │ • findLowStock()│ • findActive() │ • findByReceipt│  │
│  │ • Custom @Query│ • dateRange()   │ • search()     │ • dateRange()   │  │
│  └────────────────┴─────────────────┴────────────────┴─────────────────┘  │
│  ┌────────────────┬─────────────────┬────────────────┬─────────────────┐  │
│  │ CustomerRepo   │ PurchaseOrderRep│ SaleItemRepo   │ InventoryHist   │  │
│  │                │                 │                │ Repository      │  │
│  │ • findById()   │ • findByStatus()│ • findBySale() │ • Audit Trail   │  │
│  │ • findByEmail()│ • findBySupp()  │ • save()       │ • Query History │  │
│  │ • search()     │ • dateRange()   │ • delete()     │ • findByType()  │  │
│  └────────────────┴─────────────────┴────────────────┴─────────────────┘  │
│                                                                              │
│  JPA Queries → Hibernat → SQL Generation → Query Execution                │
└──────────────────────────────┬───────────────────────────────────────────────┘
                               │ JDBC
                               ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                      DATABASE LAYER                                          │
│                      (MySQL 8.0)                                            │
│                                                                              │
│  ┌────────────────┬─────────────────┬────────────────┬─────────────────┐  │
│  │ products       │ inventory_history│ suppliers      │ sales           │  │
│  │ • id (PK)      │ • id (PK)       │ • id (PK)      │ • id (PK)       │  │
│  │ • name         │ • product_id(FK)│ • name         │ • receipt_num   │  │
│  │ • category     │ • qty_change    │ • contact      │ • sale_date     │  │
│  │ • price        │ • type          │ • city         │ • total_amount  │  │
│  │ • stock        │ • timestamp     │ • payment_term │ • tax_amount    │  │
│  └────────────────┴─────────────────┴────────────────┴─────────────────┘  │
│  ┌────────────────┬─────────────────┬────────────────┬─────────────────┐  │
│  │ customers      │ purchase_orders │ sale_items     │ purchase_order_ │  │
│  │ • id (PK)      │ • id (PK)       │ • id (PK)      │ items           │  │
│  │ • name         │ • po_number     │ • sale_id(FK)  │ • id (PK)       │  │
│  │ • phone        │ • supplier_id   │ • product_id   │ • po_id (FK)    │  │
│  │ • email        │ • po_date       │ • quantity     │ • product_id    │  │
│  │ • loyalty_pts  │ • status        │ • price        │ • quantity      │  │
│  └────────────────┴─────────────────┴────────────────┴─────────────────┘  │
│                                                                              │
│  Indexes: created on product_id, supplier_id, sale_date, po_date etc.     │
│  Foreign Keys: Ensure referential integrity                               │
│  Constraints: NOT NULL, UNIQUE, CHECK constraints                         │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Component Interaction Flow

### 1. Request Flow (Client → Server)

```
┌─────────────┐
│   Client    │
│  (Browser   │
│   /Mobile)  │
└──────┬──────┘
       │ HTTP Request
       │ GET /api/products/1
       ▼
┌──────────────────────────┐
│   API Gateway / Router   │
│  (Spring DispatcherSvlt) │
└──────┬───────────────────┘
       │ Route to Controller
       ▼
┌──────────────────────────┐
│  ProductController       │
│  ├─ @GetMapping("/{id}") │
│  └─ Validate request     │
└──────┬───────────────────┘
       │ Call Service
       ▼
┌──────────────────────────┐
│  ProductService          │
│  ├─ Business logic       │
│  ├─ Validation           │
│  └─ Call Repository      │
└──────┬───────────────────┘
       │ Query Database
       ▼
┌──────────────────────────┐
│  ProductRepository       │
│  ├─ findById(1)         │
│  └─ Execute JPA Query    │
└──────┬───────────────────┘
       │ SQL Query
       ▼
┌──────────────────────────┐
│  MySQL Database          │
│  └─ SELECT * FROM        │
│     products WHERE id=1  │
└──────┬───────────────────┘
```

### 2. Response Flow (Server → Client)

```
┌──────────────────────────┐
│  MySQL Database          │
│  └─ Result Set: Product  │
└──────┬───────────────────┘
       │ Return Entity
       ▼
┌──────────────────────────┐
│  ProductRepository       │
│  └─ Return Product       │
└──────┬───────────────────┘
       │ Entity Object
       ▼
┌──────────────────────────┐
│  ProductService          │
│  └─ Process Result       │
└──────┬───────────────────┘
       │ Service Result
       ▼
┌──────────────────────────┐
│  ProductMapper           │
│  └─ Entity → DTO         │
│     Product → ProductRes │
└──────┬───────────────────┘
       │ DTO Object
       ▼
┌──────────────────────────┐
│  ProductController       │
│  └─ Wrap in ApiResponse  │
└──────┬───────────────────┘
       │ ApiResponse<ProductResponse>
       ▼
┌──────────────────────────┐
│  Jackson/JSON Converter  │
│  └─ Serialize to JSON    │
└──────┬───────────────────┘
       │ HTTP Response
       │ Content-Type: application/json
       ▼
┌──────────────────────────┐
│   Client Receives JSON   │
│  {                       │
│   "success": true,       │
│   "data": {              │
│    "id": 1,              │
│    "name": "Product",    │
│    ...                   │
│   }                      │
│  }                       │
└──────────────────────────┘
```

---

## Exception Handling Flow

```
┌────────────────────────────────────┐
│ Any Layer Throws Exception         │
│ (Repository, Service, Controller)  │
└──────────────┬─────────────────────┘
               │
               ▼
┌────────────────────────────────────┐
│ Spring Exception Handling          │
│ DispatcherServlet catches it       │
└──────────────┬─────────────────────┘
               │
               ▼
┌────────────────────────────────────┐
│ GlobalExceptionHandler             │
│ @ControllerAdvice                  │
│ ├─ @ExceptionHandler(...)          │
│ ├─ Identify Exception Type         │
│ └─ Generate Error Response         │
└──────────────┬─────────────────────┘
               │
        ┌──────┴──────┬──────────────┐
        │             │              │
        ▼             ▼              ▼
    ┌─────────┐ ┌──────────────┐ ┌─────────────┐
    │Business │ │ResourceNot   │ │Validation   │
    │Exception│ │Found         │ │Exception    │
    └────┬────┘ └──────┬───────┘ └────┬────────┘
         │             │              │
         ├─────────────┼──────────────┤
         │             │              │
         ▼             ▼              ▼
    ┌──────────────────────────────────────────┐
    │  StandardErrorResponse                   │
    │  {                                        │
    │   "success": false,                       │
    │   "message": "Error description",        │
    │   "timestamp": 1234567890                │
    │  }                                        │
    └──────────────┬───────────────────────────┘
                   │
                   ▼
         ┌──────────────────────┐
         │ HTTP Response        │
         │ 400/404/500 Status   │
         │ + Error JSON Body    │
         └──────────────────────┘
```

---

## Database Relationships Diagram

```
                 ┌─────────────────┐
                 │   SUPPLIER      │
                 ├─────────────────┤
                 │ supplier_id (PK)│
                 │ name            │
                 │ contact         │
                 │ city            │
                 └────────┬────────┘
                          │
                 ┌────────┴────────┐
                 │ (1 to Many)     │
                 ▼                 ▼
         ┌─────────────────────────────────┐
         │   PRODUCT                       │
         ├─────────────────────────────────┤
         │ product_id (PK)                 │
         │ name                            │
         │ category                        │
         │ price                           │
         │ current_stock                   │
         │ reorder_level                   │
         └────────┬────────────────────────┘
                  │
        ┌─────────┴──────────┬────────────┐
        │                    │            │
        │ (1 to Many)        │            │
        ▼                    ▼            ▼
    ┌──────────────┐ ┌──────────────┐ ┌────────────────────┐
    │ INVENTORY_   │ │   SALE_      │ │PURCHASE_ORDER_     │
    │ HISTORY      │ │   ITEMS      │ │ITEMS               │
    │              │ │              │ │                    │
    │ • qty_change │ │ • quantity   │ │ • quantity         │
    │ • type       │ │ • price      │ │ • price            │
    │ • timestamp  │ │ • sale_id    │ │ • po_id            │
    └──────────────┘ └────┬─────────┘ └────────────────────┘
                         │                        │
                         │ (Many to 1)            │ (Many to 1)
                         ▼                        ▼
                    ┌───────────────┐    ┌────────────────┐
                    │     SALE      │    │ PURCHASE_ORDER │
                    ├───────────────┤    ├────────────────┤
                    │ sale_id (PK)  │    │ po_id (PK)     │
                    │ receipt_num   │    │ po_number      │
                    │ sale_date     │    │ po_date        │
                    │ total_amount  │    │ supplier_id(FK)│
                    │ tax_amount    │    │ status         │
                    │ discount      │    │ total          │
                    │ customer_id   │    └────────────────┘
                    └───────┬───────┘
                            │ (Many to 1)
                            ▼
                    ┌───────────────┐
                    │   CUSTOMER    │
                    ├───────────────┤
                    │ customer_id   │
                    │ name          │
                    │ phone         │
                    │ email         │
                    │ loyalty_pts   │
                    │ total_purchase│
                    └───────────────┘
```

---

## API Gateway Pattern (Spring DispatcherServlet)

```
┌──────────────────────────────────────────────────────────┐
│               HTTP Request                               │
│        (GET /api/products/123)                           │
└────────────────────┬─────────────────────────────────────┘
                     │
                     ▼
       ┌─────────────────────────────┐
       │  Spring DispatcherServlet   │
       │  (Central Request Router)   │
       └────────────┬────────────────┘
                    │
      ┌─────────────┼─────────────┐
      │             │             │
      ▼             ▼             ▼
┌──────────┐ ┌──────────┐ ┌──────────┐
│Request   │ │URL Path  │ │HTTP      │
│Params    │ │Mapping   │ │Method    │
└─────┬────┘ └────┬─────┘ └────┬─────┘
      │           │            │
      └───────────┼────────────┘
                  │ Matches route pattern
                  ▼
      ┌──────────────────────┐
      │  Controller Method   │
      │ @GetMapping("/{id}") │
      └──────────┬───────────┘
                 │ Execute
                 ▼
      ┌──────────────────────┐
      │ Business Logic       │
      │ (Service call)       │
      └──────────┬───────────┘
                 │ Result
                 ▼
      ┌──────────────────────┐
      │ Response Formatting  │
      │ (Jackson JSON)       │
      └──────────┬───────────┘
                 │
                 ▼
      ┌──────────────────────┐
      │  HTTP Response       │
      │  (JSON Body)         │
      │  (Status Code)       │
      └──────────────────────┘
```

---

## Module Dependency Graph

```
┌─────────────────────────────────────────────────────────┐
│               MODULE 1: Setup                           │
│          (Base Configuration)                           │
│  ┌────────────────────────────────────────────────┐   │
│  │ • Spring Boot configuration                    │   │
│  │ • Database connection                          │   │
│  │ • API response wrapper                         │   │
│  │ • Exception handler                            │   │
│  └────────────────────────────────────────────────┘   │
└─────────────────┬──────────────────────────────────────┘
                  │ (Depends on)
    ┌─────────────┴─────────────────────┬─────────────────┐
    │                                   │                 │
    ▼                                   ▼                 ▼
┌──────────────┐             ┌──────────────┐     ┌────────────────┐
│ MODULE 2:    │             │ MODULE 3:    │     │ MODULE 4:      │
│ Product Mgmt │             │ Inventory    │     │ Supplier Mgmt  │
│ ✅ COMPLETE  │             │ ✅ COMPLETE  │     │ ✅ COMPLETE    │
└──────┬───────┘             └──────┬───────┘     └────────┬───────┘
       │                            │                     │
       │ (Product entity/repo)      │                     │
       │                     (Inventory history)  (Supplier ref)
       │                            │                     │
       └────────────────┬───────────┴─────────────────────┘
                        │ (Depends on these)
        ┌───────────────┼───────────────────┐
        │               │                   │
        ▼               ▼                   ▼
   ┌──────────┐  ┌───────────────┐  ┌──────────────┐
   │MODULE 5: │  │ MODULE 6:     │  │ MODULE 7:    │
   │Purchase  │  │ Sales/POS     │  │ Customer     │
   │Orders    │  │ 50% Complete  │  │ 40% Complete │
   │60% Done  │  └───────┬───────┘  └──────┬───────┘
   └────┬─────┘          │                 │
        │                │                 │
        └────────┬───────┼─────────────────┘
                 │       │
        ┌────────┴───────┴──────────┐
        │ (Depend on products,      │
        │  inventory, suppliers)    │
        │                           │
        ▼                           ▼
   ┌────────────────┐         ┌────────────────┐
   │ MODULE 8:      │         │ MODULE 9:      │
   │ Dashboard      │         │ Reports        │
   │ 0% (Planned)   │         │ 0% (Planned)   │
   └────────────────┘         └────────────────┘
        │                           │
        │ (Aggregate all modules)   │
        └─────────────┬─────────────┘
                      │
           (Reports & KPIs generated from)
             Products, Inventory, Sales,
               Suppliers, Customers
```

---

## Session/Transaction Flow

```
┌───────────────────────────────────────────────────────────┐
│ 1. Client makes HTTP Request                              │
│    POST /api/sales with SaleRequest JSON                  │
└───────────────────────┬─────────────────────────────────┬─┘
                        │                                 │
                        ▼                                 ▼
            ┌─────────────────────┐         ┌──────────────────────┐
            │ SalesController     │         │ Request Validation   │
            │ createSale()        │         │ @Valid @RequestBody  │
            └────────┬────────────┘         └──────────┬───────────┘
                     │                                 │
                     └─────────────────┬───────────────┘
                                       │
                                       ▼
                        ┌──────────────────────────────┐
                        │ SalesService.createSale()   │
                        │ @Transactional               │
                        └─────────┬────────────────────┘
                                  │
                    ┌─────────────┼──────────────────┐
                    │             │                  │
                    ▼             ▼                  ▼
            ┌─────────────┐ ┌────────────┐ ┌──────────────────┐
            │ Validate    │ │ Calculate  │ │ Generate Receipt │
            │ inventory   │ │ tax/total  │ │ number           │
            └──────┬──────┘ └─────┬──────┘ └────────┬─────────┘
                   │              │               │
                   └──────────────┬┴───────────────┘
                                  │
                                  ▼
                        ┌────────────────────┐
                        │ Save Sale Record   │
                        │ SalesRepository    │
                        └────────┬───────────┘
                                 │
                                 ▼
                        ┌────────────────────┐
                        │ For Each SaleItem: │
                        └────────┬───────────┘
                                 │
            ┌────────────────────┬┴────────────────────┐
            │                    │                    │
            ▼                    ▼                    ▼
        ┌─────────────┐   ┌─────────────┐  ┌──────────────────┐
        │ Save Item   │   │ Deduct      │  │ Create Inventory │
        │ Record      │   │ Inventory   │  │ History Entry    │
        │             │   │             │  │ (Audit Trail)    │
        └─────────────┘   └─────────────┘  └──────────────────┘
            │                    │                    │
            └────────────────────┬────────────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────────┐
                    │ TRANSACTION COMMIT          │
                    │ (All or Nothing)            │
                    │ All data saved successfully │
                    └────────┬────────────────────┘
                             │
                             ▼
                    ┌─────────────────────────────┐
                    │ Response Back to Controller │
                    │ SaleResponse object         │
                    └────────┬────────────────────┘
                             │
                             ▼
                    ┌─────────────────────────────┐
                    │ Wrap in ApiResponse         │
                    │ success: true               │
                    │ message: "Sale created"     │
                    │ data: {saleResponse}        │
                    └────────┬────────────────────┘
                             │
                             ▼
                    ┌─────────────────────────────┐
                    │ Convert to JSON             │
                    │ (Jackson serialization)     │
                    └────────┬────────────────────┘
                             │
                             ▼
                    ┌─────────────────────────────┐
                    │ HTTP 201 Response           │
                    │ Content-Type: application/  │
                    │ json                        │
                    │ Body: {...}                 │
                    └────────┬────────────────────┘
                             │
                             ▼
                    ┌─────────────────────────────┐
                    │ Client Receives Response    │
                    │ & Updates UI                │
                    └─────────────────────────────┘
```

---

## Caching & Performance Strategy (Current & Future)

```
┌────────────────────────────────────────────────────┐
│         REQUEST RECEIVED                           │
└────────────────┬─────────────────────────────────┬─┘
                 │                                 │
        ┌────────▼──────────┐                      │
        │ Check L1 Cache    │                      │
        │ (HTTP Cache       │                      │
        │  Headers)         │                      │
        └────────┬──────────┘                      │
                 │                                 │
         ┌───────┴───────┬────────┐               │
         │               │        │                │
        NO             YES       │                │
         │               │       │                │
         ▼               ▼       │                │
    ┌─────────┐  ┌──────────────┐│               │
    │ Check L2│  │Return Cached ││               │
    │ Cache   │  │Response      ││               │
    │(Redis-  │  └──────────────┘│               │
    │Future)  │                  │               │
    └────┬────┘                  │               │
         │                       │               │
    ┌────┴────┐                  │               │
    │          │                 │               │
   YES        NO                │               │
    │          │                │               │
    ▼          ▼                │               │
┌────────┐ ┌────────────────┐   │               │
│Return  │ │Query Database  │   │               │
│Cached  │ │(JPA Query)     │   │               │
│Data    │ └────┬───────────┘   │               │
└────────┘      │               │               │
                ▼               │               │
         ┌──────────────────┐   │               │
         │Get Result from DB│   │               │
         │from MySQL        │   │               │
         └────┬─────────────┘   │               │
              │                 │               │
              ▼                 │               │
         ┌──────────────────┐   │               │
         │ Store in L1 Cache│   │               │
         │ Store in L2      │   │               │
         │ (future)         │   │               │
         └────┬─────────────┘   │               │
              │                 │               │
              └─────────────────┼──────────────┤
                                │              │
                                ▼              ▼
                         ┌─────────────┐  ┌──────────────┐
                         │Send Response│  │Cache Headers │
                         │to Client    │  │Set-Cookie    │
                         │             │  │Expires       │
                         └─────────────┘  └──────────────┘
```

---

## Monitoring & Logging Architecture

```
┌──────────────────────────────────────────────────────────┐
│              Application Code Execution                  │
│                                                          │
│  ┌──────────────┐    ┌──────────────┐    ┌────────┐    │
│  │ Controller   │    │ Service      │    │DAO    │    │
│  │ @Logger      │    │ @Logger      │    │@Logger│    │
│  └────┬─────────┘    └────┬─────────┘    └───┬───┘    │
│       │                   │                   │        │
│       └───────────────────┼───────────────────┘        │
│                           │                            │
│              SLF4J (Simple Logging Facade)             │
│                           │                            │
│         ┌─────────────────┼─────────────────┐          │
│         │                 │                 │          │
│         ▼                 ▼                 ▼          │
│   ┌──────────┐   ┌────────────┐   ┌──────────────┐   │
│   │ Logback  │   │ Console    │   │ File Output  │   │
│   │(Config)  │   │ Appender   │   │ Appender     │   │
│   └──────────┘   └────────────┘   └──────────────┘   │
│                                                        │
└────────────────────┬─────────────────────────────────┬─┘
                     │                                 │
                     ▼                                 ▼
         ┌──────────────────────┐        ┌─────────────────┐
         │ Console Output       │        │ Log Files       │
         │ (Development)        │        │ (audit_log.txt) │
         │ Real-time viewing    │        │ Persistent      │
         └──────────────────────┘        └─────────────────┘
```

---

**This is a comprehensive visual representation of the Retail Management System architecture.**

