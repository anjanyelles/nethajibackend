package com.nethaji.service.impl;

import com.nethaji.entity.StudentEnrollment;
import com.nethaji.repositories.StudentEnrollmentRepository;
import com.nethaji.service.StudentEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    @Autowired
    private StudentEnrollmentRepository enrollmentRepository;

    @Override
    public StudentEnrollment saveEnrollment(StudentEnrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Page<StudentEnrollment> getAllEnrollments(Pageable pageable) {
        return enrollmentRepository.findAll(pageable);
    }

    @Override
    public StudentEnrollment getEnrollmentById(UUID id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
    }

    @Override
    public void deleteEnrollment(UUID id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new RuntimeException("Enrollment not found with id: " + id);
        }
        enrollmentRepository.deleteById(id);
    }

    @Override
    public StudentEnrollment updateEnrollment(UUID id, StudentEnrollment enrollment) {
        return enrollmentRepository.findById(id)
                .map(existingEnrollment -> {
                    existingEnrollment.setEmail(enrollment.getEmail());
                    existingEnrollment.setFullName(enrollment.getFullName());
                    existingEnrollment.setHallTicketNo(enrollment.getHallTicketNo());
                    existingEnrollment.setDateOfBirth(enrollment.getDateOfBirth());
                    existingEnrollment.setInterGroupCollege(enrollment.getInterGroupCollege());
                    existingEnrollment.setAadharNo(enrollment.getAadharNo());
                    existingEnrollment.setAadharMobile(enrollment.getAadharMobile());
                    existingEnrollment.setWhatsappNo(enrollment.getWhatsappNo());
                    existingEnrollment.setAddress(enrollment.getAddress());
                    existingEnrollment.setVillage(enrollment.getVillage());
                    existingEnrollment.setCourses(enrollment.getCourses());
                    existingEnrollment.setReferredBy(enrollment.getReferredBy());
                    return enrollmentRepository.save(existingEnrollment);
                })
                .orElseThrow(() -> new RuntimeException("Enrollment not found with id: " + id));
    }
}
