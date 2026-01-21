# API Testing with cURL Commands

## Base URL
```
http://localhost:9029/api/nethaji-service
```

---

## 🔐 Authentication APIs

### 1. User Registration
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/registration \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "password": "password123"
  }'
```

### 2. User Login (Email/Password)
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "password": "password123"
  }'
```

### 3. Student Login (Email or Enrollment)
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/student-login \
  -H "Content-Type: application/json" \
  -d '{
    "emailOrEnrollment": "student@example.com",
    "password": "password123"
  }'
```

### 4. Change Password
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student@example.com",
    "oldPassword": "oldpass123",
    "newPassword": "newpass123"
  }'
```

---

## 👥 User Management APIs

### 5. Get All Students (Paginated)
```bash
curl -X GET "http://localhost:9029/api/nethaji-service/auth/students?page=0&size=10&sortBy=firstName&sortDirection=ASC"
```

### 6. Get All Lecturers
```bash
curl -X GET http://localhost:9029/api/nethaji-service/auth/lecturers
```

### 7. Get Student Profile by ID
```bash
curl -X GET http://localhost:9029/api/nethaji-service/auth/getuserprofilebyid/{studentId}
```

### 8. Update Student Profile
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/saveuserprofile \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "student-uuid-here",
    "dateOfBirth": "2000-01-15",
    "gender": "MALE",
    "address": "123 Main St",
    "city": "City",
    "state": "State",
    "pincode": "123456",
    "guardianName": "Parent Name",
    "guardianContact": "1234567890"
  }'
```

---

## 📚 Academic Structure APIs

### 9. Create/Update Department
```bash
curl -X POST http://localhost:9029/api/nethaji-service/acadamic/adddepartments \
  -H "Content-Type: application/json" \
  -d '{
    "departmentCode": "CSE",
    "departmentName": "Computer Science Engineering",
    "programId": "program-uuid-here"
  }'
```

### 10. Get All Departments
```bash
curl -X GET http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
```

### 11. Create/Update Program
```bash
curl -X POST http://localhost:9029/api/nethaji-service/acadamic/addprograms \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bachelor of Technology",
    "programCode": "BTECH",
    "level": "UNDERGRADUATE",
    "durationYears": 4
  }'
```

### 12. Get All Programs
```bash
curl -X GET http://localhost:9029/api/nethaji-service/acadamic/getallprograms
```

### 13. Add Course
```bash
curl -X POST http://localhost:9029/api/nethaji-service/acadamic/addcourses \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Data Structures",
    "courseCode": "CS101",
    "description": "Introduction to Data Structures",
    "courseType": "CORE",
    "credits": 4,
    "departmentSemesterId": "semester-uuid-here"
  }'
```

### 14. Get All Courses
```bash
curl -X GET http://localhost:9029/api/nethaji-service/acadamic/all-courses
```

---

## ✅ Attendance APIs

### 15. Mark Individual Attendance
```bash
curl -X POST http://localhost:9029/api/nethaji-service/attendance/mark \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "student-uuid-here",
    "courseId": "course-uuid-here",
    "attendanceDate": "2024-12-15",
    "status": "PRESENT",
    "markedBy": "lecturer-uuid-here",
    "remarks": "On time"
  }'
```

### 16. Mark Bulk Attendance
```bash
curl -X POST http://localhost:9029/api/nethaji-service/attendance/mark-bulk \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "course-uuid-here",
    "attendanceDate": "2024-12-15",
    "markedBy": "lecturer-uuid-here",
    "students": [
      {
        "studentId": "student1-uuid",
        "status": "PRESENT",
        "remarks": ""
      },
      {
        "studentId": "student2-uuid",
        "status": "ABSENT",
        "remarks": "Sick leave"
      }
    ]
  }'
```

### 17. Get Attendance by Student
```bash
curl -X GET http://localhost:9029/api/nethaji-service/attendance/student/{studentId}
```

### 18. Get Attendance by Course
```bash
curl -X GET http://localhost:9029/api/nethaji-service/attendance/course/{courseId}
```

### 19. Get Attendance Report
```bash
curl -X GET http://localhost:9029/api/nethaji-service/attendance/report/student/{studentId}/course/{courseId}
```

### 20. Get Attendance by Date Range
```bash
curl -X GET "http://localhost:9029/api/nethaji-service/attendance/student/{studentId}/course/{courseId}/date-range?startDate=2024-12-01&endDate=2024-12-31"
```

---

## 📝 Marks APIs

### 21. Enter Marks
```bash
curl -X POST http://localhost:9029/api/nethaji-service/marks/enter \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "student-uuid-here",
    "courseId": "course-uuid-here",
    "examType": "INTERNAL",
    "marksObtained": 85.5,
    "maxMarks": 100.0,
    "examDate": "2024-12-10",
    "evaluatedBy": "lecturer-uuid-here",
    "remarks": "Good performance"
  }'
