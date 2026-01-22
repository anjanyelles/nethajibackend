package com.nethaji.service;

import com.nethaji.dto.AttendanceDTO;
import com.nethaji.dto.AttendanceReportDTO;
import com.nethaji.dto.BulkAttendanceDTO;
import com.nethaji.dto.AttendanceViewRowDTO;
import com.nethaji.dto.CourseAttendanceReportRowDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceService {
    ResponseEntity<AttendanceDTO> markAttendance(AttendanceDTO attendanceDTO);
    ResponseEntity<List<AttendanceDTO>> markBulkAttendance(BulkAttendanceDTO bulkAttendanceDTO);
    ResponseEntity<List<AttendanceDTO>> getAttendanceByStudent(UUID studentId);
    ResponseEntity<List<AttendanceDTO>> getAttendanceByCourse(UUID courseId);
    ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentAndCourse(UUID studentId, UUID courseId);
    ResponseEntity<List<AttendanceDTO>> getAttendanceByDateRange(UUID studentId, UUID courseId, LocalDate startDate, LocalDate endDate);
    ResponseEntity<AttendanceReportDTO> getAttendanceReport(UUID studentId, UUID courseId);
    ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportByCourse(UUID courseId);
    ResponseEntity<List<AttendanceViewRowDTO>> getAttendanceViewByCourseAndDate(UUID courseId, LocalDate date, String status, String search);
    ResponseEntity<List<CourseAttendanceReportRowDTO>> getCourseAttendanceReportByDateRange(UUID courseId, LocalDate startDate, LocalDate endDate, Double lowAttendanceThreshold);
    ResponseEntity<AttendanceDTO> updateAttendance(UUID id, AttendanceDTO attendanceDTO);
    ResponseEntity<Void> deleteAttendance(UUID id);
}

