package com.nethaji.serviceImpl;

import com.nethaji.dto.*;
import com.nethaji.entity.*;
import com.nethaji.repositories.*;
import com.nethaji.service.AttendanceService;
import com.nethaji.service.GradesService;
import com.nethaji.service.MarksService;
import com.nethaji.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private LecturerCourseAssignmentRepository lecturerCourseAssignmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartMentSemestersRepo departMentSemestersRepo;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProgramsRepository programsRepository;

    @Autowired
    private StudentEducationInfoRepository studentEducationInfoRepository;

    @Autowired
    private StudentSectionRepository studentSectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private MarksService marksService;

    @Autowired
    private GradesService gradesService;

    @Override
    public ResponseEntity<Void> upsertLecturerCourseAssignment(CreateLecturerCourseAssignmentRequest request) {
        if (request == null || request.getLecturerId() == null || request.getCourseId() == null) {
            return ResponseEntity.badRequest().build();
        }

        LecturerCourseAssignment assignment = lecturerCourseAssignmentRepository
                .findByLecturerIdAndCourseId(request.getLecturerId(), request.getCourseId())
                .orElseGet(LecturerCourseAssignment::new);
        assignment.setLecturerId(request.getLecturerId());
        assignment.setCourseId(request.getCourseId());
        assignment.setAssignmentType(request.getAssignmentType() != null ? request.getAssignmentType() : LecturerDetails.AssignmentType.LECTURER);
        assignment.setSubjectType(request.getSubjectType());
        assignment.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);

        lecturerCourseAssignmentRepository.save(assignment);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<LecturerCourseDTO>> getCoursesForLecturer(UUID lecturerId) {
        if (lecturerId == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<LecturerCourseAssignment> assignments = lecturerCourseAssignmentRepository.findByLecturerIdAndIsActiveTrue(lecturerId);
        if (assignments == null || assignments.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<LecturerCourseDTO> result = new ArrayList<>();

        for (LecturerCourseAssignment a : assignments) {
            Course course = courseRepository.findById(a.getCourseId()).orElse(null);
            if (course == null) {
                continue;
            }

            DepartMentSemesters depSem = course.getDepartmentSemesterId() != null
                    ? departMentSemestersRepo.findById(course.getDepartmentSemesterId()).orElse(null)
                    : null;

            Department department = (depSem != null && depSem.getDepartmentId() != null)
                    ? departmentRepository.findById(depSem.getDepartmentId()).orElse(null)
                    : null;

            Programs program = (department != null && department.getProgramId() != null)
                    ? programsRepository.findById(department.getProgramId()).orElse(null)
                    : null;

            LecturerCourseDTO dto = new LecturerCourseDTO();
            dto.setCourseId(course.getId());
            dto.setCourseName(course.getName());
            dto.setCourseCode(course.getCourseCode());
            dto.setCourseType(course.getCourseType());
            dto.setDepartmentSemesterId(course.getDepartmentSemesterId());
            dto.setSemester(depSem != null ? depSem.getSemester() : null);

            if (department != null) {
                dto.setDepartmentId(department.getId());
                dto.setDepartmentName(department.getDepartmentName());
                dto.setDepartmentCode(department.getDepartmentCode());
            }

            if (program != null) {
                dto.setProgramId(program.getId());
                dto.setProgramName(program.getName());
                dto.setProgramCode(program.getProgramCode());
            }

            dto.setAssignmentType(a.getAssignmentType());

            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<StudentRosterDTO>> getCourseRoster(UUID lecturerId, UUID courseId) {
        if (lecturerId == null || courseId == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        boolean allowed = lecturerCourseAssignmentRepository.existsByLecturerIdAndCourseIdAndIsActiveTrue(lecturerId, courseId);
        if (!allowed) {
            return ResponseEntity.status(403).body(Collections.emptyList());
        }

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null || course.getDepartmentSemesterId() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        DepartMentSemesters depSem = departMentSemestersRepo.findById(course.getDepartmentSemesterId()).orElse(null);
        if (depSem == null || depSem.getDepartmentId() == null || depSem.getSemester() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        Department department = departmentRepository.findById(depSem.getDepartmentId()).orElse(null);
        if (department == null || department.getProgramId() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<StudentSection> sections = studentSectionRepository.findActiveByDepartmentIdAndSemester(depSem.getDepartmentId(), depSem.getSemester());
        if (sections == null || sections.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<StudentRosterDTO> roster = new ArrayList<>();
        for (StudentSection ss : sections) {
            if (ss == null || ss.getStudentId() == null) {
                continue;
            }
            User student = userRepository.findById(ss.getStudentId()).orElse(null);
            if (student == null) {
                continue;
            }

            StudentRosterDTO dto = new StudentRosterDTO();
            dto.setStudentId(student.getId());
            dto.setFirstName(student.getFirstName());
            dto.setLastName(student.getLastName());
            dto.setEnrollmentNumber(student.getEnrollmentNumber());
            dto.setEmail(student.getEmail());
            dto.setSectionName(ss.getSectionName());
            roster.add(dto);
        }

        return ResponseEntity.ok(roster);
    }

    @Override
    public ResponseEntity<List<AttendanceDTO>> markBulkAttendanceForCourse(UUID lecturerId, UUID courseId, BulkAttendanceDTO bulkAttendanceDTO) {
        if (lecturerId == null || courseId == null || bulkAttendanceDTO == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        boolean allowed = lecturerCourseAssignmentRepository.existsByLecturerIdAndCourseIdAndIsActiveTrue(lecturerId, courseId);
        if (!allowed) {
            return ResponseEntity.status(403).body(Collections.emptyList());
        }

        ResponseEntity<List<StudentRosterDTO>> rosterResp = getCourseRoster(lecturerId, courseId);
        List<StudentRosterDTO> roster = rosterResp.getBody();
        if (roster == null || roster.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        Set<UUID> allowedStudentIds = new HashSet<>();
        for (StudentRosterDTO s : roster) {
            if (s != null && s.getStudentId() != null) {
                allowedStudentIds.add(s.getStudentId());
            }
        }

        if (bulkAttendanceDTO.getStudents() != null) {
            for (BulkAttendanceDTO.StudentAttendanceEntry e : bulkAttendanceDTO.getStudents()) {
                if (e == null || e.getStudentId() == null || !allowedStudentIds.contains(e.getStudentId())) {
                    return ResponseEntity.badRequest().body(Collections.emptyList());
                }
            }
        }

        bulkAttendanceDTO.setCourseId(courseId);
        bulkAttendanceDTO.setMarkedBy(lecturerId);

        ResponseEntity<List<AttendanceDTO>> resp = attendanceService.markBulkAttendance(bulkAttendanceDTO);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody() != null ? resp.getBody() : Collections.emptyList());
    }

    @Override
    public ResponseEntity<List<MarksDTO>> enterBulkMarksForCourse(UUID lecturerId, UUID courseId, BulkMarksDTO bulkMarksDTO) {
        if (lecturerId == null || courseId == null || bulkMarksDTO == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        boolean allowed = lecturerCourseAssignmentRepository.existsByLecturerIdAndCourseIdAndIsActiveTrue(lecturerId, courseId);
        if (!allowed) {
            return ResponseEntity.status(403).body(Collections.emptyList());
        }

        ResponseEntity<List<StudentRosterDTO>> rosterResp = getCourseRoster(lecturerId, courseId);
        List<StudentRosterDTO> roster = rosterResp.getBody();
        if (roster == null || roster.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        Set<UUID> allowedStudentIds = new HashSet<>();
        for (StudentRosterDTO s : roster) {
            if (s != null && s.getStudentId() != null) {
                allowedStudentIds.add(s.getStudentId());
            }
        }

        if (bulkMarksDTO.getStudents() != null) {
            for (BulkMarksDTO.StudentMarksEntry e : bulkMarksDTO.getStudents()) {
                if (e == null || e.getStudentId() == null || !allowedStudentIds.contains(e.getStudentId())) {
                    return ResponseEntity.badRequest().body(Collections.emptyList());
                }
            }
        }

        bulkMarksDTO.setCourseId(courseId);
        bulkMarksDTO.setEvaluatedBy(lecturerId);

        ResponseEntity<List<MarksDTO>> resp = marksService.enterBulkMarks(bulkMarksDTO);
        return ResponseEntity.status(resp.getStatusCode()).body(resp.getBody() != null ? resp.getBody() : Collections.emptyList());
    }

    @Override
    public ResponseEntity<List<GradesDTO>> calculateGradesForCourse(UUID lecturerId, UUID courseId) {
        if (lecturerId == null || courseId == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        boolean allowed = lecturerCourseAssignmentRepository.existsByLecturerIdAndCourseIdAndIsActiveTrue(lecturerId, courseId);
        if (!allowed) {
            return ResponseEntity.status(403).body(Collections.emptyList());
        }

        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null || course.getDepartmentSemesterId() == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        UUID semesterId = course.getDepartmentSemesterId();
        ResponseEntity<List<StudentRosterDTO>> rosterResp = getCourseRoster(lecturerId, courseId);
        List<StudentRosterDTO> roster = rosterResp.getBody();
        if (roster == null || roster.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<GradesDTO> out = new ArrayList<>();
        for (StudentRosterDTO s : roster) {
            if (s == null || s.getStudentId() == null) {
                continue;
            }
            ResponseEntity<GradesDTO> g = gradesService.calculateGrade(s.getStudentId(), courseId, semesterId);
            if (g.getBody() != null) {
                out.add(g.getBody());
            }
        }

        return ResponseEntity.ok(out);
    }
}
