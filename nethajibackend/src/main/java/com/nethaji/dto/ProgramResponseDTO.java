package com.nethaji.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProgramResponseDTO {
    private UUID id;
    private String name;
    private String programCode;
    private String level;
    private Integer durationYears;

    private UUID departmentId;
    private String departmentCode;
    private String departmentName;

    private boolean status;
    private String message;
}
