# Data Seeding Guide

## Overview
This guide explains how to populate the database with sample/test data for testing and API integration.

---

## Method 1: Automatic Data Seeding (Recommended)

The application is configured to automatically load sample data from `data.sql` when it starts.

### Steps:

1. **Restart the application**:
   ```bash
   cd /Users/anjanyelle/Desktop/nethajifullstack/nethajibackend
   mvn spring-boot:run
   ```

2. **Data will be automatically loaded** when the application starts.

3. **Verify data**:
   ```bash
   # Check departments
   curl http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
   
   # Check programs
   curl http://localhost:9029/api/nethaji-service/acadamic/getallprograms
   
   # Check students
   curl http://localhost:9029/api/nethaji-service/auth/students?page=0&size=10
   ```

---

## Method 2: Manual Data Seeding via API

Use the seed endpoint to populate data:

```bash
curl -X POST http://localhost:9029/api/nethaji-service/admin/seed-data
```

This will create:
- Programs (BTECH, MTECH)
- Departments (CSE, ECE)
- Users (1 Admin, 1 Lecturer, 5 Students)
- Courses (CS101, CS102, etc.)
- Sample Attendance Records
- Sample Marks
- Sample Assignments

---

## Sample Data Included

### Users Created:

**Super Admin:**
- Email: `admin@nethaji.edu`
- Password: `admin123` (hashed)
- Enrollment: `ADMIN001`

**Lecturer:**
- Email: `prof.sharma@nethaji.edu`
- Password: `admin123` (hashed)
- Enrollment: `LEC001`

**Students:**
- `student1@nethaji.edu` - STU2024001
- `student2@nethaji.edu` - STU2024002
- `student3@nethaji.edu` - STU2024003
- `student4@nethaji.edu` - STU2024004
- `student5@nethaji.edu` - STU2024005

**Default Password for all users:** `admin123`

### Programs:
- Bachelor of Technology (BTECH)
- Master of Technology (MTECH)

### Departments:
- Computer Science Engineering (CSE)
- Electronics and Communication Engineering (ECE)

### Courses:
- CS101 - Data Structures and Algorithms
- CS102 - Database Management Systems
- CS201 - Object Oriented Programming
- CS301 - Operating Systems
- EC101 - Digital Electronics

### Sample Records:
- 5 Attendance records
- 5 Marks entries
- 2 Assignments
- 2 Student assignment submissions

---

## Test Credentials

### Login as Admin:
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@nethaji.edu",
    "password": "admin123"
  }'
```

### Login as Student:
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/student-login \
  -H "Content-Type: application/json" \
  -d '{
    "emailOrEnrollment": "student1@nethaji.edu",
    "password": "admin123"
  }'
```

### Login as Lecturer:
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "prof.sharma@nethaji.edu",
    "password": "admin123"
  }'
```

---

## Testing APIs with Sample Data

### Get All Departments:
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
```

### Get All Programs:
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getallprograms
```

### Get All Courses:
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/all-courses
```

### Get All Students:
```bash
curl http://localhost:9029/api/nethaji-service/auth/students?page=0&size=10
```

### Get Attendance for Student:
```bash
curl http://localhost:9029/api/nethaji-service/attendance/student/550e8400-e29b-41d4-a716-446655440301
```

### Get Marks for Student:
```bash
curl http://localhost:9029/api/nethaji-service/marks/student/550e8400-e29b-41d4-a716-446655440301
```

### Get Assignments for Course:
```bash
curl http://localhost:9029/api/nethaji-service/assignments/course/550e8400-e29b-41d4-a716-446655440701
```

---

## UUIDs Reference

**Programs:**
- BTECH: `550e8400-e29b-41d4-a716-446655440001`
- MTECH: `550e8400-e29b-41d4-a716-446655440002`

**Departments:**
- CSE: `550e8400-e29b-41d4-a716-446655440011`
- ECE: `550e8400-e29b-41d4-a716-446655440012`

**Users:**
- Admin: `550e8400-e29b-41d4-a716-446655440101`
- Lecturer: `550e8400-e29b-41d4-a716-446655440201`
- Student 1: `550e8400-e29b-41d4-a716-446655440301`
- Student 2: `550e8400-e29b-41d4-a716-446655440302`
- Student 3: `550e8400-e29b-41d4-a716-446655440303`

**Courses:**
- CS101: `550e8400-e29b-41d4-a716-446655440701`
- CS102: `550e8400-e29b-41d4-a716-446655440702`

---

## Clear and Reseed Data

If you want to clear existing data and reseed:

1. **Stop the application**

2. **Clear database** (optional):
   ```sql
   -- Connect to database
   psql -U nethaji_user -d nethaji
   
   -- Drop and recreate (WARNING: This deletes all data)
   DROP SCHEMA public CASCADE;
   CREATE SCHEMA public;
   ```

3. **Restart application** - Data will be automatically seeded

---

## Troubleshooting

### Data not loading?
- Check `application.yml` has `spring.sql.init.mode: always`
- Check `data.sql` file exists in `src/main/resources/`
- Check application logs for SQL errors

### Duplicate key errors?
- Data already exists - this is normal
- Use the seed API endpoint instead: `/api/nethaji-service/admin/seed-data`

### Want more data?
- Edit `data.sql` file to add more records
- Or use the seed API endpoint multiple times (it checks for existing data)

---

**Happy Testing! 🚀**

