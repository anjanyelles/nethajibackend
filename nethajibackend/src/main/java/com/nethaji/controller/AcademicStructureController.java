package com.nethaji.controller;


import com.nethaji.Enums.ProgramLevel;
import com.nethaji.dto.*;
import com.nethaji.service.AcademicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nethaji-service/acadamic")
@CrossOrigin  //http://localhost:9029/api/nethaji-service/acadamic/adddepartments
public class AcademicStructureController {

    @Autowired
    private AcademicService academicService;

    @PostMapping("/adddepartments")
    public ResponseEntity<DepartmentDTO> createAndUpdateDepartment(@RequestBody DepartmentDTO dto) {
        return academicService.createAndUpdateDepartment(dto);
    }
    @GetMapping("/getalldepartments")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartment(@RequestParam(required = false) UUID id) {
        return academicService.getAllDepartment(id);
    }



    @PostMapping("/addprograms")
    public ResponseEntity<ProgramDTO> createOrUpdateProgram(@RequestBody ProgramDTO dto) {
        return academicService.createOrUpdateProgram(dto);
    }
    @GetMapping("/getallprograms")
    public ResponseEntity<List<ProgramResponseDTO>> getAllPrograms(@RequestParam(required = false) UUID id) {
        return academicService.getAllPrograms(id);
    }


    @PostMapping("/addSemesters")
    public ResponseEntity<DepartMentSemestersDto> addSemesters(@RequestBody DepartMentSemestersDto dto) {
        return academicService.setSemesteresDepartMentWise(dto);
    }



    @PostMapping("/addcourses")
    public ResponseEntity<CourseDTO> createOrUpdateProgram(@RequestBody CourseDTO dto) {
        return academicService.createOrUpdateCourse(dto);
    }



    @GetMapping("/all-courses")
    public ResponseEntity<List<CourseDTO>> getAllCourses(@RequestParam(required = false) UUID id)
    {
        return academicService.getAllCoursesAdmin(id);

    }


    @GetMapping("/all-courses-info")
    public ResponseEntity<List<ProgramDTO>> getProgramHierarchy()
    {
        return academicService.getProgramHierarchy();

    }




    @GetMapping("/program/details")
    public ResponseEntity<List<ProgramDepaSemCourseList>> getDetails(@RequestParam ProgramLevel programLevel) {
        List<ProgramDepaSemCourseList> response = academicService.getAllProgramBasedDetails(programLevel);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getDepartMentAndSemesterSubjectsInfo")
    public ResponseEntity<List<DepartMentSemestersDto>> getDepartmentAndSemesterBasedInfo(@RequestParam String departMentType, @RequestParam Integer semester) {
        List<DepartMentSemestersDto> response = academicService.getBySemesterType(departMentType,semester);
        return ResponseEntity.ok(response);
    }


}
