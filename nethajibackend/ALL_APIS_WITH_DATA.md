# Complete API Testing Guide - All Endpoints with Sample Data

## Base URL
```
http://localhost:9029/api/nethaji-service
```

## Sample UUIDs (Use these in your tests).  
```
Programs:
- BTECH: 550e8400-e29b-41d4-a716-446655440001
- MTECH: 550e8400-e29b-41d4-a716-446655440002
- BSC: 550e8400-e29b-41d4-a716-446655440003

Departments:
- CSE: 550e8400-e29b-41d4-a716-446655440011
- ECE: 550e8400-e29b-41d4-a716-446655440012
- ME: 550e8400-e29b-41d4-a716-446655440013

Users:
- Admin: 550e8400-e29b-41d4-a716-446655440101
- Lecturer: 550e8400-e29b-41d4-a716-446655440201
- Student 1: 550e8400-e29b-41d4-a716-446655440301
- Student 2: 550e8400-e29b-41d4-a716-446655440302
- Student 3: 550e8400-e29b-41d4-a716-446655440303

Courses:
- CS101: 550e8400-e29b-41d4-a716-446655440701
- CS102: 550e8400-e29b-41d4-a716-446655440702
```

---

## 🔐 Authentication APIs

### 1. User Registration
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/registration \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newstudent@nethaji.edu",
    "password": "password123"
  }'
```

### 2. User Login (Email/Password)
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@nethaji.edu",
    "password": "admin123"
  }'
```

### 3. Student Login (Email or Enrollment)
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/student-login \
  -H "Content-Type: application/json" \
  -d '{
    "emailOrEnrollment": "student1@nethaji.edu",
    "password": "admin123"
  }'
```

### 4. Change Password
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/change-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "student1@nethaji.edu",
    "oldPassword": "admin123",
    "newPassword": "newpassword123"
  }'
```

### 5. Create User (Admin/Principal)
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/admin-or-principal/create-user \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newlecturer@nethaji.edu",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "mobileNumber": "9876543210",
    "userType": "LECTURER"
  }'
```

### 6. Get All Students (Paginated)
```bash
curl "http://localhost:9029/api/nethaji-service/auth/students?page=0&size=10&sortBy=firstName&sortDirection=ASC"
```

### 7. Get All Students (Simple)
```bash
curl http://localhost:9029/api/nethaji-service/auth/all-students
```

### 8. Get All Lecturers
```bash
curl http://localhost:9029/api/nethaji-service/auth/lecturers
```

### 9. Get Active Students by Program and Semester
```bash
curl "http://localhost:9029/api/nethaji-service/auth/active-students-semester-programmed?programId=550e8400-e29b-41d4-a716-446655440001&semester=1"
```

### 10. Update Student Program
```bash
curl -X PATCH http://localhost:9029/api/nethaji-service/auth/550e8400-e29b-41d4-a716-446655440301/program/550e8400-e29b-41d4-a716-446655440001
```

### 11. Update Password by Admin
```bash
curl -X PATCH "http://localhost:9029/api/nethaji-service/auth/update-password-by-admin?studentId=550e8400-e29b-41d4-a716-446655440301&password=newpassword123"
```

### 12. Activate/Deactivate Student
```bash
curl -X PATCH "http://localhost:9029/api/nethaji-service/auth/active-inactive-student?studentId=550e8400-e29b-41d4-a716-446655440301&status=true"
```

### 13. Get All Lectures
```bash
curl http://localhost:9029/api/nethaji-service/auth/all-lectures
```

### 14. Get Lecturers and Assistants by Course
```bash
curl http://localhost:9029/api/nethaji-service/auth/lecturers-and-assistants-courses
```

---

## 📚 Academic Structure APIs

### 15. Create/Update Department
```bash
curl -X POST http://localhost:9029/api/nethaji-service/acadamic/adddepartments \
  -H "Content-Type: application/json" \
  -d '{
    "departmentCode": "ME",
    "departmentName": "Mechanical Engineering",
    "programId": "550e8400-e29b-41d4-a716-446655440001"
  }'
```

### 16. Get All Departments
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
```

### 17. Get Department by ID
```bash
curl "http://localhost:9029/api/nethaji-service/acadamic/getalldepartments?id=550e8400-e29b-41d4-a716-446655440011"
```

### 18. Create/Update Program
```bash
curl -X POST http://localhost:9029/api/nethaji-service/acadamic/addprograms \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bachelor of Commerce",
    "programCode": "BCOM",
    "level": "BCOM",
    "durationYears": 3
  }'
```

