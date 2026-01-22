package com.nethaji.serviceImpl;

import com.nethaji.dto.BulkMarksDTO;
import com.nethaji.dto.MarksDTO;
import com.nethaji.entity.Course;
import com.nethaji.entity.Marks;
import com.nethaji.entity.User;
import com.nethaji.repositories.CourseRepository;
import com.nethaji.repositories.MarksRepository;
import com.nethaji.repositories.UserRepository;
import com.nethaji.service.MarksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MarksServiceImpl implements MarksService {

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ResponseEntity<MarksDTO> enterMarks(MarksDTO marksDTO) {
        try {
            if (marksDTO == null) {
                return ResponseEntity.badRequest().build();
            }
            if (isMarksOutOfRange(marksDTO.getMarksObtained(), marksDTO.getMaxMarks())) {
                return ResponseEntity.badRequest().build();
            }
            Marks marks = new Marks();
            marks.setStudentId(marksDTO.getStudentId());
            marks.setCourseId(marksDTO.getCourseId());
            marks.setExamType(marksDTO.getExamType());
            marks.setMarksObtained(marksDTO.getMarksObtained());
            marks.setMaxMarks(marksDTO.getMaxMarks());
            marks.setExamDate(marksDTO.getExamDate());
            marks.setEvaluatedBy(marksDTO.getEvaluatedBy());
            marks.setRemarks(marksDTO.getRemarks());

            Marks saved = marksRepository.save(marks);
            return ResponseEntity.ok(convertToDTO(saved));
        } catch (Exception e) {
            log.error("Error entering marks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isMarksOutOfRange(BigDecimal obtained, BigDecimal max) {
        if (obtained == null || max == null) {
            return false;
        }
        if (max.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }
        return obtained.compareTo(BigDecimal.ZERO) < 0 || obtained.compareTo(max) > 0;
    }

    @Override
    public ResponseEntity<List<MarksDTO>> enterBulkMarks(BulkMarksDTO bulkMarksDTO) {
        try {
            if (bulkMarksDTO == null || bulkMarksDTO.getMaxMarks() == null) {
                return ResponseEntity.badRequest().build();
            }
            if (bulkMarksDTO.getMaxMarks().compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().build();
            }
            List<Marks> marksList = new ArrayList<>();
            for (BulkMarksDTO.StudentMarksEntry entry : bulkMarksDTO.getStudents()) {
                if (entry == null) {
                    return ResponseEntity.badRequest().build();
                }
                if (isMarksOutOfRange(entry.getMarksObtained(), bulkMarksDTO.getMaxMarks())) {
                    return ResponseEntity.badRequest().build();
                }
                Marks marks = new Marks();
                marks.setStudentId(entry.getStudentId());
                marks.setCourseId(bulkMarksDTO.getCourseId());
                marks.setExamType(bulkMarksDTO.getExamType());
                marks.setMarksObtained(entry.getMarksObtained());
                marks.setMaxMarks(bulkMarksDTO.getMaxMarks());
                marks.setExamDate(bulkMarksDTO.getExamDate());
                marks.setEvaluatedBy(bulkMarksDTO.getEvaluatedBy());
                marks.setRemarks(entry.getRemarks());
                marksList.add(marks);
            }
            List<Marks> saved = marksRepository.saveAll(marksList);
            return ResponseEntity.ok(saved.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error entering bulk marks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MarksDTO>> getMarksByStudent(UUID studentId) {
        try {
            List<Marks> marks = marksRepository.findByStudentId(studentId);
            return ResponseEntity.ok(marks.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching marks by student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MarksDTO>> getMarksByCourse(UUID courseId) {
        try {
            List<Marks> marks = marksRepository.findByCourseId(courseId);
            return ResponseEntity.ok(marks.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching marks by course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<MarksDTO>> getMarksByStudentAndCourse(UUID studentId, UUID courseId) {
        try {
            List<Marks> marks = marksRepository.findByStudentIdAndCourseId(studentId, courseId);
            return ResponseEntity.ok(marks.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching marks by student and course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<MarksDTO> updateMarks(UUID id, MarksDTO marksDTO) {
        try {
            Marks marks = marksRepository.findById(id).orElse(null);
            if (marks == null) {
                return ResponseEntity.notFound().build();
            }

            if (marksDTO == null) {
                return ResponseEntity.badRequest().build();
            }
            if (isMarksOutOfRange(marksDTO.getMarksObtained(), marksDTO.getMaxMarks())) {
                return ResponseEntity.badRequest().build();
            }

            marks.setMarksObtained(marksDTO.getMarksObtained());
            marks.setMaxMarks(marksDTO.getMaxMarks());
            marks.setExamDate(marksDTO.getExamDate());
            marks.setRemarks(marksDTO.getRemarks());

            Marks updated = marksRepository.save(marks);
            return ResponseEntity.ok(convertToDTO(updated));
        } catch (Exception e) {
            log.error("Error updating marks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteMarks(UUID id) {
        try {
            marksRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting marks", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private MarksDTO convertToDTO(Marks marks) {
        MarksDTO dto = new MarksDTO();
        dto.setId(marks.getId());
        dto.setStudentId(marks.getStudentId());
        dto.setCourseId(marks.getCourseId());
        dto.setExamType(marks.getExamType());
        dto.setMarksObtained(marks.getMarksObtained());
        dto.setMaxMarks(marks.getMaxMarks());
        dto.setExamDate(marks.getExamDate());
        dto.setEvaluatedBy(marks.getEvaluatedBy());
        dto.setRemarks(marks.getRemarks());

        User student = userRepository.findById(marks.getStudentId()).orElse(null);
        Course course = courseRepository.findById(marks.getCourseId()).orElse(null);

        if (student != null) {
            dto.setStudentName(student.getFirstName() + " " + student.getLastName());
        }
        if (course != null) {
            dto.setCourseName(course.getName());
        }

        return dto;
    }
}

