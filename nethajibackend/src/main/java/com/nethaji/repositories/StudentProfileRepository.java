package com.nethaji.repositories;

import com.nethaji.Enums.StudentStatus;
import com.nethaji.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {
    Optional<StudentProfile> findByUserId(UUID id);

    @Query(value="select * from public.profile where user_id=:studentId and studentStatus=:active",nativeQuery = true)
    StudentProfile findByUserIdAndStatus(@Param("studentId") UUID studentId,@Param("active") StudentStatus active);

}
