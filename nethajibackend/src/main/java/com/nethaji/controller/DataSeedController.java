package com.nethaji.controller;

import com.nethaji.Enums.Branch;
import com.nethaji.Enums.EnrollmentStatus;
import com.nethaji.entity.*;
import com.nethaji.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/nethaji-service/admin")
@CrossOrigin
public class DataSeedController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgramsRepository programsRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private StaffProfileRepo staffProfileRepo;

    @Autowired
    private StudentEducationInfoRepository studentEducationInfoRepository;

    @Autowired
    private StudentProgramsRepository studentProgramsRepository;

    @Autowired
    private StudentSectionRepository studentSectionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentAssignmentRepository studentAssignmentRepository;

    @Autowired
    private DepartMentSemestersRepo departMentSemestersRepo;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @PostMapping("/seed-data")
    public ResponseEntity<Map<String, Object>> seedData() {
        Map<String, Object> response = new HashMap<>();
        List<String> messages = new ArrayList<>();

        messages.add("seed_version=v3_degree_upsert");

        boolean hasErrors = false;

        TransactionTemplate tx = new TransactionTemplate(transactionManager);

        // Seed Programs
        try { tx.execute(status -> { seedPrograms(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Programs: " + e.getMessage()); hasErrors = true; }

        // Seed Departments
        try { tx.execute(status -> { seedDepartments(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Departments: " + e.getMessage()); hasErrors = true; }

        // Seed Department Semesters
        try { tx.execute(status -> { seedDepartmentSemesters(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Department Semesters: " + e.getMessage()); hasErrors = true; }

        // Seed Users
        try { tx.execute(status -> { seedUsers(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Users: " + e.getMessage()); hasErrors = true; }

        // Seed Student Programs
        try { tx.execute(status -> { seedStudentPrograms(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Student Programs: " + e.getMessage()); hasErrors = true; }

        // Seed Student Education Info
        try { tx.execute(status -> { seedStudentEducationInfo(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Student Education Info: " + e.getMessage()); hasErrors = true; }

        // Seed Student Sections
        try { tx.execute(status -> { seedStudentSections(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Student Sections: " + e.getMessage()); hasErrors = true; }

        // Seed Student Profiles
        try { tx.execute(status -> { seedStudentProfiles(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Student Profiles: " + e.getMessage()); hasErrors = true; }

        // Seed Staff Profiles
        try { tx.execute(status -> { seedStaffProfiles(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Staff Profiles: " + e.getMessage()); hasErrors = true; }

        // Seed Courses
        try { tx.execute(status -> { seedCourses(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Courses: " + e.getMessage()); hasErrors = true; }

        // Seed Lecturer Course Assignments
        try { tx.execute(status -> { seedLecturerCourseAssignments(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Lecturer Course Assignments: " + e.getMessage()); hasErrors = true; }

        // Seed Attendance
        try { tx.execute(status -> { seedAttendance(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Attendance: " + e.getMessage()); hasErrors = true; }

        // Seed Marks
        try { tx.execute(status -> { seedMarks(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Marks: " + e.getMessage()); hasErrors = true; }

        // Seed Grades
        try { tx.execute(status -> { seedGrades(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Grades: " + e.getMessage()); hasErrors = true; }

        // Seed Assignments
        try { tx.execute(status -> { seedAssignments(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Assignments: " + e.getMessage()); hasErrors = true; }

        // Seed Student Assignments
        try { tx.execute(status -> { seedStudentAssignments(messages); return null; }); }
        catch (Exception e) { messages.add("✗ Student Assignments: " + e.getMessage()); hasErrors = true; }

        response.put("success", !hasErrors);
        response.put("message", hasErrors ? "Seed completed with errors" : "Data seeded successfully");
        response.put("details", messages);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/seed-data")
    public ResponseEntity<Map<String, Object>> seedDataGet() {
        return seedData();
    }

    private void seedPrograms(List<String> messages) {
        int upserted = 0;

        UUID bscId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        upserted += upsertProgram(
                bscId,
                "Bachelor of Science",
                "BSC",
                com.nethaji.Enums.ProgramLevel.BSC.name(),
                3
        );

        UUID bcomId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        upserted += upsertProgram(
                bcomId,
                "Bachelor of Commerce",
                "BCOM",
                com.nethaji.Enums.ProgramLevel.BCOM.name(),
                3
        );

        UUID bbaId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");
        upserted += upsertProgram(
                bbaId,
                "Bachelor of Business Administration",
                "BBA",
                com.nethaji.Enums.ProgramLevel.BBA.name(),
                3
        );

        messages.add("✓ Upserted Programs (" + upserted + " applied)");
    }

    private int upsertProgram(UUID id, String name, String programCode, String level, int durationYears) {
        return entityManager.createNativeQuery(
                        "INSERT INTO programs (id, name, program_code, level, duration_years, is_active, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, true, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "program_code = EXCLUDED.program_code, " +
                                "level = EXCLUDED.level, " +
                                "duration_years = EXCLUDED.duration_years, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, id)
                .setParameter(2, name)
                .setParameter(3, programCode)
                .setParameter(4, level)
                .setParameter(5, durationYears)
                .executeUpdate();
    }

    private void seedDepartments(List<String> messages) {
        int upserted = 0;

        UUID bscId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID bcomId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        UUID bbaId = UUID.fromString("550e8400-e29b-41d4-a716-446655440003");

        UUID bscDsId = UUID.fromString("550e8400-e29b-41d4-a716-446655440011");
        upserted += upsertDepartment(bscDsId, "BSC_DS", "B.Sc (Data Science)", bscId);

        UUID bscFoodId = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");
        upserted += upsertDepartment(bscFoodId, "BSC_FS", "B.Sc (Food Science)", bscId);

        UUID bcomCaId = UUID.fromString("550e8400-e29b-41d4-a716-446655440013");
        upserted += upsertDepartment(bcomCaId, "BCOM_CA", "B.Com (CA)", bcomId);

        UUID bcomBaId = UUID.fromString("550e8400-e29b-41d4-a716-446655440014");
        upserted += upsertDepartment(bcomBaId, "BCOM_BA", "B.Com (Business Analytics)", bcomId);

        UUID bbaAiId = UUID.fromString("550e8400-e29b-41d4-a716-446655440015");
        upserted += upsertDepartment(bbaAiId, "BBA_AI", "BBA (Artificial Intelligence)", bbaId);

        UUID bscMpcsId = UUID.fromString("550e8400-e29b-41d4-a716-446655440016");
        upserted += upsertDepartment(bscMpcsId, "BSC_MPCS", "B.Sc (MPCs)", bscId);

        messages.add("✓ Upserted Departments (" + upserted + " applied)");
    }

    private int upsertDepartment(UUID id, String departmentCode, String departmentName, UUID programId) {
        return entityManager.createNativeQuery(
                        "INSERT INTO department (id, department_code, department_name, program_id, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "department_code = EXCLUDED.department_code, " +
                                "department_name = EXCLUDED.department_name, " +
                                "program_id = EXCLUDED.program_id, " +
                                "updated_at = now()"
                )
                .setParameter(1, id)
                .setParameter(2, departmentCode)
                .setParameter(3, departmentName)
                .setParameter(4, programId)
                .executeUpdate();
    }

    private void seedUsers(List<String> messages) {
        // Update existing users' passwords if they don't have proper BCrypt hash
        List<User> existingUsers = userRepository.findAll();
        int updatedCount = 0;
        for (User user : existingUsers) {
            if (user.getPasswordHash() != null && 
                (user.getSalt() != null || 
                 !user.getPasswordHash().startsWith("$2a$") || 
                 user.getPasswordHash().equals("$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"))) {
                // Update password to use proper BCrypt hash
                user.setPasswordHash(passwordEncoder.encode("admin123"));
                user.setSalt(null);
                userRepository.save(user);
                updatedCount++;
            }
        }
        if (updatedCount > 0) {
            messages.add("✓ Updated " + updatedCount + " existing user passwords");
        }

        int created = 0;
        int updated = 0;

        // Ensure Admin
        UUID adminId = UUID.fromString("550e8400-e29b-41d4-a716-446655440101");
        int[] adminRes = ensureUserByEmail(
                adminId,
                "ADMIN001",
                "admin@nethaji.edu",
                "Super",
                "Admin",
                "9876543210",
                "+91",
                User.UserType.SUPER_ADMIN
        );
        created += adminRes[0];
        updated += adminRes[1];

        // Ensure Lecturer
        UUID lecturerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440201");
        int[] lecturerRes = ensureUserByEmail(
                lecturerId,
                "LEC001",
                "prof.sharma@nethaji.edu",
                "Rajesh",
                "Sharma",
                "9876543220",
                "+91",
                User.UserType.LECTURER
        );
        created += lecturerRes[0];
        updated += lecturerRes[1];

        // Ensure Students
        for (int i = 1; i <= 5; i++) {
            UUID studentId = UUID.fromString("550e8400-e29b-41d4-a716-44665544030" + i);
            int[] studentRes = ensureUserByEmail(
                    studentId,
                    "STU202400" + i,
                    "student" + i + "@nethaji.edu",
                    "Student" + i,
                    "Name" + i,
                    "987654330" + i,
                    "+91",
                    User.UserType.STUDENT
            );
            created += studentRes[0];
            updated += studentRes[1];
        }

        if (created > 0) {
            messages.add("✓ Seeded Users (" + created + " inserted)");
        }
        if (updated > 0) {
            messages.add("✓ Updated Seed Users (" + updated + " updated)");
        }
        if (created == 0 && updated == 0) {
            messages.add("⚠ Users already exist");
        }
    }

    private int[] ensureUserByEmail(UUID fallbackId,
                                   String enrollmentNumber,
                                   String email,
                                   String firstName,
                                   String lastName,
                                   String mobileNumber,
                                   String countryCode,
                                   User.UserType userType) {
        Optional<User> existingByEmail = userRepository.findByEmail(email);
        if (existingByEmail.isPresent()) {
            User user = existingByEmail.get();
            boolean changed = false;

            if (user.getEnrollmentNumber() == null || user.getEnrollmentNumber().isEmpty()) {
                user.setEnrollmentNumber(enrollmentNumber);
                changed = true;
            }

            if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
                user.setFirstName(firstName);
                changed = true;
            }
            if (user.getLastName() == null || user.getLastName().isEmpty()) {
                user.setLastName(lastName);
                changed = true;
            }
            if (user.getMobileNumber() == null || user.getMobileNumber().isEmpty()) {
                user.setMobileNumber(mobileNumber);
                changed = true;
            }
            if (user.getCountryCode() == null || user.getCountryCode().isEmpty()) {
                user.setCountryCode(countryCode);
                changed = true;
            }
            if (user.getIsActive() == null || !user.getIsActive()) {
                user.setIsActive(true);
                changed = true;
            }
            if (user.getUserType() != userType) {
                user.setUserType(userType);
                changed = true;
            }

            // Force known login password for seeded users
            String desiredHash = passwordEncoder.encode("admin123");
            user.setPasswordHash(desiredHash);
            user.setSalt(null);
            user.setUpdatedAt(new Date());
            changed = true;

            if (changed) {
                userRepository.save(user);
                return new int[]{0, 1};
            }
            return new int[]{0, 0};
        }

        int inserted = insertUserIfMissing(
                fallbackId,
                enrollmentNumber,
                email,
                firstName,
                lastName,
                mobileNumber,
                countryCode,
                passwordEncoder.encode("admin123"),
                userType.name(),
                2024
        );
        return new int[]{inserted, 0};
    }

    private int insertUserIfMissing(UUID id,
                                   String enrollmentNumber,
                                   String email,
                                   String firstName,
                                   String lastName,
                                   String mobileNumber,
                                   String countryCode,
                                   String passwordHash,
                                   String userType,
                                   int joiningYear) {
        if (userRepository.existsById(id)) {
            return 0;
        }

        return entityManager.createNativeQuery(
                        "INSERT INTO users (id, enrollment_number, email, first_name, last_name, mobile_number, country_code, password_hash, salt, user_type, is_active, joining_year, created_at, updated_at, last_login, section_id) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, NULL, ?9, true, ?10, now(), now(), NULL, NULL) " +
                                "ON CONFLICT (id) DO NOTHING"
                )
                .setParameter(1, id)
                .setParameter(2, enrollmentNumber)
                .setParameter(3, email)
                .setParameter(4, firstName)
                .setParameter(5, lastName)
                .setParameter(6, mobileNumber)
                .setParameter(7, countryCode)
                .setParameter(8, passwordHash)
                .setParameter(9, userType)
                .setParameter(10, joiningYear)
                .executeUpdate();
    }

    private void seedStudentProfiles(List<String> messages) {
        int created = 0;

        List<User> students = userRepository.findUsersByType(User.UserType.STUDENT);
        for (User student : students) {
            if (studentProfileRepository.findByUserId(student.getId()).isPresent()) {
                continue;
            }

            StudentProfile profile = new StudentProfile();
            profile.setUserId(student.getId());
            profile.setIsActive(true);
            profile.setCreatedAt(new Date());
            profile.setUpdatedAt(new Date());
            studentProfileRepository.save(profile);
            created++;
        }

        if (created > 0) {
            messages.add("✓ Seeded " + created + " Student Profiles");
        } else {
            messages.add("⚠ Student Profiles already exist");
        }
    }

    private void seedStaffProfiles(List<String> messages) {
        int created = 0;

        List<User> staffUsers = userRepository.findUsersByType(User.UserType.LECTURER);
        for (User staff : staffUsers) {
            StaffProfile existing = staffProfileRepo.findByUserId(staff.getId());
            if (existing != null) {
                continue;
            }

            StaffProfile sp = new StaffProfile();
            sp.setUserId(staff.getId());
            sp.setFirstName(staff.getFirstName() != null ? staff.getFirstName() : "Staff");
            sp.setLastName(staff.getLastName() != null ? staff.getLastName() : "User");
            sp.setDepartment("CSE");
            sp.setDesignation("Lecturer");
            sp.setJoiningDate(LocalDate.now().minusYears(2));
            sp.setCreatedAt(new Date());
            sp.setUpdatedAt(new Date());
            sp.setEmployeeSubject("PHYSICS");
            staffProfileRepo.save(sp);
            created++;
        }

        if (created > 0) {
            messages.add("✓ Seeded " + created + " Staff Profiles");
        } else {
            messages.add("⚠ Staff Profiles already exist");
        }
    }

    private void seedStudentPrograms(List<String> messages) {
        int created = 0;
        UUID bscId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        List<User> students = userRepository.findUsersByType(User.UserType.STUDENT);
        for (User student : students) {
            StudentPrograms existing = studentProgramsRepository.findByStudentIdAndIsActiveTrue(student.getId());
            if (existing != null) {
                continue;
            }

            StudentPrograms sp = new StudentPrograms();
            sp.setStudentId(student.getId());
            sp.setProgramId(bscId);
            sp.setEnrollmentStatus(EnrollmentStatus.NOT_STARTED);
            sp.setActive(true);
            sp.setCreatedAt(new Date());
            sp.setUpdatedAt(new Date());
            studentProgramsRepository.save(sp);
            created++;
        }

        if (created > 0) {
            messages.add("✓ Seeded " + created + " Student Programs");
        } else {
            messages.add("⚠ Student Programs already exist");
        }
    }

    private void seedStudentEducationInfo(List<String> messages) {
        int created = 0;
        UUID bscId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        List<User> students = userRepository.findUsersByType(User.UserType.STUDENT);
        for (User student : students) {
            if (studentEducationInfoRepository.findByStudentIdAndSemesterAndIsActiveTrue(student.getId(), 1).isPresent()) {
                continue;
            }

            StudentEducationInfo info = new StudentEducationInfo();
            info.setStudentId(student.getId());
            info.setSemester(1);
            info.setProgramId(bscId);
            info.setBranch(Branch.BSC);
            info.setGraduationType("BSC_DS");
            info.setSemesterStatus(SemesterStatus.NOT_STARTED);
            info.setActive(true);
            info.setCreatedAt(new Date());
            info.setUpdatedAt(new Date());
            studentEducationInfoRepository.save(info);
            created++;
        }

        if (created > 0) {
            messages.add("✓ Seeded " + created + " Student Education Info");
        } else {
            messages.add("⚠ Student Education Info already exist");
        }
    }

    private void seedStudentSections(List<String> messages) {
        int created = 0;

        UUID bscDsDeptId = UUID.fromString("550e8400-e29b-41d4-a716-446655440011");

        List<User> students = userRepository.findUsersByType(User.UserType.STUDENT);
        for (User student : students) {
            StudentSection existing = studentSectionRepository.findActiveSectionByStudentAndSemester(student.getId(), 1);
            if (existing != null) {
                continue;
            }

            StudentSection section = new StudentSection();
            section.setStudentId(student.getId());
            section.setSectionName("A");
            section.setProgramCode("BSC");
            section.setDepartmentId(bscDsDeptId);
            section.setStatus(true);
            section.setSemester(1);
            section.setBranch(Branch.BSC);
            section.setSemesterStartDate(new Date());
            section.setSemesterYear("2024-2025");
            section.setCreatedAt(new Date());
            section.setUpdatedAt(new Date());
            studentSectionRepository.save(section);
            created++;
        }

        if (created > 0) {
            messages.add("✓ Seeded " + created + " Student Sections");
        } else {
            messages.add("⚠ Student Sections already exist");
        }
    }

    private void seedDepartmentSemesters(List<String> messages) {
        try {
            int inserted = 0;

            UUID bscDsDeptId = UUID.fromString("550e8400-e29b-41d4-a716-446655440011");
            UUID bscFsDeptId = UUID.fromString("550e8400-e29b-41d4-a716-446655440012");
            UUID bcomCaDeptId = UUID.fromString("550e8400-e29b-41d4-a716-446655440013");
            UUID bcomBaDeptId = UUID.fromString("550e8400-e29b-41d4-a716-446655440014");
            UUID bbaAiDeptId = UUID.fromString("550e8400-e29b-41d4-a716-446655440015");
            UUID bscMpcsDeptId = UUID.fromString("550e8400-e29b-41d4-a716-446655440016");

            for (int sem = 1; sem <= 3; sem++) {
                UUID deptSemId = UUID.fromString("550e8400-e29b-41d4-a716-44665544060" + sem);
                inserted += entityManager.createNativeQuery(
                                "INSERT INTO department_semesters (id, department_id, semester, semester_year, semester_subjects, semester_total_labs) " +
                                        "VALUES (?1, ?2, ?3, ?4, ?5, ?6) ON CONFLICT (id) DO NOTHING"
                        )
                        .setParameter(1, deptSemId)
                        .setParameter(2, bscDsDeptId)
                        .setParameter(3, sem)
                        .setParameter(4, "2024-2025")
                        .setParameter(5, 5)
                        .setParameter(6, 2)
                        .executeUpdate();
            }

            for (int sem = 1; sem <= 3; sem++) {
                UUID deptSemId = UUID.fromString("550e8400-e29b-41d4-a716-44665544060" + (3 + sem));
                inserted += entityManager.createNativeQuery(
                                "INSERT INTO department_semesters (id, department_id, semester, semester_year, semester_subjects, semester_total_labs) " +
                                        "VALUES (?1, ?2, ?3, ?4, ?5, ?6) ON CONFLICT (id) DO NOTHING"
                        )
                        .setParameter(1, deptSemId)
                        .setParameter(2, bscFsDeptId)
                        .setParameter(3, sem)
                        .setParameter(4, "2024-2025")
                        .setParameter(5, 5)
                        .setParameter(6, 2)
                        .executeUpdate();
            }

            for (int sem = 1; sem <= 3; sem++) {
                UUID deptSemId = UUID.fromString("550e8400-e29b-41d4-a716-44665544061" + sem);
                inserted += entityManager.createNativeQuery(
                                "INSERT INTO department_semesters (id, department_id, semester, semester_year, semester_subjects, semester_total_labs) " +
                                        "VALUES (?1, ?2, ?3, ?4, ?5, ?6) ON CONFLICT (id) DO NOTHING"
                        )
                        .setParameter(1, deptSemId)
                        .setParameter(2, bcomCaDeptId)
                        .setParameter(3, sem)
                        .setParameter(4, "2024-2025")
                        .setParameter(5, 5)
                        .setParameter(6, 2)
                        .executeUpdate();
            }

            for (int sem = 1; sem <= 3; sem++) {
                UUID deptSemId = UUID.fromString("550e8400-e29b-41d4-a716-44665544062" + sem);
                inserted += entityManager.createNativeQuery(
                                "INSERT INTO department_semesters (id, department_id, semester, semester_year, semester_subjects, semester_total_labs) " +
                                        "VALUES (?1, ?2, ?3, ?4, ?5, ?6) ON CONFLICT (id) DO NOTHING"
                        )
                        .setParameter(1, deptSemId)
                        .setParameter(2, bcomBaDeptId)
                        .setParameter(3, sem)
                        .setParameter(4, "2024-2025")
                        .setParameter(5, 5)
                        .setParameter(6, 2)
                        .executeUpdate();
            }

            for (int sem = 1; sem <= 3; sem++) {
                UUID deptSemId = UUID.fromString("550e8400-e29b-41d4-a716-44665544063" + sem);
                inserted += entityManager.createNativeQuery(
                                "INSERT INTO department_semesters (id, department_id, semester, semester_year, semester_subjects, semester_total_labs) " +
                                        "VALUES (?1, ?2, ?3, ?4, ?5, ?6) ON CONFLICT (id) DO NOTHING"
                        )
                        .setParameter(1, deptSemId)
                        .setParameter(2, bbaAiDeptId)
                        .setParameter(3, sem)
                        .setParameter(4, "2024-2025")
                        .setParameter(5, 5)
                        .setParameter(6, 2)
                        .executeUpdate();
            }

            for (int sem = 1; sem <= 3; sem++) {
                UUID deptSemId = UUID.fromString("550e8400-e29b-41d4-a716-44665544064" + sem);
                inserted += entityManager.createNativeQuery(
                                "INSERT INTO department_semesters (id, department_id, semester, semester_year, semester_subjects, semester_total_labs) " +
                                        "VALUES (?1, ?2, ?3, ?4, ?5, ?6) ON CONFLICT (id) DO NOTHING"
                        )
                        .setParameter(1, deptSemId)
                        .setParameter(2, bscMpcsDeptId)
                        .setParameter(3, sem)
                        .setParameter(4, "2024-2025")
                        .setParameter(5, 5)
                        .setParameter(6, 2)
                        .executeUpdate();
            }

            if (inserted > 0) {
                messages.add("✓ Seeded Department Semesters (" + inserted + " inserted)");
            } else {
                messages.add("⚠ Department Semesters already exist");
            }
        } catch (Exception e) {
            messages.add("⚠ Error seeding semesters: " + e.getMessage());
        }
    }

    private void seedCourses(List<String> messages) {
        int upserted = 0;

        UUID bscDsSem1Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440601");
        UUID bscDsSem2Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440602");
        UUID bscFsSem1Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440604");
        UUID bscMpcsSem1Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440641");
        UUID bcomBaSem1Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440621");
        UUID bcomCaSem1Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440611");
        UUID bbaAiSem1Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440631");

        UUID course1Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440701");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course1Id)
                .setParameter(2, "Introduction to Data Science")
                .setParameter(3, "DS101")
                .setParameter(4, "Fundamentals of data science")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bscDsSem1Id)
                .executeUpdate();

        UUID course2Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440702");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course2Id)
                .setParameter(2, "Business Analytics Basics")
                .setParameter(3, "BA101")
                .setParameter(4, "Introduction to business analytics")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bcomBaSem1Id)
                .executeUpdate();

        UUID course3Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440703");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course3Id)
                .setParameter(2, "Statistics for Data Science")
                .setParameter(3, "STAT101")
                .setParameter(4, "Basic statistics for data analysis")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bscDsSem1Id)
                .executeUpdate();

        UUID course4Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440704");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course4Id)
                .setParameter(2, "Programming with Python")
                .setParameter(3, "PY201")
                .setParameter(4, "Python programming fundamentals")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY_LAB.name())
                .setParameter(6, 4)
                .setParameter(7, bscDsSem2Id)
                .executeUpdate();

        UUID course5Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440705");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course5Id)
                .setParameter(2, "Food Chemistry")
                .setParameter(3, "FC101")
                .setParameter(4, "Chemistry principles applied to food")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bscFsSem1Id)
                .executeUpdate();

        UUID course6Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440706");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course6Id)
                .setParameter(2, "Principles of Accounting")
                .setParameter(3, "ACC101")
                .setParameter(4, "Introduction to accounting")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bcomCaSem1Id)
                .executeUpdate();

        UUID course7Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440707");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course7Id)
                .setParameter(2, "Microeconomics")
                .setParameter(3, "ECO101")
                .setParameter(4, "Basics of microeconomics")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bcomCaSem1Id)
                .executeUpdate();

        UUID course8Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440708");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course8Id)
                .setParameter(2, "Fundamentals of Management")
                .setParameter(3, "MGT101")
                .setParameter(4, "Introduction to management")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bbaAiSem1Id)
                .executeUpdate();

        UUID course9Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440709");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course9Id)
                .setParameter(2, "Introduction to Artificial Intelligence")
                .setParameter(3, "AI101")
                .setParameter(4, "Core concepts of AI")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bbaAiSem1Id)
                .executeUpdate();

        UUID course10Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440710");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course10Id)
                .setParameter(2, "Physics")
                .setParameter(3, "PHY101")
                .setParameter(4, "Fundamentals of physics")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bscMpcsSem1Id)
                .executeUpdate();

        UUID course11Id = UUID.fromString("550e8400-e29b-41d4-a716-446655440711");
        upserted += entityManager.createNativeQuery(
                        "INSERT INTO course (id, name, course_code, description, course_type, credits, department_semester_id, is_elective, is_active, syllabus_pdf_url, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, false, true, NULL, now(), now()) " +
                                "ON CONFLICT (id) DO UPDATE SET " +
                                "name = EXCLUDED.name, " +
                                "course_code = EXCLUDED.course_code, " +
                                "description = EXCLUDED.description, " +
                                "course_type = EXCLUDED.course_type, " +
                                "credits = EXCLUDED.credits, " +
                                "department_semester_id = EXCLUDED.department_semester_id, " +
                                "is_active = EXCLUDED.is_active, " +
                                "updated_at = now()"
                )
                .setParameter(1, course11Id)
                .setParameter(2, "Mathematics")
                .setParameter(3, "MATH101")
                .setParameter(4, "Core mathematics")
                .setParameter(5, com.nethaji.Enums.CourseType.THEORY.name())
                .setParameter(6, 4)
                .setParameter(7, bscMpcsSem1Id)
                .executeUpdate();

        messages.add("✓ Upserted Courses (" + upserted + " applied)");
    }

    private void seedLecturerCourseAssignments(List<String> messages) {
        UUID lecturerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440201");

        if (!userRepository.existsById(lecturerId)) {
            messages.add("⚠ Cannot seed lecturer course assignments - Lecturer user not found");
            return;
        }

        List<UUID> courseIds = new ArrayList<>();
        UUID preferredCourseId = UUID.fromString("550e8400-e29b-41d4-a716-446655440701");
        if (courseRepository.existsById(preferredCourseId)) {
            courseIds.add(preferredCourseId);
        }

        if (courseIds.isEmpty()) {
            List<Course> any = courseRepository.findAll();
            if (any == null || any.isEmpty()) {
                messages.add("⚠ Cannot seed lecturer course assignments - No courses found");
                return;
            }
            courseIds.add(any.get(0).getId());
        }

        // Add a second course if available (helps testing My Courses UI)
        if (courseIds.size() < 2) {
            List<Course> any = courseRepository.findAll();
            for (Course c : any) {
                if (c != null && c.getId() != null && !courseIds.contains(c.getId())) {
                    courseIds.add(c.getId());
                    break;
                }
            }
        }

        int upserted = 0;
        for (UUID courseId : courseIds) {
            UUID assignmentId = UUID.nameUUIDFromBytes(("lecturer_course_assignment|" + lecturerId + "|" + courseId).getBytes());
            upserted += entityManager.createNativeQuery(
                            "INSERT INTO lecturer_course_assignments (id, lecturer_id, course_id, assignment_type, subject_type, is_active, created_at, updated_at) " +
                                    "VALUES (?1, ?2, ?3, ?4, NULL, true, now(), now()) " +
                                    "ON CONFLICT (id) DO UPDATE SET " +
                                    "lecturer_id = EXCLUDED.lecturer_id, " +
                                    "course_id = EXCLUDED.course_id, " +
                                    "assignment_type = EXCLUDED.assignment_type, " +
                                    "is_active = EXCLUDED.is_active, " +
                                    "updated_at = now()"
                    )
                    .setParameter(1, assignmentId)
                    .setParameter(2, lecturerId)
                    .setParameter(3, courseId)
                    .setParameter(4, com.nethaji.entity.LecturerDetails.AssignmentType.LECTURER.name())
                    .executeUpdate();
        }

        messages.add("✓ Seeded Lecturer Course Assignments (" + upserted + " applied)");
    }

    private void seedAttendance(List<String> messages) {
        try {
            UUID markedBy = UUID.fromString("550e8400-e29b-41d4-a716-446655440201");

            List<Course> courses = courseRepository.findAll().stream()
                    .filter(c -> c.getDepartmentSemesterId() != null)
                    .sorted(Comparator.comparing(Course::getCourseCode, Comparator.nullsLast(String::compareToIgnoreCase)))
                    .toList();
            if (courses == null || courses.isEmpty()) {
                messages.add("⚠ Cannot seed attendance - Courses not found");
                return;
            }

            List<User> students = userRepository.findUsersByType(User.UserType.STUDENT);
            if (students == null || students.isEmpty()) {
                messages.add("⚠ Cannot seed attendance - No student users found");
                return;
            }

            int inserted = 0;

            for (User student : students) {
                UUID studentId = student.getId();

                for (int i = 0; i < Math.min(2, courses.size()); i++) {
                    Course course = courses.get(i);
                    UUID courseId = course.getId();

                    for (int d = 1; d <= 3; d++) {
                        LocalDate date = LocalDate.now().minusDays(d);
                        UUID attendanceId = UUID.nameUUIDFromBytes(("attendance|" + studentId + "|" + courseId + "|" + date).getBytes());

                        inserted += entityManager.createNativeQuery(
                                        "INSERT INTO attendance (id, student_id, course_id, attendance_date, status, marked_by, remarks, created_at, updated_at) " +
                                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, now(), now()) " +
                                                "ON CONFLICT (id) DO NOTHING"
                                )
                                .setParameter(1, attendanceId)
                                .setParameter(2, studentId)
                                .setParameter(3, courseId)
                                .setParameter(4, date)
                                .setParameter(5, (d % 3 == 0 ? com.nethaji.Enums.AttendanceStatus.ABSENT.name() : com.nethaji.Enums.AttendanceStatus.PRESENT.name()))
                                .setParameter(6, markedBy)
                                .setParameter(7, (d % 3 == 0 ? "Absent" : "Present"))
                                .executeUpdate();
                    }
                }
            }

            messages.add("✓ Seeded Attendance (" + inserted + " inserted)");
        } catch (Exception e) {
            messages.add("⚠ Error seeding attendance: " + e.getMessage());
        }
    }

    private void seedGrades(List<String> messages) {
        try {
            List<Course> courses = courseRepository.findAll().stream()
                    .filter(c -> c.getDepartmentSemesterId() != null)
                    .sorted(Comparator.comparing(Course::getCourseCode, Comparator.nullsLast(String::compareToIgnoreCase)))
                    .toList();
            if (courses == null || courses.isEmpty()) {
                messages.add("⚠ Cannot seed grades - Courses not found");
                return;
            }

            List<User> students = userRepository.findUsersByType(User.UserType.STUDENT);
            if (students == null || students.isEmpty()) {
                messages.add("⚠ Cannot seed grades - No student users found");
                return;
            }

            int inserted = 0;

            for (User student : students) {
                UUID studentId = student.getId();

                for (int i = 0; i < Math.min(2, courses.size()); i++) {
                    Course course = courses.get(i);
                    UUID courseId = course.getId();
                    UUID semesterId = course.getDepartmentSemesterId();

                    if (semesterId == null) {
                        continue;
                    }

                    UUID gradeId = UUID.nameUUIDFromBytes(("grades|" + studentId + "|" + courseId + "|" + semesterId).getBytes());

                    BigDecimal internalMarks = BigDecimal.valueOf(25);
                    BigDecimal midtermMarks = BigDecimal.valueOf(25);
                    BigDecimal finalMarks = BigDecimal.valueOf(40);
                    BigDecimal totalMarks = internalMarks.add(midtermMarks).add(finalMarks);
                    BigDecimal percentage = totalMarks;

                    com.nethaji.Enums.Grade gradeEnum = com.nethaji.Enums.Grade.A;
                    BigDecimal gradePoint = BigDecimal.valueOf(9.0);

                    inserted += entityManager.createNativeQuery(
                                    "INSERT INTO grades (id, student_id, course_id, semester_id, internal_marks, midterm_marks, final_marks, total_marks, grade, grade_point, percentage, created_at, updated_at) " +
                                            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, now(), now()) " +
                                            "ON CONFLICT (id) DO NOTHING"
                            )
                            .setParameter(1, gradeId)
                            .setParameter(2, studentId)
                            .setParameter(3, courseId)
                            .setParameter(4, semesterId)
                            .setParameter(5, internalMarks)
                            .setParameter(6, midtermMarks)
                            .setParameter(7, finalMarks)
                            .setParameter(8, totalMarks)
                            .setParameter(9, gradeEnum.name())
                            .setParameter(10, gradePoint)
                            .setParameter(11, percentage)
                            .executeUpdate();
                }
            }

            messages.add("✓ Seeded Grades (" + inserted + " inserted)");
        } catch (Exception e) {
            messages.add("⚠ Error seeding grades: " + e.getMessage());
        }
    }

    private void seedMarks(List<String> messages) {
        try {
            // Check if course exists before creating marks
            Course course = courseRepository.findById(UUID.fromString("550e8400-e29b-41d4-a716-446655440701")).orElse(null);
            if (course == null) {
                messages.add("⚠ Cannot seed marks - Course not found");
                return;
            }
            
            if (marksRepository.count() == 0) {
                Marks mark1 = new Marks();
                mark1.setStudentId(UUID.fromString("550e8400-e29b-41d4-a716-446655440301"));
                mark1.setCourseId(UUID.fromString("550e8400-e29b-41d4-a716-446655440701"));
                mark1.setExamType(com.nethaji.Enums.ExamType.INTERNAL);
                mark1.setMarksObtained(BigDecimal.valueOf(85.5));
                mark1.setMaxMarks(BigDecimal.valueOf(100.0));
                mark1.setExamDate(LocalDate.now().minusDays(10));
                mark1.setEvaluatedBy(UUID.fromString("550e8400-e29b-41d4-a716-446655440201"));
                marksRepository.save(mark1);

                messages.add("✓ Seeded Sample Marks");
            } else {
                messages.add("⚠ Marks already exist");
            }
        } catch (Exception e) {
            messages.add("⚠ Error seeding marks: " + e.getMessage());
        }
    }

    private void seedAssignments(List<String> messages) {
        // Check if course exists before creating assignment
        UUID courseId = UUID.fromString("550e8400-e29b-41d4-a716-446655440701");
        if (!courseRepository.existsById(courseId)) {
            messages.add("⚠ Cannot seed assignments - Course not found");
            return;
        }

        UUID assignmentId = UUID.fromString("550e8400-e29b-41d4-a716-446655441301");
        if (assignmentRepository.existsById(assignmentId)) {
            messages.add("⚠ Assignments already exist");
            return;
        }

        int inserted = entityManager.createNativeQuery(
                        "INSERT INTO assignments (id, course_id, title, description, due_date, max_marks, created_by, file_url, is_active, created_at, updated_at) " +
                                "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, NULL, true, now(), now()) ON CONFLICT (id) DO NOTHING"
                )
                .setParameter(1, assignmentId)
                .setParameter(2, courseId)
                .setParameter(3, "Assignment 1: Array Operations")
                .setParameter(4, "Implement basic array operations")
                .setParameter(5, LocalDate.now().plusDays(7))
                .setParameter(6, BigDecimal.valueOf(100.0))
                .setParameter(7, UUID.fromString("550e8400-e29b-41d4-a716-446655440201"))
                .executeUpdate();

        if (inserted > 0) {
            messages.add("✓ Seeded Sample Assignments");
        } else {
            messages.add("⚠ Assignments already exist");
        }
    }

    private void seedStudentAssignments(List<String> messages) {
        if (studentAssignmentRepository.count() > 0) {
            messages.add("⚠ Student Assignments already exist");
            return;
        }

        UUID assignmentId = UUID.fromString("550e8400-e29b-41d4-a716-446655441301");
        UUID studentId = UUID.fromString("550e8400-e29b-41d4-a716-446655440301");
        UUID lecturerId = UUID.fromString("550e8400-e29b-41d4-a716-446655440201");

        StudentAssignment sa = new StudentAssignment();
        sa.setAssignmentId(assignmentId);
        sa.setStudentId(studentId);
        sa.setSubmissionDate(LocalDate.now().minusDays(1));
        sa.setFileUrl("https://example.com/dummy-assignment.pdf");
        sa.setStatus(com.nethaji.Enums.AssignmentStatus.SUBMITTED);
        sa.setGradedBy(lecturerId);
        sa.setGradedAt(new Date());
        sa.setMarksObtained(BigDecimal.valueOf(90.0));
        sa.setFeedback("Good work");
        studentAssignmentRepository.save(sa);

        messages.add("✓ Seeded 1 Student Assignment");
    }
}

