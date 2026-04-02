-- Seed Data for Nethaji College Management System
-- Execute this on your Render PostgreSQL database

-- ============================================
-- 1. PROGRAMS (Degree Programs)
-- ============================================

INSERT INTO programs (id, name, program_code, level, duration_years, is_active, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Bachelor of Technology', 'BTECH', 'BTECH', 4, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440002', 'Master of Technology', 'MTECH', 'MTECH', 2, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440003', 'Bachelor of Science', 'BSC', 'BSC', 3, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440004', 'Master of Science', 'MSC', 'MSC', 2, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440005', 'Bachelor of Commerce', 'BCOM', 'BCOM', 3, true, NOW(), NOW()),
('550e8400-e29b-41d4-a716-446655440006', 'Bachelor of Business Administration', 'BBA', 'BBA', 3, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 2. DEPARTMENTS
-- ============================================

INSERT INTO department (id, department_code, department_name, program_id, created_at, updated_at) VALUES
-- B.Tech Departments
('650e8400-e29b-41d4-a716-446655440001', 'CSE', 'Computer Science Engineering', '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('650e8400-e29b-41d4-a716-446655440002', 'ECE', 'Electronics and Communication Engineering', '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('650e8400-e29b-41d4-a716-446655440003', 'EEE', 'Electrical and Electronics Engineering', '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
('650e8400-e29b-41d4-a716-446655440004', 'CIVIL', 'Civil Engineering', '550e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),

-- M.Tech Departments
('650e8400-e29b-41d4-a716-446655440005', 'CSE', 'Computer Science Engineering (M.Tech)', '550e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),

-- BSC Departments
('650e8400-e29b-41d4-a716-446655440006', 'BSC', 'Bachelor of Science', '550e8400-e29b-41d4-a716-446655440003', NOW(), NOW()),

-- MSC Departments
('650e8400-e29b-41d4-a716-446655440007', 'MSC', 'Master of Science', '550e8400-e29b-41d4-a716-446655440004', NOW(), NOW()),

-- BCOM Department
('650e8400-e29b-41d4-a716-446655440008', 'BCOM', 'Bachelor of Commerce', '550e8400-e29b-41d4-a716-446655440005', NOW(), NOW()),

-- BBA Department
('650e8400-e29b-41d4-a716-446655440009', 'BBA', 'Bachelor of Business Administration', '550e8400-e29b-41d4-a716-446655440006', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 3. DEPARTMENT SEMESTERS
-- ============================================

-- CSE B.Tech Semesters (8 semesters)
INSERT INTO department_semesters (id, department_id, semester_number, is_active, created_at, updated_at) VALUES
('750e8400-e29b-41d4-a716-446655440001', '650e8400-e29b-41d4-a716-446655440001', 1, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440002', '650e8400-e29b-41d4-a716-446655440001', 2, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440003', '650e8400-e29b-41d4-a716-446655440001', 3, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440004', '650e8400-e29b-41d4-a716-446655440001', 4, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440005', '650e8400-e29b-41d4-a716-446655440001', 5, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440006', '650e8400-e29b-41d4-a716-446655440001', 6, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440007', '650e8400-e29b-41d4-a716-446655440001', 7, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440008', '650e8400-e29b-41d4-a716-446655440001', 8, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ECE B.Tech Semesters (8 semesters)
INSERT INTO department_semesters (id, department_id, semester_number, is_active, created_at, updated_at) VALUES
('750e8400-e29b-41d4-a716-446655440011', '650e8400-e29b-41d4-a716-446655440002', 1, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440012', '650e8400-e29b-41d4-a716-446655440002', 2, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440013', '650e8400-e29b-41d4-a716-446655440002', 3, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440014', '650e8400-e29b-41d4-a716-446655440002', 4, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440015', '650e8400-e29b-41d4-a716-446655440002', 5, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440016', '650e8400-e29b-41d4-a716-446655440002', 6, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440017', '650e8400-e29b-41d4-a716-446655440002', 7, true, NOW(), NOW()),
('750e8400-e29b-41d4-a716-446655440018', '650e8400-e29b-41d4-a716-446655440002', 8, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 4. COURSES (Sample courses for CSE Semester 1)
-- ============================================

INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, created_at, updated_at) VALUES
-- CSE Semester 1
('850e8400-e29b-41d4-a716-446655440001', 'Programming in C', 'CSE101', 'Introduction to C programming language', 'THEORY', 4, '750e8400-e29b-41d4-a716-446655440001', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440002', 'Mathematics-I', 'MATH101', 'Engineering Mathematics', 'THEORY', 4, '750e8400-e29b-41d4-a716-446655440001', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440003', 'Physics', 'PHY101', 'Engineering Physics', 'THEORY', 3, '750e8400-e29b-41d4-a716-446655440001', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440004', 'English', 'ENG101', 'Professional English', 'THEORY', 2, '750e8400-e29b-41d4-a716-446655440001', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440005', 'C Programming Lab', 'CSE101L', 'C Programming Laboratory', 'LAB', 2, '750e8400-e29b-41d4-a716-446655440001', false, true, NOW(), NOW()),

-- CSE Semester 2
('850e8400-e29b-41d4-a716-446655440011', 'Data Structures', 'CSE201', 'Data Structures and Algorithms', 'THEORY', 4, '750e8400-e29b-41d4-a716-446655440002', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440012', 'Mathematics-II', 'MATH201', 'Advanced Engineering Mathematics', 'THEORY', 4, '750e8400-e29b-41d4-a716-446655440002', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440013', 'Digital Electronics', 'ECE201', 'Digital Logic Design', 'THEORY', 3, '750e8400-e29b-41d4-a716-446655440002', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440014', 'Data Structures Lab', 'CSE201L', 'Data Structures Laboratory', 'LAB', 2, '750e8400-e29b-41d4-a716-446655440002', false, true, NOW(), NOW()),

-- CSE Semester 3
('850e8400-e29b-41d4-a716-446655440021', 'Database Management Systems', 'CSE301', 'DBMS Concepts and SQL', 'THEORY', 4, '750e8400-e29b-41d4-a716-446655440003', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440022', 'Operating Systems', 'CSE302', 'OS Concepts and Unix', 'THEORY', 4, '750e8400-e29b-41d4-a716-446655440003', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440023', 'Computer Networks', 'CSE303', 'Networking Fundamentals', 'THEORY', 3, '750e8400-e29b-41d4-a716-446655440003', false, true, NOW(), NOW()),
('850e8400-e29b-41d4-a716-446655440024', 'DBMS Lab', 'CSE301L', 'Database Laboratory', 'LAB', 2, '750e8400-e29b-41d4-a716-446655440003', false, true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 5. SAMPLE USERS (Students and Lecturers)
-- ============================================

-- Sample Lecturers
INSERT INTO users (id, email, first_name, last_name, mobile_number, password_hash, salt, user_type, is_active, joining_year, created_at, updated_at) VALUES
('950e8400-e29b-41d4-a716-446655440001', 'lecturer1@nethaji.com', 'Dr. Rajesh', 'Kumar', '9876543210', 'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9', 'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4', 'LECTURER', true, 2020, NOW(), NOW()),
('950e8400-e29b-41d4-a716-446655440002', 'lecturer2@nethaji.com', 'Dr. Priya', 'Sharma', '9876543211', 'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9', 'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4', 'LECTURER', true, 2019, NOW(), NOW()),
('950e8400-e29b-41d4-a716-446655440003', 'lecturer3@nethaji.com', 'Prof. Suresh', 'Reddy', '9876543212', 'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9', 'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4', 'LECTURER', true, 2018, NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- Sample Students
INSERT INTO users (id, email, first_name, last_name, mobile_number, password_hash, salt, user_type, is_active, joining_year, enrollment_number, created_at, updated_at) VALUES
('950e8400-e29b-41d4-a716-446655440011', 'student1@nethaji.com', 'Aarav', 'Patel', '9876543220', 'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9', 'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4', 'STUDENT', true, 2024, 'CSE202401', NOW(), NOW()),
('950e8400-e29b-41d4-a716-446655440012', 'student2@nethaji.com', 'Ananya', 'Singh', '9876543221', 'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9', 'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4', 'STUDENT', true, 2024, 'CSE202402', NOW(), NOW()),
('950e8400-e29b-41d4-a716-446655440013', 'student3@nethaji.com', 'Arjun', 'Verma', '9876543222', 'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9', 'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4', 'STUDENT', true, 2024, 'CSE202403', NOW(), NOW()),
('950e8400-e29b-41d4-a716-446655440014', 'student4@nethaji.com', 'Diya', 'Gupta', '9876543223', 'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9', 'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4', 'STUDENT', true, 2024, 'CSE202404', NOW(), NOW()),
('950e8400-e29b-41d4-a716-446655440015', 'student5@nethaji.com', 'Ishaan', 'Mehta', '9876543224', 'BEA5814087D7CCE9868500543A43DE166E1D51F91E120E8E4A66E69E2F166C450D84F7FB27FB7C903A5D350CCB65D1700F4F47B57DCB9F113BFE0A83913A33D9', 'f21d744f-2f8e-4b93-866d-a985879dbd807f1c1013-4', 'STUDENT', true, 2024, 'CSE202405', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;

-- ============================================
-- 6. LECTURER DETAILS
-- ============================================

INSERT INTO lecturer_details (id, lecture_id, assignment_type, subject_type, graduation_lecturer, created_at, updated_at) VALUES
('a50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440001', 'LECTURER', 'THEORY', 'CSE', NOW(), NOW()),
('a50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440002', 'LECTURER', 'THEORY', 'CSE', NOW(), NOW()),
('a50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440003', 'LECTURER', 'LAB', 'CSE', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 7. STUDENT EDUCATION INFO
-- ============================================

INSERT INTO student_education_info (id, student_id, program_id, branch, semester, graduation_type, semester_status, created_at, updated_at) VALUES
('b50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440001', 'CSE', 1, 'CSE', 'IN_PROGRESS', NOW(), NOW()),
('b50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440001', 'CSE', 1, 'CSE', 'IN_PROGRESS', NOW(), NOW()),
('b50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440013', '550e8400-e29b-41d4-a716-446655440001', 'CSE', 2, 'CSE', 'IN_PROGRESS', NOW(), NOW()),
('b50e8400-e29b-41d4-a716-446655440004', '950e8400-e29b-41d4-a716-446655440014', '550e8400-e29b-41d4-a716-446655440001', 'CSE', 2, 'CSE', 'IN_PROGRESS', NOW(), NOW()),
('b50e8400-e29b-41d4-a716-446655440005', '950e8400-e29b-41d4-a716-446655440015', '550e8400-e29b-41d4-a716-446655440001', 'CSE', 3, 'CSE', 'IN_PROGRESS', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- 8. LECTURER COURSE ASSIGNMENTS
-- ============================================

INSERT INTO lecturer_course_assignment (id, lecturer_id, course_id, academic_year, created_at, updated_at) VALUES
('c50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440001', '850e8400-e29b-41d4-a716-446655440001', 2024, NOW(), NOW()),
('c50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440002', '850e8400-e29b-41d4-a716-446655440011', 2024, NOW(), NOW()),
('c50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440003', '850e8400-e29b-41d4-a716-446655440021', 2024, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- VERIFICATION QUERIES
-- ============================================

-- Check Programs
SELECT 'Programs' as table_name, COUNT(*) as count FROM programs;

-- Check Departments
SELECT 'Departments' as table_name, COUNT(*) as count FROM department;

-- Check Courses
SELECT 'Courses' as table_name, COUNT(*) as count FROM course;

-- Check Users
SELECT 'Users' as table_name, user_type, COUNT(*) as count FROM users GROUP BY user_type;

-- Check Student Education Info
SELECT 'Student Education' as table_name, COUNT(*) as count FROM student_education_info;

-- Check Lecturer Details
SELECT 'Lecturer Details' as table_name, COUNT(*) as count FROM lecturer_details;

-- Summary
SELECT 
    'SEED DATA SUMMARY' as info,
    (SELECT COUNT(*) FROM programs) as programs,
    (SELECT COUNT(*) FROM department) as departments,
    (SELECT COUNT(*) FROM course) as courses,
    (SELECT COUNT(*) FROM users WHERE user_type = 'STUDENT') as students,
    (SELECT COUNT(*) FROM users WHERE user_type = 'LECTURER') as lecturers,
    (SELECT COUNT(*) FROM users WHERE user_type = 'ADMIN') as admins;
