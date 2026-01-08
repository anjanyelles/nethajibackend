package com.nethaji.repositories;

import com.nethaji.Enums.ProgramLevel;
import com.nethaji.entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProgramsRepository extends JpaRepository<Programs, UUID> {


    @Query(value = "SELECT * FROM programs WHERE UPPER(program_code) = UPPER(:code)", nativeQuery = true)
    Optional<Programs> findByCode(@Param("code") String code);

    @Query(value = "SELECT * FROM programs WHERE level=:valueOf", nativeQuery = true)
    Programs findByProgramLevel(@Param("valueOf") String valueOf);
}
