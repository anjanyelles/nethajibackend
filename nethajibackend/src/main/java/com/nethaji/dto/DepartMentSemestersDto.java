package com.nethaji.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
public class DepartMentSemestersDto {

    private UUID id;

    private Date semesterStartDate;

    private Date semesterEndDate;

    private Integer semester;

    private String semesterYear;

    private int semesterSubjects;

    private int semesterTotalLabs;

    private UUID departmentId;


    private Boolean status;
    private String message;

    private List<CourseListInfo> courseListInfo;
}
