# Test Endpoints - Quick Reference

## ✅ Health Check (Fixed)
```bash
curl http://localhost:9029/api/nethaji-service/health
```

Expected Response:
```json
"OK"
```

---

## 🔐 Authentication Endpoints

### Get All Students
```bash
curl http://localhost:9029/api/nethaji-service/auth/students?page=0&size=10
```

### Get All Lecturers
```bash
curl http://localhost:9029/api/nethaji-service/auth/lecturers
```

### Get All Students (Simple)
```bash
curl http://localhost:9029/api/nethaji-service/auth/all-students
```

---

## 📚 Academic Structure Endpoints

### Get All Departments
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
```

### Get All Programs
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getallprograms
```

### Get All Courses
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/all-courses
```

---

## ✅ Attendance Endpoints

### Get Attendance by Student
```bash
curl http://localhost:9029/api/nethaji-service/attendance/student/{studentId}
```

### Get Attendance by Course
```bash
curl http://localhost:9029/api/nethaji-service/attendance/course/{courseId}
```

---

## 📝 Marks Endpoints

### Get Marks by Student
```bash
curl http://localhost:9029/api/nethaji-service/marks/student/{studentId}
```

### Get Marks by Course
```bash
curl http://localhost:9029/api/nethaji-service/marks/course/{courseId}
```

---

## 🎓 Grades Endpoints

### Get Grades by Student
```bash
curl http://localhost:9029/api/nethaji-service/grades/student/{studentId}
```

### Get CGPA
```bash
curl http://localhost:9029/api/nethaji-service/grades/cgpa/student/{studentId}
```

---

## 📄 Assignment Endpoints

### Get Assignments by Course
```bash
curl http://localhost:9029/api/nethaji-service/assignments/course/{courseId}
```

### Get Student Assignments
```bash
curl http://localhost:9029/api/nethaji-service/assignments/student/{studentId}
```

---

## 🧪 Quick Test Sequence

1. **Health Check**:
```bash
curl http://localhost:9029/api/nethaji-service/health
```

2. **Get Departments**:
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
```

3. **Get Programs**:
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getallprograms
```

---

**Note**: Replace `{studentId}`, `{courseId}`, etc. with actual UUIDs from your database.

