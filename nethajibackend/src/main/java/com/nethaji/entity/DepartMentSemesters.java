package com.nethaji.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "department_semesters")
public class DepartMentSemesters {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;


    @Column(name="semester_start_date")
    private Date semesterStartDate;


    @Column(name="semester_end_date")
    private Date semesterEndDate;

    @Column(name="semester")
    private Integer semester;


    @Column(name="semester_year")
    private String semesterYear;

    @Column(name="semester_subjects")
    private int semesterSubjects;


    @Column(name="semester_total_labs")
    private int semesterTotalLabs;

    @Column(name="department_id")
    private UUID departmentId;


}
