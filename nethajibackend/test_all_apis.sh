#!/bin/bash

# Complete API Testing Script
# This script tests all endpoints with sample data

BASE_URL="http://localhost:9029/api/nethaji-service"

echo "=========================================="
echo "Testing Nethaji College Management APIs"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test function
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo -e "${YELLOW}Testing: $description${NC}"
    echo "Endpoint: $method $endpoint"
    
    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        echo -e "${GREEN}✓ Success (HTTP $http_code)${NC}"
        echo "Response: $body" | head -c 200
        echo ""
    else
        echo -e "${RED}✗ Failed (HTTP $http_code)${NC}"
        echo "Response: $body"
    fi
    echo ""
    sleep 1
}

# 1. Health Check
test_endpoint "GET" "/health" "" "Health Check"

# 2. Seed Data
echo -e "${YELLOW}Seeding sample data...${NC}"
test_endpoint "POST" "/admin/seed-data" "" "Seed Sample Data"
sleep 2

# 3. Get All Departments
test_endpoint "GET" "/acadamic/getalldepartments" "" "Get All Departments"

# 4. Get All Programs
test_endpoint "GET" "/acadamic/getallprograms" "" "Get All Programs"

# 5. Get All Courses
test_endpoint "GET" "/acadamic/all-courses" "" "Get All Courses"

# 6. Get All Students
test_endpoint "GET" "/auth/students?page=0&size=10" "" "Get All Students"

# 7. Get All Lecturers
test_endpoint "GET" "/auth/lecturers" "" "Get All Lecturers"

# 8. Mark Attendance
test_endpoint "POST" "/attendance/mark" '{
    "studentId": "550e8400-e29b-41d4-a716-446655440301",
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "attendanceDate": "2024-12-15",
    "status": "PRESENT",
    "markedBy": "550e8400-e29b-41d4-a716-446655440201",
    "remarks": "On time"
}' "Mark Attendance"

# 9. Get Attendance by Student
test_endpoint "GET" "/attendance/student/550e8400-e29b-41d4-a716-446655440301" "" "Get Attendance by Student"

# 10. Enter Marks
test_endpoint "POST" "/marks/enter" '{
    "studentId": "550e8400-e29b-41d4-a716-446655440301",
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "examType": "INTERNAL",
    "marksObtained": 85.5,
    "maxMarks": 100.0,
    "examDate": "2024-12-10",
    "evaluatedBy": "550e8400-e29b-41d4-a716-446655440201",
    "remarks": "Good performance"
}' "Enter Marks"

# 11. Get Marks by Student
test_endpoint "GET" "/marks/student/550e8400-e29b-41d4-a716-446655440301" "" "Get Marks by Student"

# 12. Calculate Grade
test_endpoint "POST" "/grades/calculate?studentId=550e8400-e29b-41d4-a716-446655440301&courseId=550e8400-e29b-41d4-a716-446655440701&semesterId=550e8400-e29b-41d4-a716-446655440601" "" "Calculate Grade"

# 13. Get Grades by Student
test_endpoint "GET" "/grades/student/550e8400-e29b-41d4-a716-446655440301" "" "Get Grades by Student"

# 14. Get CGPA
test_endpoint "GET" "/grades/cgpa/student/550e8400-e29b-41d4-a716-446655440301" "" "Get CGPA"

# 15. Create Assignment
test_endpoint "POST" "/assignments/create" '{
    "courseId": "550e8400-e29b-41d4-a716-446655440701",
    "title": "Assignment 2: Linked Lists",
    "description": "Implement linked lists",
    "dueDate": "2024-12-25",
    "maxMarks": 100.0,
    "createdBy": "550e8400-e29b-41d4-a716-446655440201"
}' "Create Assignment"

# 16. Get Assignments by Course
test_endpoint "GET" "/assignments/course/550e8400-e29b-41d4-a716-446655440701" "" "Get Assignments by Course"

echo "=========================================="
echo "Testing Complete!"
echo "=========================================="

