package com.nethaji.service;

import com.nethaji.entity.StudentEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StudentEnrollmentService {

    StudentEnrollment saveEnrollment(StudentEnrollment enrollment);

    Page<StudentEnrollment> getAllEnrollments(Pageable pageable);

    StudentEnrollment getEnrollmentById(UUID id);

    void deleteEnrollment(UUID id);

    StudentEnrollment updateEnrollment(UUID id, StudentEnrollment enrollment);
}
