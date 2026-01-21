package com.nethaji.controller;

import com.nethaji.dto.AssignmentDTO;
import com.nethaji.dto.StudentAssignmentDTO;
import com.nethaji.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nethaji-service/assignments")
@CrossOrigin
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @PostMapping("/create")
    public ResponseEntity<AssignmentDTO> createAssignment(@RequestBody AssignmentDTO assignmentDTO) {
        return assignmentService.createAssignment(assignmentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignmentDTO> updateAssignment(
            @PathVariable UUID id,
            @RequestBody AssignmentDTO assignmentDTO) {
        return assignmentService.updateAssignment(id, assignmentDTO);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByCourse(@PathVariable UUID courseId) {
        return assignmentService.getAssignmentsByCourse(courseId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentDTO> getAssignmentById(@PathVariable UUID id) {
        return assignmentService.getAssignmentById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable UUID id) {
        return assignmentService.deleteAssignment(id);
    }

    @PostMapping("/{assignmentId}/submit/student/{studentId}")
    public ResponseEntity<StudentAssignmentDTO> submitAssignment(
            @PathVariable UUID assignmentId,
            @PathVariable UUID studentId,
            @RequestBody StudentAssignmentDTO studentAssignmentDTO) {
        return assignmentService.submitAssignment(assignmentId, studentId, studentAssignmentDTO);
    }

    @PutMapping("/grade/{id}")
    public ResponseEntity<StudentAssignmentDTO> gradeAssignment(
            @PathVariable UUID id,
            @RequestBody StudentAssignmentDTO studentAssignmentDTO) {
        return assignmentService.gradeAssignment(id, studentAssignmentDTO);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentAssignmentDTO>> getStudentAssignments(@PathVariable UUID studentId) {
        return assignmentService.getStudentAssignments(studentId);
    }

    @GetMapping("/{assignmentId}/submissions")
    public ResponseEntity<List<StudentAssignmentDTO>> getSubmissionsByAssignment(@PathVariable UUID assignmentId) {
        return assignmentService.getSubmissionsByAssignment(assignmentId);
    }

    @GetMapping("/{assignmentId}/student/{studentId}")
    public ResponseEntity<StudentAssignmentDTO> getStudentAssignment(
            @PathVariable UUID assignmentId,
            @PathVariable UUID studentId) {
        return assignmentService.getStudentAssignment(assignmentId, studentId);
    }
}

