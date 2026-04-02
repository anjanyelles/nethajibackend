# Seed Data Documentation - Nethaji College Management System

## 🎉 **Seed Data Successfully Loaded!**

Your live Render database has been populated with sample data for testing and development.

---

## 📊 **Data Summary**

| Category | Count | Details |
|----------|-------|---------|
| **Programs** | 6 | BTECH, MTECH, BSC, MSC, BCOM, BBA |
| **Departments** | 9 | CSE, ECE, EEE, CIVIL (BTECH), CSE (MTECH), BSC, MSC, BCOM, BBA |
| **Semesters** | 16 | 8 semesters for CSE & ECE BTECH |
| **Courses** | 13 | Sample courses for CSE Semesters 1, 2, 3 |
| **Students** | 5 | Sample students in CSE program |
| **Lecturers** | 3 | Sample lecturers for CSE |
| **Admin** | 1 | admin@nethaji.com |
| **Lecturer Details** | 3 | Lecturer profile information |
| **Course Assignments** | 3 | Lecturers assigned to courses |

---

## 👥 **Test User Credentials**

### **Admin User**
- **Email:** `admin@nethaji.com`
- **Password:** `admin123`
- **Role:** ADMIN

### **Lecturer Accounts** (Password: `lecturer123` for all)
1. **Dr. Rajesh Kumar**
   - Email: `lecturer1@nethaji.com`
   - Subject: Computer Science
   - Course: Programming in C (CSE101)

2. **Dr. Priya Sharma**
   - Email: `lecturer2@nethaji.com`
   - Subject: Data Science
   - Course: Data Structures (CSE201)

3. **Prof. Suresh Reddy**
   - Email: `lecturer3@nethaji.com`
   - Subject: Computer Science
   - Course: Database Management Systems (CSE301)

### **Student Accounts** (Password: `student123` for all)
1. **Aarav Patel**
   - Email: `student1@nethaji.com`
   - Enrollment: CSE202401
   - Semester: 1

2. **Ananya Singh**
   - Email: `student2@nethaji.com`
   - Enrollment: CSE202402
   - Semester: 1

3. **Arjun Verma**
   - Email: `student3@nethaji.com`
   - Enrollment: CSE202403
   - Semester: 2

4. **Diya Gupta**
   - Email: `student4@nethaji.com`
   - Enrollment: CSE202404
   - Semester: 2

5. **Ishaan Mehta**
   - Email: `student5@nethaji.com`
   - Enrollment: CSE202405
   - Semester: 3

---

## 🎓 **Programs Seeded**

### **1. Bachelor of Technology (BTECH)**
- **Program Code:** BTECH
- **Duration:** 4 years (8 semesters)
- **Departments:**
  - Computer Science Engineering (CSE)
  - Electronics and Communication Engineering (ECE)
  - Electrical and Electronics Engineering (EEE)
  - Civil Engineering (CIVIL)

### **2. Master of Technology (MTECH)**
- **Program Code:** MTECH
- **Duration:** 2 years
- **Department:** Computer Science Engineering

### **3. Bachelor of Science (BSC)**
- **Program Code:** BSC
- **Duration:** 3 years

### **4. Master of Science (MSC)**
- **Program Code:** MSC
- **Duration:** 2 years

### **5. Bachelor of Commerce (BCOM)**
- **Program Code:** BCOM
- **Duration:** 3 years

### **6. Bachelor of Business Administration (BBA)**
- **Program Code:** BBA
- **Duration:** 3 years

---

## 📚 **Sample Courses (CSE BTECH)**

### **Semester 1**
1. **Programming in C** (CSE101) - 4 credits - Theory
2. **Mathematics-I** (MATH101) - 4 credits - Theory
3. **Physics** (PHY101) - 3 credits - Theory
4. **English** (ENG101) - 2 credits - Theory
5. **C Programming Lab** (CSE101L) - 2 credits - Lab

### **Semester 2**
1. **Data Structures** (CSE201) - 4 credits - Theory
2. **Mathematics-II** (MATH201) - 4 credits - Theory
3. **Digital Electronics** (ECE201) - 3 credits - Theory
4. **Data Structures Lab** (CSE201L) - 2 credits - Lab

### **Semester 3**
1. **Database Management Systems** (CSE301) - 4 credits - Theory
2. **Operating Systems** (CSE302) - 4 credits - Theory
3. **Computer Networks** (CSE303) - 3 credits - Theory
4. **DBMS Lab** (CSE301L) - 2 credits - Lab

