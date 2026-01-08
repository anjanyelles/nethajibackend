package com.nethaji.entity;


import com.nethaji.Enums.Branch;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "student_sections")
@Data
public class StudentSection {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;



    @Column(name = "student_id",nullable = false)
    private UUID studentId;


    @Column(name = "section_name", nullable = false) // Example: "A", "B"
    private String sectionName;

    @Column(name = "program_code",nullable = false)
    private String programCode;


    @Column(name = "department_id",nullable = false)
    private UUID departmentId;


    @Column(name = "status",nullable = false)
    private Boolean status=true;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "branch", nullable = false)
    private Branch branch;

    @Column(name = "semester_start_date", nullable = false)
    private Date semesterStartDate;

    @Column(name = "semester_year", nullable = false)
    private String semesterYear;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


}








