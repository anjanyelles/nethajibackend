package com.nethaji.controller;

import com.nethaji.dto.BulkMarksDTO;
import com.nethaji.dto.MarksDTO;
import com.nethaji.service.MarksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nethaji-service/marks")
@CrossOrigin
public class MarksController {

    @Autowired
    private MarksService marksService;

    @PostMapping("/enter")
    public ResponseEntity<MarksDTO> enterMarks(@RequestBody MarksDTO marksDTO) {
        return marksService.enterMarks(marksDTO);
    }

    @PostMapping("/enter-bulk")
    public ResponseEntity<List<MarksDTO>> enterBulkMarks(@RequestBody BulkMarksDTO bulkMarksDTO) {
        return marksService.enterBulkMarks(bulkMarksDTO);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<MarksDTO>> getMarksByStudent(@PathVariable UUID studentId) {
        return marksService.getMarksByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<MarksDTO>> getMarksByCourse(@PathVariable UUID courseId) {
        return marksService.getMarksByCourse(courseId);
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<MarksDTO>> getMarksByStudentAndCourse(
            @PathVariable UUID studentId,
            @PathVariable UUID courseId) {
        return marksService.getMarksByStudentAndCourse(studentId, courseId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MarksDTO> updateMarks(
            @PathVariable UUID id,
            @RequestBody MarksDTO marksDTO) {
        return marksService.updateMarks(id, marksDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMarks(@PathVariable UUID id) {
        return marksService.deleteMarks(id);
    }
}

