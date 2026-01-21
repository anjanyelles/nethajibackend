package com.nethaji.service;

import com.nethaji.dto.AssignmentDTO;
import com.nethaji.dto.StudentAssignmentDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AssignmentService {
    ResponseEntity<AssignmentDTO> createAssignment(AssignmentDTO assignmentDTO);
    ResponseEntity<AssignmentDTO> updateAssignment(UUID id, AssignmentDTO assignmentDTO);
    ResponseEntity<List<AssignmentDTO>> getAssignmentsByCourse(UUID courseId);
    ResponseEntity<AssignmentDTO> getAssignmentById(UUID id);
    ResponseEntity<Void> deleteAssignment(UUID id);
    ResponseEntity<StudentAssignmentDTO> submitAssignment(UUID assignmentId, UUID studentId, StudentAssignmentDTO studentAssignmentDTO);
    ResponseEntity<StudentAssignmentDTO> gradeAssignment(UUID id, StudentAssignmentDTO studentAssignmentDTO);
    ResponseEntity<List<StudentAssignmentDTO>> getStudentAssignments(UUID studentId);
    ResponseEntity<List<StudentAssignmentDTO>> getSubmissionsByAssignment(UUID assignmentId);
    ResponseEntity<StudentAssignmentDTO> getStudentAssignment(UUID assignmentId, UUID studentId);
}

