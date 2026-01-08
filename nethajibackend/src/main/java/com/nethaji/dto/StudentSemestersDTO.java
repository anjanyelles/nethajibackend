package com.nethaji.dto;

import com.nethaji.entity.SemesterStatus;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class StudentSemestersDTO {

    private UUID studentSemesterId;
    private Integer studentSemester;

    private SemesterStatus studentSemesterStatus;
    private boolean studentSemesterActivity;
    private Date createdAt;
    private  Date updatedAt;

}
