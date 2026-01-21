# Quick Setup Instructions

## 🚀 How to Run the Application

### Step 1: Check Prerequisites
```bash
# Check Java version (should be 17+)
java -version

# Check Maven version
mvn -version
```

### Step 2: Navigate to Project Directory
```bash
cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend
```

### Step 3: Build and Run
```bash
# Option 1: Using Maven directly
mvn clean install
mvn spring-boot:run

# Option 2: Using the quick start script
./QUICK_START.sh
```

### Step 4: Verify
- Application runs on: **http://localhost:9029**
- Health check: **http://localhost:9029/api/nethaji-service/health**

---

## 🗄️ Database Setup

### Option 1: Use Existing AWS RDS (Already Configured)
✅ **No action needed!** The database is already configured in `application.yml`
- Database will be **auto-created** by Hibernate when you run the application
- All tables will be created automatically

### Option 2: Use Local PostgreSQL

1. **Install PostgreSQL** (if not installed)
   ```bash
   # macOS
   brew install postgresql@14
   brew services start postgresql@14
   ```

2. **Create Database**
   ```bash
   psql -U postgres
   CREATE DATABASE nethaji;
   \q
   ```

3. **Update application.yml**
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/nethaji
       username: postgres
       password: your_password
   ```

---

## 📋 What Happens When You Run?

1. ✅ Application connects to PostgreSQL database
2. ✅ Hibernate automatically creates all tables (because `ddl-auto: update`)
3. ✅ Application starts on port 9029
4. ✅ All API endpoints become available

**No manual SQL scripts needed!** Everything is automatic.

---

## 🔍 Verify Database Tables

After running the application, connect to database:

```bash
psql -h sample.cxe44200eugx.ap-south-1.rds.amazonaws.com -p 5432 -U postgres -d nethaji

# List tables
\dt

# You should see:
# - attendance
# - marks  
# - grades
# - assignments
# - student_assignments
# - users
# - student_profile
# - staff_profile
# - course
# - department
# - programs
# ... and more
```

---

## ⚠️ Troubleshooting

**Database Connection Error?**
- Check if AWS RDS is accessible
- Verify credentials in `application.yml`
- Check network connectivity

**Port Already in Use?**
```bash
lsof -i :9029
kill -9 <PID>
```

**Build Fails?**
```bash
mvn clean
mvn dependency:resolve
mvn install
```

---

## 📚 More Information

See `SETUP_GUIDE.md` for detailed instructions.

---

**Ready to go! Just run: `mvn spring-boot:run`** 🎉

