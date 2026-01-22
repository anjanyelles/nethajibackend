package com.nethaji.repositories;

import com.nethaji.entity.LecturerCourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LecturerCourseAssignmentRepository extends JpaRepository<LecturerCourseAssignment, UUID> {
    List<LecturerCourseAssignment> findByLecturerIdAndIsActiveTrue(UUID lecturerId);

    List<LecturerCourseAssignment> findByLecturerId(UUID lecturerId);

    Optional<LecturerCourseAssignment> findByLecturerIdAndCourseId(UUID lecturerId, UUID courseId);

    boolean existsByLecturerIdAndCourseIdAndIsActiveTrue(UUID lecturerId, UUID courseId);
}
