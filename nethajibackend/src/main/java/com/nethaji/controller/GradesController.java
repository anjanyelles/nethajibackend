package com.nethaji.controller;

import com.nethaji.dto.GradesDTO;
import com.nethaji.dto.TranscriptDTO;
import com.nethaji.service.GradesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nethaji-service/grades")
@CrossOrigin
public class GradesController {

    @Autowired
    private GradesService gradesService;

    @PostMapping("/calculate")
    public ResponseEntity<GradesDTO> calculateGrade(
            @RequestParam UUID studentId,
            @RequestParam UUID courseId,
            @RequestParam UUID semesterId) {
        return gradesService.calculateGrade(studentId, courseId, semesterId);
    }

    @PostMapping("/calculate-semester")
    public ResponseEntity<List<GradesDTO>> calculateGradesForSemester(
            @RequestParam UUID studentId,
            @RequestParam UUID semesterId) {
        return gradesService.calculateGradesForSemester(studentId, semesterId);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradesDTO>> getGradesByStudent(@PathVariable UUID studentId) {
        return gradesService.getGradesByStudent(studentId);
    }

    @GetMapping("/student/{studentId}/semester/{semesterId}")
    public ResponseEntity<List<GradesDTO>> getGradesByStudentAndSemester(
            @PathVariable UUID studentId,
            @PathVariable UUID semesterId) {
        return gradesService.getGradesByStudentAndSemester(studentId, semesterId);
    }

    @GetMapping("/transcript/student/{studentId}/semester/{semesterId}")
    public ResponseEntity<TranscriptDTO> getTranscript(
            @PathVariable UUID studentId,
            @PathVariable UUID semesterId) {
        return gradesService.getTranscript(studentId, semesterId);
    }

    @GetMapping("/sgpa/student/{studentId}/semester/{semesterId}")
    public ResponseEntity<BigDecimal> getSGPA(
            @PathVariable UUID studentId,
            @PathVariable UUID semesterId) {
        return gradesService.getSGPA(studentId, semesterId);
    }

    @GetMapping("/cgpa/student/{studentId}")
    public ResponseEntity<BigDecimal> getCGPA(@PathVariable UUID studentId) {
        return gradesService.getCGPA(studentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradesDTO> updateGrade(
            @PathVariable UUID id,
            @RequestBody GradesDTO gradesDTO) {
        return gradesService.updateGrade(id, gradesDTO);
    }
}

