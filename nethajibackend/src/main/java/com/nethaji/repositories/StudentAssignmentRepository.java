package com.nethaji.repositories;

import com.nethaji.entity.StudentAssignment;
import com.nethaji.Enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentAssignmentRepository extends JpaRepository<StudentAssignment, UUID> {

    List<StudentAssignment> findByStudentId(UUID studentId);

    List<StudentAssignment> findByAssignmentId(UUID assignmentId);

    StudentAssignment findByStudentIdAndAssignmentId(UUID studentId, UUID assignmentId);

    List<StudentAssignment> findByStudentIdAndStatus(UUID studentId, AssignmentStatus status);

    @Query("SELECT sa FROM StudentAssignment sa WHERE sa.assignmentId = :assignmentId AND sa.status = :status")
    List<StudentAssignment> findByAssignmentIdAndStatus(@Param("assignmentId") UUID assignmentId, @Param("status") AssignmentStatus status);
}

