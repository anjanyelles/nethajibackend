package com.nethaji.dto;

import com.nethaji.Enums.ProgramLevel;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ProgramDepaSemCourseList {

    private String name;

    private String programCode;

    private ProgramLevel level; // B.Tech,M.Tech

    private Integer durationYears;

    private Boolean isActive = true;

   public List<DepartMentInfoList> departMentInfoList;

   public List<DepartMentSemistersListInfo>departMentSemistersListInfo;

   public List<CourseListInfo>courseListInfo;


}
