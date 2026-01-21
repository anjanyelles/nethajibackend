package com.nethaji.repositories;

import com.nethaji.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {

    List<Assignment> findByCourseId(UUID courseId);

    List<Assignment> findByCreatedBy(UUID createdBy);

    List<Assignment> findByCourseIdAndIsActiveTrue(UUID courseId);

    @Query("SELECT a FROM Assignment a WHERE a.courseId = :courseId AND a.isActive = true ORDER BY a.dueDate ASC")
    List<Assignment> findActiveAssignmentsByCourseIdOrderByDueDate(@Param("courseId") UUID courseId);
}

