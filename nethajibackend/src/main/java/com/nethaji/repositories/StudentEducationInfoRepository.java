package com.nethaji.repositories;

import com.nethaji.Enums.Branch;
import com.nethaji.entity.StudentEducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StudentEducationInfoRepository extends JpaRepository<StudentEducationInfo, UUID > {


    Optional<StudentEducationInfo> findByStudentIdAndSemesterAndIsActiveTrue(UUID studentId, Integer semester);


    List<StudentEducationInfo> findByStudentId(UUID studentId);

    StudentEducationInfo findByBranchAndSemester(String branch, Integer semester);

    @Query("SELECT s FROM StudentEducationInfo s WHERE s.programId = :programId AND s.semester = :semester AND s.isActive = true")
    List<StudentEducationInfo> findActiveStudentsByProgramAndSemester(@Param("programId") UUID programId, @Param("semester") Integer semester);



   /* @Query("SELECT s FROM StudentSemesters s WHERE s.studentId = :studentId AND s.isActive = true")
    StudentEducationInfo findActiveSemesterByStudentId(@Param("studentId") UUID studentId);

    @Query("SELECT s FROM StudentSemesters s WHERE s.branch = :branch AND s.student_semester =:semester")
    StudentEducationInfo findByBranchAndSemester(@Param("branch")String branch,@Param("semester") Integer semester);*/
}
