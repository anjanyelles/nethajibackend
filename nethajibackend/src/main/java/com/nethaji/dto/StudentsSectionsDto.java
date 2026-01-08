package com.nethaji.dto;

import com.nethaji.Enums.Branch;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class StudentsSectionsDto {

    private UUID studentId;

    private String sectionName;

    private String programCode;

    private UUID departmentId;

    private Integer semester;

    private Branch branch;

    private Date createdAt;

    private String status;

    private String fullName;

    private String registeredOn;


}
