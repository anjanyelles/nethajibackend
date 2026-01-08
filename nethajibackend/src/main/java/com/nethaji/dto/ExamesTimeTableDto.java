package com.nethaji.dto;

import com.nethaji.Enums.ExamType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class ExamesTimeTableDto {

    private UUID id;
    private String examTime;

    private String examSubject;

    private String examCenter;

    private UUID departmentId;
    private UUID semesterId;

    private ExamType examType;

    private Date exameDate;

    private Date createdAt;
}
