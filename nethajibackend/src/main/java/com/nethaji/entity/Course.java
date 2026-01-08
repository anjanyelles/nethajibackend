package com.nethaji.entity;

import com.nethaji.Enums.CourseType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "course")
public class Course {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_type")
    private CourseType courseType;

    @Column(name = "credits")
    private Integer credits ;
//
//    @Column(name = "semester")
//    private Integer semester;

    @Column(name = "department_semester_id")
    private UUID departmentSemesterId;

    @Column(name = "is_elective")
    private Boolean isElective = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "syllabus_pdf_url")
    private String syllabusPdfUrl;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


}