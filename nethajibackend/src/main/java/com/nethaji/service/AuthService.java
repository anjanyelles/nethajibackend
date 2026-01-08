package com.nethaji.service;

import com.nethaji.dto.*;
import com.nethaji.entity.StaffProfile;
import com.nethaji.entity.StudentProfile;
import com.nethaji.entity.StudentSection;
import com.nethaji.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;


public interface AuthService {
    ResponseEntity<VerifyDtoResponse> LoginWithEmail(EmailOtpRequest emailOtpRequest);

    ResponseEntity<StudentProfileDTO> saveOrUpdateUserProfile(StudentProfileDTO dto);


    ResponseEntity<StudentProfileDTO> getUserProfileById(UUID id);

    public List<StudentProfile>getListOfStudentsInfo();

    ResponseEntity<VerifyDtoResponse> ChangePassword(EmailOtpRequest emailOtpRequest);

    ResponseEntity<VerifyDtoResponse> AddOthersByAdminOrPrincipal(RegisterRequest emailOtpRequest);


    public List<StudentEducationInfoResponse>getAllNewStudentsByBranchAndSemester(UUID programId,Integer semester);

    public StudentsSectionsDto setSectionsForNewStudents(StudentSection studentSection);

    public List<StudentsSectionsDto> getAllStudentsBasedOnSectionAndSemester(String section,Integer semester);

    public StaffProfile saveStaffProfile(StaffProfile StaffProfile);

    public StaffProfile getStaffProfile(UUID userId);

    public List<StaffProfile>getListOfStaffDetails();



    ResponseEntity<PrincipalHierarchyResponse> getLecturers();

    ResponseEntity<List<RegisterRequest>> getUsersByType(User.UserType userType);

    ResponseEntity<Page<RegisterRequest>> getUsersByTypeWithPagination(User.UserType userType, int page, int size, String sortBy, String sortDirection);


    ResponseEntity<List<StudentWithSemesterAndProgramDTO>> getActiveStudentsByProgramIdAndSemester(UUID programId, int semester);

    ResponseEntity<VerifyDtoResponse> studentLoginWithEmailorEnrollment(StudentLoginRequest emailOtpRequest);

    ResponseEntity<Map<String, Object>> updateStudentProgram(UUID studentId, UUID newProgramId);

    ResponseEntity<Map<String, Object>> activeOrInactiveStudentForLogin(UUID studentId,Boolean status);

    ResponseEntity<List<RegisterRequest>> getAllStudents();

    ResponseEntity<List<LectureResponseDTO>> getAllLectures(UUID lectureId);

    public TimeTableDetailsDto setTimeTablesForSemestersAndHolidaysExams(TimeTableDetailsDto timeTableDetailsDto);

    public List<TimeTableDetailsDto>getSylabusTimeTableOnDepartAndSemesterId(UUID departmentId,UUID semesterId);

    ResponseEntity<VerifyDtoResponse> Registration(EmailOtpRequest emailOtpRequest);

    ResponseEntity<Map<String, Object>> updatePasswordByAdmin(UUID studentId, String password);
}
