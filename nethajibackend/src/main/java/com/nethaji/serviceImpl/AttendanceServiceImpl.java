package com.nethaji.serviceImpl;

import com.nethaji.dto.AttendanceDTO;
import com.nethaji.dto.AttendanceReportDTO;
import com.nethaji.dto.BulkAttendanceDTO;
import com.nethaji.entity.Attendance;
import com.nethaji.entity.Course;
import com.nethaji.entity.User;
import com.nethaji.repositories.AttendanceRepository;
import com.nethaji.repositories.CourseRepository;
import com.nethaji.repositories.UserRepository;
import com.nethaji.service.AttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public ResponseEntity<AttendanceDTO> markAttendance(AttendanceDTO attendanceDTO) {
        try {
            Attendance attendance = new Attendance();
            attendance.setStudentId(attendanceDTO.getStudentId());
            attendance.setCourseId(attendanceDTO.getCourseId());
            attendance.setAttendanceDate(attendanceDTO.getAttendanceDate());
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
    public ResponseEntity<List<AttendanceDTO>> markBulkAttendance(BulkAttendanceDTO bulkAttendanceDTO) {
        try {
            List<Attendance> attendances = new ArrayList<>();
            for (BulkAttendanceDTO.StudentAttendanceEntry entry : bulkAttendanceDTO.getStudents()) {
                Attendance attendance = new Attendance();
                attendance.setStudentId(entry.getStudentId());
                attendance.setCourseId(bulkAttendanceDTO.getCourseId());
                attendance.setAttendanceDate(bulkAttendanceDTO.getAttendanceDate());
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
}

