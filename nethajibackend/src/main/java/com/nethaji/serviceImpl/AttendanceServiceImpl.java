package com.nethaji.serviceImpl;

import com.nethaji.dto.AttendanceDTO;
import com.nethaji.dto.AttendanceReportDTO;
import com.nethaji.dto.AttendanceViewRowDTO;
import com.nethaji.dto.BulkAttendanceDTO;
import com.nethaji.dto.CourseAttendanceReportRowDTO;
import com.nethaji.entity.Department;
import com.nethaji.entity.Attendance;
import com.nethaji.entity.Course;
import com.nethaji.entity.DepartMentSemesters;
import com.nethaji.entity.StudentEducationInfo;
import com.nethaji.entity.User;
import com.nethaji.Enums.AttendanceStatus;
import com.nethaji.repositories.AttendanceRepository;
import com.nethaji.repositories.CourseRepository;
import com.nethaji.repositories.DepartMentSemestersRepo;
import com.nethaji.repositories.DepartmentRepository;
import com.nethaji.repositories.StudentEducationInfoRepository;
import com.nethaji.repositories.UserRepository;
import com.nethaji.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartMentSemestersRepo departMentSemestersRepo;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private StudentEducationInfoRepository studentEducationInfoRepository;

    @Override
    public ResponseEntity<AttendanceDTO> markAttendance(AttendanceDTO attendanceDTO) {
        try {
            Attendance attendance = attendanceRepository.findByStudentIdAndCourseIdAndAttendanceDate(
                    attendanceDTO.getStudentId(),
                    attendanceDTO.getCourseId(),
                    attendanceDTO.getAttendanceDate()
            );
            if (attendance == null) {
                attendance = new Attendance();
                attendance.setStudentId(attendanceDTO.getStudentId());
                attendance.setCourseId(attendanceDTO.getCourseId());
                attendance.setAttendanceDate(attendanceDTO.getAttendanceDate());
            }
            attendance.setStatus(attendanceDTO.getStatus());
            attendance.setMarkedBy(attendanceDTO.getMarkedBy());
            attendance.setRemarks(attendanceDTO.getRemarks());

            Attendance saved = attendanceRepository.save(attendance);
            return ResponseEntity.ok(convertToDTO(saved));
        } catch (Exception e) {
            log.error("Error marking attendance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Override
    public ResponseEntity<List<AttendanceViewRowDTO>> getAttendanceViewByCourseAndDate(UUID courseId, LocalDate date, String status, String search) {
        try {
            if (courseId == null || date == null) {
                return ResponseEntity.badRequest().body(new ArrayList<>());
            }

            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<User> roster = resolveRosterStudentsByCourse(course);
            Map<UUID, Attendance> attendanceByStudentId = attendanceRepository.findByCourseIdAndDate(courseId, date)
                    .stream()
                    .collect(Collectors.toMap(Attendance::getStudentId, a -> a, (a1, a2) -> a1));

            List<AttendanceViewRowDTO> rows = new ArrayList<>();
            for (User student : roster) {
                Attendance a = attendanceByStudentId.get(student.getId());
                AttendanceViewRowDTO row = new AttendanceViewRowDTO();
                row.setStudentId(student.getId());
                row.setStudentName(buildUserFullName(student));
                row.setEnrollmentNumber(student.getEnrollmentNumber());
                row.setCourseId(course.getId());
                row.setCourseName(course.getName());
                row.setAttendanceDate(date);
                if (a == null) {
                    row.setStatus("NOT_MARKED");
                } else {
                    row.setStatus(a.getStatus() != null ? a.getStatus().name() : "NOT_MARKED");
                    row.setAttendanceId(a.getId());
                    row.setRemarks(a.getRemarks());
                }
                rows.add(row);
            }

            if (search != null && !search.isBlank()) {
                String q = search.trim().toLowerCase();
                rows = rows.stream()
                        .filter(r -> (r.getStudentName() != null && r.getStudentName().toLowerCase().contains(q))
                                || (r.getEnrollmentNumber() != null && r.getEnrollmentNumber().toLowerCase().contains(q)))
                        .collect(Collectors.toList());
            }

            if (status != null && !status.isBlank()) {
                String s = status.trim().toUpperCase();
                rows = rows.stream()
                        .filter(r -> r.getStatus() != null && r.getStatus().equalsIgnoreCase(s))
                        .collect(Collectors.toList());
            }

            return ResponseEntity.ok(rows);
        } catch (Exception e) {
            log.error("Error generating attendance view", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<CourseAttendanceReportRowDTO>> getCourseAttendanceReportByDateRange(UUID courseId, LocalDate startDate, LocalDate endDate, Double lowAttendanceThreshold) {
        try {
            if (courseId == null || startDate == null || endDate == null) {
                return ResponseEntity.badRequest().body(new ArrayList<>());
            }
            if (endDate.isBefore(startDate)) {
                return ResponseEntity.badRequest().body(new ArrayList<>());
            }

            double threshold = lowAttendanceThreshold != null ? lowAttendanceThreshold : 75.0;

            Course course = courseRepository.findById(courseId).orElse(null);
            if (course == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<User> roster = resolveRosterStudentsByCourse(course);
            List<LocalDate> classDates = attendanceRepository.findDistinctDatesByCourseIdAndDateRange(courseId, startDate, endDate);
            classDates = classDates.stream().distinct().sorted().collect(Collectors.toList());
            long totalClasses = classDates.size();

            List<Attendance> all = attendanceRepository.findByCourseIdAndDateRange(courseId, startDate, endDate);
            Map<UUID, Map<LocalDate, Attendance>> byStudentThenDate = new HashMap<>();
            for (Attendance a : all) {
                if (a.getStudentId() == null || a.getAttendanceDate() == null) {
                    continue;
                }
                byStudentThenDate
                        .computeIfAbsent(a.getStudentId(), k -> new HashMap<>())
                        .putIfAbsent(a.getAttendanceDate(), a);
            }

            List<CourseAttendanceReportRowDTO> rows = new ArrayList<>();
            for (User student : roster) {
                long present = 0;
                long absent = 0;
                long late = 0;
                long excused = 0;
                long notMarked = 0;

                Map<LocalDate, Attendance> map = byStudentThenDate.get(student.getId());
                for (LocalDate d : classDates) {
                    Attendance a = map == null ? null : map.get(d);
                    if (a == null || a.getStatus() == null) {
                        notMarked++;
                        continue;
                    }
                    AttendanceStatus st = a.getStatus();
                    if (st == AttendanceStatus.PRESENT) {
                        present++;
                    } else if (st == AttendanceStatus.ABSENT) {
                        absent++;
                    } else if (st == AttendanceStatus.LATE) {
                        late++;
                    } else if (st == AttendanceStatus.EXCUSED) {
                        excused++;
                    }
                }

                CourseAttendanceReportRowDTO row = new CourseAttendanceReportRowDTO();
                row.setStudentId(student.getId());
                row.setStudentName(buildUserFullName(student));
                row.setEnrollmentNumber(student.getEnrollmentNumber());
                row.setCourseId(course.getId());
                row.setCourseName(course.getName());
                row.setStartDate(startDate);
                row.setEndDate(endDate);
                row.setTotalClasses(totalClasses);
                row.setPresentCount(present);
                row.setAbsentCount(absent);
                row.setLateCount(late);
                row.setExcusedCount(excused);
                row.setNotMarkedCount(notMarked);

                double percent = 0.0;
                if (totalClasses > 0) {
                    percent = ((double) (present + late + excused) / (double) totalClasses) * 100.0;
                }
                row.setAttendancePercentage(percent);
                row.setLowAttendance(percent < threshold);

                rows.add(row);
            }

            rows = rows.stream()
                    .sorted(Comparator.comparing(CourseAttendanceReportRowDTO::getAttendancePercentage))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(rows);
        } catch (Exception e) {
            log.error("Error generating course attendance report by date range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<AttendanceDTO>> markBulkAttendance(BulkAttendanceDTO bulkAttendanceDTO) {
        try {
            List<Attendance> attendances = new ArrayList<>();
            for (BulkAttendanceDTO.StudentAttendanceEntry entry : bulkAttendanceDTO.getStudents()) {
                Attendance attendance = attendanceRepository.findByStudentIdAndCourseIdAndAttendanceDate(
                        entry.getStudentId(),
                        bulkAttendanceDTO.getCourseId(),
                        bulkAttendanceDTO.getAttendanceDate()
                );
                if (attendance == null) {
                    attendance = new Attendance();
                    attendance.setStudentId(entry.getStudentId());
                    attendance.setCourseId(bulkAttendanceDTO.getCourseId());
                    attendance.setAttendanceDate(bulkAttendanceDTO.getAttendanceDate());
                }
                attendance.setStatus(entry.getStatus());
                attendance.setMarkedBy(bulkAttendanceDTO.getMarkedBy());
                attendance.setRemarks(entry.getRemarks());
                attendances.add(attendance);
            }
            List<Attendance> saved = attendanceRepository.saveAll(attendances);
            return ResponseEntity.ok(saved.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error marking bulk attendance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudent(UUID studentId) {
        try {
            List<Attendance> attendances = attendanceRepository.findByStudentId(studentId);
            return ResponseEntity.ok(attendances.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching attendance by student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByCourse(UUID courseId) {
        try {
            List<Attendance> attendances = attendanceRepository.findByCourseId(courseId);
            return ResponseEntity.ok(attendances.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching attendance by course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentAndCourse(UUID studentId, UUID courseId) {
        try {
            List<Attendance> attendances = attendanceRepository.findByStudentIdAndCourseId(studentId, courseId);
            return ResponseEntity.ok(attendances.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching attendance by student and course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByDateRange(UUID studentId, UUID courseId, LocalDate startDate, LocalDate endDate) {
        try {
            List<Attendance> attendances = attendanceRepository.findByStudentIdAndCourseIdAndDateRange(studentId, courseId, startDate, endDate);
            return ResponseEntity.ok(attendances.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching attendance by date range", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<AttendanceReportDTO> getAttendanceReport(UUID studentId, UUID courseId) {
        try {
            Long totalClasses = attendanceRepository.countTotalByStudentIdAndCourseId(studentId, courseId);
            Long presentCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, com.nethaji.Enums.AttendanceStatus.PRESENT);
            Long absentCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, com.nethaji.Enums.AttendanceStatus.ABSENT);
            Long lateCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, com.nethaji.Enums.AttendanceStatus.LATE);
            Long excusedCount = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, com.nethaji.Enums.AttendanceStatus.EXCUSED);

            AttendanceReportDTO report = new AttendanceReportDTO();
            report.setStudentId(studentId);
            report.setCourseId(courseId);
            report.setTotalClasses(totalClasses);
            report.setPresentCount(presentCount);
            report.setAbsentCount(absentCount);
            report.setLateCount(lateCount);
            report.setExcusedCount(excusedCount);

            if (totalClasses > 0) {
                double percentage = ((double) (presentCount + lateCount + excusedCount) / totalClasses) * 100;
                report.setAttendancePercentage(percentage);
            } else {
                report.setAttendancePercentage(0.0);
            }

            User student = userRepository.findById(studentId).orElse(null);
            Course course = courseRepository.findById(courseId).orElse(null);
            if (student != null) {
                report.setStudentName(student.getFirstName() + " " + student.getLastName());
            }
            if (course != null) {
                report.setCourseName(course.getName());
            }

            return ResponseEntity.ok(report);
        } catch (Exception e) {
            log.error("Error generating attendance report", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportByCourse(UUID courseId) {
        try {
            List<Attendance> attendances = attendanceRepository.findByCourseId(courseId);
            List<UUID> studentIds = attendances.stream()
                    .map(Attendance::getStudentId)
                    .distinct()
                    .collect(Collectors.toList());

            List<AttendanceReportDTO> reports = new ArrayList<>();
            for (UUID studentId : studentIds) {
                ResponseEntity<AttendanceReportDTO> reportResponse = getAttendanceReport(studentId, courseId);
                if (reportResponse.getStatusCode() == HttpStatus.OK && reportResponse.getBody() != null) {
                    reports.add(reportResponse.getBody());
                }
            }
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            log.error("Error generating attendance report by course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<AttendanceDTO> updateAttendance(UUID id, AttendanceDTO attendanceDTO) {
        try {
            Attendance attendance = attendanceRepository.findById(id).orElse(null);
            if (attendance == null) {
                return ResponseEntity.notFound().build();
            }

            attendance.setStatus(attendanceDTO.getStatus());
            attendance.setRemarks(attendanceDTO.getRemarks());
            attendance.setAttendanceDate(attendanceDTO.getAttendanceDate());

            Attendance updated = attendanceRepository.save(attendance);
            return ResponseEntity.ok(convertToDTO(updated));
        } catch (Exception e) {
            log.error("Error updating attendance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteAttendance(UUID id) {
        try {
            attendanceRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting attendance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {
        AttendanceDTO dto = new AttendanceDTO();
        dto.setId(attendance.getId());
        dto.setStudentId(attendance.getStudentId());
        dto.setCourseId(attendance.getCourseId());
        dto.setAttendanceDate(attendance.getAttendanceDate());
        dto.setStatus(attendance.getStatus());
        dto.setMarkedBy(attendance.getMarkedBy());
        dto.setRemarks(attendance.getRemarks());

        User student = userRepository.findById(attendance.getStudentId()).orElse(null);
        Course course = courseRepository.findById(attendance.getCourseId()).orElse(null);

        if (student != null) {
            dto.setStudentName(student.getFirstName() + " " + student.getLastName());
        }
        if (course != null) {
            dto.setCourseName(course.getName());
        }

        return dto;
    }

    private List<User> resolveRosterStudentsByCourse(Course course) {
        if (course == null || course.getDepartmentSemesterId() == null) {
            return new ArrayList<>();
        }

        DepartMentSemesters sem = departMentSemestersRepo.findById(course.getDepartmentSemesterId()).orElse(null);
        if (sem == null || sem.getDepartmentId() == null || sem.getSemester() == null) {
            return new ArrayList<>();
        }

        Department dept = departmentRepository.findById(sem.getDepartmentId()).orElse(null);
        if (dept == null || dept.getProgramId() == null || dept.getDepartmentCode() == null) {
            return new ArrayList<>();
        }

        List<StudentEducationInfo> infos = studentEducationInfoRepository
                .findActiveStudentsByProgramAndSemesterAndGraduationType(dept.getProgramId(), sem.getSemester(), dept.getDepartmentCode());

        if (infos == null || infos.isEmpty()) {
            return new ArrayList<>();
        }

        List<User> roster = new ArrayList<>();
        for (StudentEducationInfo info : infos) {
            User u = info.getStudent();
            if (u == null && info.getStudentId() != null) {
                u = userRepository.findById(info.getStudentId()).orElse(null);
            }
            if (u != null && u.getIsActive() != null && u.getIsActive()) {
                roster.add(u);
            }
        }
        return roster;
    }

    private String buildUserFullName(User user) {
        if (user == null) {
            return "";
        }
        String first = user.getFirstName() == null ? "" : user.getFirstName();
        String last = user.getLastName() == null ? "" : user.getLastName();
        return (first + " " + last).trim();
    }
}

