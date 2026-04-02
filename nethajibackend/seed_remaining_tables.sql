-- Seed remaining tables with correct schema

-- ============================================
-- STUDENT SECTIONS (Corrected schema)
-- ============================================

INSERT INTO student_sections (
    id, student_id, section_name, branch, semester, 
    department_id, program_code, semester_year, semester_start_date,
    status, created_at, updated_at
) VALUES
-- CSE Students Section A
('f50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440011', 'A', 3, 1,
 '650e8400-e29b-41d4-a716-446655440001', 'BTECH', '2024', '2024-07-01', true, NOW(), NOW()),
('f50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440012', 'A', 3, 1,
 '650e8400-e29b-41d4-a716-446655440001', 'BTECH', '2024', '2024-07-01', true, NOW(), NOW()),
('f50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440013', 'A', 3, 2,
 '650e8400-e29b-41d4-a716-446655440001', 'BTECH', '2024', '2024-01-01', true, NOW(), NOW()),
('f50e8400-e29b-41d4-a716-446655440004', '950e8400-e29b-41d4-a716-446655440014', 'A', 3, 2,
 '650e8400-e29b-41d4-a716-446655440001', 'BTECH', '2024', '2024-01-01', true, NOW(), NOW()),
('f50e8400-e29b-41d4-a716-446655440005', '950e8400-e29b-41d4-a716-446655440015', 'A', 3, 3,
 '650e8400-e29b-41d4-a716-446655440001', 'BTECH', '2024', '2024-07-01', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- STUDENT PROGRAMS (Corrected schema)
-- ============================================

INSERT INTO student_programs (id, student_id, program_id, enrollment_status, is_active, created_at, updated_at) VALUES
('g50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440011', '550e8400-e29b-41d4-a716-446655440001', 'ACTIVE', true, NOW(), NOW()),
('g50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440012', '550e8400-e29b-41d4-a716-446655440001', 'ACTIVE', true, NOW(), NOW()),
('g50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440013', '550e8400-e29b-41d4-a716-446655440001', 'ACTIVE', true, NOW(), NOW()),
('g50e8400-e29b-41d4-a716-446655440004', '950e8400-e29b-41d4-a716-446655440014', '550e8400-e29b-41d4-a716-446655440001', 'ACTIVE', true, NOW(), NOW()),
('g50e8400-e29b-41d4-a716-446655440005', '950e8400-e29b-41d4-a716-446655440015', '550e8400-e29b-41d4-a716-446655440001', 'ACTIVE', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- ATTENDANCE (Corrected - added marked_by)
-- ============================================

INSERT INTO attendance (id, student_id, course_id, attendance_date, status, marked_by, remarks, created_at, updated_at) VALUES
-- Student 1 attendance
(gen_random_uuid(), '950e8400-e29b-41d4-a716-446655440011', '850e8400-e29b-41d4-a716-446655440001', '2024-07-15', 'PRESENT', '950e8400-e29b-41d4-a716-446655440001', NULL, NOW(), NOW()),
(gen_random_uuid(), '950e8400-e29b-41d4-a716-446655440011', '850e8400-e29b-41d4-a716-446655440001', '2024-07-16', 'PRESENT', '950e8400-e29b-41d4-a716-446655440001', NULL, NOW(), NOW()),
(gen_random_uuid(), '950e8400-e29b-41d4-a716-446655440011', '850e8400-e29b-41d4-a716-446655440001', '2024-07-17', 'ABSENT', '950e8400-e29b-41d4-a716-446655440001', 'Sick', NOW(), NOW()),
-- Student 2 attendance
(gen_random_uuid(), '950e8400-e29b-41d4-a716-446655440012', '850e8400-e29b-41d4-a716-446655440001', '2024-07-15', 'PRESENT', '950e8400-e29b-41d4-a716-446655440001', NULL, NOW(), NOW()),
(gen_random_uuid(), '950e8400-e29b-41d4-a716-446655440012', '850e8400-e29b-41d4-a716-446655440001', '2024-07-16', 'PRESENT', '950e8400-e29b-41d4-a716-446655440001', NULL, NOW(), NOW()),
-- Student 3 attendance
(gen_random_uuid(), '950e8400-e29b-41d4-a716-446655440013', '850e8400-e29b-41d4-a716-446655440011', '2024-07-15', 'PRESENT', '950e8400-e29b-41d4-a716-446655440002', NULL, NOW(), NOW()),
(gen_random_uuid(), '950e8400-e29b-41d4-a716-446655440013', '850e8400-e29b-41d4-a716-446655440011', '2024-07-16', 'LATE', '950e8400-e29b-41d4-a716-446655440002', '10 mins late', NOW(), NOW());

-- ============================================
-- ASSIGNMENTS (Corrected - added is_active)
-- ============================================

INSERT INTO assignments (id, course_id, title, description, due_date, max_marks, is_active, created_by, created_at, updated_at) VALUES
-- Programming in C assignments
(gen_random_uuid(), '850e8400-e29b-41d4-a716-446655440001', 
 'Basic C Programs', 'Write programs for basic operations', '2024-08-15', 20, true,
 '950e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
(gen_random_uuid(), '850e8400-e29b-41d4-a716-446655440001',
 'Arrays and Strings', 'Implement array and string operations', '2024-08-30', 25, true,
 '950e8400-e29b-41d4-a716-446655440001', NOW(), NOW()),
-- Data Structures assignments
(gen_random_uuid(), '850e8400-e29b-41d4-a716-446655440011',
 'Linked List Implementation', 'Implement singly and doubly linked lists', '2024-09-15', 30, true,
 '950e8400-e29b-41d4-a716-446655440002', NOW(), NOW()),
-- DBMS assignments
(gen_random_uuid(), '850e8400-e29b-41d4-a716-446655440021',
 'SQL Queries', 'Write complex SQL queries', '2024-09-20', 25, true,
 '950e8400-e29b-41d4-a716-446655440003', NOW(), NOW());

-- ============================================
-- VERIFICATION
-- ============================================

SELECT 'Student Sections' as table_name, COUNT(*) as count FROM student_sections;
SELECT 'Student Programs' as table_name, COUNT(*) as count FROM student_programs;
SELECT 'Attendance Records' as table_name, COUNT(*) as count FROM attendance;
SELECT 'Assignments' as table_name, COUNT(*) as count FROM assignments;

-- Final Complete Summary
SELECT 
    'ALL TABLES COMPLETE' as status,
    (SELECT COUNT(*) FROM programs) as programs,
    (SELECT COUNT(*) FROM department) as departments,
    (SELECT COUNT(*) FROM course) as courses,
    (SELECT COUNT(*) FROM users WHERE user_type = 'STUDENT') as students,
    (SELECT COUNT(*) FROM users WHERE user_type = 'LECTURER') as lecturers,
    (SELECT COUNT(*) FROM student_profile) as student_profiles,
    (SELECT COUNT(*) FROM staff_profile) as staff_profiles,
    (SELECT COUNT(*) FROM student_education_info) as student_education,
    (SELECT COUNT(*) FROM student_sections) as student_sections,
    (SELECT COUNT(*) FROM student_programs) as student_programs,
    (SELECT COUNT(*) FROM attendance) as attendance_records,
    (SELECT COUNT(*) FROM assignments) as assignments;
