package com.nethaji.repositories;

import com.nethaji.entity.StudentSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StudentSectionRepository extends JpaRepository<StudentSection, UUID> {



    @Query(value = "SELECT * FROM student_sections s WHERE s.student_id = :studentId AND s.semester = :semester AND s.status = true ",nativeQuery = true)
    StudentSection findActiveSectionByStudentAndSemester(@Param("studentId") UUID studentId, @Param("semester") Integer semester);


    @Query(value = "SELECT * FROM student_sections s WHERE s.student_id = :studentId AND s.semester = :semester AND s.status = true ",nativeQuery = true)
    StudentSection findByStudentId(@Param("studentId") UUID studentId, @Param("semester") Integer semester);

    @Query(value = "SELECT * FROM student_sections s WHERE s.section_name = :section AND s.semester = :semester AND s.status = true ",nativeQuery = true)
    List<StudentSection> findBySectionNameAndSemester(@Param("section") String section,@Param("semester") Integer semester);
}
