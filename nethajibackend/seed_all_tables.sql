-- COMPLETE SEED DATA FOR ALL TABLES - Nethaji College Management System
-- This script populates ALL tables with comprehensive test data

-- ============================================
-- 1. PROGRAMS (Already seeded - 6 programs)
-- ============================================
-- Already exists from previous seed

-- ============================================
-- 2. DEPARTMENTS (Already seeded - 9 departments)
-- ============================================
-- Already exists from previous seed

-- ============================================
-- 3. DEPARTMENT SEMESTERS (Already seeded - 16 semesters)
-- ============================================
-- Already exists from previous seed

-- ============================================
-- 4. COURSES (Already seeded - 13 courses)
-- ============================================
-- Already exists from previous seed

-- ============================================
-- 5. USERS (Already seeded - 1 admin, 3 lecturers, 5 students)
-- ============================================
-- Already exists from previous seed

-- ============================================
-- 6. STUDENT PROFILES (NEW - Add profiles for all students)
-- ============================================

INSERT INTO student_profile (
    id, user_id, date_of_birth, gender, address, city, state, pincode,
    guardian_name, guardian_contact, guardian_relation,
    blood_group, alternate_contact,
    tenth_percentage, tenth_board, tenth_year_of_passing,
    twelfth_percentage, twelfth_board, twelfth_year_of_passing,
    is_active, created_at, updated_at
) VALUES
-- Student 1: Aarav Patel
('d50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440011', 
 '2006-05-15', 'MALE', '123 MG Road', 'Hyderabad', 'Telangana', '500001',
 'Ramesh Patel', '9876543230', 'Father',
 'O+', '9876543231',
 95.5, 'CBSE', 2022,
 92.8, 'CBSE', 2024,
 true, NOW(), NOW()),

-- Student 2: Ananya Singh
('d50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440012',
 '2006-08-22', 'FEMALE', '456 Jubilee Hills', 'Hyderabad', 'Telangana', '500033',
 'Vijay Singh', '9876543232', 'Father',
 'A+', '9876543233',
 94.2, 'State Board', 2022,
 91.5, 'State Board', 2024,
 true, NOW(), NOW()),

-- Student 3: Arjun Verma
('d50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440013',
 '2005-12-10', 'MALE', '789 Banjara Hills', 'Hyderabad', 'Telangana', '500034',
 'Suresh Verma', '9876543234', 'Father',
 'B+', '9876543235',
 93.8, 'CBSE', 2021,
 90.2, 'CBSE', 2023,
 true, NOW(), NOW()),

-- Student 4: Diya Gupta
('d50e8400-e29b-41d4-a716-446655440004', '950e8400-e29b-41d4-a716-446655440014',
 '2006-03-18', 'FEMALE', '321 Madhapur', 'Hyderabad', 'Telangana', '500081',
 'Anil Gupta', '9876543236', 'Father',
 'AB+', '9876543237',
 96.1, 'CBSE', 2022,
 93.4, 'CBSE', 2024,
 true, NOW(), NOW()),

