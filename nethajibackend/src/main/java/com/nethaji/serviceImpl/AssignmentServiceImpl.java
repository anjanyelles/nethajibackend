package com.nethaji.serviceImpl;

import com.nethaji.Enums.AssignmentStatus;
import com.nethaji.dto.AssignmentDTO;
import com.nethaji.dto.StudentAssignmentDTO;
import com.nethaji.entity.Assignment;
import com.nethaji.entity.Course;
import com.nethaji.entity.StudentAssignment;
import com.nethaji.entity.User;
import com.nethaji.repositories.AssignmentRepository;
import com.nethaji.repositories.CourseRepository;
import com.nethaji.repositories.StudentAssignmentRepository;
import com.nethaji.repositories.UserRepository;
import com.nethaji.service.AssignmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StudentAssignmentRepository studentAssignmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ResponseEntity<AssignmentDTO> createAssignment(AssignmentDTO assignmentDTO) {
        try {
            Assignment assignment = new Assignment();
            assignment.setCourseId(assignmentDTO.getCourseId());
            assignment.setTitle(assignmentDTO.getTitle());
            assignment.setDescription(assignmentDTO.getDescription());
            assignment.setDueDate(assignmentDTO.getDueDate());
            assignment.setMaxMarks(assignmentDTO.getMaxMarks());
            assignment.setCreatedBy(assignmentDTO.getCreatedBy());
            assignment.setFileUrl(assignmentDTO.getFileUrl());
            assignment.setIsActive(true);

            Assignment saved = assignmentRepository.save(assignment);
            return ResponseEntity.ok(convertToDTO(saved));
        } catch (Exception e) {
            log.error("Error creating assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<AssignmentDTO> updateAssignment(UUID id, AssignmentDTO assignmentDTO) {
        try {
            Assignment assignment = assignmentRepository.findById(id).orElse(null);
            if (assignment == null) {
                return ResponseEntity.notFound().build();
            }

            assignment.setTitle(assignmentDTO.getTitle());
            assignment.setDescription(assignmentDTO.getDescription());
            assignment.setDueDate(assignmentDTO.getDueDate());
            assignment.setMaxMarks(assignmentDTO.getMaxMarks());
            assignment.setFileUrl(assignmentDTO.getFileUrl());
            assignment.setIsActive(assignmentDTO.getIsActive());

            Assignment updated = assignmentRepository.save(assignment);
            return ResponseEntity.ok(convertToDTO(updated));
        } catch (Exception e) {
            log.error("Error updating assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<AssignmentDTO>> getAssignmentsByCourse(UUID courseId) {
        try {
            List<Assignment> assignments = assignmentRepository.findByCourseIdAndIsActiveTrue(courseId);
            return ResponseEntity.ok(assignments.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching assignments by course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<AssignmentDTO> getAssignmentById(UUID id) {
        try {
            Assignment assignment = assignmentRepository.findById(id).orElse(null);
            if (assignment == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(convertToDTO(assignment));
        } catch (Exception e) {
            log.error("Error fetching assignment by id", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteAssignment(UUID id) {
        try {
            Assignment assignment = assignmentRepository.findById(id).orElse(null);
            if (assignment == null) {
                return ResponseEntity.notFound().build();
            }
            assignment.setIsActive(false);
            assignmentRepository.save(assignment);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<StudentAssignmentDTO> submitAssignment(UUID assignmentId, UUID studentId, StudentAssignmentDTO studentAssignmentDTO) {
        try {
            Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
            if (assignment == null) {
                return ResponseEntity.notFound().build();
            }

            StudentAssignment existing = studentAssignmentRepository.findByStudentIdAndAssignmentId(studentId, assignmentId);
            StudentAssignment studentAssignment;
            
            if (existing != null) {
                studentAssignment = existing;
            } else {
                studentAssignment = new StudentAssignment();
                studentAssignment.setAssignmentId(assignmentId);
                studentAssignment.setStudentId(studentId);
            }

            studentAssignment.setSubmissionDate(LocalDate.now());
            studentAssignment.setFileUrl(studentAssignmentDTO.getFileUrl());
            
            // Check if submission is late
            if (assignment.getDueDate().isBefore(LocalDate.now())) {
                studentAssignment.setStatus(AssignmentStatus.LATE);
            } else {
                studentAssignment.setStatus(AssignmentStatus.SUBMITTED);
            }

            StudentAssignment saved = studentAssignmentRepository.save(studentAssignment);
            return ResponseEntity.ok(convertToStudentAssignmentDTO(saved));
        } catch (Exception e) {
            log.error("Error submitting assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<StudentAssignmentDTO> gradeAssignment(UUID id, StudentAssignmentDTO studentAssignmentDTO) {
        try {
            StudentAssignment studentAssignment = studentAssignmentRepository.findById(id).orElse(null);
            if (studentAssignment == null) {
                return ResponseEntity.notFound().build();
            }

            studentAssignment.setMarksObtained(studentAssignmentDTO.getMarksObtained());
            studentAssignment.setFeedback(studentAssignmentDTO.getFeedback());
            studentAssignment.setStatus(AssignmentStatus.GRADED);
            studentAssignment.setGradedBy(studentAssignmentDTO.getGradedBy());
            studentAssignment.setGradedAt(new Date());

            StudentAssignment saved = studentAssignmentRepository.save(studentAssignment);
            return ResponseEntity.ok(convertToStudentAssignmentDTO(saved));
        } catch (Exception e) {
            log.error("Error grading assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<StudentAssignmentDTO>> getStudentAssignments(UUID studentId) {
        try {
            List<StudentAssignment> studentAssignments = studentAssignmentRepository.findByStudentId(studentId);
            return ResponseEntity.ok(studentAssignments.stream().map(this::convertToStudentAssignmentDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching student assignments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<StudentAssignmentDTO>> getSubmissionsByAssignment(UUID assignmentId) {
        try {
            List<StudentAssignment> studentAssignments = studentAssignmentRepository.findByAssignmentId(assignmentId);
            return ResponseEntity.ok(studentAssignments.stream().map(this::convertToStudentAssignmentDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching submissions by assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<StudentAssignmentDTO> getStudentAssignment(UUID assignmentId, UUID studentId) {
        try {
            StudentAssignment studentAssignment = studentAssignmentRepository.findByStudentIdAndAssignmentId(studentId, assignmentId);
            if (studentAssignment == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(convertToStudentAssignmentDTO(studentAssignment));
        } catch (Exception e) {
            log.error("Error fetching student assignment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private AssignmentDTO convertToDTO(Assignment assignment) {
        AssignmentDTO dto = new AssignmentDTO();
        dto.setId(assignment.getId());
        dto.setCourseId(assignment.getCourseId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setMaxMarks(assignment.getMaxMarks());
        dto.setCreatedBy(assignment.getCreatedBy());
        dto.setFileUrl(assignment.getFileUrl());
        dto.setIsActive(assignment.getIsActive());

        Course course = courseRepository.findById(assignment.getCourseId()).orElse(null);
        User createdBy = userRepository.findById(assignment.getCreatedBy()).orElse(null);

        if (course != null) {
            dto.setCourseName(course.getName());
        }
        if (createdBy != null) {
            dto.setCreatedByName(createdBy.getFirstName() + " " + createdBy.getLastName());
        }

        return dto;
    }

    private StudentAssignmentDTO convertToStudentAssignmentDTO(StudentAssignment studentAssignment) {
        StudentAssignmentDTO dto = new StudentAssignmentDTO();
        dto.setId(studentAssignment.getId());
        dto.setAssignmentId(studentAssignment.getAssignmentId());
        dto.setStudentId(studentAssignment.getStudentId());
        dto.setSubmissionDate(studentAssignment.getSubmissionDate());
        dto.setFileUrl(studentAssignment.getFileUrl());
        dto.setMarksObtained(studentAssignment.getMarksObtained());
        dto.setFeedback(studentAssignment.getFeedback());
        dto.setStatus(studentAssignment.getStatus());
        dto.setGradedBy(studentAssignment.getGradedBy());

        Assignment assignment = assignmentRepository.findById(studentAssignment.getAssignmentId()).orElse(null);
        User student = userRepository.findById(studentAssignment.getStudentId()).orElse(null);

        if (assignment != null) {
            dto.setAssignmentTitle(assignment.getTitle());
            dto.setDueDate(assignment.getDueDate());
            dto.setMaxMarks(assignment.getMaxMarks());
        }
        if (student != null) {
            dto.setStudentName(student.getFirstName() + " " + student.getLastName());
        }

        return dto;
    }
}

