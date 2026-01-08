package com.nethaji.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class DepartmentDTO {
    private UUID id;


    private String departmentCode;
    private String departmentName;

    private Date createdAt;
    private Date updatedAt;

    private UUID programId;

    private Boolean status;
    private String message;


    private List<DepartMentSemestersDto> departMentSemestersDtoList;
}
