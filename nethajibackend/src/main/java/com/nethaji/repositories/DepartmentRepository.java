package com.nethaji.repositories;

import com.nethaji.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {



    @Query(value = "SELECT * FROM department WHERE UPPER(department_code) = UPPER(:code)", nativeQuery = true)
    Optional<Department> findbyDepartMentCode(@Param("code") String code);

    @Query(value = "SELECT * FROM department WHERE id=:id", nativeQuery = true)
    List<Department> findBYProgramId(@Param("id") UUID id);

    @Query(value = "SELECT * FROM department WHERE department_code=:departMentTYpe", nativeQuery = true)
    Department findByDepartMentCode(@Param("departMentTYpe") String departMentTYpe);

    List<Department> findByProgramId(UUID id);
}
