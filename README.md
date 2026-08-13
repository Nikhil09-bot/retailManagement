# Retail Management System

A comprehensive Full Stack Retail Management System built for independent convenience stores and gas stations.

## Module 1: Project Setup - COMPLETED ✅

### Overview
This module establishes the foundational structure of the application with Spring Boot, MySQL configuration, and proper package organization following clean architecture principles.

### Technology Stack
- **Java**: 21 (LTS)
- **Framework**: Spring Boot 3.3.0
- **Build Tool**: Maven
- **Database**: MySQL 8.0
- **ORM**: Hibernate (via Spring Data JPA)
- **Project**: Spring Security, Lombok, JWT

### Project Structure

```
com.retailbus/
├── RetailManagementApplication.java    # Main Spring Boot application
├── config/                              # Configuration classes
│   └── package-info.java
├── entity/                              # JPA entities (database models)
│   └── package-info.java
├── dto/                                 # Data Transfer Objects
│   ├── ApiResponse.java                # Standard API response wrapper
│   └── package-info.java
├── repository/                          # Data access layer (Spring Data JPA)
│   └── package-info.java
├── service/                             # Business logic layer
│   └── package-info.java
├── controller/                          # REST API endpoints
│   ├── HealthCheckController.java       # Health check endpoint
│   └── package-info.java
├── exception/                           # Exception handling
│   ├── GlobalExceptionHandler.java      # Global exception handler
│   ├── BusinessException.java           # Custom business exception
│   ├── ResourceNotFoundException.java   # Resource not found exception
│   └── package-info.java
└── util/                                # Utility classes and constants
    └── package-info.java
```

### Database Configuration

The application uses MySQL as the primary database. Configuration is done in `application.properties`:

```properties
# Database Connection
spring.datasource.url=jdbc:mysql://localhost:3306/retail_management_system
spring.datasource.username=root
spring.datasource.password=root

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update  # Automatically creates/updates schema
spring.jpa.show-sql=false
```

### Dependencies Added

#### Core Spring Boot
- `spring-boot-starter-web` - Web application support
- `spring-boot-starter-data-jpa` - Database access with JPA
- `spring-boot-starter-validation` - Bean validation support
- `spring-boot-starter-security` - Security support (for future authentication)

#### Database
- `mysql-connector-java` - MySQL JDBC driver
- `hibernate-core` - ORM framework

#### Development Tools
- `lombok` - Reduces boilerplate code (getters, setters, constructors)

#### API Documentation
- `springdoc-openapi-starter-webmvc-ui` - Swagger UI for API documentation

#### Testing
- `spring-boot-starter-test` - Spring testing framework
- `junit-jupiter` - JUnit 5 testing framework
- `mockito-core` - Mocking framework for unit tests

#### Security (Future Modules)
- `jjwt-api` - JWT token generation and validation

### Setup Instructions

#### Prerequisites
- Java 21 JDK installed
- Maven 3.6+ installed
- MySQL Server 8.0+ running
- MySQL client or tool (like Workbench)

#### 1. Create MySQL Database

```sql
CREATE DATABASE retail_management_system;
USE retail_management_system;
```

#### 2. Clone/Setup Project

```bash
cd C:\Users\user\OneDrive\Desktop\RetailBus\RetailBussiness
```

#### 3. Download Dependencies

```bash
mvn clean install
```

#### 4. Run Application

```bash
mvn spring-boot:run
```

Or run from IDE by executing the `RetailManagementApplication` class.

#### 5. Verify Application is Running

```bash
curl http://localhost:8080/api/health
```

Expected response:
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

### API Documentation
Once the application is running, access Swagger UI:
- **URL**: `http://localhost:8080/api/swagger-ui.html`

### Key Design Decisions

1. **Package Structure**: Organized by technical layer (controller, service, repository) instead of feature to follow MVC pattern and make it easier to understand the application architecture.

2. **ApiResponse Wrapper**: All API responses follow a standard format with success flag, message, data, and timestamp for consistency and better client-side handling.

3. **Global Exception Handler**: Centralized exception handling ensures consistent error responses across all endpoints.

4. **DTOs**: Separating DTOs from entities ensures loose coupling and provides flexibility to modify internal models without breaking API contracts.

5. **Spring Data JPA**: Used for simplicity and reducing boilerplate in data access layer.

### Configuration Files

- **pom.xml**: Maven configuration with all dependencies and plugins
- **application.properties**: Application settings for database, logging, and server configuration

### Logging

Logging is configured in `application.properties`:
- **Root Level**: INFO
- **Application Level (com.retailbus)**: DEBUG
- **SQL Queries**: DEBUG (to track database operations during development)

### Next Steps

Once Module 1 is verified and working:
1. Proceed to Module 2: Product Management
2. Create Product entity, repository, service, and controller
3. Implement CRUD operations for products

### Troubleshooting

**Issue**: Connection to MySQL failed
- **Solution**: Ensure MySQL is running and credentials in `application.properties` are correct

**Issue**: Port 8080 already in use
- **Solution**: Change port in `application.properties`: `server.port=8081`

**Issue**: Build fails with dependency errors
- **Solution**: Run `mvn clean install` to download all dependencies

---

**Module Status**: ✅ Complete
**Date**: July 2026
**Version**: 1.0.0

