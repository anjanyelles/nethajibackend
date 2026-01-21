# API Integration Guide

## Overview
This guide explains how to use the API services in the frontend application.

## Base Configuration

The API base URL is configured in `src/services/api.js`:
- **Development**: `http://localhost:9029/api/nethaji-service/`
- **Production**: `https://api.nethajidcs.com/api/nethaji-service/`

## Available Services

### 1. Authentication Service (`authService.js`)
```javascript
import { loginWithEmailPassword, getAllStudents, createUser } from '@/services/authService';

// Login
const loginData = await loginWithEmailPassword(email, password);

// Get all students
const students = await getAllStudents(0, 10);

// Create user (admin)
const newUser = await createUser(userData);
```

### 2. Academic Service (`academicService.js`)
```javascript
import { getAllDepartments, getAllPrograms, createOrUpdateCourse } from '@/services/academicService';

// Get departments
const departments = await getAllDepartments();

// Get programs
const programs = await getAllPrograms();

// Create course
const course = await createOrUpdateCourse({
  name: "Data Structures",
  courseCode: "CS101",
  courseType: "THEORY",
  credits: 4,
  departmentSemesterId: "uuid-here"
});
```

### 3. Attendance Service (`attendanceService.js`)
```javascript
import { markAttendance, getAttendanceByStudent } from '@/services/attendanceService';

// Mark attendance
await markAttendance({
  studentId: "uuid",
  courseId: "uuid",
  attendanceDate: "2024-12-15",
  status: "PRESENT",
  markedBy: "uuid"
});

// Get student attendance
const attendance = await getAttendanceByStudent(studentId);
```

### 4. Marks Service (`marksService.js`)
```javascript
import { enterMarks, getMarksByStudent } from '@/services/marksService';

// Enter marks
await enterMarks({
  studentId: "uuid",
  courseId: "uuid",
  examType: "INTERNAL",
  marksObtained: 85.5,
  maxMarks: 100.0,
  examDate: "2024-12-10",
  evaluatedBy: "uuid"
});

// Get student marks
const marks = await getMarksByStudent(studentId);
```

### 5. Grades Service (`gradesService.js`)
```javascript
import { calculateGrade, getCGPA, getTranscript } from '@/services/gradesService';

// Calculate grade
await calculateGrade(studentId, courseId, semesterId);

// Get CGPA
const cgpa = await getCGPA(studentId);

// Get transcript
const transcript = await getTranscript(studentId, semesterId);
```

### 6. Assignment Service (`assignmentService.js`)
```javascript
import { createAssignment, submitAssignment, getAssignmentsByCourse } from '@/services/assignmentService';

// Create assignment
await createAssignment({
  courseId: "uuid",
  title: "Assignment 1",
  description: "Description here",
  dueDate: "2024-12-25",
  maxMarks: 100.0,
  createdBy: "uuid"
});

// Submit assignment
await submitAssignment(assignmentId, studentId, fileUrl);

// Get course assignments
const assignments = await getAssignmentsByCourse(courseId);
```

## Example Component Usage

### Example: Login Component
```javascript
"use client";
import { useState } from "react";
import { loginWithEmailPassword } from "@/services/authService";
import { useRouter } from "next/navigation";

export default function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const router = useRouter();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      const data = await loginWithEmailPassword(email, password);
      
      if (data.status === "Login Successful") {
        // Store tokens
        sessionStorage.setItem("accessToken", data.accessToken);
        sessionStorage.setItem("userRole", data.userRole);
        sessionStorage.setItem("userId", data.userId);

        // Navigate based on role
        if (data.userRole === "ADMIN" || data.userRole === "SUPER_ADMIN") {
          router.push("/dashboards/admin-dashboard");
        } else if (data.userRole === "LECTURER") {
          router.push("/dashboards/instructor-dashboard");
        } else {
          router.push("/dashboards/student-dashboard");
        }
      }
    } catch (err) {
      setError(err.message || "Login failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {/* Form fields */}
    </form>
  );
}
```

### Example: Departments List Component
```javascript
"use client";
import { useState, useEffect } from "react";
import { getAllDepartments } from "@/services/academicService";

export default function DepartmentsList() {
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchDepartments();
  }, []);

  const fetchDepartments = async () => {
    try {
      setLoading(true);
      const data = await getAllDepartments();
      setDepartments(data);
    } catch (err) {
      setError("Failed to load departments");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>{error}</div>;

  return (
    <div>
      {departments.map((dept) => (
        <div key={dept.id}>{dept.departmentName}</div>
      ))}
    </div>
  );
}
```

### Example: Mark Attendance Component
```javascript
"use client";
import { useState } from "react";
import { markBulkAttendance } from "@/services/attendanceService";

export default function MarkAttendanceForm({ courseId, students }) {
  const [attendanceData, setAttendanceData] = useState({});
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const studentsList = students.map(student => ({
        studentId: student.id,
        status: attendanceData[student.id] || "ABSENT",
        remarks: ""
      }));

      await markBulkAttendance({
        courseId,
        attendanceDate: new Date().toISOString().split('T')[0],
        markedBy: sessionStorage.getItem("userId"),
        students: studentsList
      });

      alert("Attendance marked successfully!");
    } catch (err) {
      alert("Failed to mark attendance: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      {/* Form fields */}
    </form>
  );
}
```

## Error Handling

All services throw errors that should be caught:

```javascript
try {
  const data = await getAllDepartments();
} catch (error) {
  // Handle error
  console.error("API Error:", error);
  // error.response?.data contains the error details
}
```

## Authentication

The API service automatically includes the authorization token from localStorage if available. Make sure to store the token after login:

```javascript
sessionStorage.setItem("accessToken", token);
```

## Common Patterns

### Loading States
```javascript
const [loading, setLoading] = useState(false);

const fetchData = async () => {
  setLoading(true);
  try {
    const data = await someService();
    // Handle data
  } finally {
    setLoading(false);
  }
};
```

### Error States
```javascript
const [error, setError] = useState("");

try {
  await someService();
} catch (err) {
  setError(err.response?.data?.message || "An error occurred");
}
```

### Refresh Data
```javascript
const [data, setData] = useState([]);

const refreshData = async () => {
  const newData = await getAllDepartments();
  setData(newData);
};

useEffect(() => {
  refreshData();
}, []);
```

## File Structure

```
src/
  services/
    api.js              # Axios instance with base config
    authService.js      # Authentication & user management
    academicService.js   # Departments, programs, courses
    attendanceService.js # Attendance management
    marksService.js     # Marks management
    gradesService.js    # Grades & transcripts
    assignmentService.js # Assignment management
    departmentService.js # Legacy (backward compatibility)
    index.js            # Central export
```

## Migration Guide

### Old Way (Direct fetch)
```javascript
const response = await fetch("http://localhost:9029/api/nethaji-service/acadamic/getalldepartments");
const data = await response.json();
```

### New Way (Using Services)
```javascript
import { getAllDepartments } from "@/services/academicService";
const data = await getAllDepartments();
```

## Next Steps

1. Update existing components to use new services
2. Replace hardcoded API URLs with service calls
3. Add proper error handling
4. Implement loading states
5. Add form validation

