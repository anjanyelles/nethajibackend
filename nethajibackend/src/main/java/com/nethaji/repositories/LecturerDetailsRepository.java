package com.nethaji.repositories;

import com.nethaji.entity.LecturerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LecturerDetailsRepository extends JpaRepository<LecturerDetails, UUID> {

}
