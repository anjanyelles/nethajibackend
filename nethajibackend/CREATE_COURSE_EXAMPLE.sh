#!/bin/bash

# Example: Create a Course via API
# This will create a course that can be used for testing

curl -X POST http://localhost:9029/api/nethaji-service/acadamic/addcourses \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Data Structures and Algorithms",
    "courseCode": "CS101",
    "description": "Introduction to data structures and algorithms",
    "courseType": "THEORY",
    "credits": 4,
    "departmentSemesterId": "550e8400-e29b-41d4-a716-446655440601",
    "isElective": false
  }'

echo ""
echo "Course created! Now you can test attendance, marks, etc."

