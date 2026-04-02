-- Final Complete Seed Data for Nethaji College Management System
-- This script adds the remaining lecturer details and course assignments

-- ============================================
-- LECTURER DETAILS (with correct subject_type values)
-- ============================================

INSERT INTO lecture_details (id, lecture_id, assignment_type, subject_type, graduation_lecturer, created_at, updated_at) VALUES
('a50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440001', 'LECTURER', 'COMPUTER_SCIENCE', 'CSE', NOW(), NOW()),
('a50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440002', 'LECTURER', 'DATA_SCIENCE', 'CSE', NOW(), NOW()),
('a50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440003', 'LECTURER', 'COMPUTER_SCIENCE', 'CSE', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- LECTURER COURSE ASSIGNMENTS (with correct columns)
-- ============================================

INSERT INTO lecturer_course_assignments (id, lecturer_id, course_id, assignment_type, subject_type, is_active, created_at, updated_at) VALUES
('c50e8400-e29b-41d4-a716-446655440001', '950e8400-e29b-41d4-a716-446655440001', '850e8400-e29b-41d4-a716-446655440001', 'LECTURER', 'COMPUTER_SCIENCE', true, NOW(), NOW()),
('c50e8400-e29b-41d4-a716-446655440002', '950e8400-e29b-41d4-a716-446655440002', '850e8400-e29b-41d4-a716-446655440011', 'LECTURER', 'DATA_SCIENCE', true, NOW(), NOW()),
('c50e8400-e29b-41d4-a716-446655440003', '950e8400-e29b-41d4-a716-446655440003', '850e8400-e29b-41d4-a716-446655440021', 'LECTURER', 'COMPUTER_SCIENCE', true, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- ============================================
-- FINAL VERIFICATION
-- ============================================

SELECT 'Lecturer Details' as table_name, COUNT(*) as count FROM lecture_details;
SELECT 'Lecturer Assignments' as table_name, COUNT(*) as count FROM lecturer_course_assignments;

-- Complete Summary
SELECT 
    'COMPLETE SEED DATA' as info,
    (SELECT COUNT(*) FROM programs) as programs,
    (SELECT COUNT(*) FROM department) as departments,
    (SELECT COUNT(*) FROM department_semesters) as semesters,
    (SELECT COUNT(*) FROM course) as courses,
    (SELECT COUNT(*) FROM users WHERE user_type = 'STUDENT') as students,
    (SELECT COUNT(*) FROM users WHERE user_type = 'LECTURER') as lecturers,
    (SELECT COUNT(*) FROM users WHERE user_type = 'ADMIN') as admins,
    (SELECT COUNT(*) FROM lecture_details) as lecturer_details,
    (SELECT COUNT(*) FROM lecturer_course_assignments) as course_assignments;
