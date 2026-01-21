# Local Database Setup Guide

## Problem
The AWS RDS database is not accessible. Let's set up a local PostgreSQL database instead.

---

## Step 1: Install PostgreSQL (if not installed)

### macOS:
```bash
brew install postgresql@14
brew services start postgresql@14
```

### Verify Installation:
```bash
psql --version
```

---

## Step 2: Create Database and User

```bash
# Connect to PostgreSQL
psql postgres

# Create database
CREATE DATABASE nethaji;

# Create user (optional, or use postgres user)
CREATE USER nethaji_user WITH PASSWORD 'nethaji123';

# Grant privileges
GRANT ALL PRIVILEGES ON DATABASE nethaji TO nethaji_user;

# Exit
\q
```

---

## Step 3: Update application.yml

Update the datasource configuration to use local database:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/nethaji
    username: nethaji_user
    password: Anjan$123
```

---

## Step 4: Test Database Connection

```bash
# Test connection
psql -U postgres -d nethaji -h localhost
```

If it asks for password, enter your PostgreSQL password.

---

## Step 5: Run Application

```bash
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend
mvn spring-boot:run
```

The application will automatically create all tables!

---

## Quick Setup Script

Run these commands:

```bash
# 1. Install PostgreSQL (if needed)
brew install postgresql@14
brew services start postgresql@14

# 2. Create database
createdb nethaji

# 3. Update application.yml with local database settings
# 4. Run application
mvn spring-boot:run
```

---

## Alternative: Use Docker PostgreSQL

If you have Docker:

```bash
# Run PostgreSQL in Docker
docker run --name nethaji-postgres \
  -e POSTGRES_PASSWORD=nethaji123 \
  -e POSTGRES_DB=nethaji \
  -p 5432:5432 \
  -d postgres:14

# Update application.yml:
# url: jdbc:postgresql://localhost:5432/nethaji
# username: postgres
# password: nethaji123
```

---

## Troubleshooting

### Can't connect to localhost?
- Make sure PostgreSQL is running: `brew services list`
- Check if port 5432 is available: `lsof -i :5432`

### Permission denied?
- Make sure you're using the correct username/password
- Check PostgreSQL authentication settings

### Database already exists?
- Drop and recreate: `DROP DATABASE nethaji; CREATE DATABASE nethaji;`