### 19. Get All Programs
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/getallprograms
```

### 20. Get Program by ID
```bash
curl "http://localhost:9029/api/nethaji-service/acadamic/getallprograms?id=550e8400-e29b-41d4-a716-446655440001"
```

### 21. Add Semesters
```bash
curl -X POST http://localhost:9029/api/nethaji-service/acadamic/addSemesters \
  -H "Content-Type: application/json" \
  -d '{
    "departmentId": "550e8400-e29b-41d4-a716-446655440011",
    "semesterNumber": 4,
    "isActive": true
  }'
```

### 22. Create/Update Course
```bash
curl -X POST http://localhost:9029/api/nethaji-service/acadamic/addcourses \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Computer Networks",
    "courseCode": "CS401",
    "description": "Introduction to computer networks",
    "courseType": "THEORY",
    "credits": 4,
    "departmentSemesterId": "550e8400-e29b-41d4-a716-446655440603",
    "isElective": false
  }'
```

### 23. Get All Courses
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/all-courses
```

### 24. Get Course by ID
```bash
curl "http://localhost:9029/api/nethaji-service/acadamic/all-courses?id=550e8400-e29b-41d4-a716-446655440701"
```

### 25. Get Program Hierarchy
```bash
curl http://localhost:9029/api/nethaji-service/acadamic/all-courses-info
```

### 26. Get Program Details by Level
```bash
curl "http://localhost:9029/api/nethaji-service/acadamic/program/details?programLevel=BTECH"
```

### 27. Get Department and Semester Subjects Info
```bash
curl "http://localhost:9029/api/nethaji-service/acadamic/getDepartMentAndSemesterSubjectsInfo?departMentType=CSE&semester=1"
```

---

## ✅ Attendance APIs

### 28. Mark Individual Attendance
```bash
curl -X POST http://localhost:9029/api/nethaji-service/attendance/mark \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "550e8400-e29b-41d4-a716-446655440301",
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "attendanceDate": "2024-12-15",
    "status": "PRESENT",
    "markedBy": "550e8400-e29b-41d4-a716-446655440201",
    "remarks": "On time"
  }'
```

### 29. Mark Bulk Attendance
```bash
curl -X POST http://localhost:9029/api/nethaji-service/attendance/mark-bulk \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "attendanceDate": "2024-12-15",
    "markedBy": "550e8400-e29b-41d4-a716-446655440201",
    "students": [
      {
        "studentId": "550e8400-e29b-41d4-a716-446655440301",
        "status": "PRESENT",
        "remarks": ""
      },
      {
        "studentId": "550e8400-e29b-41d4-a716-446655440302",
        "status": "ABSENT",
        "remarks": "Sick leave"
      },
      {
        "studentId": "550e8400-e29b-41d4-a716-446655440303",
        "status": "LATE",
        "remarks": "Traffic"
      }
    ]
  }'
```

### 30. Get Attendance by Student
```bash
curl http://localhost:9029/api/nethaji-service/attendance/student/550e8400-e29b-41d4-a716-446655440301
```

### 31. Get Attendance by Course
```bash
curl http://localhost:9029/api/nethaji-service/attendance/course/550e8400-e29b-41d4-a716-446655440701
```

### 32. Get Attendance by Student and Course
```bash
curl http://localhost:9029/api/nethaji-service/attendance/student/550e8400-e29b-41d4-a716-446655440301/course/550e8400-e29b-41d4-a716-446655440701
```

### 33. Get Attendance by Date Range
```bash
curl "http://localhost:9029/api/nethaji-service/attendance/student/550e8400-e29b-41d4-a716-446655440301/course/550e8400-e29b-41d4-a716-446655440701/date-range?startDate=2024-12-01&endDate=2024-12-31"
```

### 34. Get Attendance Report (Student + Course)
```bash
curl http://localhost:9029/api/nethaji-service/attendance/report/student/550e8400-e29b-41d4-a716-446655440301/course/550e8400-e29b-41d4-a716-446655440701
```

### 35. Get Attendance Report by Course
```bash
curl http://localhost:9029/api/nethaji-service/attendance/report/course/550e8400-e29b-41d4-a716-446655440701
```

### 36. Update Attendance
```bash
curl -X PUT http://localhost:9029/api/nethaji-service/attendance/550e8400-e29b-41d4-a716-446655441101 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "EXCUSED",
    "remarks": "Medical certificate provided",
    "attendanceDate": "2024-12-15"
  }'
```

### 37. Delete Attendance
```bash
curl -X DELETE http://localhost:9029/api/nethaji-service/attendance/550e8400-e29b-41d4-a716-446655441101
```

---

## 📝 Marks APIs

