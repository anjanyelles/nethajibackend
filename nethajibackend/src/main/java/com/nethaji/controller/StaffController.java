package com.nethaji.controller;

import com.nethaji.dto.*;
import com.nethaji.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nethaji-service/staff")
@CrossOrigin
public class StaffController {

    @Autowired
    private StaffService staffService;

    @PostMapping("/course-assignments")
    public ResponseEntity<Void> upsertLecturerCourseAssignment(@RequestBody CreateLecturerCourseAssignmentRequest request) {
        return staffService.upsertLecturerCourseAssignment(request);
    }

    @GetMapping("/lecturer/{lecturerId}/courses")
    public ResponseEntity<List<LecturerCourseDTO>> getCoursesForLecturer(@PathVariable UUID lecturerId) {
        return staffService.getCoursesForLecturer(lecturerId);
    }

    @GetMapping("/lecturer/{lecturerId}/courses/{courseId}/students")
    public ResponseEntity<List<StudentRosterDTO>> getCourseRoster(@PathVariable UUID lecturerId, @PathVariable UUID courseId) {
        return staffService.getCourseRoster(lecturerId, courseId);
    }

    @PostMapping("/lecturer/{lecturerId}/courses/{courseId}/attendance/mark-bulk")
    public ResponseEntity<List<AttendanceDTO>> markBulkAttendanceForCourse(
            @PathVariable UUID lecturerId,
            @PathVariable UUID courseId,
            @RequestBody BulkAttendanceDTO bulkAttendanceDTO
    ) {
        return staffService.markBulkAttendanceForCourse(lecturerId, courseId, bulkAttendanceDTO);
    }

    @PostMapping("/lecturer/{lecturerId}/courses/{courseId}/marks/enter-bulk")
    public ResponseEntity<List<MarksDTO>> enterBulkMarksForCourse(
            @PathVariable UUID lecturerId,
            @PathVariable UUID courseId,
            @RequestBody BulkMarksDTO bulkMarksDTO
    ) {
        return staffService.enterBulkMarksForCourse(lecturerId, courseId, bulkMarksDTO);
    }

    @PostMapping("/lecturer/{lecturerId}/courses/{courseId}/grades/calculate")
    public ResponseEntity<List<GradesDTO>> calculateGradesForCourse(
            @PathVariable UUID lecturerId,
            @PathVariable UUID courseId
    ) {
        return staffService.calculateGradesForCourse(lecturerId, courseId);
    }
}