-- Student 5: Ishaan Mehta
('d50e8400-e29b-41d4-a716-446655440005', '950e8400-e29b-41d4-a716-446655440015',
 '2005-09-25', 'MALE', '654 Gachibowli', 'Hyderabad', 'Telangana', '500032',
 'Rajesh Mehta', '9876543238', 'Father',
 'O-', '9876543239',
 92.7, 'State Board', 2021,
 89.8, 'State Board', 2023,
 true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 7. STAFF PROFILES (NEW - Add profiles for all lecturers)
-- ============================================

INSERT INTO staff_profile (
    id, user_id, first_name, middle_name, last_name,
    department, designation, qualification,
    joining_date, experience_years, availability,
    phone_number, email, employment_type, salary,
    emergency_contact_name, emergency_contact_phone,
    date_of_birth, gender, address, employee_subject, status,
    created_at, updated_at
) VALUES
-- Lecturer 1: Dr. Rajesh Kumar
('e50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440001',
 'Rajesh', 'Kumar', 'Reddy',
 'CSE', 'Associate Professor', 'PhD in Computer Science',
 '2020-07-01', 4, 'Mon-Fri 9AM-5PM',
 '9876543210', 'lecturer1@nethaji.com', 0, 75000.00,
 'Lakshmi Kumar', '9876543240',
 '1985-04-12', 0, '12 Faculty Colony, Hyderabad', 'Computer Science', 0,
 NOW(), NOW()),

-- Lecturer 2: Dr. Priya Sharma
('e50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440002',
 'Priya', NULL, 'Sharma',
 'CSE', 'Assistant Professor', 'PhD in Data Science',
 '2019-08-15', 5, 'Mon-Fri 9AM-5PM',
 '9876543211', 'lecturer2@nethaji.com', 0, 70000.00,
 'Amit Sharma', '9876543241',
 '1986-11-20', 1, '34 Faculty Apartments, Hyderabad', 'Data Science', 0,
 NOW(), NOW()),

-- Lecturer 3: Prof. Suresh Reddy
('e50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440003',
 'Suresh', 'Babu', 'Reddy',
 'CSE', 'Professor', 'PhD in Database Systems',
 '2018-06-01', 6, 'Mon-Fri 9AM-5PM',
 '9876543212', 'lecturer3@nethaji.com', 0, 85000.00,
 'Kavitha Reddy', '9876543242',
 '1984-02-28', 0, '56 University Road, Hyderabad', 'Database Systems', 0,
 NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 8. STUDENT SECTIONS (NEW - Assign students to sections)
-- ============================================

INSERT INTO student_sections (id, section_name, student_id, semester_id, created_at, updated_at) VALUES
-- Section A - Semester 1 students
('f50e8400-e29b-41d4-a716-446655440001', 'A', '950e8400-e29b-41d4-a716-446655440011', '750e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('f50e8400-e29b-41d4-a716-446655440002', 'A', '950e8400-e29b-41d4-a716-446655440012', '750e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),

-- Section A - Semester 2 students
('f50e8400-e29b-41d4-a716-446655440003', 'A', '950e8400-e29b-41d4-a716-446655440013', '750e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),
('f50e8400-e29b-41d4-a716-446655440004', 'A', '950e8400-e29b-41d4-a716-446655440014', '750e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),

-- Section A - Semester 3 students
('f50e8400-e29b-41d4-a716-446655440005', 'A', '950e8400-e29b-41d4-a716-446655440015', '750e8400-e29b-41d4-a716-446655440003', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 9. STUDENT PROGRAMS (NEW - Link students to programs)
-- ============================================

INSERT INTO student_programs (id, student_id, program_id, enrollment_date, is_active, created_at, updated_at) VALUES
('g50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440001', '2024-07-01', true, NOW(), NOW()),
('g50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440001', '2024-07-01', true, NOW(), NOW()),
('g50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440013', '550e8400-e29b-41d4-a716-446655440001', '2023-07-01', true, NOW(), NOW()),
('g50e8400-e29b-41d4-a716-446655440004', '950e8400-e29b-41d4-a716-446655440014', '550e8400-e29b-41d4-a716-446655440001', '2023-07-01', true, NOW(), NOW()),
('g50e8400-e29b-41d4-a716-446655440005', '950e8400-e29b-41d4-a716-446655440015', '550e8400-e29b-41d4-a716-446655440001', '2022-07-01', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 10. ATTENDANCE (NEW - Sample attendance records)
-- ============================================

INSERT INTO attendance (id, student_id, course_id, attendance_date, status, remarks, created_at, updated_at) VALUES
-- Student 1 attendance for Programming in C
('h50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440011', '850e8400-e29b-41d4-a716-446655440001', '2024-07-15', 'PRESENT', NULL, NOW(), NOW()),
('h50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440011', '850e8400-e29b-41d4-a716-446655440001', '2024-07-16', 'PRESENT', NULL, NOW(), NOW()),
('h50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440011', '850e8400-e29b-41d4-a716-446655440001', '2024-07-17', 'ABSENT', 'Sick', NOW(), NOW()),

-- Student 2 attendance
('h50e8400-e29b-41d4-a716-446655440004', '950e8400-e29b-41d4-a716-446655440012', '850e8400-e29b-41d4-a716-446655440001', '2024-07-15', 'PRESENT', NULL, NOW(), NOW()),
('h50e8400-e29b-41d4-a716-446655440005', '950e8400-e29b-41d4-a716-446655440012', '850e8400-e29b-41d4-a716-446655440001', '2024-07-16', 'PRESENT', NULL, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 11. ASSIGNMENTS (NEW - Sample assignments)
-- ============================================

INSERT INTO assignments (id, course_id, title, description, due_date, max_marks, created_by, created_at, updated_at) VALUES
-- Programming in C assignments
('i50e8400-e29b-41d4-a716-446655440001', '850e8400-e29b-41d4-a716-446655440001', 
 'Basic C Programs', 'Write programs for basic operations', '2024-08-15', 20, 
 '950e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
 
('i50e8400-e29b-41d4-a716-446655440002', '850e8400-e29b-41d4-a716-446655440001',
 'Arrays and Strings', 'Implement array and string operations', '2024-08-30', 25,
 '950e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),

-- Data Structures assignments
('i50e8400-e29b-41d4-a716-446655440003', '850e8400-e29b-41d4-a716-446655440011',
 'Linked List Implementation', 'Implement singly and doubly linked lists', '2024-09-15', 30,
 '950e8400-e29b-41d4-a716-446655440002', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 12. STUDENT ASSIGNMENTS (NEW - Student submissions)
-- ============================================

INSERT INTO student_assignments (id, assignment_id, student_id, submission_date, marks_obtained, status, feedback, created_at, updated_at) VALUES
-- Student 1 submissions
('j50e8400-e29b-41d4-a716-446655440001', 'i50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440011',
 '2024-08-14', 18, 'GRADED', 'Good work', NOW(), NOW()),
 
-- Student 2 submissions
('j50e8400-e29b-41d4-a716-446655440002', 'i50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440012',
 '2024-08-13', 19, 'GRADED', 'Excellent', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 13. MARKS (NEW - Exam marks)
-- ============================================

INSERT INTO marks (id, student_id, course_id, exam_type, marks_obtained, max_marks, exam_date, created_at, updated_at) VALUES
-- Student 1 marks
('k50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440011', '850e8400-e29b-41d4-a716-446655440001',
 'MID_TERM', 38, 50, '2024-09-01', NOW(), NOW()),
 
-- Student 2 marks
('k50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440012', '850e8400-e29b-41d4-a716-446655440001',
 'MID_TERM', 42, 50, '2024-09-01', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 14. GRADES (NEW - Final grades)
-- ============================================

INSERT INTO grades (id, student_id, course_id, semester_id, grade, grade_points, credits, created_at, updated_at) VALUES
-- Student 3 grades (completed semester 2)
('l50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440013', '850e8400-e29b-41d4-a716-446655440011',
 '750e8400-e29b-41d4-a716-446655440002', 'A', 9.0, 4, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 15. TIME TABLE (NEW - Class schedule)
-- ============================================

INSERT INTO time_table_details (id, course_id, lecturer_id, day_of_week, start_time, end_time, room_number, semester_id, created_at, updated_at) VALUES
-- Monday schedule
('m50e8400-e29b-41d4-a716-446655440001', '850e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440001',
 'MONDAY', '09:00:00', '10:00:00', 'Room 101', '750e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
 
('m50e8400-e29b-41d4-a716-446655440002', '850e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440001',
 'MONDAY', '10:00:00', '11:00:00', 'Room 101', '750e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),

-- Tuesday schedule
('m50e8400-e29b-41d4-a716-446655440003', '850e8400-e29b-41d4-a716-446655440011', '950e8400-e29b-41d4-a716-446655440002',
 'TUESDAY', '09:00:00', '10:00:00', 'Room 102', '750e8400-e29b-41d4-a716-446655440002', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 16. HOLIDAYS (NEW - Holiday calendar)
-- ============================================

INSERT INTO holiday_time_table (id, holiday_name, holiday_date, description, is_active, created_at, updated_at) VALUES
('n50e8400-e29b-41d4-a716-446655440001', 'Independence Day', '2024-08-15', 'National Holiday', true, NOW(), NOW()),
('n50e8400-e29b-41d4-a716-446655440002', 'Gandhi Jayanti', '2024-10-02', 'National Holiday', true, NOW(), NOW()),
('n50e8400-e29b-41d4-a716-446655440003', 'Diwali', '2024-11-01', 'Festival Holiday', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- FINAL VERIFICATION
-- ============================================

SELECT 'Complete Data Summary' as info;
SELECT 'Programs' as table_name, COUNT(*) as count FROM programs;
SELECT 'Departments' as table_name, COUNT(*) as count FROM department;
SELECT 'Semesters' as table_name, COUNT(*) as count FROM department_semesters;
SELECT 'Courses' as table_name, COUNT(*) as count FROM course;
SELECT 'Users' as table_name, user_type, COUNT(*) as count FROM users GROUP BY user_type;
SELECT 'Student Profiles' as table_name, COUNT(*) as count FROM student_profile;
SELECT 'Staff Profiles' as table_name, COUNT(*) as count FROM staff_profile;
SELECT 'Student Education' as table_name, COUNT(*) as count FROM student_education_info;
SELECT 'Lecturer Details' as table_name, COUNT(*) as count FROM lecture_details;
SELECT 'Course Assignments' as table_name, COUNT(*) as count FROM lecturer_course_assignments;
SELECT 'Student Sections' as table_name, COUNT(*) as count FROM student_sections;
SELECT 'Student Programs' as table_name, COUNT(*) as count FROM student_programs;
SELECT 'Attendance' as table_name, COUNT(*) as count FROM attendance;
SELECT 'Assignments' as table_name, COUNT(*) as count FROM assignments;
SELECT 'Student Submissions' as table_name, COUNT(*) as count FROM student_assignments;
SELECT 'Marks' as table_name, COUNT(*) as count FROM marks;
SELECT 'Grades' as table_name, COUNT(*) as count FROM grades;
SELECT 'Time Table' as table_name, COUNT(*) as count FROM time_table_details;
SELECT 'Holidays' as table_name, COUNT(*) as count FROM holiday_time_table;

-- Complete summary
SELECT 
    'ALL TABLES SEEDED' as status,
    (SELECT COUNT(*) FROM programs) as programs,
    (SELECT COUNT(*) FROM department) as departments,
    (SELECT COUNT(*) FROM course) as courses,
    (SELECT COUNT(*) FROM users WHERE user_type = 'STUDENT') as students,
    (SELECT COUNT(*) FROM users WHERE user_type = 'LECTURER') as lecturers,
    (SELECT COUNT(*) FROM student_profile) as student_profiles,
    (SELECT COUNT(*) FROM staff_profile) as staff_profiles,
    (SELECT COUNT(*) FROM attendance) as attendance_records,
    (SELECT COUNT(*) FROM assignments) as assignments,
    (SELECT COUNT(*) FROM marks) as marks_records;