### 38. Enter Marks
```bash
curl -X POST http://localhost:9029/api/nethaji-service/marks/enter \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "550e8400-e29b-41d4-a716-446655440301",
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "examType": "INTERNAL",
    "marksObtained": 85.5,
    "maxMarks": 100.0,
    "examDate": "2024-12-10",
    "evaluatedBy": "550e8400-e29b-41d4-a716-446655440201",
    "remarks": "Good performance"
  }'
```

### 39. Enter Bulk Marks
```bash
curl -X POST http://localhost:9029/api/nethaji-service/marks/enter-bulk \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "examType": "MIDTERM",
    "maxMarks": 100.0,
    "examDate": "2024-12-10",
    "evaluatedBy": "550e8400-e29b-41d4-a716-446655440201",
    "students": [
      {
        "studentId": "550e8400-e29b-41d4-a716-446655440301",
        "marksObtained": 85.5,
        "remarks": "Excellent"
      },
      {
        "studentId": "550e8400-e29b-41d4-a716-446655440302",
        "marksObtained": 92.0,
        "remarks": "Outstanding"
      },
      {
        "studentId": "550e8400-e29b-41d4-a716-446655440303",
        "marksObtained": 78.0,
        "remarks": "Can improve"
      }
    ]
  }'
```

### 40. Get Marks by Student
```bash
curl http://localhost:9029/api/nethaji-service/marks/student/550e8400-e29b-41d4-a716-446655440301
```

### 41. Get Marks by Course
```bash
curl http://localhost:9029/api/nethaji-service/marks/course/550e8400-e29b-41d4-a716-446655440701
```

### 42. Get Marks by Student and Course
```bash
curl http://localhost:9029/api/nethaji-service/marks/student/550e8400-e29b-41d4-a716-446655440301/course/550e8400-e29b-41d4-a716-446655440701
```

### 43. Update Marks
```bash
curl -X PUT http://localhost:9029/api/nethaji-service/marks/550e8400-e29b-41d4-a716-446655441201 \
  -H "Content-Type: application/json" \
  -d '{
    "marksObtained": 88.0,
    "maxMarks": 100.0,
    "examDate": "2024-12-10",
    "remarks": "Updated after review"
  }'
```

### 44. Delete Marks
```bash
curl -X DELETE http://localhost:9029/api/nethaji-service/marks/550e8400-e29b-41d4-a716-446655441201
```

---

## 🎓 Grades APIs

### 45. Calculate Grade for a Course
```bash
curl -X POST "http://localhost:9029/api/nethaji-service/grades/calculate?studentId=550e8400-e29b-41d4-a716-446655440301&courseId=550e8400-e29b-41d4-a716-446655440701&semesterId=550e8400-e29b-41d4-a716-446655440601"
```

### 46. Calculate Grades for Entire Semester
```bash
curl -X POST "http://localhost:9029/api/nethaji-service/grades/calculate-semester?studentId=550e8400-e29b-41d4-a716-446655440301&semesterId=550e8400-e29b-41d4-a716-446655440601"
```

### 47. Get All Grades by Student
```bash
curl http://localhost:9029/api/nethaji-service/grades/student/550e8400-e29b-41d4-a716-446655440301
```

### 48. Get Grades by Student and Semester
```bash
curl http://localhost:9029/api/nethaji-service/grades/student/550e8400-e29b-41d4-a716-446655440301/semester/550e8400-e29b-41d4-a716-446655440601
```

### 49. Get Transcript
```bash
curl http://localhost:9029/api/nethaji-service/grades/transcript/student/550e8400-e29b-41d4-a716-446655440301/semester/550e8400-e29b-41d4-a716-446655440601
```

### 50. Get SGPA (Semester GPA)
```bash
curl http://localhost:9029/api/nethaji-service/grades/sgpa/student/550e8400-e29b-41d4-a716-446655440301/semester/550e8400-e29b-41d4-a716-446655440601
```

### 51. Get CGPA (Cumulative GPA)
```bash
curl http://localhost:9029/api/nethaji-service/grades/cgpa/student/550e8400-e29b-41d4-a716-446655440301
```

### 52. Update Grade
```bash
curl -X PUT http://localhost:9029/api/nethaji-service/grades/550e8400-e29b-41d4-a716-446655441301 \
  -H "Content-Type: application/json" \
  -d '{
    "totalMarks": 88.5,
    "grade": "A_PLUS",
    "gradePoint": 9.0,
    "percentage": 88.5
  }'
```

---

## 📄 Assignment APIs

