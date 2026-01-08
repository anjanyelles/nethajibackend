package com.nethaji.repositories;

import com.nethaji.entity.ExamesTimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExamesTimeTableRepo extends JpaRepository<ExamesTimeTable, UUID> {

    @Query(value = "select * from public.exames_time_table where department_id=:departmentId and semester_id=:semesterId and exam_subject=:examSubject",nativeQuery = true)
    ExamesTimeTable findByDepartmentIdAndSemesterIdAndExamSubject(@Param("departmentId") UUID departmentId,@Param("semesterId") UUID semesterId,@Param("examSubject") String examSubject);
}
