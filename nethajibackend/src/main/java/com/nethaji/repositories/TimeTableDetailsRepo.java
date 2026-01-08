package com.nethaji.repositories;

import com.nethaji.entity.TimeTableDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimeTableDetailsRepo extends JpaRepository<TimeTableDetails, UUID> {

    @Query(value="select * from public.time_table_details where department_id=:departmentId and semester_id=:semesterId and subject_name=:subjectName",nativeQuery = true)
    TimeTableDetails findByDepartmentIdAndSemesterIdAndSubjectName(@Param("departmentId") UUID departmentId,@Param("semesterId") UUID semesterId,@Param("subjectName") String subjectName);

    @Query(value="select * from public.time_table_details where department_id=:departmentId and semester_id=:semesterId",nativeQuery = true)
    List<TimeTableDetails> findByDepartmentIdAndSemesterId(@Param("departmentId") UUID departmentId,@Param("semesterId") UUID semesterId);
}