### 53. Create Assignment
```bash
curl -X POST http://localhost:9029/api/nethaji-service/assignments/create \
  -H "Content-Type: application/json" \
  -d '{
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "title": "Assignment 2: Linked Lists",
    "description": "Implement singly and doubly linked lists with all operations",
    "dueDate": "2024-12-25",
    "maxMarks": 100.0,
    "createdBy": "550e8400-e29b-41d4-a716-446655440201",
    "fileUrl": "https://example.com/assignments/assignment2.pdf"
  }'
```

### 54. Update Assignment
```bash
curl -X PUT http://localhost:9029/api/nethaji-service/assignments/550e8400-e29b-41d4-a716-446655441301 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Assignment 1: Array Operations (Updated)",
    "description": "Updated description",
    "dueDate": "2024-12-22",
    "maxMarks": 100.0,
    "isActive": true
  }'
```

### 55. Get Assignments by Course
```bash
curl http://localhost:9029/api/nethaji-service/assignments/course/550e8400-e29b-41d4-a716-446655440701
```

### 56. Get Assignment by ID
```bash
curl http://localhost:9029/api/nethaji-service/assignments/550e8400-e29b-41d4-a716-446655441301
```

### 57. Delete Assignment (Soft Delete)
```bash
curl -X DELETE http://localhost:9029/api/nethaji-service/assignments/550e8400-e29b-41d4-a716-446655441301
```

### 58. Submit Assignment
```bash
curl -X POST http://localhost:9029/api/nethaji-service/assignments/550e8400-e29b-41d4-a716-446655441301/submit/student/550e8400-e29b-41d4-a716-446655440301 \
  -H "Content-Type: application/json" \
  -d '{
    "fileUrl": "https://example.com/submissions/student1_assignment1.pdf"
  }'
```

### 59. Grade Assignment
```bash
curl -X PUT http://localhost:9029/api/nethaji-service/assignments/grade/550e8400-e29b-41d4-a716-446655441401 \
  -H "Content-Type: application/json" \
  -d '{
    "marksObtained": 90.0,
    "feedback": "Excellent work! Very well implemented.",
    "gradedBy": "550e8400-e29b-41d4-a716-446655440201"
  }'
```

### 60. Get Student Assignments
```bash
curl http://localhost:9029/api/nethaji-service/assignments/student/550e8400-e29b-41d4-a716-446655440301
```

### 61. Get Submissions for Assignment
```bash
curl http://localhost:9029/api/nethaji-service/assignments/550e8400-e29b-41d4-a716-446655441301/submissions
```

### 62. Get Specific Student Assignment
```bash
curl http://localhost:9029/api/nethaji-service/assignments/550e8400-e29b-41d4-a716-446655441301/student/550e8400-e29b-41d4-a716-446655440301
```

---

## 👥 Student Profile APIs

### 63. Save/Update Student Profile
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/saveuserprofile \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "550e8400-e29b-41d4-a716-446655440301",
    "dateOfBirth": "2005-05-15",
    "gender": "MALE",
    "address": "123 Main Street",
    "city": "Bangalore",
    "state": "Karnataka",
    "pincode": "560001",
    "guardianName": "Ramesh Singh",
    "guardianContact": "9876543401",
    "guardianRelation": "Father",
    "bloodGroup": "O+",
    "tenthPercentage": 85.5,
    "tenthBoard": "CBSE",
    "tenthYearOfPassing": 2022,
    "twelfthPercentage": 88.0,
    "twelfthBoard": "CBSE",
    "twelfthYearOfPassing": 2024
  }'
```

### 64. Get Student Profile by ID
```bash
curl http://localhost:9029/api/nethaji-service/auth/getuserprofilebyid/550e8400-e29b-41d4-a716-446655440301
```

### 65. Get Students by Branch and Semester
```bash
curl http://localhost:9029/api/nethaji-service/auth/getAllStudentsInfoNew/550e8400-e29b-41d4-a716-446655440001/1
```

### 66. Get Students by Section and Semester
```bash
curl http://localhost:9029/api/nethaji-service/auth/getSectionAndSemesterStudents/A/1
```

---

## 👨‍🏫 Staff Profile APIs

### 67. Save/Update Staff Profile
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/saveStaffProfile \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "550e8400-e29b-41d4-a716-446655440201",
    "firstName": "Rajesh",
    "lastName": "Sharma",
    "department": "CSE",
    "designation": "Professor",
    "qualification": "Ph.D. in Computer Science",
    "joiningDate": "2018-01-15",
    "experienceYears": 15,
    "phoneNumber": "9876543220",
    "email": "prof.sharma@nethaji.edu",
    "employmentType": "FULL_TIME",
    "salary": 120000.00,
    "emergencyContactName": "Rekha Sharma",
    "emergencyContactPhone": "9876543220",
    "status": "ACTIVE",
    "address": "101 Faculty Quarters",
    "dateOfBirth": "1975-03-20",
    "gender": "MALE",
    "employeeSubject": "Data Structures"
  }'
```

