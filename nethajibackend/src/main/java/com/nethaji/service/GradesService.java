package com.nethaji.service;

import com.nethaji.dto.GradesDTO;
import com.nethaji.dto.TranscriptDTO;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface GradesService {
    ResponseEntity<GradesDTO> calculateGrade(UUID studentId, UUID courseId, UUID semesterId);
    ResponseEntity<List<GradesDTO>> calculateGradesForSemester(UUID studentId, UUID semesterId);
    ResponseEntity<List<GradesDTO>> getGradesByStudent(UUID studentId);
    ResponseEntity<List<GradesDTO>> getGradesByStudentAndSemester(UUID studentId, UUID semesterId);
    ResponseEntity<TranscriptDTO> getTranscript(UUID studentId, UUID semesterId);
    ResponseEntity<BigDecimal> getSGPA(UUID studentId, UUID semesterId);
    ResponseEntity<BigDecimal> getCGPA(UUID studentId);
    ResponseEntity<GradesDTO> updateGrade(UUID id, GradesDTO gradesDTO);
}

