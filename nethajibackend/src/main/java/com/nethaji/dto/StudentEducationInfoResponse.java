package com.nethaji.dto;

import com.nethaji.Enums.Branch;
import com.nethaji.entity.SemesterStatus;
import com.nethaji.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class StudentEducationInfoResponse {

    private UUID id;

    private Integer semester;
    private UUID studentId;
    private SemesterStatus semesterStatus;
    private User student;
    private boolean isActive;
    private Branch branch;
    private String graduationType;
    private Date createdAt;



}
