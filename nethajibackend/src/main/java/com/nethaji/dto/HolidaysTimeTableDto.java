package com.nethaji.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class HolidaysTimeTableDto {

    private UUID id;
    private Date holidaysStartDate;

    private String holidayType;

    private Date holidaysEndDate;

    private Date createdAt;
}
