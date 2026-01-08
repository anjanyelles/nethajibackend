package com.nethaji.entity;

import com.nethaji.Enums.Gender;
import com.nethaji.Enums.StudentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "student_profile")
@Data
public class StudentProfile{


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    @Column(name = "user_id",nullable = false)
    private UUID userId;


    @Column(name ="date_of_birth" )
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "pincode")
    private String pincode;


    @Column(name = "guardian_contact")
    private String guardianContact;

    @Column(name = "guardian_name")
    private String guardianName;

    @Column(name = "guardian_relation")
    private String guardianRelation; // Father, Mother, Guardian

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;


    @Column(name = "blood_group")
    private String bloodGroup;

    @Column(name = "alternate_contact")
    private String alternateContact;

    @Column(name = "tenth_percentage")
    private BigDecimal tenthPercentage;

    @Column(name = "tenth_board")
    private String tenthBoard;




    @Column(name = "tenth_year_of_passing")
    private Integer tenthYearOfPassing;


    @Column(name = "twelfth_percentage")
    private BigDecimal twelfthPercentage;

    @Column(name = "twelfth_board")
    private String twelfthBoard;

    @Column(name = "twelfth_year_of_passing")
    private Integer twelfthYearOfPassing;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;





}




