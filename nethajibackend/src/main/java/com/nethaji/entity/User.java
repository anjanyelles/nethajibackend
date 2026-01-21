package com.nethaji.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    public static enum UserType{
        SUPER_ADMIN, ADMIN, STUDENT, LECTURER
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "enrollment_number",unique = true)
    private String enrollmentNumber;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "mobile_number",nullable = true)
    private String mobileNumber;

    @Column(name = "country_code",nullable = true)
    private String countryCode;

    @Column(name = "password_hash", nullable = true)
    private String passwordHash;

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.STUDENT;


    @Column(name = "created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "salt", nullable = true)
    private String salt;



    @Column(name = "last_login", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Column(name = "is_active", nullable = true)
    private Boolean isActive=true;







    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private StudentProfile studentProfile;


    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private StudentSection section;


    @Column(name = "joining_year")
    private Integer joiningYear;



}






