package com.nethaji.dto;

import lombok.Data;

import java.util.List;

@Data
public class HierarchyResponse{
    private List<LecturerResponse> lecturers;
    private List<AssistantLecturerResponse> assistantLecturers;
}

