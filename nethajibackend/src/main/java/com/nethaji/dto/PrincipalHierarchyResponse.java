package com.nethaji.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PrincipalHierarchyResponse {


    private UUID principalId;
    private String principalName;
    private int totalLecturers;
    private List<LecturerInfo> lecturers=new ArrayList<>();

}
