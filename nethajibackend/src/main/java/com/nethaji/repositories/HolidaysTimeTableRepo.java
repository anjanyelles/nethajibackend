package com.nethaji.repositories;

import com.nethaji.entity.HolidaysTimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface HolidaysTimeTableRepo extends JpaRepository<HolidaysTimeTable, UUID> {
}
