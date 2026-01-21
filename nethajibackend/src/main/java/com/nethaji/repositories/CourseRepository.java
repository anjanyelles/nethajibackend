package com.nethaji.repositories;

import com.nethaji.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    List<Course> findByDepartmentSemesterId(UUID id);

    Optional<Course> findByCourseCodeIgnoreCase(String courseCode);

   // List<Course> findByProgramId(UUID programId);
}
