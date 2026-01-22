package com.nethaji.controller;

import com.nethaji.dto.AttendanceDTO;
import com.nethaji.dto.AttendanceReportDTO;
import com.nethaji.dto.AttendanceViewRowDTO;
import com.nethaji.dto.BulkAttendanceDTO;
import com.nethaji.dto.CourseAttendanceReportRowDTO;
import com.nethaji.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nethaji-service/attendance")
@CrossOrigin
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<AttendanceDTO> markAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.markAttendance(attendanceDTO);
    }

    @PostMapping("/mark-bulk")
    public ResponseEntity<List<AttendanceDTO>> markBulkAttendance(@RequestBody BulkAttendanceDTO bulkAttendanceDTO) {
        return attendanceService.markBulkAttendance(bulkAttendanceDTO);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudent(@PathVariable UUID studentId) {
        return attendanceService.getAttendanceByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByCourse(@PathVariable UUID courseId) {
        return attendanceService.getAttendanceByCourse(courseId);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByStudentAndCourse(
            @PathVariable UUID studentId,
            @PathVariable UUID courseId) {
        return attendanceService.getAttendanceByStudentAndCourse(studentId, courseId);
    }

    @GetMapping("/student/{studentId}/course/{courseId}/date-range")
    public ResponseEntity<List<AttendanceDTO>> getAttendanceByDateRange(
            @PathVariable UUID studentId,
            @PathVariable UUID courseId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return attendanceService.getAttendanceByDateRange(studentId, courseId, startDate, endDate);
    }

    @GetMapping("/report/student/{studentId}/course/{courseId}")
    public ResponseEntity<AttendanceReportDTO> getAttendanceReport(
            @PathVariable UUID studentId,
            @PathVariable UUID courseId) {
        return attendanceService.getAttendanceReport(studentId, courseId);
    }

    @GetMapping("/report/course/{courseId}")
    public ResponseEntity<List<AttendanceReportDTO>> getAttendanceReportByCourse(@PathVariable UUID courseId) {
        return attendanceService.getAttendanceReportByCourse(courseId);
    }

    @GetMapping("/view/course/{courseId}")
    public ResponseEntity<List<AttendanceViewRowDTO>> getAttendanceViewByCourseAndDate(
            @PathVariable UUID courseId,
            @RequestParam LocalDate date,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        return attendanceService.getAttendanceViewByCourseAndDate(courseId, date, status, search);
    }

    @GetMapping("/report/course/{courseId}/date-range")
    public ResponseEntity<List<CourseAttendanceReportRowDTO>> getCourseAttendanceReportByDateRange(
            @PathVariable UUID courseId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) Double lowAttendanceThreshold) {
        return attendanceService.getCourseAttendanceReportByDateRange(courseId, startDate, endDate, lowAttendanceThreshold);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDTO> updateAttendance(
            @PathVariable UUID id,
            @RequestBody AttendanceDTO attendanceDTO) {
        return attendanceService.updateAttendance(id, attendanceDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable UUID id) {
        return attendanceService.deleteAttendance(id);
    }
}

