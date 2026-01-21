#!/bin/bash

# Quick Test Commands - Copy and paste ready!

BASE_URL="http://localhost:9029/api/nethaji-service"

echo "=========================================="
echo "Quick API Test Commands"
echo "=========================================="
echo ""

echo "1. Health Check:"
echo "curl $BASE_URL/health"
echo ""

echo "2. Seed Data (Do this first!):"
echo "curl -X POST $BASE_URL/admin/seed-data"
echo ""

echo "3. Get All Departments:"
echo "curl $BASE_URL/acadamic/getalldepartments"
echo ""

echo "4. Get All Programs:"
echo "curl $BASE_URL/acadamic/getallprograms"
echo ""

echo "5. Get All Courses:"
echo "curl $BASE_URL/acadamic/all-courses"
echo ""

echo "6. Get All Students:"
echo "curl \"$BASE_URL/auth/students?page=0&size=10\""
echo ""

echo "7. Mark Attendance:"
echo "curl -X POST $BASE_URL/attendance/mark \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d '{\"studentId\":\"550e8400-e29b-41d4-a716-446655440301\",\"courseId\":\"550e8400-e29b-41d4-a716-446655440701\",\"attendanceDate\":\"2024-12-15\",\"status\":\"PRESENT\",\"markedBy\":\"550e8400-e29b-41d4-a716-446655440201\"}'"
echo ""

echo "8. Enter Marks:"
echo "curl -X POST $BASE_URL/marks/enter \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d '{\"studentId\":\"550e8400-e29b-41d4-a716-446655440301\",\"courseId\":\"550e8400-e29b-41d4-a716-446655440701\",\"examType\":\"INTERNAL\",\"marksObtained\":85.5,\"maxMarks\":100.0,\"examDate\":\"2024-12-10\",\"evaluatedBy\":\"550e8400-e29b-41d4-a716-446655440201\"}'"
echo ""

echo "=========================================="
echo "Copy and paste these commands to test!"
echo "=========================================="

