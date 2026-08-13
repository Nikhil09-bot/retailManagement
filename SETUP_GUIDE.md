# Module 1: Setup Guide - Retail Management System

## ✅ Module 1 Setup Complete

This guide will help you verify the Module 1 setup and prepare to build the application.

---

## Prerequisites

Before starting, ensure you have the following installed:

### 1. Java 21 JDK
```bash
java -version
# Should show Java 21 or later
```

If not installed:
- Download from: https://www.oracle.com/java/technologies/downloads/#java21
- Or use OpenJDK: https://adoptium.net/

### 2. Maven 3.9.x
```bash
mvn -version
# Should show Maven 3.9.x or later
```

If not installed:
- Download from: https://maven.apache.org/download.cgi
- Install and add to PATH

### 3. MySQL 8.0
```bash
mysql --version
# Should show MySQL 8.0 or later
```

If not installed:
- Download from: https://dev.mysql.com/downloads/mysql/

### 4. MySQL Client Tool (Optional but recommended)
- MySQL Workbench: https://dev.mysql.com/downloads/workbench/
- Or use command line

---

## Step 1: Create MySQL Database

Open MySQL shell or MySQL Workbench and run:

```sql
CREATE DATABASE retail_management_system;
USE retail_management_system;
SHOW TABLES;  -- Should be empty
```

Or from command line:
```bash
mysql -u root -p -e "CREATE DATABASE retail_management_system;"
```

---

## Step 2: Verify Project Structure

The project should have this structure:

```
RetailBussiness/
├── pom.xml
├── mvnw.cmd
├── mvnw
├── .mvn/wrapper/maven-wrapper.properties
├── README.md
├── SETUP_GUIDE.md
├── .gitignore
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/retailbus/
    │   │       ├── RetailManagementApplication.java
    │   │       ├── config/
    │   │       ├── controller/
    │   │       │   └── HealthCheckController.java
    │   │       ├── dto/
    │   │       │   └── ApiResponse.java
    │   │       ├── entity/
    │   │       ├── exception/
    │   │       │   ├── BusinessException.java
    │   │       │   ├── GlobalExceptionHandler.java
    │   │       │   └── ResourceNotFoundException.java
    │   │       ├── repository/
    │   │       ├── service/
    │   │       └── util/
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/
```

---

## Step 3: Build the Application

Open terminal/PowerShell and navigate to the project directory:

```bash
cd C:\Users\user\OneDrive\Desktop\RetailBus\RetailBussiness
```

### Option A: Using Maven (Recommended)

```bash
mvn clean compile
```

If you get "mvn: command not found", you need to install Maven or set MAVEN_HOME.

### Option B: Using Maven Wrapper

```bash
mvnw.cmd clean compile
```

Or on Linux/Mac:
```bash
./mvnw clean compile
```

### Option C: Using IntelliJ IDE

1. Open the project in IntelliJ IDEA
2. Right-click on `pom.xml`
3. Select `Maven` → `Reload project`
4. Wait for dependencies to download
5. Build → `Build Project`

---

## Step 4: Run the Application

### Option A: Using Maven

```bash
mvn spring-boot:run
```

### Option B: Using IDE

1. Right-click on `RetailManagementApplication.java`
2. Select `Run 'RetailManagementApplication'`

### Option C: Build JAR and Run

```bash
mvn clean package
java -jar target/retail-management-system-1.0.0.jar
```

---

## Step 5: Verify Application is Running

Once the application starts, you should see output like:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.3.0)

2026-07-12 10:30:00.123  INFO 12345 --- [main] c.r.RetailManagementApplication: Starting RetailManagementApplication
...
2026-07-12 10:30:05.456  INFO 12345 --- [main] c.r.RetailManagementApplication: Started RetailManagementApplication in 5.333 seconds
```

Server will be running on: **http://localhost:8080**

### Test Health Endpoint

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
  "timestamp": 1234567890123
}
```

