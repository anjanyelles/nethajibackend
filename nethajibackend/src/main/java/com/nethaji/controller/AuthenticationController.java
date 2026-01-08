package com.nethaji.controller;

import com.nethaji.dto.*;
import com.nethaji.entity.StaffProfile;
import com.nethaji.entity.StudentSection;
import com.nethaji.entity.User;
import com.nethaji.exceptions.InvalidCredentialsException;
import com.nethaji.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;
import java.util.UUID;



@RestController
@RequestMapping("/api/nethaji-service/auth")
@CrossOrigin  //http//:localhost:9029/api/nethaji-service/auth
public class AuthenticationController {


    @Autowired
    private AuthService authService;


    @PostMapping("/registration")
    ResponseEntity<VerifyDtoResponse> Registration(@RequestBody EmailOtpRequest emailOtpRequest)throws InvalidCredentialsException {

        return authService.Registration(emailOtpRequest);

    }



    @PostMapping("/userLoginWithEmailPassword")
    ResponseEntity<VerifyDtoResponse> LoginWithEmail(@RequestBody EmailOtpRequest emailOtpRequest)throws InvalidCredentialsException {

        return authService.LoginWithEmail(emailOtpRequest);

    }

    @PostMapping("/student-login")
    ResponseEntity<VerifyDtoResponse> studentLoginWithEmailorEnrollment(@RequestBody StudentLoginRequest  emailOtpRequest){

        return authService.studentLoginWithEmailorEnrollment(emailOtpRequest);

    }


    @PostMapping("/admin-or-principal/create-user")
    ResponseEntity<VerifyDtoResponse> AddOthersByAdminOrPrincipal(@RequestBody RegisterRequest emailOtpRequest)throws InvalidCredentialsException {

        return authService.AddOthersByAdminOrPrincipal(emailOtpRequest);

    }



    @PostMapping("/change-password")
    ResponseEntity<VerifyDtoResponse> ChangePassword(@RequestBody EmailOtpRequest emailOtpRequest){

        return  authService.ChangePassword(emailOtpRequest);

    }



    @GetMapping("/lecturers-and-assistants-courses")
    public ResponseEntity<PrincipalHierarchyResponse> getLecturersAndAssistantsByCourseId() {

        return authService.getLecturers();
    }


    @GetMapping("/lecturers")
    public ResponseEntity<List<RegisterRequest>> getAllLecturers() {

        return  authService.getUsersByType(User.UserType.LECTURER);


    }



    @GetMapping("/active-students-semester-programmed")
    public ResponseEntity<List<StudentWithSemesterAndProgramDTO>> getActiveStudentsByProgramIdAndSemester(@RequestParam UUID programId, @RequestParam int semester) {

        return authService.getActiveStudentsByProgramIdAndSemester(programId, semester);
    }


    @PatchMapping("/{studentId}/program/{newProgramId}")
    public ResponseEntity<Map<String ,Object>> updateStudentProgram(@PathVariable UUID studentId, @PathVariable UUID newProgramId) {

        return authService.updateStudentProgram(studentId, newProgramId);

    }


    @GetMapping("/all-students")
    public ResponseEntity<List<RegisterRequest>> getAllStudents(){

        return  authService.getAllStudents();

    }



    @PatchMapping("/update-password-by-admin")
    public ResponseEntity<Map<String ,Object>> updatePasswordByAdmin(@RequestParam UUID studentId,@RequestParam String  password){

        return  authService.updatePasswordByAdmin(studentId,password);

    }


    @PatchMapping("/active-inactive-student")
    public ResponseEntity<Map<String ,Object>> activeOrInactiveStudentForLogin(@RequestParam UUID studentId,@RequestParam Boolean status){


        return  authService.activeOrInactiveStudentForLogin(studentId,status);

    }


    @GetMapping("/all-lectures")
    public ResponseEntity<List<LectureResponseDTO>> getAllLectures(@RequestParam(required = false) UUID lectureId){

        return  authService.getAllLectures(lectureId);

    }



    @GetMapping("/students")
    public ResponseEntity<Page<RegisterRequest>> getAllStudents(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "firstName") String sortBy, @RequestParam(defaultValue = "ASC") String sortDirection) {

        return authService.getUsersByTypeWithPagination(User.UserType.STUDENT, page, size, sortBy, sortDirection);
    }

    @PostMapping("/saveStaffProfile")
    public StaffProfile saveOrUpdateStaffProfile(@RequestBody StaffProfile dto) {
        return authService.saveStaffProfile(dto);
    }


    @GetMapping("/getStaffProfileById/{id}")
    public StaffProfile getStaffProfileById(@PathVariable UUID id) {
        return authService.getStaffProfile(id);
    }

    @GetMapping("/getListofStaffDetails")
    public List<StaffProfile> getStaffProfilesList() {

        return authService.getListOfStaffDetails();
    }




    @PostMapping("/saveuserprofile")
    public ResponseEntity<StudentProfileDTO> saveOrUpdateUserProfile(@RequestBody StudentProfileDTO dto) {
        return authService.saveOrUpdateUserProfile(dto);
    }

    @GetMapping("/getuserprofilebyid/{id}")
    public ResponseEntity<StudentProfileDTO> getUserProfileById(@PathVariable UUID id) {
        return authService.getUserProfileById(id);
    }

    @GetMapping("/getAllStudentsInfoNew/{programId}/{semester}")
    public List<StudentEducationInfoResponse> getStudentsByBranchSemster(@PathVariable UUID programId,@PathVariable Integer semester) {
        return authService.getAllNewStudentsByBranchAndSemester(programId,semester);
    }


    @PostMapping("/setSectionsForNewStudents")
    public StudentsSectionsDto setSectionsStudents(@RequestParam StudentSection studentSection) {
        return authService.setSectionsForNewStudents(studentSection);
    }

    @GetMapping("/getSectionAndSemesterStudents/{section}/{semester}")
    public List<StudentsSectionsDto> getStudentsOnSectionWiseandSemester(@PathVariable String section,@PathVariable Integer semester) {
        return authService.getAllStudentsBasedOnSectionAndSemester(section,semester);
    }


    @PostMapping("/setTimeTablesForSylExamHoly")
    public TimeTableDetailsDto setTimeTable(@RequestParam TimeTableDetailsDto timeTableDetailsDto) {
        return authService.setTimeTablesForSemestersAndHolidaysExams(timeTableDetailsDto);
    }


}




