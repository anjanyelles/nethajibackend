package com.nethaji.repositories;


import com.nethaji.entity.StudentPrograms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StudentProgramsRepository extends JpaRepository<StudentPrograms, UUID> {




    List<StudentPrograms> findByProgramIdAndIsActiveTrue(UUID programId);

    StudentPrograms findByStudentIdAndIsActiveTrue(UUID studentId);
}
