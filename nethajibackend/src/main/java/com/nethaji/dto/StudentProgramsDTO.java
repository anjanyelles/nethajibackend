package com.nethaji.dto;

import com.nethaji.Enums.EnrollmentStatus;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class StudentProgramsDTO {

    private UUID studentProgramId;
    private String programName;
    private String programCode;
    private EnrollmentStatus enrolmentStatus;
    private  boolean programStatus;
    private Date createdAt;
    private Date updatedAt;

}
