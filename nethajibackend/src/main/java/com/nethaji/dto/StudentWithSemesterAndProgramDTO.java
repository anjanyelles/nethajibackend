package com.nethaji.dto;

import com.nethaji.Enums.EnrollmentStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
public class StudentWithSemesterAndProgramDTO {



    private UUID studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String enrollmentNumber;
    private Boolean isActive;


    private UUID studentProgramId;
    private String programName;
    private String programCode;
    private EnrollmentStatus enrolmentStatus;
    private  boolean programStatus;
    private Date createdAt;
    private Date updatedAt;


    private List<StudentSemestersDTO> studentSemestersDTOList;

    private UUID studentSectionId;
    private String studentFullSection;






}
