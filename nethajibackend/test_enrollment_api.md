# Student Enrollment API Test Documentation

## 🚀 **New Feature: Inter Student Enrollment**

A complete student enrollment system has been added to your Nethaji College Management System.

---

## 📋 **API Endpoints**

### **Base URL**: `https://nethaji-backend.onrender.com/api/nethaji-service/enrollment`

---

## 🎯 **1. Create Student Enrollment**

**POST** `/api/nethaji-service/enrollment`

**Request Body:**
```json
{
  "email": "student@example.com",
  "fullName": "John Doe",
  "hallTicketNo": "HT123456",
  "dateOfBirth": "2000-01-15",
  "interGroupCollege": "ABC Junior College",
  "aadharNo": "123456789012",
  "aadharMobile": "9876543210",
  "whatsappNo": "9876543210",
  "address": "123 Main Street, Hyderabad",
  "village": "Hyderabad",
  "courses": ["Mathematics", "Physics", "Chemistry"],
  "referredBy": "Friend"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Student enrollment saved successfully",
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "email": "student@example.com",
    "fullName": "John Doe",
    "hallTicketNo": "HT123456",
    "dateOfBirth": "2000-01-15",
    "interGroupCollege": "ABC Junior College",
    "aadharNo": "123456789012",
    "aadharMobile": "9876543210",
    "whatsappNo": "9876543210",
    "address": "123 Main Street, Hyderabad",
    "village": "Hyderabad",
    "courses": ["Mathematics", "Physics", "Chemistry"],
    "referredBy": "Friend",
    "createdAt": "2026-04-02T10:00:00",
    "updatedAt": "2026-04-02T10:00:00"
  }
}
```

---

## 📖 **2. Get All Enrollments (Paginated)**

**GET** `/api/nethaji-service/enrollment`

**Query Parameters:**
- `page` (default: 0) - Page number
- `size` (default: 10) - Page size
- `sortBy` (default: createdAt) - Sort field
- `sortDir` (default: desc) - Sort direction (asc/desc)

**Example:** `GET /api/nethaji-service/enrollment?page=0&size=5&sortBy=fullName&sortDir=asc`

**Response:**
```json
{
  "success": true,
  "message": "Enrollments retrieved successfully",
  "data": {
    "content": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440001",
        "email": "student@example.com",
        "fullName": "John Doe",
        "hallTicketNo": "HT123456",
        "courses": ["Mathematics", "Physics", "Chemistry"],
        "createdAt": "2026-04-02T10:00:00",
        "updatedAt": "2026-04-02T10:00:00"
      }
    ],
    "totalElements": 1,
    "totalPages": 1,
    "size": 5,
    "number": 0
  }
}
```

---

## 🔍 **3. Search Enrollments**

**GET** `/api/nethaji-service/enrollment/search`

**Query Parameters:**
- `email` (optional) - Search by email
- `fullName` (optional) - Search by full name
- `interGroupCollege` (optional) - Search by college
- `page`, `size`, `sortBy`, `sortDir` - Same as pagination

**Example:** `GET /api/nethaji-service/enrollment/search?fullName=John&college=ABC`

---

## 🎯 **4. Get Single Enrollment**

**GET** `/api/nethaji-service/enrollment/{id}`

**Example:** `GET /api/nethaji-service/enrollment/550e8400-e29b-41d4-a716-446655440001`

---

## ✏️ **5. Update Enrollment**

**PUT** `/api/nethaji-service/enrollment/{id}`

**Request Body:** Same as create enrollment

---

## 🗑️ **6. Delete Enrollment**

**DELETE** `/api/nethaji-service/enrollment/{id}`

---

## 🧪 **Test Commands**

### **Test Create Enrollment**
```bash
curl -X POST https://nethaji-backend.onrender.com/api/nethaji-service/enrollment \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "fullName": "John Doe",
    "hallTicketNo": "HT123456",
    "dateOfBirth": "2000-01-15",
    "interGroupCollege": "ABC Junior College",
    "aadharNo": "123456789012",
    "aadharMobile": "9876543210",
    "whatsappNo": "9876543210",
    "address": "123 Main Street, Hyderabad",
    "village": "Hyderabad",
    "courses": ["Mathematics", "Physics", "Chemistry"],
    "referredBy": "Friend"
  }'
```

### **Test Get All Enrollments**
```bash
curl -X GET "https://nethaji-backend.onrender.com/api/nethaji-service/enrollment?page=0&size=10"
```

### **Test Search Enrollments**
```bash
curl -X GET "https://nethaji-backend.onrender.com/api/nethaji-service/enrollment/search?fullName=John"
```

### **Test Get Single Enrollment**
```bash
curl -X GET https://nethaji-backend.onrender.com/api/nethaji-service/enrollment/{ID}
```

---

## ✅ **Validation Rules**

- **email**: Required, valid email format, unique
- **fullName**: Required
- **aadharMobile**: Required
- **hallTicketNo**: Optional, unique if provided
- **Duplicate Prevention**: Cannot create enrollment with existing email OR hall ticket number

---

## 🏗️ **Architecture**

### **Components Created:**
1. **Entity**: `StudentEnrollment.java` - Database model
2. **Repository**: `StudentEnrollmentRepository.java` - Data access layer
3. **DTOs**: 
   - `StudentEnrollmentRequest.java` - Request model
   - `StudentEnrollmentResponse.java` - Response model
   - `ApiResponse.java` - Standard response format
4. **Service**: 
   - `StudentEnrollmentService.java` - Interface
   - `StudentEnrollmentServiceImpl.java` - Implementation
5. **Controller**: `StudentEnrollmentController.java` - REST endpoints
6. **Exception**: `EnrollmentException.java` - Custom exception

### **Database Tables:**
- `student_enrollment` - Main enrollment table
- `student_enrollment_courses` - Junction table for courses array

---

## 🔐 **Security & Features**

- ✅ **Input Validation** using Jakarta Validation
- ✅ **Duplicate Prevention** by email and hall ticket
- ✅ **Pagination** for large datasets
- ✅ **Search Functionality** with multiple filters
- ✅ **Error Handling** with proper response format
- ✅ **CORS Enabled** for frontend integration
- ✅ **Transaction Management** for data integrity

---

## 🚀 **Ready to Use!**

The Inter Student Enrollment feature is now fully integrated into your existing Nethaji College Management System and follows all existing patterns and coding standards.

**Test the endpoints using the curl commands above or integrate with your frontend application!**
