package com.nethaji.service;

import com.nethaji.Enums.ProgramLevel;
import com.nethaji.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface AcademicService {
    ResponseEntity<DepartmentDTO> createAndUpdateDepartment(DepartmentDTO dto);

    ResponseEntity<List<DepartmentDTO>> getAllDepartment(UUID id);

   // ResponseEntity<DepartmentDTO> getDepartmentDetailsByHODId(UUID hodId);

    ResponseEntity<ProgramDTO> createOrUpdateProgram(ProgramDTO dto);

    ResponseEntity<List<ProgramResponseDTO>> getAllPrograms(UUID id);

    ResponseEntity<DepartMentSemestersDto>setSemesteresDepartMentWise(DepartMentSemestersDto departMentSemestersDto);

      public List<DepartMentSemestersDto>getBySemesterType(String departMentTYpe, Integer semeste);

    ResponseEntity<CourseDTO> createOrUpdateCourse(CourseDTO dto);

   // ResponseEntity<Map<String, Object>> getAllAcadamicDeailsByHODId(UUID hodId);

    public List<ProgramDepaSemCourseList>getAllProgramBasedDetails(ProgramLevel programLevel);

    ResponseEntity<List<CourseDTO>> getAllCoursesAdmin(UUID id);

    ResponseEntity<List<ProgramDTO>> getProgramHierarchy();
}