Or use browser: `http://localhost:8080/api/health`

---

## Step 6: Access API Documentation

Once the application is running:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

---

## Troubleshooting

### Issue: "Cannot connect to MySQL"

**Solution**:
1. Verify MySQL is running:
   ```bash
   mysql -u root -p -e "SELECT 1;"
   ```
2. Check credentials in `application.properties`
3. Verify database exists:
   ```bash
   mysql -u root -p -e "SHOW DATABASES;" | grep retail_management_system
   ```

### Issue: "Port 8080 already in use"

**Solution**:
1. Change port in `application.properties`:
   ```properties
   server.port=8081
   ```
2. Or kill the process using port 8080:
   ```bash
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   ```

### Issue: "Maven not found"

**Solution**:
1. Install Maven from https://maven.apache.org/download.cgi
2. Add Maven bin directory to PATH
3. Or use the Maven wrapper (mvnw.cmd)

### Issue: "Java version mismatch"

**Solution**:
1. Verify Java 21 is installed:
   ```bash
   java -version
   ```
2. Update pom.xml if needed:
   ```xml
   <properties>
       <java.version>21</java.version>
   </properties>
   ```

### Issue: "Compilation errors"

**Solution**:
1. Clean and rebuild:
   ```bash
   mvn clean install -DskipTests
   ```
2. Check that all package-info.java files are in place
3. Verify all required dependencies are downloaded

---

## Application.properties Configuration

Key configurations in `src/main/resources/application.properties`:

```properties
# Server
server.port=8080                                    # Application port
server.servlet.context-path=/api                   # API base path

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/retail_management_system
spring.datasource.username=root                     # Change as needed
spring.datasource.password=root                     # Change as needed

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update               # auto-creates tables
spring.jpa.show-sql=false                          # Set to true for SQL debugging

# Logging
logging.level.com.retailbus=DEBUG                  # Application logs
logging.level.org.hibernate.SQL=DEBUG              # SQL logs
```

---

## What's Included in Module 1

✅ **Spring Boot 3.3.0** - Latest stable version
✅ **Maven Configuration** - All dependencies defined
✅ **Package Structure** - Clean, layered architecture
✅ **Database Configuration** - MySQL ready
✅ **Exception Handling** - Global exception handler
✅ **API Response Wrapper** - Standardized responses
✅ **Health Check Endpoint** - Verify app is running
✅ **Lombok Integration** - Reduce boilerplate code
✅ **Documentation** - Swagger/OpenAPI ready
✅ **Testing Foundation** - JUnit & Mockito configured

---

## Next Steps

After verifying Module 1 is working:

1. Wait for instruction to proceed to **Module 2: Product Management**
2. Will create Product entity, repository, service, and controller
3. Implement CRUD operations for products

---

## Development Tips

### IDE Integration

**IntelliJ IDEA**:
1. File → Open → Select the project directory
2. IDE will auto-detect it as a Maven project
3. Right-click pom.xml → Maven → Reload project
4. Run configurations will be available automatically

### Database Management

**View tables** (after first run):
```sql
USE retail_management_system;
SHOW TABLES;
DESCRIBE <table_name>;
```

### Logging

To increase logging level for debugging, in `application.properties`:
```properties
logging.level.com.retailbus=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Common Maven Commands

```bash
mvn clean                        # Remove target directory
mvn compile                      # Compile code
mvn test                         # Run tests
mvn package                      # Create JAR
mvn spring-boot:run             # Run application
mvn dependency:tree             # Show dependency tree
mvn -DskipTests package         # Package without tests
```

---

## Questions?

Refer to:
- **README.md** - Project overview
- **pom.xml** - Dependencies and build configuration
- **application.properties** - Application settings

---

**Module 1 Status**: ✅ COMPLETE
**Date**: July 12, 2026
**Ready for**: Module 2 - Product Management

