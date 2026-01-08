package com.nethaji.entity;

import com.nethaji.Enums.EmploymentType;
import com.nethaji.Enums.Gender;
import com.nethaji.Enums.StaffStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "staff_profile")
public class StaffProfile {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    @Column(name = "user_id",nullable = false)
    private UUID userId;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "department", nullable = false)
    private String department; // CSE, ECE, Math

    @Column(name = "designation", nullable = false)
    private String designation; // Professor, Assistant Prof.

    @Column(name = "qualification")
    private String qualification; // PhD, M.Tech, M.Sc

    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Column(name = "experience_years")
    private Long experienceYears;


    @Column(name = "availability")
    private String availability; // e.g., "Mon-Fri 9AM-4PM"

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "employment_type")
    private EmploymentType employmentType;

    @Column(name = "salary")
    private Double salary;


    @Column(name = "emergency_contact_name")
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone")
    private String emergencyContactPhone;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "status")
    private StaffStatus status;

    @Column(name = "address")
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private Gender gender;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "employee_subject", nullable = false)
    private String employeeSubject;




    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;
}