```

### 22. Enter Bulk Marks
```bash
curl -X POST http://localhost:9029/api/nethaji-service/marks/enter-bulk \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "course-uuid-here",
    "examType": "MIDTERM",
    "maxMarks": 100.0,
    "examDate": "2024-12-10",
    "evaluatedBy": "lecturer-uuid-here",
    "students": [
      {
        "studentId": "student1-uuid",
        "marksObtained": 85.5,
        "remarks": "Excellent"
      },
      {
        "studentId": "student2-uuid",
        "marksObtained": 72.0,
        "remarks": "Good"
      }
    ]
  }'
```

### 23. Get Marks by Student
```bash
curl -X GET http://localhost:9029/api/nethaji-service/marks/student/{studentId}
```

### 24. Get Marks by Course
```bash
curl -X GET http://localhost:9029/api/nethaji-service/marks/course/{courseId}
```

### 25. Get Marks by Student and Course
```bash
curl -X GET http://localhost:9029/api/nethaji-service/marks/student/{studentId}/course/{courseId}
```

---

## 🎓 Grades APIs

### 26. Calculate Grade for a Course
```bash
curl -X POST "http://localhost:9029/api/nethaji-service/grades/calculate?studentId={studentId}&courseId={courseId}&semesterId={semesterId}"
```

### 27. Calculate Grades for Semester
```bash
curl -X POST "http://localhost:9029/api/nethaji-service/grades/calculate-semester?studentId={studentId}&semesterId={semesterId}"
```

### 28. Get All Grades by Student
```bash
curl -X GET http://localhost:9029/api/nethaji-service/grades/student/{studentId}
```

### 29. Get Grades by Student and Semester
```bash
curl -X GET http://localhost:9029/api/nethaji-service/grades/student/{studentId}/semester/{semesterId}
```

### 30. Get Transcript
```bash
curl -X GET http://localhost:9029/api/nethaji-service/grades/transcript/student/{studentId}/semester/{semesterId}
```

### 31. Get SGPA
```bash
curl -X GET http://localhost:9029/api/nethaji-service/grades/sgpa/student/{studentId}/semester/{semesterId}
```

### 32. Get CGPA
```bash
curl -X GET http://localhost:9029/api/nethaji-service/grades/cgpa/student/{studentId}
```

---

## 📄 Assignment APIs

### 33. Create Assignment
```bash
curl -X POST http://localhost:9029/api/nethaji-service/assignments/create \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "course-uuid-here",
    "title": "Assignment 1: Data Structures",
    "description": "Implement binary search tree",
    "dueDate": "2024-12-20",
    "maxMarks": 100.0,
    "createdBy": "lecturer-uuid-here",
    "fileUrl": "https://example.com/assignment.pdf"
  }'
```

### 34. Get Assignments by Course
```bash
curl -X GET http://localhost:9029/api/nethaji-service/assignments/course/{courseId}
```

### 35. Submit Assignment
```bash
curl -X POST http://localhost:9029/api/nethaji-service/assignments/{assignmentId}/submit/student/{studentId} \
  -H "Content-Type: application/json" \
  -d '{
    "fileUrl": "https://example.com/submission.pdf"
  }'
```

### 36. Grade Assignment
```bash
curl -X PUT http://localhost:9029/api/nethaji-service/assignments/grade/{studentAssignmentId} \
  -H "Content-Type: application/json" \
  -d '{
    "marksObtained": 85.0,
    "feedback": "Well done! Good implementation.",
    "gradedBy": "lecturer-uuid-here"
  }'
```

### 37. Get Student Assignments
```bash
curl -X GET http://localhost:9029/api/nethaji-service/assignments/student/{studentId}
```

### 38. Get Submissions for Assignment
```bash
curl -X GET http://localhost:9029/api/nethaji-service/assignments/{assignmentId}/submissions
```

---

## 🏥 Health Check

### 39. Health Check
```bash
curl -X GET http://localhost:9029/api/nethaji-service/health
```

---

## 📋 Notes

1. **Replace placeholders**: Replace `{studentId}`, `{courseId}`, etc. with actual UUIDs
2. **Authentication**: Some endpoints may require JWT tokens (add `-H "Authorization: Bearer {token}"`)
3. **Date Format**: Use `YYYY-MM-DD` format for dates
4. **UUID Format**: Use valid UUID format (e.g., `550e8400-e29b-41d4-a716-446655440000`)

---

## 🔑 Common Headers

For authenticated requests, add:
```bash
-H "Authorization: Bearer {your-jwt-token}"
```

For JSON requests:
```bash
-H "Content-Type: application/json"
```

---

## 🧪 Quick Test Sequence

1. **Health Check**:
```bash
curl http://localhost:9029/api/nethaji-service/health
```

2. **Get All Departments**:
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
```

3. **Get All Programs**:
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getallprograms
```

---

**Happy Testing! 🚀**

