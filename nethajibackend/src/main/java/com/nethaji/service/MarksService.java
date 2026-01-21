package com.nethaji.service;

import com.nethaji.dto.BulkMarksDTO;
import com.nethaji.dto.MarksDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface MarksService {
    ResponseEntity<MarksDTO> enterMarks(MarksDTO marksDTO);
    ResponseEntity<List<MarksDTO>> enterBulkMarks(BulkMarksDTO bulkMarksDTO);
    ResponseEntity<List<MarksDTO>> getMarksByStudent(UUID studentId);
    ResponseEntity<List<MarksDTO>> getMarksByCourse(UUID courseId);
    ResponseEntity<List<MarksDTO>> getMarksByStudentAndCourse(UUID studentId, UUID courseId);
    ResponseEntity<MarksDTO> updateMarks(UUID id, MarksDTO marksDTO);
    ResponseEntity<Void> deleteMarks(UUID id);
}