### 68. Get Staff Profile by ID
```bash
curl http://localhost:9029/api/nethaji-service/auth/getStaffProfileById/550e8400-e29b-41d4-a716-446655440501
```

### 69. Get All Staff Profiles
```bash
curl http://localhost:9029/api/nethaji-service/auth/getListofStaffDetails
```

---

## 🗓️ Timetable APIs

### 70. Set Timetable (Syllabus/Exam/Holidays)
```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/setTimeTablesForSylExamHoly \
  -H "Content-Type: application/json" \
  -d '{
    "timeTableType": "EXAMS",
    "departmentId": "550e8400-e29b-41d4-a716-446655440011",
    "semesterId": "550e8400-e29b-41d4-a716-446655440601",
    "examSubject": "Data Structures",
    "examTime": "10:00 AM - 1:00 PM",
    "examType": "MIDTERM",
    "examCenter": "Main Hall",
    "exameDate": "2024-12-20"
  }'
```

---

## 🏥 Health Check

### 71. Health Check
```bash
curl http://localhost:9029/api/nethaji-service/health
```

---

## 🌱 Data Seeding API

### 72. Seed Sample Data
```bash
curl -X POST http://localhost:9029/api/nethaji-service/admin/seed-data
```

This will populate the database with:
- Programs (BTECH, MTECH)
- Departments (CSE, ECE)
- Users (Admin, Lecturers, Students)
- Courses
- Sample Attendance, Marks, Assignments

---

## 📋 Quick Test Sequence

Run these in order to test the complete flow:

```bash
# 1. Health Check
curl http://localhost:9029/api/nethaji-service/health

# 2. Seed Data (if not already loaded)
curl -X POST http://localhost:9029/api/nethaji-service/admin/seed-data

# 3. Get Departments
curl http://localhost:9029/api/nethaji-service/acadamic/getalldepartments

# 4. Get Programs
curl http://localhost:9029/api/nethaji-service/acadamic/getallprograms

# 5. Get Students
curl http://localhost:9029/api/nethaji-service/auth/students?page=0&size=10

# 6. Get Courses
curl http://localhost:9029/api/nethaji-service/acadamic/all-courses

# 7. Mark Attendance
curl -X POST http://localhost:9029/api/nethaji-service/attendance/mark \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "550e8400-e29b-41d4-a716-446655440301",
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "attendanceDate": "2024-12-15",
    "status": "PRESENT",
    "markedBy": "550e8400-e29b-41d4-a716-446655440201"
  }'

# 8. Enter Marks
curl -X POST http://localhost:9029/api/nethaji-service/marks/enter \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "550e8400-e29b-41d4-a716-446655440301",
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "examType": "INTERNAL",
    "marksObtained": 85.5,
    "maxMarks": 100.0,
    "examDate": "2024-12-10",
    "evaluatedBy": "550e8400-e29b-41d4-a716-446655440201"
  }'

# 9. Calculate Grade
curl -X POST "http://localhost:9029/api/nethaji-service/grades/calculate?studentId=550e8400-e29b-41d4-a716-446655440301&courseId=550e8400-e29b-41d4-a716-446655440701&semesterId=550e8400-e29b-41d4-a716-446655440601"

# 10. Get Transcript
curl http://localhost:9029/api/nethaji-service/grades/transcript/student/550e8400-e29b-41d4-a716-446655440301/semester/550e8400-e29b-41d4-a716-446655440601
```

---

## 🔑 Test Credentials

**All users have password**: `admin123`

- **Super Admin**: `admin@nethaji.edu` / `admin123`
- **Lecturer**: `prof.sharma@nethaji.edu` / `admin123`
- **Student 1**: `student1@nethaji.edu` / `admin123`
- **Student 2**: `student2@nethaji.edu` / `admin123`

---

## 📝 Notes

1. **Replace UUIDs**: Use actual UUIDs from your database responses
2. **Date Format**: Use `YYYY-MM-DD` format for dates
3. **Enum Values**: 
   - AttendanceStatus: `PRESENT`, `ABSENT`, `LATE`, `EXCUSED`
   - ExamType: `INTERNAL`, `MIDTERM`, `FINAL`, `ASSIGNMENT`, `QUIZ`
   - CourseType: `THEORY`, `THEORY_LAB`, `LAB`
   - ProgramLevel: `BTECH`, `MTECH`, `BSC`, `MSC`, `BCOM`, `BBA`

---

**Happy Testing! 🚀**