---

## 🧪 **Testing the Seeded Data**

### **1. Test Admin Login**
```bash
curl -X POST https://nethaji-backend.onrender.com/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@nethaji.com",
    "password": "admin123"
  }'
```

### **2. Test Lecturer Login**
```bash
curl -X POST https://nethaji-backend.onrender.com/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "lecturer1@nethaji.com",
    "password": "lecturer123"
  }'
```

### **3. Test Student Login**
```bash
curl -X POST https://nethaji-backend.onrender.com/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student1@nethaji.com",
    "password": "student123"
  }'
```

### **4. Get All Programs**
```bash
curl -X GET https://nethaji-backend.onrender.com/api/nethaji-service/programs \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **5. Get Departments**
```bash
curl -X GET https://nethaji-backend.onrender.com/api/nethaji-service/departments \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### **6. Get Courses**
```bash
curl -X GET https://nethaji-backend.onrender.com/api/nethaji-service/courses \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 🔄 **Re-seeding Data**

If you need to re-seed the data:

```bash
cd "/Users/anjanyelle/Desktop/nethaji full code/nethajibackend/nethajibackend"

# Run the complete seed script
psql "postgresql://nethaji_user:PASSWORD@dpg-xxxxx.singapore-postgres.render.com:5432/nethaji" -f seed_data_fixed.sql

# Then run the additional data
psql "postgresql://nethaji_user:PASSWORD@dpg-xxxxx.singapore-postgres.render.com:5432/nethaji" -f seed_data_complete.sql
```

---

## 📝 **Seed Data Files**

1. **`seed_data_fixed.sql`** - Main seed data (programs, departments, courses, users, student info)
2. **`seed_data_complete.sql`** - Additional data (lecturer details, course assignments)
3. **`create_admin_production.sql`** - Admin user creation (already executed)

---

## 🎯 **What's Included**

### ✅ **Programs & Departments**
- All major degree programs (BTECH, MTECH, BSC, MSC, BCOM, BBA)
- Engineering departments (CSE, ECE, EEE, CIVIL)
- Science and commerce departments

### ✅ **Semesters**
- 8 semesters configured for CSE BTECH
- 8 semesters configured for ECE BTECH
- Semester dates set for 2024-2025 academic year

### ✅ **Courses**
- Core courses for CSE Semesters 1, 2, 3
- Mix of theory and lab courses
- Proper credit allocation

### ✅ **Users**
- 1 Admin user
- 3 Lecturer users with profiles
- 5 Student users with education info

### ✅ **Relationships**
- Students enrolled in programs
- Students assigned to semesters
- Lecturers assigned to courses
- Courses linked to department semesters

---

## 🚀 **Next Steps**

1. **Test all user logins** with the provided credentials
2. **Verify data** appears correctly in your frontend
3. **Add more data** as needed using the admin endpoints
4. **Create additional courses** for other semesters
5. **Add more students and lecturers** via API

---

## 📊 **Database Verification**

To verify the data in your database:

```sql
-- Check all tables
SELECT 'programs' as table_name, COUNT(*) FROM programs
UNION ALL
SELECT 'departments', COUNT(*) FROM department
UNION ALL
SELECT 'semesters', COUNT(*) FROM department_semesters
UNION ALL
SELECT 'courses', COUNT(*) FROM course
UNION ALL
SELECT 'users', COUNT(*) FROM users
UNION ALL
SELECT 'students', COUNT(*) FROM users WHERE user_type = 'STUDENT'
UNION ALL
SELECT 'lecturers', COUNT(*) FROM users WHERE user_type = 'LECTURER'
UNION ALL
SELECT 'student_education', COUNT(*) FROM student_education_info
UNION ALL
SELECT 'lecturer_details', COUNT(*) FROM lecture_details
UNION ALL
SELECT 'course_assignments', COUNT(*) FROM lecturer_course_assignments;
```

---

## 🎓 **Sample Data Use Cases**

### **For Students:**
- Login and view enrolled courses
- Check semester information
- View assigned lecturers
- Access course materials

### **For Lecturers:**
- Login and view assigned courses
- See enrolled students
- Manage course content
- Grade assignments

### **For Admin:**
- Manage all users
- Create new programs/departments
- Assign courses to lecturers
- Monitor system activity

---

**Your live database is now fully populated and ready for testing!** 🎉
