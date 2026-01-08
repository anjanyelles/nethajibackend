package com.nethaji.dto;


import com.nethaji.entity.User;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class LectureResponseDTO {

    private UUID lectureId;
    private String  lectureName;
    private String lectureEmail;
    private String lectureMobileNumber;
    private User.UserType userType;
    private Boolean lectureStatus;
    private Date createdAt;
    private Date updatedAt;


    private List<LectureCoursesDTO> lectureCoursesDTOList;

    private List<AssistantLectureDTO > assistantLectureDTOList;


}
