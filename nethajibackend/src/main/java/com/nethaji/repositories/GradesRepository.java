package com.nethaji.repositories;

import com.nethaji.entity.Grades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GradesRepository extends JpaRepository<Grades, UUID> {

    List<Grades> findByStudentId(UUID studentId);

    List<Grades> findByStudentIdAndSemesterId(UUID studentId, UUID semesterId);

    List<Grades> findByCourseId(UUID courseId);

    Grades findByStudentIdAndCourseId(UUID studentId, UUID courseId);

    @Query("SELECT AVG(g.gradePoint) FROM Grades g WHERE g.studentId = :studentId AND g.semesterId = :semesterId")
    Double calculateSGPA(@Param("studentId") UUID studentId, @Param("semesterId") UUID semesterId);

    @Query("SELECT AVG(g.gradePoint) FROM Grades g WHERE g.studentId = :studentId")
    Double calculateCGPA(@Param("studentId") UUID studentId);
}

