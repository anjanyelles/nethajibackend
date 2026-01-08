package com.nethaji.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class DepartMentInfoList {

    private String departmentCode;

    private String departmentName;

    private UUID programId;

    public List<DepartMentSemistersListInfo> departMentSemistersListInfo;

}
