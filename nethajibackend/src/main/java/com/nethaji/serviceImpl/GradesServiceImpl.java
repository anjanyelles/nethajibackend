package com.nethaji.serviceImpl;

import com.nethaji.Enums.Grade;
import com.nethaji.dto.GradesDTO;
import com.nethaji.dto.TranscriptDTO;
import com.nethaji.entity.Course;
import com.nethaji.entity.Grades;
import com.nethaji.entity.Marks;
import com.nethaji.entity.User;
import com.nethaji.repositories.CourseRepository;
import com.nethaji.repositories.GradesRepository;
import com.nethaji.repositories.MarksRepository;
import com.nethaji.repositories.UserRepository;
import com.nethaji.service.GradesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GradesServiceImpl implements GradesService {

    @Autowired
    private GradesRepository gradesRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public ResponseEntity<GradesDTO> calculateGrade(UUID studentId, UUID courseId, UUID semesterId) {
        try {
            List<Marks> marksList = marksRepository.findByStudentIdAndCourseId(studentId, courseId);
            
            BigDecimal internalMarks = BigDecimal.ZERO;
            BigDecimal midtermMarks = BigDecimal.ZERO;
            BigDecimal finalMarks = BigDecimal.ZERO;

            for (Marks marks : marksList) {
                switch (marks.getExamType()) {
                    case INTERNAL:
                    case QUIZ:
                    case ASSIGNMENT:
                        internalMarks = internalMarks.add(marks.getMarksObtained());
                        break;
                    case MIDTERM:
                        midtermMarks = marks.getMarksObtained();
                        break;
                    case FINAL:
                    case SEMESTEREXAMES:
                        finalMarks = marks.getMarksObtained();
                        break;
                }
            }

            // Calculate total marks (assuming weightage: Internal 30%, Midterm 20%, Final 50%)
            BigDecimal totalMarks = internalMarks.multiply(BigDecimal.valueOf(0.3))
                    .add(midtermMarks.multiply(BigDecimal.valueOf(0.2)))
                    .add(finalMarks.multiply(BigDecimal.valueOf(0.5)));

            // Get max marks for percentage calculation
            BigDecimal maxInternal = marksList.stream()
                    .filter(m -> m.getExamType() == com.nethaji.Enums.ExamType.INTERNAL || 
                                m.getExamType() == com.nethaji.Enums.ExamType.QUIZ ||
                                m.getExamType() == com.nethaji.Enums.ExamType.ASSIGNMENT)
                    .map(Marks::getMaxMarks)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal maxMidterm = marksList.stream()
                    .filter(m -> m.getExamType() == com.nethaji.Enums.ExamType.MIDTERM)
                    .map(Marks::getMaxMarks)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);

            BigDecimal maxFinal = marksList.stream()
                    .filter(m -> m.getExamType() == com.nethaji.Enums.ExamType.FINAL || 
                                m.getExamType() == com.nethaji.Enums.ExamType.SEMESTEREXAMES)
                    .map(Marks::getMaxMarks)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);

            BigDecimal maxTotal = maxInternal.multiply(BigDecimal.valueOf(0.3))
                    .add(maxMidterm.multiply(BigDecimal.valueOf(0.2)))
                    .add(maxFinal.multiply(BigDecimal.valueOf(0.5)));

            BigDecimal percentage = maxTotal.compareTo(BigDecimal.ZERO) > 0 ?
                    totalMarks.divide(maxTotal, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                    BigDecimal.ZERO;

            Grade grade = Grade.getGradeByPercentage(percentage.doubleValue());

            Grades existingGrade = gradesRepository.findByStudentIdAndCourseId(studentId, courseId);
            Grades gradeEntity;
            
            if (existingGrade != null) {
                gradeEntity = existingGrade;
            } else {
                gradeEntity = new Grades();
                gradeEntity.setStudentId(studentId);
                gradeEntity.setCourseId(courseId);
                gradeEntity.setSemesterId(semesterId);
            }

            gradeEntity.setInternalMarks(internalMarks);
            gradeEntity.setMidtermMarks(midtermMarks);
            gradeEntity.setFinalMarks(finalMarks);
            gradeEntity.setTotalMarks(totalMarks);
            gradeEntity.setGrade(grade);
            gradeEntity.setGradePoint(BigDecimal.valueOf(grade.getGradePoint()));
            gradeEntity.setPercentage(percentage);

            Grades saved = gradesRepository.save(gradeEntity);
            return ResponseEntity.ok(convertToDTO(saved));
        } catch (Exception e) {
            log.error("Error calculating grade", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<GradesDTO>> calculateGradesForSemester(UUID studentId, UUID semesterId) {
        try {
            // Get all courses for the semester and calculate grades
            List<Marks> allMarks = marksRepository.findByStudentId(studentId);
            List<UUID> courseIds = allMarks.stream()
                    .map(Marks::getCourseId)
                    .distinct()
                    .collect(Collectors.toList());

            List<GradesDTO> grades = new ArrayList<>();
            for (UUID courseId : courseIds) {
                ResponseEntity<GradesDTO> gradeResponse = calculateGrade(studentId, courseId, semesterId);
                if (gradeResponse.getStatusCode() == HttpStatus.OK && gradeResponse.getBody() != null) {
                    grades.add(gradeResponse.getBody());
                }
            }
            return ResponseEntity.ok(grades);
        } catch (Exception e) {
            log.error("Error calculating grades for semester", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<GradesDTO>> getGradesByStudent(UUID studentId) {
        try {
            List<Grades> grades = gradesRepository.findByStudentId(studentId);
            return ResponseEntity.ok(grades.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching grades by student", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<List<GradesDTO>> getGradesByStudentAndSemester(UUID studentId, UUID semesterId) {
        try {
            List<Grades> grades = gradesRepository.findByStudentIdAndSemesterId(studentId, semesterId);
            return ResponseEntity.ok(grades.stream().map(this::convertToDTO).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error fetching grades by student and semester", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<TranscriptDTO> getTranscript(UUID studentId, UUID semesterId) {
        try {
            List<Grades> grades = gradesRepository.findByStudentIdAndSemesterId(studentId, semesterId);
            User student = userRepository.findById(studentId).orElse(null);

            TranscriptDTO transcript = new TranscriptDTO();
            transcript.setStudentId(studentId);
            if (student != null) {
                transcript.setStudentName(student.getFirstName() + " " + student.getLastName());
                transcript.setEnrollmentNumber(student.getEnrollmentNumber());
            }

            List<TranscriptDTO.CourseGradeDTO> courseGrades = new ArrayList<>();
            BigDecimal totalGradePoints = BigDecimal.ZERO;
            int totalCredits = 0;

            for (Grades grade : grades) {
                Course course = courseRepository.findById(grade.getCourseId()).orElse(null);
                TranscriptDTO.CourseGradeDTO courseGrade = new TranscriptDTO.CourseGradeDTO();
                if (course != null) {
                    courseGrade.setCourseName(course.getName());
                    courseGrade.setCourseCode(course.getCourseCode());
                    courseGrade.setCredits(course.getCredits() != null ? course.getCredits() : 0);
                }
                courseGrade.setTotalMarks(grade.getTotalMarks());
                courseGrade.setGrade(grade.getGrade().name());
                courseGrade.setGradePoint(grade.getGradePoint());

                if (course != null && course.getCredits() != null) {
                    totalGradePoints = totalGradePoints.add(grade.getGradePoint().multiply(BigDecimal.valueOf(course.getCredits())));
                    totalCredits += course.getCredits();
                }

                courseGrades.add(courseGrade);
            }

            transcript.setCourses(courseGrades);
            
            if (totalCredits > 0) {
                BigDecimal sgpa = totalGradePoints.divide(BigDecimal.valueOf(totalCredits), 2, RoundingMode.HALF_UP);
                transcript.setSgpa(sgpa);
            } else {
                transcript.setSgpa(BigDecimal.ZERO);
            }

            Double cgpaValue = gradesRepository.calculateCGPA(studentId);
            transcript.setCgpa(cgpaValue != null ? BigDecimal.valueOf(cgpaValue) : BigDecimal.ZERO);

            return ResponseEntity.ok(transcript);
        } catch (Exception e) {
            log.error("Error generating transcript", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<BigDecimal> getSGPA(UUID studentId, UUID semesterId) {
        try {
            Double sgpa = gradesRepository.calculateSGPA(studentId, semesterId);
            return ResponseEntity.ok(sgpa != null ? BigDecimal.valueOf(sgpa) : BigDecimal.ZERO);
        } catch (Exception e) {
            log.error("Error calculating SGPA", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<BigDecimal> getCGPA(UUID studentId) {
        try {
            Double cgpa = gradesRepository.calculateCGPA(studentId);
            return ResponseEntity.ok(cgpa != null ? BigDecimal.valueOf(cgpa) : BigDecimal.ZERO);
        } catch (Exception e) {
            log.error("Error calculating CGPA", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<GradesDTO> updateGrade(UUID id, GradesDTO gradesDTO) {
        try {
            Grades grade = gradesRepository.findById(id).orElse(null);
            if (grade == null) {
                return ResponseEntity.notFound().build();
            }

            grade.setTotalMarks(gradesDTO.getTotalMarks());
            grade.setGrade(gradesDTO.getGrade());
            grade.setGradePoint(gradesDTO.getGradePoint());
            grade.setPercentage(gradesDTO.getPercentage());

            Grades updated = gradesRepository.save(grade);
            return ResponseEntity.ok(convertToDTO(updated));
        } catch (Exception e) {
            log.error("Error updating grade", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private GradesDTO convertToDTO(Grades grade) {
        GradesDTO dto = new GradesDTO();
        dto.setId(grade.getId());
        dto.setStudentId(grade.getStudentId());
        dto.setCourseId(grade.getCourseId());
        dto.setSemesterId(grade.getSemesterId());
        dto.setInternalMarks(grade.getInternalMarks());
        dto.setMidtermMarks(grade.getMidtermMarks());
        dto.setFinalMarks(grade.getFinalMarks());
        dto.setTotalMarks(grade.getTotalMarks());
        dto.setGrade(grade.getGrade());
        dto.setGradePoint(grade.getGradePoint());
        dto.setPercentage(grade.getPercentage());

        User student = userRepository.findById(grade.getStudentId()).orElse(null);
        Course course = courseRepository.findById(grade.getCourseId()).orElse(null);

        if (student != null) {
            dto.setStudentName(student.getFirstName() + " " + student.getLastName());
        }
        if (course != null) {
            dto.setCourseName(course.getName());
        }

        return dto;
    }
}

