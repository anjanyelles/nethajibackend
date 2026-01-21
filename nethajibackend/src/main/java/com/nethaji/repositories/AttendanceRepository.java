package com.nethaji.repositories;

import com.nethaji.entity.Attendance;
import com.nethaji.Enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    List<Attendance> findByStudentId(UUID studentId);

    List<Attendance> findByCourseId(UUID courseId);

    List<Attendance> findByStudentIdAndCourseId(UUID studentId, UUID courseId);

    @Query("SELECT a FROM Attendance a WHERE a.studentId = :studentId AND a.courseId = :courseId AND a.attendanceDate BETWEEN :startDate AND :endDate")
    List<Attendance> findByStudentIdAndCourseIdAndDateRange(
            @Param("studentId") UUID studentId,
            @Param("courseId") UUID courseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT a FROM Attendance a WHERE a.courseId = :courseId AND a.attendanceDate = :date")
    List<Attendance> findByCourseIdAndDate(@Param("courseId") UUID courseId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.studentId = :studentId AND a.courseId = :courseId AND a.status = :status")
    Long countByStudentIdAndCourseIdAndStatus(
            @Param("studentId") UUID studentId,
            @Param("courseId") UUID courseId,
            @Param("status") AttendanceStatus status
    );

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.studentId = :studentId AND a.courseId = :courseId")
    Long countTotalByStudentIdAndCourseId(
            @Param("studentId") UUID studentId,
            @Param("courseId") UUID courseId
    );
}

