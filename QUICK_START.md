# 🚀 Quick Start - Retail Management System

## First Time Setup (5 minutes)

### 1. Create Database
```bash
mysql -u root -p
```
Then run:
```sql
CREATE DATABASE retail_management_system;
EXIT;
```

### 2. Build Project
```bash
cd C:\Users\user\OneDrive\Desktop\RetailBus\RetailBussiness
mvn clean install
```

### 3. Run Application
```bash
mvn spring-boot:run
```

### 4. Verify It Works
Open in browser or terminal:
```bash
curl http://localhost:8080/api/health
```

You should see:
```json
{
  "success": true,
  "message": "Application is running",
  "data": {
    "status": "UP",
    "application": "Retail Management System",
    "version": "1.0.0"
  },
  "timestamp": 1234567890
}
```

✅ **If you see this, Module 1 is working!**

---

## Project Structure at a Glance

```
src/main/java/com/retailbus/
├── RetailManagementApplication.java         👈 Start here
├── controller/
│   └── HealthCheckController.java           📍 Health check endpoint
├── service/                                 📍 Business logic (empty - ready for Module 2)
├── repository/                              📍 Database access (empty - ready for Module 2)
├── entity/                                  📍 Database models (empty - ready for Module 2)
├── dto/
│   └── ApiResponse.java                     🔄 Response wrapper (all endpoints use this)
├── exception/
│   ├── GlobalExceptionHandler.java          ⚠️ Error handling
│   ├── BusinessException.java
│   └── ResourceNotFoundException.java
├── config/                                  ⚙️ Configuration classes (empty - ready for security)
└── util/                                    🛠️ Utilities (empty - ready for helpers)
```

---

## Key Files to Know

| File | Purpose |
|------|---------|
| `pom.xml` | All dependencies and build config |
| `src/main/resources/application.properties` | Database & server config |
| `RetailManagementApplication.java` | Main entry point |
| `ApiResponse.java` | Standard response format |
| `GlobalExceptionHandler.java` | Centralized error handling |

---

## Available Endpoints (Module 1)

### Health Check
```
GET http://localhost:8080/api/health
```
Returns status of application

### API Documentation
```
http://localhost:8080/api/swagger-ui.html
```
Interactive API documentation

---

## Common Commands

```bash
# Build only (don't run)
mvn clean compile

# Build and run
mvn spring-boot:run

# Run tests
mvn test

# Skip tests and build
mvn clean package -DskipTests

# Run JAR file
java -jar target/retail-management-system-1.0.0.jar
```

---

## Troubleshooting

### Problem: MySQL connection fails
```
Error: Communications link failure
```
**Solution**: Make sure MySQL is running
```bash
mysql -u root -p -e "SELECT 1;"  # Should show "1"
```

### Problem: Port 8080 in use
```
Error: Address already in use
```
**Solution**: Change port in `application.properties`
```properties
server.port=8081
```

### Problem: Cannot find maven
```
'mvn' is not recognized
```
**Solution**: Install Maven or use IDE's built-in Maven

---

## Next Steps

✅ **Module 1**: Project Setup - **COMPLETE**
⏳ **Module 2**: Product Management - Ready for instruction

When ready, I'll implement:
- Product entity with fields (name, sku, category, price, stock)
- Database repository for CRUD operations
- Service layer with business logic
- REST controller with endpoints

---

**Once you verify Module 1 is working, reply with:** "Ready for Module 2"

🎯 Then we'll build the Product Management system!

