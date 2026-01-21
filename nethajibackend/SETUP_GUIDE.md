# Setup Guide - Nethaji College Management System

## Prerequisites

Before running the application, ensure you have:

1. **Java 17** or higher installed
   ```bash
   java -version
   ```
   Should show: `openjdk version "17"` or higher

2. **Maven** installed
   ```bash
   mvn -version
   ```
   Should show Maven version 3.6+ 

3. **PostgreSQL** (if using local database)
   - PostgreSQL 12+ installed
   - Or use the existing AWS RDS instance (already configured)

---

## Database Setup

### Option 1: Using Existing AWS RDS Database (Recommended)

The application is already configured to use AWS RDS PostgreSQL:
- **Host**: `sample.cxe44200eugx.ap-south-1.rds.amazonaws.com`
- **Port**: `5432`
- **Database**: `nethaji`
- **Username**: `postgres`
- **Password**: `Nethaji$2025`

**No manual database creation needed!** Hibernate will automatically create all tables when you run the application.

**To verify connection:**
```bash
# Using psql (if installed)
psql -h sample.cxe44200eugx.ap-south-1.rds.amazonaws.com -p 5432 -U postgres -d nethaji
```

---

### Option 2: Using Local PostgreSQL Database

If you want to use a local PostgreSQL database:

#### Step 1: Install PostgreSQL (if not installed)

**macOS:**
```bash
brew install postgresql@14
brew services start postgresql@14
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install postgresql postgresql-contrib
sudo systemctl start postgresql
```

**Windows:**
Download and install from: https://www.postgresql.org/download/windows/

#### Step 2: Create Database

```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE nethaji;

# Create user (optional)
CREATE USER nethaji_user WITH PASSWORD 'your_password';

# Grant privileges
GRANT ALL PRIVILEGES ON DATABASE nethaji TO nethaji_user;

# Exit
\q
```

#### Step 3: Update application.yml

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nethaji
    username: postgres  # or nethaji_user
    password: your_password
```

---

## Running the Application

### Method 1: Using Maven (Recommended)

```bash
# Navigate to project directory
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend

# Clean and build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Method 2: Using Maven Wrapper (if available)

```bash
# Navigate to project directory
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend

# Run using Maven wrapper
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

### Method 3: Run JAR File

```bash
# Build JAR file
mvn clean package

# Run JAR file
java -jar target/nethaji.jar
```

### Method 4: Using IDE (IntelliJ IDEA / Eclipse)

1. **IntelliJ IDEA:**
   - Open the project
   - Right-click on `NethajiApplication.java`
   - Select "Run 'NethajiApplication'"

2. **Eclipse:**
   - Import as Maven project
   - Right-click on `NethajiApplication.java`
   - Run As → Java Application

---

## Verification

### 1. Check Application is Running

Once started, you should see:
```
Started NethajiApplication in X.XXX seconds
```

The application will run on: **http://localhost:9029**

### 2. Test Health Check Endpoint

```bash
curl http://localhost:9029/api/nethaji-service/health
```

Or open in browser: http://localhost:9029/api/nethaji-service/health

### 3. Verify Database Tables Created

Connect to your database and check tables:

```sql
-- Connect to database
psql -h sample.cxe44200eugx.ap-south-1.rds.amazonaws.com -p 5432 -U postgres -d nethaji

-- List all tables
\dt

-- You should see tables like:
-- attendance
-- marks
-- grades
-- assignments
-- student_assignments
-- users
-- student_profile
-- staff_profile
-- course
-- department
-- programs
-- etc.
```

### 4. Check API Documentation (Swagger)

If Swagger is configured, visit:
```
http://localhost:9029/swagger-ui.html
```

---

## Database Tables Auto-Creation

The application uses **Hibernate DDL Auto = update**, which means:

✅ **Tables are automatically created** when you run the application
✅ **Tables are updated** if entity structure changes
✅ **No manual SQL scripts needed**

**Important:** Make sure the database connection is working before starting the application.

---

## Troubleshooting

### Issue 1: Database Connection Failed

**Error:** `Connection refused` or `Connection timeout`

**Solutions:**
1. Check if database server is running (for local PostgreSQL)
2. Verify database credentials in `application.yml`
3. Check network connectivity (for AWS RDS)
4. Verify security groups allow your IP (for AWS RDS)

### Issue 2: Port Already in Use

**Error:** `Port 9029 is already in use`

**Solutions:**
```bash
# Find process using port 9029
lsof -i :9029

# Kill the process
kill -9 <PID>

# Or change port in application.yml
server:
  port: 9028  # Use different port
```

### Issue 3: Java Version Mismatch

**Error:** `Unsupported class file major version`

**Solution:**
```bash
# Check Java version
java -version

# Should be Java 17 or higher
# Install Java 17 if needed
```

### Issue 4: Maven Build Fails

**Error:** `Could not resolve dependencies`

**Solutions:**
```bash
# Clean Maven cache
mvn clean

# Update dependencies
mvn dependency:resolve

# If still failing, try offline mode
mvn -o clean install
```

### Issue 5: Tables Not Created

**Check:**
1. Verify `hibernate.ddl-auto: update` in `application.yml`
2. Check application logs for Hibernate errors
3. Verify database user has CREATE TABLE permissions
4. Check database connection is successful

---

## Application Configuration

### Key Configuration Files

1. **application.yml** - Main configuration
   - Database connection
   - Server port
   - JWT settings
   - AWS credentials

2. **pom.xml** - Maven dependencies

### Important Settings

```yaml
spring:
  jpa:
    hibernate.ddl-auto: update  # Auto-create/update tables
    show-sql: true              # Show SQL queries in logs
  datasource:
    url: jdbc:postgresql://...  # Database URL
    username: postgres           # Database username
    password: Nethaji$2025       # Database password

server:
  port: 9029                     # Application port
```

---

## Quick Start Commands

```bash
# 1. Navigate to project
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend

# 2. Clean and build
mvn clean install

# 3. Run application
mvn spring-boot:run

# 4. Application will start on http://localhost:9029
```

---

## API Endpoints

Once running, you can access:

- **Health Check**: `GET http://localhost:9029/api/nethaji-service/health`
- **Authentication**: `POST http://localhost:9029/api/nethaji-service/auth/registration`
- **Attendance**: `POST http://localhost:9029/api/nethaji-service/attendance/mark`
- **Marks**: `POST http://localhost:9029/api/nethaji-service/marks/enter`
- **Grades**: `POST http://localhost:9029/api/nethaji-service/grades/calculate`
- **Assignments**: `POST http://localhost:9029/api/nethaji-service/assignments/create`

---

## Next Steps

1. ✅ Database is ready (auto-created by Hibernate)
2. ✅ Application is running
3. 📝 Test API endpoints using Postman or curl
4. 📝 Integrate with frontend application
5. 📝 Add security/authentication as needed

---

## Support

If you encounter any issues:
1. Check application logs in console
2. Verify database connection
3. Check Java and Maven versions
4. Review error messages for specific issues

---

**Happy Coding! 🚀**

