package com.nethaji.service;

import com.nethaji.dto.BulkAttendanceDTO;
import com.nethaji.dto.BulkMarksDTO;
import com.nethaji.dto.CreateLecturerCourseAssignmentRequest;
import com.nethaji.dto.GradesDTO;
import com.nethaji.dto.LecturerCourseDTO;
import com.nethaji.dto.MarksDTO;
import com.nethaji.dto.StudentRosterDTO;
import com.nethaji.dto.AttendanceDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface StaffService {
    ResponseEntity<Void> upsertLecturerCourseAssignment(CreateLecturerCourseAssignmentRequest request);

    ResponseEntity<List<LecturerCourseDTO>> getCoursesForLecturer(UUID lecturerId);

    ResponseEntity<List<StudentRosterDTO>> getCourseRoster(UUID lecturerId, UUID courseId);

    ResponseEntity<List<AttendanceDTO>> markBulkAttendanceForCourse(UUID lecturerId, UUID courseId, BulkAttendanceDTO bulkAttendanceDTO);

    ResponseEntity<List<MarksDTO>> enterBulkMarksForCourse(UUID lecturerId, UUID courseId, BulkMarksDTO bulkMarksDTO);

    ResponseEntity<List<GradesDTO>> calculateGradesForCourse(UUID lecturerId, UUID courseId);
}
