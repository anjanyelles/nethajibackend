-- Sample Data for Nethaji College Management System
-- This file will be executed automatically when the application starts

-- Insert Programs
INSERT INTO programs (id, name, program_code, level, duration_years, is_active, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440001', 'Bachelor of Technology', 'BTECH', 'BTECH', 4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440002', 'Master of Technology', 'MTECH', 'MTECH', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440003', 'Bachelor of Science', 'BSC', 'BSC', 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Departments
INSERT INTO department (id, department_code, department_name, program_id, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440011', 'CSE', 'Computer Science Engineering', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440012', 'ECE', 'Electronics and Communication Engineering', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440013', 'ME', 'Mechanical Engineering', '550e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Users (Admin, Lecturers, Students)
INSERT INTO users (id, enrollment_number, email, first_name, last_name, mobile_number, country_code, password_hash, user_type, is_active, created_at, updated_at, joining_year)
VALUES 
    -- Super Admin
    ('550e8400-e29b-41d4-a716-446655440101', 'ADMIN001', 'admin@nethaji.edu', 'Super', 'Admin', '9876543210', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SUPER_ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2020),
    
    -- Admin
    ('550e8400-e29b-41d4-a716-446655440102', 'ADMIN002', 'admin2@nethaji.edu', 'College', 'Admin', '9876543211', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2021),
    
    -- Lecturers
    ('550e8400-e29b-41d4-a716-446655440201', 'LEC001', 'prof.sharma@nethaji.edu', 'Rajesh', 'Sharma', '9876543220', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'LECTURER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2018),
    ('550e8400-e29b-41d4-a716-446655440202', 'LEC002', 'prof.kumar@nethaji.edu', 'Amit', 'Kumar', '9876543221', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'LECTURER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2019),
    ('550e8400-e29b-41d4-a716-446655440203', 'LEC003', 'prof.patel@nethaji.edu', 'Priya', 'Patel', '9876543222', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'LECTURER', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2020),
    
    -- Students
    ('550e8400-e29b-41d4-a716-446655440301', 'STU2024001', 'student1@nethaji.edu', 'Rahul', 'Singh', '9876543301', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2024),
    ('550e8400-e29b-41d4-a716-446655440302', 'STU2024002', 'student2@nethaji.edu', 'Sneha', 'Verma', '9876543302', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2024),
    ('550e8400-e29b-41d4-a716-446655440303', 'STU2024003', 'student3@nethaji.edu', 'Arjun', 'Reddy', '9876543303', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2024),
    ('550e8400-e29b-41d4-a716-446655440304', 'STU2024004', 'student4@nethaji.edu', 'Ananya', 'Das', '9876543304', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2024),
    ('550e8400-e29b-41d4-a716-446655440305', 'STU2024005', 'student5@nethaji.edu', 'Vikram', 'Malhotra', '9876543305', '+91', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'STUDENT', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2024)
ON CONFLICT (enrollment_number) DO NOTHING;

-- Insert Student Profiles
INSERT INTO student_profile (id, user_id, date_of_birth, gender, address, city, state, pincode, guardian_name, guardian_contact, guardian_relation, blood_group, tenth_percentage, tenth_board, tenth_year_of_passing, twelfth_percentage, twelfth_board, twelfth_year_of_passing, is_active, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440401', '550e8400-e29b-41d4-a716-446655440301', '2005-05-15', 'MALE', '123 Main Street', 'Bangalore', 'Karnataka', '560001', 'Ramesh Singh', '9876543401', 'Father', 'O+', 85.5, 'CBSE', 2022, 88.0, 'CBSE', 2024, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440402', '550e8400-e29b-41d4-a716-446655440302', '2005-08-20', 'FEMALE', '456 Park Avenue', 'Bangalore', 'Karnataka', '560002', 'Sunita Verma', '9876543402', 'Mother', 'A+', 90.0, 'CBSE', 2022, 92.5, 'CBSE', 2024, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440403', '550e8400-e29b-41d4-a716-446655440303', '2005-03-10', 'MALE', '789 MG Road', 'Bangalore', 'Karnataka', '560003', 'Krishna Reddy', '9876543403', 'Father', 'B+', 82.0, 'State Board', 2022, 85.0, 'State Board', 2024, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440404', '550e8400-e29b-41d4-a716-446655440304', '2005-11-25', 'FEMALE', '321 Brigade Road', 'Bangalore', 'Karnataka', '560004', 'Manoj Das', '9876543404', 'Father', 'AB+', 88.5, 'CBSE', 2022, 90.0, 'CBSE', 2024, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440405', '550e8400-e29b-41d4-a716-446655440305', '2005-07-12', 'MALE', '654 Indira Nagar', 'Bangalore', 'Karnataka', '560005', 'Rajesh Malhotra', '9876543405', 'Father', 'O-', 87.0, 'ICSE', 2022, 89.5, 'ICSE', 2024, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Staff Profiles
INSERT INTO staff_profile (id, user_id, first_name, middle_name, last_name, department, designation, qualification, joining_date, experience_years, phone_number, email, employment_type, salary, emergency_contact_name, emergency_contact_phone, status, address, date_of_birth, gender, employee_subject, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440501', '550e8400-e29b-41d4-a716-446655440201', 'Rajesh', NULL, 'Sharma', 'CSE', 'Professor', 'Ph.D. in Computer Science', '2018-01-15', 15, '9876543220', 'prof.sharma@nethaji.edu', 'FULL_TIME', 120000.00, 'Rekha Sharma', '9876543220', 'ACTIVE', '101 Faculty Quarters', '1975-03-20', 'MALE', 'Data Structures', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440502', '550e8400-e29b-41d4-a716-446655440202', 'Amit', NULL, 'Kumar', 'CSE', 'Associate Professor', 'M.Tech', '2019-06-01', 10, '9876543221', 'prof.kumar@nethaji.edu', 'FULL_TIME', 95000.00, 'Sunita Kumar', '9876543221', 'ACTIVE', '102 Faculty Quarters', '1980-07-15', 'MALE', 'Database Management', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440503', '550e8400-e29b-41d4-a716-446655440203', 'Priya', NULL, 'Patel', 'ECE', 'Assistant Professor', 'M.Tech', '2020-08-10', 5, '9876543222', 'prof.patel@nethaji.edu', 'FULL_TIME', 75000.00, 'Ravi Patel', '9876543222', 'ACTIVE', '103 Faculty Quarters', '1985-09-25', 'FEMALE', 'Digital Electronics', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Department Semesters
INSERT INTO department_semesters (id, department_id, semester_number, is_active, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440601', '550e8400-e29b-41d4-a716-446655440011', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440602', '550e8400-e29b-41d4-a716-446655440011', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440603', '550e8400-e29b-41d4-a716-446655440011', 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440604', '550e8400-e29b-41d4-a716-446655440012', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440605', '550e8400-e29b-41d4-a716-446655440012', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Courses
INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440701', 'Data Structures and Algorithms', 'CS101', 'Introduction to data structures and algorithms', 'THEORY', 4, '550e8400-e29b-41d4-a716-446655440601', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440702', 'Database Management Systems', 'CS102', 'Fundamentals of database systems', 'THEORY', 4, '550e8400-e29b-41d4-a716-446655440601', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440703', 'Object Oriented Programming', 'CS201', 'Java and OOP concepts', 'THEORY_LAB', 3, '550e8400-e29b-41d4-a716-446655440602', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440704', 'Operating Systems', 'CS301', 'OS concepts and design', 'THEORY', 4, '550e8400-e29b-41d4-a716-446655440603', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440705', 'Digital Electronics', 'EC101', 'Basic digital circuits', 'THEORY_LAB', 3, '550e8400-e29b-41d4-a716-446655440604', false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Student Education Info
INSERT INTO student_education_info (id, student_semester, student_id, semester_status, program_id, is_active, branch, graduation_type, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440801', 1, '550e8400-e29b-41d4-a716-446655440301', 'IN_PROGRESS', '550e8400-e29b-41d4-a716-446655440001', true, 'CSE', 'Regular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440802', 1, '550e8400-e29b-41d4-a716-446655440302', 'IN_PROGRESS', '550e8400-e29b-41d4-a716-446655440001', true, 'CSE', 'Regular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440803', 1, '550e8400-e29b-41d4-a716-446655440303', 'IN_PROGRESS', '550e8400-e29b-41d4-a716-446655440001', true, 'CSE', 'Regular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440804', 1, '550e8400-e29b-41d4-a716-446655440304', 'IN_PROGRESS', '550e8400-e29b-41d4-a716-446655440001', true, 'ECE', 'Regular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440805', 1, '550e8400-e29b-41d4-a716-446655440305', 'IN_PROGRESS', '550e8400-e29b-41d4-a716-446655440001', true, 'CSE', 'Regular', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Student Sections
INSERT INTO student_section (id, section_name, semester, program_id, is_active, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655440901', 'A', 1, '550e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655440902', 'B', 1, '550e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Update Users with Section
UPDATE users SET section_id = '550e8400-e29b-41d4-a716-446655440901' WHERE id IN ('550e8400-e29b-41d4-a716-446655440301', '550e8400-e29b-41d4-a716-446655440302', '550e8400-e29b-41d4-a716-446655440303');
UPDATE users SET section_id = '550e8400-e29b-41d4-a716-446655440902' WHERE id IN ('550e8400-e29b-41d4-a716-446655440304', '550e8400-e29b-41d4-a716-446655440305');

-- Insert Lecturer Course Assignments
INSERT INTO lecture_details (id, lecture_id, assignment_type, status, subject_type, graduation_lecturer, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655441001', '550e8400-e29b-41d4-a716-446655440201', 'LECTURER', true, 'MATHS', 'CSE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441002', '550e8400-e29b-41d4-a716-446655440202', 'LECTURER', true, 'PHYSICS', 'CSE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441003', '550e8400-e29b-41d4-a716-446655440203', 'LECTURER', true, 'CHEMISTRY', 'ECE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Sample Attendance Records
INSERT INTO attendance (id, student_id, course_id, attendance_date, status, marked_by, remarks, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655441101', '550e8400-e29b-41d4-a716-446655440301', '550e8400-e29b-41d4-a716-446655440701', CURRENT_DATE - INTERVAL '5 days', 'PRESENT', '550e8400-e29b-41d4-a716-446655440201', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441102', '550e8400-e29b-41d4-a716-446655440302', '550e8400-e29b-41d4-a716-446655440701', CURRENT_DATE - INTERVAL '5 days', 'PRESENT', '550e8400-e29b-41d4-a716-446655440201', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441103', '550e8400-e29b-41d4-a716-446655440303', '550e8400-e29b-41d4-a716-446655440701', CURRENT_DATE - INTERVAL '5 days', 'ABSENT', '550e8400-e29b-41d4-a716-446655440201', 'Sick leave', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441104', '550e8400-e29b-41d4-a716-446655440301', '550e8400-e29b-41d4-a716-446655440702', CURRENT_DATE - INTERVAL '3 days', 'PRESENT', '550e8400-e29b-41d4-a716-446655440202', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441105', '550e8400-e29b-41d4-a716-446655440302', '550e8400-e29b-41d4-a716-446655440702', CURRENT_DATE - INTERVAL '3 days', 'LATE', '550e8400-e29b-41d4-a716-446655440202', 'Traffic', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Sample Marks
INSERT INTO marks (id, student_id, course_id, exam_type, marks_obtained, max_marks, exam_date, evaluated_by, remarks, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655441201', '550e8400-e29b-41d4-a716-446655440301', '550e8400-e29b-41d4-a716-446655440701', 'INTERNAL', 85.5, 100.0, CURRENT_DATE - INTERVAL '10 days', '550e8400-e29b-41d4-a716-446655440201', 'Good performance', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441202', '550e8400-e29b-41d4-a716-446655440302', '550e8400-e29b-41d4-a716-446655440701', 'INTERNAL', 92.0, 100.0, CURRENT_DATE - INTERVAL '10 days', '550e8400-e29b-41d4-a716-446655440201', 'Excellent', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441203', '550e8400-e29b-41d4-a716-446655440303', '550e8400-e29b-41d4-a716-446655440701', 'INTERNAL', 78.0, 100.0, CURRENT_DATE - INTERVAL '10 days', '550e8400-e29b-41d4-a716-446655440201', 'Can improve', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441204', '550e8400-e29b-41d4-a716-446655440301', '550e8400-e29b-41d4-a716-446655440701', 'MIDTERM', 88.0, 100.0, CURRENT_DATE - INTERVAL '5 days', '550e8400-e29b-41d4-a716-446655440201', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441205', '550e8400-e29b-41d4-a716-446655440302', '550e8400-e29b-41d4-a716-446655440701', 'MIDTERM', 95.0, 100.0, CURRENT_DATE - INTERVAL '5 days', '550e8400-e29b-41d4-a716-446655440201', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Sample Assignments
INSERT INTO assignments (id, course_id, title, description, due_date, max_marks, created_by, is_active, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655441301', '550e8400-e29b-41d4-a716-446655440701', 'Assignment 1: Array Operations', 'Implement basic array operations', CURRENT_DATE + INTERVAL '7 days', 100.0, '550e8400-e29b-41d4-a716-446655440201', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441302', '550e8400-e29b-41d4-a716-446655440702', 'Assignment 1: Database Design', 'Design ER diagram for library system', CURRENT_DATE + INTERVAL '10 days', 100.0, '550e8400-e29b-41d4-a716-446655440202', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- Insert Sample Student Assignments
INSERT INTO student_assignments (id, assignment_id, student_id, submission_date, file_url, marks_obtained, feedback, status, created_at, updated_at)
VALUES 
    ('550e8400-e29b-41d4-a716-446655441401', '550e8400-e29b-41d4-a716-446655441301', '550e8400-e29b-41d4-a716-446655440301', CURRENT_DATE - INTERVAL '2 days', 'https://example.com/submissions/assignment1_stu1.pdf', 85.0, 'Well done!', 'GRADED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441402', '550e8400-e29b-41d4-a716-446655441301', '550e8400-e29b-41d4-a716-446655440302', CURRENT_DATE - INTERVAL '1 days', 'https://example.com/submissions/assignment1_stu2.pdf', 92.0, 'Excellent work!', 'GRADED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('550e8400-e29b-41d4-a716-446655441403', '550e8400-e29b-41d4-a716-446655441301', '550e8400-e29b-41d4-a716-446655440303', NULL, NULL, NULL, NULL, 'NOT_SUBMITTED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

