package com.nethaji.dto;

import com.nethaji.Enums.ProgramLevel;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ProgramDTO {
    private UUID id;


    private String name;
    private String programCode;
    private ProgramLevel level;
    private Integer durationYears;
    private UUID departmentId;
    private Boolean isActive;



    private Date createdAt;
    private Date updatedAt;


    private Boolean status;
    private String message;


    private List<DepartmentDTO> departments;

}
