# Postman Testing Guide

## Base URL
```
http://localhost:9029/api/nethaji-service
```

---

## ✅ Working Endpoints for Postman

### 1. Health Check
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/health`
- **Headers**: None required
- **Expected Response**: `"OK"`

---

### 2. Get All Departments
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/acadamic/getalldepartments`
- **Headers**: None required
- **Query Params**: (Optional) `id` - UUID of specific department

**Example**:
```
GET http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
```

---

### 3. Get All Programs
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/acadamic/getallprograms`
- **Headers**: None required
- **Query Params**: (Optional) `id` - UUID of specific program

**Example**:
```
GET http://localhost:9029/api/nethaji-service/acadamic/getallprograms
```

---

### 4. Get All Courses
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/acadamic/all-courses`
- **Headers**: None required
- **Query Params**: (Optional) `id` - UUID of specific course

---

### 5. Get All Students (Paginated)
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/auth/students`
- **Query Params**:
  - `page` (default: 0)
  - `size` (default: 10)
  - `sortBy` (default: firstName)
  - `sortDirection` (default: ASC)

**Example**:
```
GET http://localhost:9029/api/nethaji-service/auth/students?page=0&size=10
```

---

### 6. Get All Lecturers
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/auth/lecturers`

---

### 7. Get Attendance by Student
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/attendance/student/{studentId}`
- **Replace**: `{studentId}` with actual UUID

**Example**:
```
GET http://localhost:9029/api/nethaji-service/attendance/student/550e8400-e29b-41d4-a716-446655440301
```

---

### 8. Get Marks by Student
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/marks/student/{studentId}`

---

### 9. Get Grades by Student
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/grades/student/{studentId}`

---

### 10. Get Assignments by Course
- **Method**: `GET`
- **URL**: `http://localhost:9029/api/nethaji-service/assignments/course/{courseId}`

---

## 🔧 Postman Setup

### Step 1: Create a New Collection
1. Open Postman
2. Click "New" → "Collection"
3. Name it "Nethaji College API"

### Step 2: Set Collection Variables
1. Click on your collection
2. Go to "Variables" tab
3. Add variable:
   - **Variable**: `base_url`
   - **Initial Value**: `http://localhost:9029/api/nethaji-service`
   - **Current Value**: `http://localhost:9029/api/nethaji-service`

### Step 3: Use Variables in Requests
Use `{{base_url}}/acadamic/getalldepartments` instead of full URL

---

## 📝 Common Headers

For most GET requests, no headers are needed. For POST/PUT requests:

```
Content-Type: application/json
```

---

## 🐛 Troubleshooting

### Error: "Connection refused"
- **Solution**: Make sure the application is running
- Check: `curl http://localhost:9029/api/nethaji-service/health`

### Error: "404 Not Found"
- **Solution**: Check the exact URL path (note: it's `/acadamic` not `/academic`)
- Verify endpoint exists in controller

### Error: CORS Error
- **Solution**: CORS is configured, but if you still see errors:
  1. Make sure application is restarted after SecurityConfig changes
  2. Check browser console for specific CORS error

### Error: Empty Response `[]`
- **Solution**: Data might not be loaded yet
- Try calling the seed endpoint: `POST http://localhost:9029/api/nethaji-service/admin/seed-data`

---

## 🧪 Quick Test Sequence

1. **Health Check**:
   ```
   GET http://localhost:9029/api/nethaji-service/health
   ```

2. **Get Departments**:
   ```
   GET http://localhost:9029/api/nethaji-service/acadamic/getalldepartments
   ```

3. **Get Programs**:
   ```
   GET http://localhost:9029/api/nethaji-service/acadamic/getallprograms
   ```

4. **Get Students**:
   ```
   GET http://localhost:9029/api/nethaji-service/auth/students?page=0&size=10
   ```

---

## 📋 Sample UUIDs (from data.sql)

**Programs**:
- BTECH: `550e8400-e29b-41d4-a716-446655440001`
- MTECH: `550e8400-e29b-41d4-a716-446655440002`

**Departments**:
- CSE: `550e8400-e29b-41d4-a716-446655440011`
- ECE: `550e8400-e29b-41d4-a716-446655440012`

**Students**:
- Student 1: `550e8400-e29b-41d4-a716-446655440301`
- Student 2: `550e8400-e29b-41d4-a716-446655440302`

**Courses**:
- CS101: `550e8400-e29b-41d4-a716-446655440701`
- CS102: `550e8400-e29b-41d4-a716-446655440702`

---

## ✅ Verify Application is Running

Before testing in Postman, verify the application is running:

```bash
# Check if port is listening
lsof -i :9029

# Or test with curl
curl http://localhost:9029/api/nethaji-service/health
```

---

**Note**: The endpoint path uses `/acadamic` (not `/academic`) - this is intentional based on the controller mapping.

