package com.nethaji.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data

public class SectionAssignmentRequest {
    private List<UUID> studentIds;
    private int sectionSize;
    private int semester;
    private String  sectionName;
}

