package com.nethaji.repositories;

import com.nethaji.entity.DepartMentSemesters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartMentSemestersRepo extends JpaRepository<DepartMentSemesters, UUID> {

    @Query(value="select * from public.department_semesters where id=:id ",nativeQuery = true)

    List<DepartMentSemesters> findByDepartmentId(@Param("id") UUID id);

    @Query(value="select * from public.department_semesters where department_id=:id ",nativeQuery = true)
    List<DepartMentSemesters> findByDepartmentId1(@Param("id") UUID id);

    @Query(value="select * from public.department_semesters where id=:id and semester=:semeste",nativeQuery = true)
    DepartMentSemesters findByDepartmentIdAndSemester(@Param("id") UUID id,@Param("semeste") Integer semeste);
}
