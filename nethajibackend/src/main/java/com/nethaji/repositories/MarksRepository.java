package com.nethaji.repositories;

import com.nethaji.entity.Marks;
import com.nethaji.Enums.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MarksRepository extends JpaRepository<Marks, UUID> {

    List<Marks> findByStudentId(UUID studentId);

    List<Marks> findByCourseId(UUID courseId);

    List<Marks> findByStudentIdAndCourseId(UUID studentId, UUID courseId);

    List<Marks> findByStudentIdAndCourseIdAndExamType(UUID studentId, UUID courseId, ExamType examType);

    @Query("SELECT m FROM Marks m WHERE m.studentId = :studentId AND m.courseId = :courseId ORDER BY m.examDate DESC")
    List<Marks> findByStudentIdAndCourseIdOrderByExamDate(@Param("studentId") UUID studentId, @Param("courseId") UUID courseId);
}

