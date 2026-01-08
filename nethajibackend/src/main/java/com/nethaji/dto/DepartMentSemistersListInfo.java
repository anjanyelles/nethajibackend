package com.nethaji.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class DepartMentSemistersListInfo {

    private Date semesterStartDate;


    private Date semesterEndDate;

    private Integer semester;


    private String semesterYear;

    private int semesterSubjects;


    private int semesterTotalLabs;

    public List<CourseListInfo> courseListInfo;


}
