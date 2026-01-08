package com.nethaji.repositories;

import com.nethaji.entity.StaffProfile;
import com.nethaji.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface StaffProfileRepo extends JpaRepository<StaffProfile, UUID> {
    StaffProfile findByUserId(UUID userId);


}
