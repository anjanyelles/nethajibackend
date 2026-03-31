# Admin User Setup & User Creation Guide

## ✅ Admin Credentials

**Email:** `admin@nethaji.com`  
**Password:** `admin123`

---

## 🔐 Admin Login

```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@nethaji.com",
    "password": "admin123"
  }'
```

**Response:**
```json
{
  "status": "Login Successful",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": "ae32ac6c-464c-4eb4-b3e1-ee6fc5aff7db",
  "refreshToke": "eyJhbGciOiJIUzUxMiJ9...",
  "userStatus": true,
  "userRole": "ADMIN"
}
```

Save the `accessToken` for subsequent requests.

---

## 👥 Create Users (Admin Only)

### Create a Student

```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/admin-or-principal/create-user \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_ADMIN_TOKEN>" \
  -d '{
    "email": "student1@nethaji.com",
    "password": "student123",
    "firstName": "John",
    "lastName": "Doe",
    "mobileNumber": "9876543210",
    "countryCode": "+91",
    "userType": "STUDENT",
    "programId": "550e8400-e29b-41d4-a716-446655440001",
    "branch": "CSE",
    "semester": 1,
    "graduationType": "BTECH"
  }'
```

**Required fields for STUDENT:**
- `email`
- `password`
- `firstName`
- `lastName`
- `mobileNumber`
- `userType`: "STUDENT"
- `programId`: UUID of the program
- `branch`: "CSE", "ECE", "ME", etc.
- `semester`: Integer (1-8)

---

### Create a Lecturer

```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/admin-or-principal/create-user \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_ADMIN_TOKEN>" \
  -d '{
    "email": "lecturer1@nethaji.com",
    "password": "lecturer123",
    "firstName": "Jane",
    "lastName": "Smith",
    "mobileNumber": "9876543211",
    "countryCode": "+91",
    "userType": "LECTURER",
    "subjectType": "THEORY",
    "graduationType": "BTECH"
  }'
```

**Required fields for LECTURER:**
- `email`
- `password`
- `firstName`
- `lastName`
- `mobileNumber`
- `userType`: "LECTURER"
- `subjectType`: "THEORY" or "LAB"
- `graduationType`: "BTECH", "MTECH", "BSC", etc.

---

### Create Another Admin

```bash
curl -X POST http://localhost:9029/api/nethaji-service/auth/admin-or-principal/create-user \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_ADMIN_TOKEN>" \
  -d '{
    "email": "admin2@nethaji.com",
    "password": "admin123",
    "firstName": "Admin",
    "lastName": "Two",
    "mobileNumber": "9876543212",
    "countryCode": "+91",
    "userType": "ADMIN"
  }'
```

---

## 📋 User Types

- `SUPER_ADMIN` - Super administrator
- `ADMIN` - Administrator
- `STUDENT` - Student
- `LECTURER` - Lecturer/Teacher

---

## 🔄 Branch Options

- `CSE` - Computer Science Engineering
- `ECE` - Electronics and Communication Engineering
- `ME` - Mechanical Engineering
- `EEE` - Electrical and Electronics Engineering
- `CIVIL` - Civil Engineering

---

## 📚 Subject Types (for Lecturers)

- `THEORY` - Theory subjects
- `LAB` - Laboratory subjects

---

## 🎓 Graduation Types

- `BTECH` - Bachelor of Technology
- `MTECH` - Master of Technology
- `BSC` - Bachelor of Science
- `MSC` - Master of Science

---

## 📝 Notes

1. **No OTP required** when admin creates users
2. Users receive **welcome email** with login credentials
3. **Enrollment numbers** are auto-generated for students
4. All passwords are **hashed with SHA-512** before storage
5. Users can login immediately after creation

---

## 🔧 Recreate Admin User

If you need to recreate the admin user, run:

```bash
cd /Users/anjanyelle/Desktop/nethaji\ full\ code/nethajibackend/nethajibackend
java -cp "src/main/java:target/classes:$HOME/.m2/repository/org/postgresql/postgresql/42.7.5/postgresql-42.7.5.jar" com.nethaji.util.CreateAdminUser
```

---

## 🚀 For Render Deployment

When deploying to Render, create the admin user by:

1. Connect to your Render PostgreSQL database
2. Run the `CreateAdminUser.java` utility with Render database credentials
3. Or manually insert via SQL after hashing the password

**Environment variables needed:**
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://<render-host>:5432/<db-name>
SPRING_DATASOURCE_USERNAME=<db-user>
SPRING_DATASOURCE_PASSWORD=<db-password>
APP_AUTH_TOKEN_SECRET=<your-secret-key>
AWS_ACCESS_KEY_ID=<aws-key>
AWS_SECRET_ACCESS_KEY=<aws-secret>
```
