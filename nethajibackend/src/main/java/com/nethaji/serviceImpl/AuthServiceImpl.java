package com.nethaji.serviceImpl;


import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import com.nethaji.Enums.Branch;
import com.nethaji.Enums.EnrollmentStatus;
import com.nethaji.Enums.StaffStatus;
import com.nethaji.Enums.StudentStatus;
import com.nethaji.dto.*;
import com.nethaji.entity.*;
import com.nethaji.repositories.*;
import com.nethaji.service.*;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j

public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomAuthenticationManager customAuthenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository  courseRepository;

    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private SignatureService signatureService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private UserHierarchyRepository userHierarchyRepository;

    @Autowired
    private ProgramsRepository programsRepository;

    @Autowired
    private StudentProgramsRepository studentProgramsRepository;

    @Autowired
    private LecturerDetailsRepository lectureCoursesRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartMentSemestersRepo departMentSemestersRepo;

    @Autowired
    private StudentEducationInfoRepository studentSemestersRepository;


    @Autowired
    private StudentSectionRepository studentSectionRepository;

    @Autowired
    private TimeTableDetailsRepo timeTableDetailsRepo;

    @Autowired
    private HolidaysTimeTableRepo holidaysTimeTableRepo;

    @Autowired
    private ExamesTimeTableRepo examesTimeTableRepo;

    @Autowired
    private StaffProfileRepo staffProfileRepo;

    @Autowired
    private LecturerCourseAssignmentRepository lecturerCourseAssignmentRepository;

    private final DateFormat expectedDateFormat = new SimpleDateFormat("dd/MM/yyyy");


    private final AmazonSimpleEmailService sesClient;

    public AuthServiceImpl( @Value("${aws.accesskey}") String accessKey,
                            @Value("${aws.secretkey}") String secretKey) {

        this.sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accessKey, secretKey)
                ))
                .build();
    }


    public ResponseEntity<Map<String, Object>> updateStaffPasswordByAdmin(UUID staffId, String password){

        Map<String, Object> res = new LinkedHashMap<>();

        User staff = userRepository.findById(staffId).orElse(null);

        if (staff == null) {
            res.put("status", false);
            res.put("message", "Staff not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }

        if (staff.getUserType() != User.UserType.LECTURER) {
            res.put("status", false);
            res.put("message", "User is not a staff");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        try {
            String salt = UUID.randomUUID().toString() + UUID.randomUUID().toString().substring(0, 10);
            String hashedPassword = signatureService.hashPassword(password, salt);

            staff.setSalt(salt);
            staff.setPasswordHash(hashedPassword);
            staff.setUpdatedAt(new Date());

            userRepository.save(staff);

            res.put("status", true);
            res.put("message", "Password updated successfully");
            res.put("staffId", staffId);

            return ResponseEntity.ok(res);

        }

        catch (Exception ex) {

            log.error("Password update failed for staffId={}", staffId, ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }


    }

    private void enrichStudentRegisterRequestIfStudent(User user, RegisterRequest dto) {
        if (user == null || dto == null || user.getUserType() != User.UserType.STUDENT) {
            return;
        }

        StudentPrograms sp = studentProgramsRepository.findByStudentIdAndIsActiveTrue(user.getId());
        if (sp != null) {
            dto.setProgramId(sp.getProgramId());
            Programs p = programsRepository.findById(sp.getProgramId()).orElse(null);
            if (p != null) {
                dto.setStudentProgramName(p.getName());
            }
        }

        List<StudentEducationInfo> semesters = studentSemestersRepository.findByStudentId(user.getId());
        StudentEducationInfo activeInfo = semesters == null
                ? null
                : semesters.stream()
                .filter(StudentEducationInfo::isActive)
                .max(Comparator.comparing(StudentEducationInfo::getSemester))
                .orElse(null);

        if (activeInfo != null) {
            dto.setSemester(activeInfo.getSemester());
            dto.setStudentCurrentSemester(activeInfo.getSemester() != null ? activeInfo.getSemester().toString() : null);
            dto.setBranch(activeInfo.getBranch());
            dto.setGraduationType(activeInfo.getGraduationType());
            if (dto.getProgramId() == null && activeInfo.getProgramId() != null) {
                dto.setProgramId(activeInfo.getProgramId());
            }
            if (dto.getStudentProgramName() == null && activeInfo.getProgramId() != null) {
                Programs p = programsRepository.findById(activeInfo.getProgramId()).orElse(null);
                if (p != null) {
                    dto.setStudentProgramName(p.getName());
                }
            }
        }
    }


    public  ResponseEntity<VerifyDtoResponse> Registration(EmailOtpRequest request){


        VerifyDtoResponse response = new VerifyDtoResponse();

        Optional<User> optionalUser = userRepository.findByEmailOrMobileNumber(request.getEmail(), request.getMobileNumber());

        if (optionalUser.isPresent()) {
            response.setStatus("User already exists. Please log in!");
            response.setUserStatus(false);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }


        boolean isOtpRequest = request.getEmailOtp() == null &&
                request.getEmailOtpSession() == null &&
                request.getPassword() == null;

        if (isOtpRequest) {


            Random random = new Random();
            String otp = String.format("%06d", random.nextInt(999999));


            String salt = UUID.randomUUID().toString() + UUID.randomUUID().toString().substring(0, 10);
            String hashedOtpSession = signatureService.hashPassword(otp, salt);


            TemplateContext templateContext = new TemplateContext();
            templateContext.put("otp", otp);

            EmailRequestDto email = new EmailRequestDto();
            email.setEmails(Arrays.asList(request.getEmail()));
            email.setSubject("OTP From Disha Hiring Platform");
            email.setTemplateName("email-verify.template");
            email.setTemplateContext(templateContext);

            sendEmail(email);


            response.setEmailOtpSession(hashedOtpSession);
            response.setSalt(salt);
            response.setTimeInMilliSeconds(String.valueOf(System.currentTimeMillis()));

            return ResponseEntity.ok(response);
        }


        if (request.getEmailOtp() != null && request.getEmailOtpSession() != null) {

            String hashedOtp = signatureService.hashPassword(request.getEmailOtp(), request.getSalt());

            if (!hashedOtp.equals(request.getEmailOtpSession())) {
                response.setStatus("Invalid OTP. Please try again!");
                response.setUserStatus(false);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }


            User newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setMobileNumber(request.getMobileNumber());
            newUser.setFirstName(request.getFirstName());
            newUser.setLastName(request.getLastName());
            newUser.setCreatedAt(new Date());
            newUser.setUpdatedAt(new Date());
            newUser.setIsActive(true);

            // Set salt & password
            String salt = UUID.randomUUID().toString() + UUID.randomUUID().toString().substring(0, 10);
            newUser.setSalt(salt);
            String hashedPassword = signatureService.hashPassword(request.getPassword(), salt);
            newUser.setPasswordHash(hashedPassword);

            // Set user type (default STUDENT)
            if (request.getPrimaryType() != null) {
                newUser.setUserType(User.UserType.valueOf(request.getPrimaryType()));
            }

            userRepository.save(newUser);

            // 5️⃣ STEP 4 — LOGIN AFTER REGISTRATION
            UsernamePasswordAuthenticationToken creds =
                    new UsernamePasswordAuthenticationToken(request.getEmail(), " ");

            Authentication authentication = customAuthenticationManager.authenticateByEmail(creds);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = tokenProvider.createToken(authentication, "ACCESS");
            String refreshToken = tokenProvider.createToken(authentication, "REFRESH");

            response.setToken(token);
            response.setRefreshToke(refreshToken);
            response.setAccessToken(token);
            response.setId(newUser.getId());
            response.setUserId(newUser.getId());
            response.setUserStatus(true);
            response.setStatus("Registration Successful");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        // If payload does not match any flow
        response.setStatus("Invalid registration request!");
        response.setUserStatus(false);
        return ResponseEntity.badRequest().body(response);


    }


    public ResponseEntity<Map<String, Object>> updatePasswordByAdmin(UUID studentId, String password){

        Map<String, Object> res = new LinkedHashMap<>();

        User student = userRepository.findById(studentId).orElse(null);

        if (student == null) {
            res.put("status", false);
            res.put("message", "Student not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
        }


        if (student.getUserType() != User.UserType.STUDENT) {
            res.put("status", false);
            res.put("message", "User is not a student");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }


        try {
            String salt = UUID.randomUUID().toString() + UUID.randomUUID().toString().substring(0, 10);

            String hashedPassword = signatureService.hashPassword(password, salt);

            student.setSalt(salt);
            student.setPasswordHash(hashedPassword);
            student.setUpdatedAt(new Date());

            userRepository.save(student);

            res.put("status", true);
            res.put("message", "Password updated successfully");
            res.put("studentId", studentId);

            return ResponseEntity.ok(res);

        }

        catch (Exception ex) {

            log.error("Password update failed for studentId={}", studentId, ex);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }


    }


    @Override
    public ResponseEntity<VerifyDtoResponse> LoginWithEmail(EmailOtpRequest loginRequest){

        VerifyDtoResponse emailOtpResponse = new VerifyDtoResponse();

        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());


        if (!optionalUser.isPresent()) {
            emailOtpResponse.setStatus("Don't have an account. Please sign up!");
            emailOtpResponse.setUserStatus(false);
            return ResponseEntity.badRequest().body(emailOtpResponse);
        }




                User existingUser = optionalUser.get();

                if(!existingUser.getIsActive()){

                    emailOtpResponse.setStatus("Your Account is In-Active Please Contact Respective Principal!");
                    emailOtpResponse.setUserStatus(false);
                    return ResponseEntity.badRequest().body(emailOtpResponse);

                }

                // Handle password verification - support both BCrypt and custom salt-based hashing
                boolean passwordMatches = false;
                
                // Check if passwordHash exists
                if (existingUser.getPasswordHash() == null || existingUser.getPasswordHash().isEmpty()) {
                    log.error("User {} has no password hash", existingUser.getEmail());
                    emailOtpResponse.setStatus("Invalid username or password");
                    emailOtpResponse.setUserStatus(false);
                    return ResponseEntity.badRequest().body(emailOtpResponse);
                }
                
                if (existingUser.getSalt() == null || existingUser.getSalt().isEmpty()) {
                    // Use BCrypt verification for users without salt (legacy/seed data)
                    try {
                        log.debug("Attempting BCrypt verification for user: {}", existingUser.getEmail());
                        passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPasswordHash());
                        log.debug("BCrypt verification result: {}", passwordMatches);
                    } catch (Exception e) {
                        log.error("BCrypt password verification failed for user: {}", existingUser.getEmail(), e);
                        passwordMatches = false;
                    }
                } else {
                    // Use custom salt-based hashing
                    try {
                        log.debug("Attempting custom salt-based verification for user: {}", existingUser.getEmail());
                        String hashedPassword = signatureService.hashPassword(loginRequest.getPassword(), existingUser.getSalt());
                        passwordMatches = hashedPassword.equals(existingUser.getPasswordHash());
                        log.debug("Custom verification result: {}", passwordMatches);
                    } catch (Exception e) {
                        log.error("Custom password verification failed for user: {}", existingUser.getEmail(), e);
                        passwordMatches = false;
                    }
                }

                if (!passwordMatches) {
                    log.warn("Password mismatch for user: {}", existingUser.getEmail());
                    emailOtpResponse.setStatus("Invalid username or password");
                    emailOtpResponse.setUserStatus(false);
                    return ResponseEntity.badRequest().body(emailOtpResponse);
                }

                UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(), " ");

                org.springframework.security.core.Authentication authentication = customAuthenticationManager
                        .authenticateByEmail(creds);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String token = tokenProvider.createToken(authentication, "ACCESS");
                String refreshToken = tokenProvider.createToken(authentication, "REFRESH");
                // UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
                emailOtpResponse.setToken(token);
                emailOtpResponse.setRefreshToke(refreshToken);
                emailOtpResponse.setId(existingUser.getId());
                emailOtpResponse.setAccessToken(token);
                emailOtpResponse.setUserId(existingUser.getId());
                emailOtpResponse.setUserRole(existingUser.getUserType().toString());
                //System.out.println("============="+existingUser.getUserType()+"===============");
                emailOtpResponse.setStatus("Login Successful");
                emailOtpResponse.setUserStatus(true);
                return ResponseEntity.ok(emailOtpResponse);


    }



    @Override
    public ResponseEntity<VerifyDtoResponse> studentLoginWithEmailorEnrollment(StudentLoginRequest loginRequest) {

        VerifyDtoResponse response = new VerifyDtoResponse();

        Optional<User> optionalUser = Optional.empty();


        if (loginRequest.getEmail() != null && !loginRequest.getEmail().isEmpty()) {
            optionalUser = userRepository.findByEmail(loginRequest.getEmail());
        }

        if ((!optionalUser.isPresent()) && loginRequest.getEnrollmentNumber() != null && !loginRequest.getEnrollmentNumber().isEmpty()) {
            optionalUser = userRepository.findByEnrollmentNumber(loginRequest.getEnrollmentNumber());
        }


        if (!optionalUser.isPresent()) {
            response.setStatus("Invalid email or enrollment number");
            response.setUserStatus(false);
            return ResponseEntity.badRequest().body(response);
        }

        User existingUser = optionalUser.get();

        if (!existingUser.getUserType().equals(User.UserType.STUDENT)) {
            response.setStatus("Only students can login here");
            response.setUserStatus(false);
            return ResponseEntity.badRequest().body(response);
        }

        if (!existingUser.getIsActive()) {
            response.setStatus("Your account is inactive. Please contact your Principal.");
            response.setUserStatus(false);
            return ResponseEntity.badRequest().body(response);
        }


        // Handle password verification - support both BCrypt and custom salt-based hashing
        boolean passwordMatches = false;
        
        if (existingUser.getSalt() == null || existingUser.getSalt().isEmpty()) {
            // Use BCrypt verification for users without salt (legacy/seed data)
            try {
                passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPasswordHash());
            } catch (Exception e) {
                log.error("BCrypt password verification failed for student: {}", existingUser.getEmail(), e);
                passwordMatches = false;
            }
        } else {
            // Use custom salt-based hashing
            try {
                String hashedPassword = signatureService.hashPassword(loginRequest.getPassword(), existingUser.getSalt());
                passwordMatches = hashedPassword.equals(existingUser.getPasswordHash());
            } catch (Exception e) {
                log.error("Custom password verification failed for student: {}", existingUser.getEmail(), e);
                passwordMatches = false;
            }
        }

        if (!passwordMatches) {
            response.setStatus("Invalid password");
            response.setUserStatus(false);
            return ResponseEntity.badRequest().body(response);
        }


        UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(existingUser.getEmail(), " ");

        org.springframework.security.core.Authentication authentication = customAuthenticationManager.authenticateByEmail(creds);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication, "ACCESS");
        String refreshToken = tokenProvider.createToken(authentication, "REFRESH");

        response.setToken(token);
        response.setRefreshToke(refreshToken);
        response.setId(existingUser.getId());
        response.setAccessToken(token);
        response.setUserId(existingUser.getId());
        response.setUserRole(existingUser.getUserType().toString());
        response.setStatus("Login Successful");
        response.setUserStatus(true);

        return ResponseEntity.ok(response);
    }



    public   ResponseEntity<List<RegisterRequest>> getAllStudents(){

        List<User> users=userRepository.findUsersByType(User.UserType.STUDENT);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<RegisterRequest> students = users.stream()
                .map(user->{

                    RegisterRequest dto = new RegisterRequest();

                    dto.setId(user.getId());
                    dto.setFirstName(user.getFirstName());
                    dto.setLastName(user.getLastName());
                    dto.setEmail(user.getEmail());
                    dto.setEnrollmentNumber(user.getEnrollmentNumber());
                    dto.setMobileNumber(user.getMobileNumber());
                    dto.setCountryCode(user.getCountryCode());
                    dto.setUserType(user.getUserType());

                    dto.setStudentStatus(user.getIsActive());

                    enrichStudentRegisterRequestIfStudent(user, dto);

                    return  dto;

                }).collect(Collectors.toList());



        return  ResponseEntity.ok(students);

    }

    public ResponseEntity<Map<String, Object>> activeOrInactiveStudentForLogin(UUID studentId,Boolean status){
        Map<String, Object> res=new LinkedHashMap<>();
        User student = userRepository.findById(studentId).orElse(null);

        if(student==null){

            res.put("status",false);
            res.put("message","Student not found");
            return ResponseEntity.badRequest().body(res);


        }

        student.setIsActive(status);
        student.setUpdatedAt(new Date());

        userRepository.save(student);


        res.put("status",true);
        res.put("message","Student status Updated");
        return ResponseEntity.ok(res);

    }

    @Override
    public ResponseEntity<Map<String, Object>> activeOrInactiveStaffForLogin(UUID staffId, Boolean status) {
        Map<String, Object> res = new LinkedHashMap<>();
        User staff = staffId == null ? null : userRepository.findById(staffId).orElse(null);

        if (staff == null) {
            res.put("status", false);
            res.put("message", "Staff not found");
            return ResponseEntity.badRequest().body(res);
        }

        staff.setIsActive(status);
        staff.setUpdatedAt(new Date());
        userRepository.save(staff);

        res.put("status", true);
        res.put("message", "Staff login status updated");
        res.put("staffId", staffId);
        res.put("loginStatus", status);
        return ResponseEntity.ok(res);
    }


    public ResponseEntity<List<LectureResponseDTO>> getAllLectures(UUID lectureId) {
        List<User> users;

        if (lectureId != null) {

            User user = userRepository.findById(lectureId).orElse(null);

            if (user == null || user.getUserType() != User.UserType.LECTURER) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            users = Collections.singletonList(user);
        }

        else {

            users = userRepository.findUsersByType(User.UserType.LECTURER);

        }

        List<LectureResponseDTO> response = users.stream().map(user -> {

            LectureResponseDTO dto = new LectureResponseDTO();

            dto.setLectureId(user.getId());
            dto.setLectureName(user.getFirstName() + " " + user.getLastName());
            dto.setLectureEmail(user.getEmail());
            dto.setLectureMobileNumber(user.getMobileNumber());
            dto.setUserType(user.getUserType());
            dto.setLectureStatus(user.getIsActive());
            dto.setCreatedAt(user.getCreatedAt());
            dto.setUpdatedAt(user.getUpdatedAt());


           /* List<LectureCoursesDTO> courseDTOs = lectureCourses.stream()
                    .map(this::mapToLectureCourseDTO)
                    .collect(Collectors.toList());*/


            //dto.setLectureCoursesDTOList(courseDTOs);


            return dto;

        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);


    }


    @Override
    public  ResponseEntity<Map<String, Object>> updateStudentProgram(UUID studentId, UUID newProgramId){

        Map<String, Object> res=new LinkedHashMap<>();

        User student = userRepository.findById(studentId).orElse(null);

        Programs newProgram = programsRepository.findById(newProgramId).orElse(null);

        if(student==null || newProgram==null){

            res.put("status",false);
            res.put("message",student==null?"Student not found":"Program not found");
            return ResponseEntity.badRequest().body(res);


        }


        StudentPrograms studentPrograms=studentProgramsRepository.findByStudentIdAndIsActiveTrue(studentId);

        if (studentPrograms == null) {

            res.put("status",false);
            res.put("message","Student has no active program to update");
            return ResponseEntity.badRequest().body(res);

        }


        if(studentPrograms.getProgramId().equals(newProgramId)){

            res.put("status",false);
            res.put("message","Student is already enrolled in this program");
            return ResponseEntity.badRequest().body(res);

        }


        if (studentPrograms.getEnrollmentStatus().equals(EnrollmentStatus.ACTIVE)) {
            res.put("status",false);
            res.put("message","Program change not allowed. Program already started.");
            return ResponseEntity.badRequest().body(res);
        }


        studentPrograms.setActive(false);
        studentPrograms.setUpdatedAt(new Date());
        studentProgramsRepository.save(studentPrograms);


        StudentPrograms newStudentProgram = new StudentPrograms();
        newStudentProgram.setStudentId(studentId);
        newStudentProgram.setProgramId(newProgramId);
        newStudentProgram.setEnrollmentStatus(EnrollmentStatus.NOT_STARTED);
        newStudentProgram.setActive(true);
        newStudentProgram.setCreatedAt(new Date());
        newStudentProgram.setUpdatedAt(new Date());
        studentProgramsRepository.save(newStudentProgram);

        res.put("status",true);
        res.put("message","Program updated successfully program name "+newProgram.getName());
        return ResponseEntity.ok(res);


    }



    @Override
    public ResponseEntity<VerifyDtoResponse> ChangePassword(EmailOtpRequest emailOtpRequest){

        VerifyDtoResponse emailOtpResponse = new VerifyDtoResponse();

        Optional<User> optionalUser = userRepository.findByEmail(emailOtpRequest.getEmail());
        String templateName = "";


        if (!optionalUser.isPresent()) {
            emailOtpResponse.setUserStatus(false);
            emailOtpResponse.setStatus("User not found with this email.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emailOtpResponse);
        }


        if (emailOtpRequest.getEmailOtp() == null && emailOtpRequest.getEmailOtpSession() == null && emailOtpRequest.getPassword() == null) {
            Random random = new Random();
            String otp = String.format("%06d", random.nextInt(999999));  // Generate 6-digit OTP
            String salt = UUID.randomUUID().toString() + UUID.randomUUID().toString().substring(0, 10);
            String hashedEmailOtpSession = signatureService.hashPassword(otp, salt);

            String mailSubject = "OTP for Password Change - Disha Hiring Platform";

            List<String> listOfEmails=Arrays.asList(emailOtpRequest.getEmail());

            TemplateContext templateContext = new TemplateContext();
            templateContext.put("otp", otp);
            templateName = "email-verify.template";

            EmailRequestDto emailRequestDto = new EmailRequestDto();
            emailRequestDto.setEmails(listOfEmails);
            emailRequestDto.setSubject(mailSubject);
            emailRequestDto.setTemplateName(templateName);
            emailRequestDto.setTemplateContext(templateContext);

            String sendEmailResponse = sendEmail(emailRequestDto);

            if ("Rejected".equals(sendEmailResponse)) {
                emailOtpResponse.setUserStatus(false);
                emailOtpResponse.setStatus("Failed to send OTP email");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emailOtpResponse);
            }

            emailOtpResponse.setEmailOtpSession(hashedEmailOtpSession);
            emailOtpResponse.setTimeInMilliSeconds(String.valueOf(System.currentTimeMillis()));
            emailOtpResponse.setSalt(salt);

            emailOtpResponse.setUserStatus(true);
            emailOtpResponse.setStatus("OTP sent successfully.");

            return ResponseEntity.ok(emailOtpResponse);
        }


        else if (emailOtpRequest.getEmailOtp() != null && emailOtpRequest.getEmailOtpSession() != null && emailOtpRequest.getPassword() != null) {


            String hashedEmailOtp = signatureService.hashPassword(emailOtpRequest.getEmailOtp(), emailOtpRequest.getSalt());



            if (!hashedEmailOtp.equals(emailOtpRequest.getEmailOtpSession())) {


                emailOtpResponse.setStatus("invalid OTP Please try again!");
                emailOtpResponse.setUserStatus(false);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(emailOtpResponse);


            }

            User newUser = new User();

            newUser.setUpdatedAt(new Date());
            newUser.setSalt(UUID.randomUUID().toString() + UUID.randomUUID().toString().substring(0, 10));
            String hashedpassword = signatureService.hashPassword(emailOtpRequest.getPassword(), newUser.getSalt());
            newUser.setPasswordHash(hashedpassword);



            userRepository.save(newUser);


            UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(emailOtpRequest.getEmail(), " ");

            org.springframework.security.core.Authentication authentication = customAuthenticationManager
                    .authenticateByEmail(creds);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = tokenProvider.createToken(authentication, "ACCESS");
            String refreshToken = tokenProvider.createToken(authentication, "REFRESH");
            // UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
            emailOtpResponse.setToken(token);
            emailOtpResponse.setRefreshToke(refreshToken);
            emailOtpResponse.setId(newUser.getId());
            emailOtpResponse.setAccessToken(token);
            emailOtpResponse.setUserId(newUser.getId());
            emailOtpResponse.setStatus("Password Updated Successful");
            emailOtpResponse.setUserStatus(true);

            return ResponseEntity.ok(emailOtpResponse);


        }

        emailOtpResponse.setUserStatus(false);
        emailOtpResponse.setStatus("Incomplete request. Please provide OTP and password to change.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emailOtpResponse);

    }




    public String sendEmail(EmailRequestDto request) {
        log.info("sending email starts");
        TemplateContext templateContext = request.getTemplateContext();
        List<String> mails = request.getEmails();

        String mailSubject = request.getSubject();
        String emailTemplateName = request.getTemplateName();

        log.info("mail to send...."+request.getEmails());


        log.info("mailSubject...."+mailSubject);

        log.info("start1");
        log.info("emailTemplateName..."+emailTemplateName);
        try {
            log.info(templateContext.toString());
            sendingEmailsFunctionality(mails, templateContext, mailSubject, emailTemplateName);
        } catch (Exception ex) {
            log.info("sending email catch block");
            return ex.getMessage();
        }
        log.info("sending email ends");
        return "success-mail";
    }



    public void sendingEmailsFunctionality(List<String> mails, TemplateContext context, String mailSubject,
                                           String emailTemplateName) throws TemplateException, IOException {
        log.info("mailIds: {}", mails.size());
        log.info("TemplateContext: {}", context.toString());
        log.info("Subject: {}", mailSubject);
        log.info("Template Name: {}", emailTemplateName);

        try {

            freemarker.template.Template template = freemarkerConfig.getTemplate(emailTemplateName);
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("otp", context.get("otp"));

            StringWriter writer = new StringWriter();
            template.process(templateData, writer);
            String htmlBody = writer.toString();


            SendEmailRequest sendEmailRequest = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(mails))
                    .withMessage(new Message()
                            .withSubject(new Content().withCharset("UTF-8").withData(mailSubject))
                            .withBody(new Body()
                                    .withHtml(new Content().withCharset("UTF-8").withData(htmlBody))
                            )
                    )
                    .withSource("admin@services.asoft.click");

            SendEmailResult result = sesClient.sendEmail(sendEmailRequest);
            log.info("Email sent successfully. Message ID: {}", result.getMessageId());

        } catch (Exception ex) {
            log.error("Failed to send email", ex);
            throw ex;
        }


    }

    @Override
    public ResponseEntity<VerifyDtoResponse> AddOthersByAdminOrPrincipal(RegisterRequest request) {

        VerifyDtoResponse response = new VerifyDtoResponse();

        String templateName;
        String subject;

        if (request.getUserType() == null) {
            response.setStatus("userType is required");
            response.setUserStatus(false);
            return ResponseEntity.badRequest().body(response);
        }

        if (request.getEmail() != null && userRepository.findByEmail(request.getEmail()).isPresent()) {
            response.setStatus("Email already exists.");
            response.setUserStatus(false);
            return ResponseEntity.badRequest().body(response);
        }

        if (request.getMobileNumber() != null && userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            response.setStatus("Mobile number already exists.");
            response.setUserStatus(false);
            return ResponseEntity.badRequest().body(response);
        }

        User.UserType userType = request.getUserType();
        int year = LocalDate.now().getYear();
        String res = null;

        if (userType.equals(User.UserType.STUDENT)) {
            if (request.getProgramId() == null) {
                response.setStatus("programId is required for STUDENT");
                response.setUserStatus(false);
                return ResponseEntity.badRequest().body(response);
            }

            UUID resolvedProgramId = request.getProgramId();
            Programs p = programsRepository.findById(resolvedProgramId).orElse(null);
            if (p == null) {
                Department dept = departmentRepository.findById(resolvedProgramId).orElse(null);
                if (dept != null && dept.getProgramId() != null) {
                    resolvedProgramId = dept.getProgramId();
                    p = programsRepository.findById(resolvedProgramId).orElse(null);
                    if (request.getGraduationType() == null || request.getGraduationType().isBlank()) {
                        request.setGraduationType(dept.getDepartmentCode());
                    }
                }
            }

            if (p == null) {
                response.setStatus("Course not found with this id :" + request.getProgramId());
                response.setUserStatus(false);
                return ResponseEntity.badRequest().body(response);
            }

            request.setProgramId(resolvedProgramId);

            if (request.getBranch() == null) {
                response.setStatus("branch is required for STUDENT");
                response.setUserStatus(false);
                return ResponseEntity.badRequest().body(response);
            }

            if (request.getSemester() == null) {
                response.setStatus("semester is required for STUDENT");
                response.setUserStatus(false);
                return ResponseEntity.badRequest().body(response);
            }

            res = generateStudentId(request.getBranch().toString(), String.valueOf(year), request.getSemester().toString());
        }

        if (userType.equals(User.UserType.LECTURER)) {
            if (request.getSubjectType() == null) {
                response.setStatus("subjectType is required for LECTURER");
                response.setUserStatus(false);
                return ResponseEntity.badRequest().body(response);
            }
            if (request.getGraduationType() == null || request.getGraduationType().isBlank()) {
                response.setStatus("graduationType is required for LECTURER");
                response.setUserStatus(false);
                return ResponseEntity.badRequest().body(response);
            }
        }

        User newUser = new User();
        newUser.setJoiningYear(year);
        newUser.setEmail(request.getEmail());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setMobileNumber(request.getMobileNumber());
        newUser.setEnrollmentNumber(res);


        String salt = UUID.randomUUID().toString() + UUID.randomUUID().toString().substring(0, 10);
        newUser.setSalt(salt);

        String hashedPassword = signatureService.hashPassword(request.getPassword(), salt);
        newUser.setPasswordHash(hashedPassword);


        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());
        newUser.setCountryCode(request.getCountryCode());

        newUser.setUserType(userType);

        userRepository.save(newUser);

        if(userType.equals(User.UserType.STUDENT)){
            StudentEducationInfo StudentEducationInfo=new StudentEducationInfo();
            StudentEducationInfo.setProgramId(request.getProgramId());
            StudentEducationInfo.setBranch(request.getBranch());
            StudentEducationInfo.setSemester(request.getSemester());
            StudentEducationInfo.setCreatedAt(new Date());
            StudentEducationInfo.setUpdatedAt(new Date());
            StudentEducationInfo.setGraduationType(request.getGraduationType());
            StudentEducationInfo.setSemesterStatus(SemesterStatus.NOT_STARTED);
            StudentEducationInfo.setStudentId(newUser.getId());

            studentSemestersRepository.save(StudentEducationInfo);
        }

        if(userType.equals(User.UserType.LECTURER)){
            LecturerDetails leDetails=new LecturerDetails();
            leDetails.setLectureId(newUser.getId());
            leDetails.setAssignmentType(LecturerDetails.AssignmentType.LECTURER);
            leDetails.setSubjectType(request.getSubjectType());
            leDetails.setGraduationLecturer(request.getGraduationType());
            leDetails.setCreatedAt(new Date());
            leDetails.setUpdatedAt(new Date());
            lectureCoursesRepository.save(leDetails);

        }


        UsernamePasswordAuthenticationToken creds = new UsernamePasswordAuthenticationToken(
                request.getEmail(), " ");

        org.springframework.security.core.Authentication authentication =
                customAuthenticationManager.authenticateByEmail(creds);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication, "ACCESS");
        String refreshToken = tokenProvider.createToken(authentication, "REFRESH");

        response.setToken(token);
        response.setRefreshToke(refreshToken);
        response.setId(newUser.getId());
        response.setAccessToken(token);
        response.setUserId(newUser.getId());
        response.setUserRole(newUser.getUserType().toString());
        response.setStatus("Registration Successful");


        if (userType == User.UserType.STUDENT) {
            templateName="student-registration-welcome.template";
            subject="Welcome to Nethaji Degree College - Student Login Details";
        }

        else{
            templateName="lecturer-registration-welcome.template";
            subject="Welcome to Nethaji Degree College - Lecturer Login Details";
        }

        TemplateContext templateContext = new TemplateContext();
        templateContext.put("name", request.getFirstName()+" "+request.getLastName());
        templateContext.put("email", request.getEmail());
        templateContext.put("password", request.getPassword());

        EmailRequestDto emailRequestDto = new EmailRequestDto();
        emailRequestDto.setEmails(Arrays.asList(request.getEmail()));
        emailRequestDto.setSubject(subject);
        emailRequestDto.setTemplateName(templateName);
        emailRequestDto.setTemplateContext(templateContext);

        sendEmail(emailRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Override
       public List<StudentEducationInfoResponse> getAllNewStudentsByBranchAndSemester(UUID programId,Integer semester){

        List<StudentEducationInfoResponse> data = new ArrayList<>();

        if (programId == null || semester == null) {
            return data;
        }

        List<StudentEducationInfo> educationInfos;
        Department dept = departmentRepository.findById(programId).orElse(null);
        if (dept != null && dept.getProgramId() != null && dept.getDepartmentCode() != null) {
            educationInfos = studentSemestersRepository.findActiveStudentsByProgramAndSemesterAndGraduationType(
                    dept.getProgramId(),
                    semester,
                    dept.getDepartmentCode()
            );
        } else {
            educationInfos = studentSemestersRepository.findActiveStudentsByProgramAndSemester(programId,semester);
        }


        for (StudentEducationInfo educationInfo : educationInfos) {

            Optional<User> userOpt = userRepository.findById(educationInfo.getStudentId());

            if (userOpt.isEmpty()) {
                continue;
            }

            User user = userOpt.get();

            StudentEducationInfoResponse response = new StudentEducationInfoResponse();

            response.setId(educationInfo.getId());
            response.setStudentId(educationInfo.getStudentId());
            response.setSemester(educationInfo.getSemester());
            response.setSemesterStatus(educationInfo.getSemesterStatus());
            response.setBranch(educationInfo.getBranch());
            response.setGraduationType(educationInfo.getGraduationType());
            response.setCreatedAt(educationInfo.getCreatedAt());
            response.setActive(educationInfo.isActive());

            response.setStudent(educationInfo.getStudent());

            data.add(response);
        }

        return data;


    }




//    @Override
//    public List<StudentEducationInfoResponse> getAllNewStudentsByBranchAndSemester(String  branch,Integer semester){
//
//        List<StudentEducationInfoResponse> data = new ArrayList<>();
//
//
//        if(branch!=null && semester!=null){
//
//
//            StudentEducationInfo educationInfo=studentSemestersRepository.findByBranchAndSemester(branch,semester);
//
//            if(educationInfo!=null){
//                StudentProfile stuProfile=studentProfileRepository.findByUserIdAndStatus(educationInfo.getStudentId(),StudentStatus.ACTIVE);
//
//                if(stuProfile==null){
//                    return null;
//                }
//                StudentEducationInfoResponse studentResponse=new StudentEducationInfoResponse();
//                studentResponse.setSemester(educationInfo.getSemester());
//                studentResponse.setStudentId(educationInfo.getStudentId());
//                studentResponse.setSemesterStatus(educationInfo.getSemesterStatus());
//
//                StudentSection sections=studentSectionRepository.findByStudentId(educationInfo.getStudentId(),semester);
//                if(sections==null) {
//
//                    studentResponse.setBranch(educationInfo.getBranch());
//
//                    Optional<User> user = userRepository.findById(educationInfo.getStudentId());
//                    if (user != null) {
//                        User uses = user.get();
//                        studentResponse.setEmail(uses.getEmail());
//                        studentResponse.setFirstName(uses.getFirstName());
//                        studentResponse.setLastName(uses.getLastName());
//                        studentResponse.setMobileNumber(uses.getMobileNumber());
//                    }
//                    studentResponse.setGraduationType(educationInfo.getGraduationType());
//                    studentResponse.setBranch(educationInfo.getBranch());
//                    studentResponse.setCreatedAt(educationInfo.getCreatedAt());
//
//                    data.add(studentResponse);
//
//                }
//            }
//
//
//
//        }
//
//        return data;
//    }
//
//

    public StudentsSectionsDto setSectionsForNewStudents(StudentSection studentSection){

        StudentsSectionsDto studentsSectionsDto=new StudentsSectionsDto();
        StudentSection sections=null;
        if(studentSection.getStudentId()!=null &&studentSection.getSemester()!=null) {
             sections = studentSectionRepository.findByStudentId(studentSection.getStudentId(), studentSection.getSemester());

            if (sections != null) {

                studentsSectionsDto.setStatus("pls check this student already present in this semester and branch"+" "+studentSection.getBranch() +""+studentSection.getSemester());

                return studentsSectionsDto;
            }

            sections=new StudentSection();
            sections.setStudentId(studentSection.getStudentId());
            sections.setSectionName(studentSection.getSectionName());
            sections.setBranch(studentSection.getBranch());
            sections.setCreatedAt(new Date());
            sections.setSemester(studentSection.getSemester());
            String departmetCode=String.valueOf(studentSection.getBranch());

            StudentEducationInfo stEducationInfo= (StudentEducationInfo) studentSemestersRepository.findByStudentId(studentSection.getStudentId());

            if(stEducationInfo!=null){

                stEducationInfo.setSemesterStatus(SemesterStatus.ACTIVE);
            }

            Department dept=departmentRepository.findByDepartMentCode(departmetCode);

            if(dept!=null) {

                sections.setDepartmentId(dept.getId());

                DepartMentSemesters depSemesters=departMentSemestersRepo.findByDepartmentIdAndSemester(dept.getId(),studentSection.getSemester());

                if(depSemesters!=null){
                    sections.setSemesterYear(depSemesters.getSemesterYear());
                    sections.setSemesterStartDate(depSemesters.getSemesterStartDate());
                }
            }

            studentSemestersRepository.save(stEducationInfo);


            studentsSectionsDto.setStudentId(sections.getStudentId());
            studentsSectionsDto.setSectionName(sections.getSectionName());
            studentsSectionsDto.setCreatedAt(new Date());
            studentSection.setDepartmentId(sections.getDepartmentId());
            studentsSectionsDto.setBranch(sections.getBranch());
            studentsSectionsDto.setSemester(sections.getSemester());

        }

        return studentsSectionsDto;
    }


@Override
    public List<StudentsSectionsDto> getAllStudentsBasedOnSectionAndSemester(String section,Integer semester){

        List<StudentsSectionsDto> list=new ArrayList<>();

        List<StudentSection>studentSections=studentSectionRepository.findBySectionNameAndSemester(section,semester);

        if(studentSections==null ||studentSections.isEmpty()){
            StudentsSectionsDto studentsSectionsDto=new StudentsSectionsDto();
            studentsSectionsDto.setStatus("there is no records with this section and semester");
            return (List<StudentsSectionsDto>) studentsSectionsDto;
        }
        studentSections.stream().forEach(a->{
        StudentsSectionsDto studentsSectionsDto=new StudentsSectionsDto();
            studentsSectionsDto.setSectionName(a.getSectionName());
            studentsSectionsDto.setSemester(a.getSemester());
            studentsSectionsDto.setStudentId(a.getStudentId());
            studentsSectionsDto.setDepartmentId(a.getDepartmentId());

            studentsSectionsDto.setBranch(a.getBranch());

            Optional<User> user=userRepository.findById(a.getStudentId());
            if(user!=null) {
                User users = user.get();


                studentsSectionsDto.setFullName(users.getFirstName()+" "+users.getLastName());
                studentsSectionsDto.setRegisteredOn(expectedDateFormat.format(users.getCreatedAt()));
            }

            list.add(studentsSectionsDto);
        });


        return list;
    }

    @Override
    public ResponseEntity<Map<String, Object>> deactivateStaff(UUID userId) {
        Map<String, Object> resp = new HashMap<>();
        if (userId == null) {
            resp.put("status", "userId is required");
            return ResponseEntity.badRequest().body(resp);
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            resp.put("status", "User not found");
            return ResponseEntity.badRequest().body(resp);
        }

        user.setIsActive(false);
        user.setUpdatedAt(new Date());
        userRepository.save(user);

        StaffProfile staffProfile = staffProfileRepo.findByUserId(userId);
        if (staffProfile != null) {
            staffProfile.setStatus(StaffStatus.INACTIVE);
            staffProfile.setUpdatedAt(new Date());
            staffProfileRepo.save(staffProfile);
        }

        LecturerDetails lecturerDetails = lectureCoursesRepository.findByLectureId(userId);
        if (lecturerDetails != null) {
            lecturerDetails.setStatus(false);
            lecturerDetails.setUpdatedAt(new Date());
            lectureCoursesRepository.save(lecturerDetails);
        }

        List<LecturerCourseAssignment> assignments = lecturerCourseAssignmentRepository.findByLecturerId(userId);
        if (assignments != null && !assignments.isEmpty()) {
            for (LecturerCourseAssignment a : assignments) {
                a.setIsActive(false);
            }
            lecturerCourseAssignmentRepository.saveAll(assignments);
        }

        resp.put("status", "Staff deactivated successfully");
        resp.put("userId", userId);
        return ResponseEntity.ok(resp);
    }

    @Override
    public StaffProfile saveStaffProfile(StaffProfile dto){

        if (dto == null || dto.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }

        StaffProfile staffProfile = staffProfileRepo.findByUserId(dto.getUserId());
        boolean isNew = false;
        if (staffProfile == null) {
            staffProfile = new StaffProfile();
            staffProfile.setUserId(dto.getUserId());
            staffProfile.setCreatedAt(new Date());
            isNew = true;
        }

        staffProfile.setUpdatedAt(new Date());

        // NOTE: This method is used by the UI for partial updates (e.g., only address/profile photo).
        // Do NOT overwrite existing required columns with null.
        staffProfile.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : staffProfile.getFirstName());
        staffProfile.setMiddleName(dto.getMiddleName() != null ? dto.getMiddleName() : staffProfile.getMiddleName());
        staffProfile.setLastName(dto.getLastName() != null ? dto.getLastName() : staffProfile.getLastName());

        staffProfile.setDepartment(dto.getDepartment() != null ? dto.getDepartment() : staffProfile.getDepartment());
        staffProfile.setDesignation(dto.getDesignation() != null ? dto.getDesignation() : staffProfile.getDesignation());
        staffProfile.setQualification(dto.getQualification() != null ? dto.getQualification() : staffProfile.getQualification());
        staffProfile.setEmployeeSubject(dto.getEmployeeSubject() != null ? dto.getEmployeeSubject() : staffProfile.getEmployeeSubject());

        staffProfile.setJoiningDate(dto.getJoiningDate() != null ? dto.getJoiningDate() : staffProfile.getJoiningDate());
        staffProfile.setExperienceYears(dto.getExperienceYears() != null ? dto.getExperienceYears() : staffProfile.getExperienceYears());

        staffProfile.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : staffProfile.getPhoneNumber());
        staffProfile.setEmail(dto.getEmail() != null ? dto.getEmail() : staffProfile.getEmail());

        staffProfile.setEmploymentType(dto.getEmploymentType() != null ? dto.getEmploymentType() : staffProfile.getEmploymentType());
        staffProfile.setSalary(dto.getSalary() != null ? dto.getSalary() : staffProfile.getSalary());

        staffProfile.setEmergencyContactName(dto.getEmergencyContactName() != null ? dto.getEmergencyContactName() : staffProfile.getEmergencyContactName());
        staffProfile.setEmergencyContactPhone(dto.getEmergencyContactPhone() != null ? dto.getEmergencyContactPhone() : staffProfile.getEmergencyContactPhone());
        staffProfile.setAddress(dto.getAddress() != null ? dto.getAddress() : staffProfile.getAddress());
        staffProfile.setDateOfBirth(dto.getDateOfBirth() != null ? dto.getDateOfBirth() : staffProfile.getDateOfBirth());
        staffProfile.setGender(dto.getGender() != null ? dto.getGender() : staffProfile.getGender());

        // Allow profile photo update via upload flow
        staffProfile.setProfilePicture(dto.getProfilePicture() != null ? dto.getProfilePicture() : staffProfile.getProfilePicture());

        // Ensure required fields are present even if older rows were created incorrectly
        if (staffProfile.getDepartment() == null) {
            staffProfile.setDepartment("NA");
        }
        if (staffProfile.getDesignation() == null) {
            staffProfile.setDesignation("NA");
        }
        if (staffProfile.getEmployeeSubject() == null) {
            staffProfile.setEmployeeSubject("NA");
        }
        if (staffProfile.getJoiningDate() == null) {
            staffProfile.setJoiningDate(java.time.LocalDate.now());
        }
        if (staffProfile.getFirstName() == null) {
            staffProfile.setFirstName("");
        }
        if (staffProfile.getLastName() == null) {
            staffProfile.setLastName("");
        }

        staffProfile.setStatus(dto.getStatus() != null ? dto.getStatus() : StaffStatus.ACTIVE);

        StaffProfile saved = staffProfileRepo.save(staffProfile);

        if (isNew) {
            return saved;
        }
        return saved;
    }

    @Override
    public StaffProfile getStaffProfile(UUID userId) {

        if (userId == null) {
            return new StaffProfile();
        }

        StaffProfile dto = staffProfileRepo.findByUserId(userId);
        if (dto == null) {
            return new StaffProfile();
        }

        User user = userRepository.findById(userId).orElse(null);

        StaffProfile staffProfile = new StaffProfile();
        staffProfile.setId(dto.getId());
        staffProfile.setUserId(dto.getUserId());
        staffProfile.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : (user != null ? user.getFirstName() : null));
        staffProfile.setMiddleName(dto.getMiddleName());
        staffProfile.setLastName(dto.getLastName() != null ? dto.getLastName() : (user != null ? user.getLastName() : null));
        staffProfile.setDepartment(dto.getDepartment());
        staffProfile.setDesignation(dto.getDesignation());
        staffProfile.setQualification(dto.getQualification());
        staffProfile.setEmployeeSubject(dto.getEmployeeSubject());
        staffProfile.setJoiningDate(dto.getJoiningDate());
        staffProfile.setExperienceYears(dto.getExperienceYears());
        staffProfile.setPhoneNumber(dto.getPhoneNumber() != null ? dto.getPhoneNumber() : (user != null ? user.getMobileNumber() : null));
        staffProfile.setEmail(dto.getEmail() != null ? dto.getEmail() : (user != null ? user.getEmail() : null));
        staffProfile.setEmploymentType(dto.getEmploymentType());
        staffProfile.setSalary(dto.getSalary());
        staffProfile.setEmergencyContactName(dto.getEmergencyContactName());
        staffProfile.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        staffProfile.setAddress(dto.getAddress());
        staffProfile.setDateOfBirth(dto.getDateOfBirth());
        staffProfile.setGender(dto.getGender());
        staffProfile.setStatus(dto.getStatus());
        staffProfile.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : (user != null ? user.getCreatedAt() : null));
        staffProfile.setUpdatedAt(dto.getUpdatedAt());

        return staffProfile;

    }
    @Override
    public List<StaffProfile>getListOfStaffDetails(){

        List<StaffProfile>list=new ArrayList<>();

        List<StaffProfile>getProfiles=staffProfileRepo.findAll();
        if(getProfiles!=null&&!getProfiles.isEmpty()){

            getProfiles.stream().forEach(d -> {
                ;
                StaffProfile staffProfile = new StaffProfile();
                staffProfile.setId(d.getId());
                staffProfile.setUserId(d.getUserId());
                staffProfile.setFirstName(d.getFirstName());
                staffProfile.setMiddleName(d.getMiddleName());
                staffProfile.setLastName(d.getLastName());
                staffProfile.setEmail(d.getEmail());
                staffProfile.setPhoneNumber(d.getPhoneNumber());
                staffProfile.setDepartment(d.getDepartment());
                staffProfile.setDesignation(d.getDesignation());
                staffProfile.setQualification(d.getQualification());
                staffProfile.setEmployeeSubject(d.getEmployeeSubject());
                staffProfile.setJoiningDate(d.getJoiningDate());
                staffProfile.setExperienceYears(d.getExperienceYears());
                staffProfile.setEmploymentType(d.getEmploymentType());
                staffProfile.setSalary(d.getSalary());
                staffProfile.setEmergencyContactName(d.getEmergencyContactName());
                staffProfile.setEmergencyContactPhone(d.getEmergencyContactPhone());
                staffProfile.setAddress(d.getAddress());
                staffProfile.setDateOfBirth(d.getDateOfBirth());
                staffProfile.setGender(d.getGender());
                staffProfile.setStatus(d.getStatus());
                staffProfile.setCreatedAt(d.getCreatedAt());
                staffProfile.setUpdatedAt(d.getUpdatedAt());

                User u = userRepository.findById(d.getUserId()).orElse(null);
                staffProfile.setLoginStatus(u != null ? u.getIsActive() : null);

                list.add(staffProfile);
            });

        }


        return list;
    }





    public String generateStudentId(String branch, String admissionYear, String semester) {
//        String cleanCourseCode = courseCode.replaceAll("_", "");
        String prefix = admissionYear + semester + branch;

        String lastId = userRepository.findLastEnrollmentNumberByPrefix(prefix);
        int nextSerial = 1;

        if (lastId != null && lastId.length() > prefix.length()) {
            String lastSerialStr = lastId.substring(prefix.length());

            System.out.println("==================");
            System.out.println(lastSerialStr);
            System.out.println("==================");// whatever remains
            try {
                int lastSerial = Integer.parseInt(lastSerialStr);
                nextSerial = lastSerial + 1;
            }

            catch (NumberFormatException ex) {

                nextSerial = 1;
            }
        }


        String paddedSerial = String.format("%04d", nextSerial);
        return prefix + paddedSerial;
    }



    @Override
    public ResponseEntity<PrincipalHierarchyResponse> getLecturers() {

        PrincipalHierarchyResponse resp = new PrincipalHierarchyResponse();

        User principal = (User) userRepository.findByUserType(User.UserType.LECTURER).orElse(null);

        if (principal != null && principal.getUserType() == User.UserType.LECTURER) {


            List<LecturerInfo> lecturers = new ArrayList<>();


            LecturerInfo li = new LecturerInfo();

         LecturerDetails lecCorces=null;

         if(lecCorces!=null){

             li.setLecturerId(lecCorces.getLectureId());
             li.setFirstName(principal.getFirstName());
             li.setLastName(principal.getLastName());
             li.setGraduationLecturer(lecCorces.getGraduationLecturer());
             li.setSubjectType(lecCorces.getSubjectType().toString());

         }


        }
        return null;
    }


    @Override
    public ResponseEntity<List<RegisterRequest>> getUsersByType(User.UserType userType) {
        try {

            List<User> users = userRepository.findUsersByType(userType);


            List<RegisterRequest> registerRequests = users.stream()
                    .map(user -> {

                        RegisterRequest registerRequest = new RegisterRequest();

                        registerRequest.setId(user.getId());
                        registerRequest.setFirstName(user.getFirstName());
                        registerRequest.setLastName(user.getLastName());
                        registerRequest.setEmail(user.getEmail());
                        registerRequest.setMobileNumber(user.getMobileNumber());
                        registerRequest.setCountryCode(user.getCountryCode());
                        registerRequest.setUserType(user.getUserType());

                        return  registerRequest;


                    })
                    .collect(Collectors.toList());


            return ResponseEntity.ok(registerRequests);

        }

        catch (Exception e) {
            System.err.println("Error fetching users by type: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @Override
    public ResponseEntity<Page<RegisterRequest>> getUsersByTypeWithPagination(User.UserType userType, int page, int size, String sortBy, String sortDirection) {
        try {

            Sort sort = sortDirection.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();


            Pageable pageable = PageRequest.of(page, size, sort);


            Page<User> userPage = userRepository.findUsersByType1(userType, pageable);


            Page<RegisterRequest> registerRequestPage = userPage.map(user -> {
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setId(user.getId());
                registerRequest.setFirstName(user.getFirstName());
                registerRequest.setLastName(user.getLastName());
                registerRequest.setEmail(user.getEmail());
                registerRequest.setEnrollmentNumber(user.getEnrollmentNumber());
                registerRequest.setMobileNumber(user.getMobileNumber());
                registerRequest.setCountryCode(user.getCountryCode());
                registerRequest.setUserType(user.getUserType());
                enrichStudentRegisterRequestIfStudent(user, registerRequest);
                return registerRequest;
            });

            return ResponseEntity.ok(registerRequestPage);


        } catch (Exception e) {
            System.err.println("Error fetching paginated users by type: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }







    private LecturerWithAssistantsResponse mapToLecturerWithAssistantsResponse(User user) {
        LecturerWithAssistantsResponse response = new LecturerWithAssistantsResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setUserType(user.getUserType().toString());
        return response;
    }

    public AssistantLecturerResponse mapToAssistantLecturerResponse(User user, User lecturer) {
        AssistantLecturerResponse response = new AssistantLecturerResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setMobileNumber(user.getMobileNumber());
        response.setUserType(user.getUserType().toString());

        if (lecturer != null) {
            response.setLectureName(lecturer.getFirstName() + " " + lecturer.getLastName());
            response.setLectureId(lecturer.getId());
        }

        return response;
    }




    public  ResponseEntity<List<StudentWithSemesterAndProgramDTO>> getActiveStudentsByProgramIdAndSemester(UUID programId, int semester){

        List<StudentWithSemesterAndProgramDTO> result = new ArrayList<>();

        Programs program=programsRepository.findById(programId).orElse(null);


        if(program==null){

            return ResponseEntity.badRequest().body(Collections.emptyList());

        }


        List<StudentPrograms> studentPrograms = studentProgramsRepository.findByProgramIdAndIsActiveTrue(programId);


        for (StudentPrograms sp : studentPrograms) {

            User student = userRepository.findById(sp.getStudentId()).orElse(null);

            if (student == null)
            {
                continue;
            }

            StudentSection sc=studentSectionRepository.findActiveSectionByStudentAndSemester(student.getId(),semester);

            List<StudentEducationInfo> semesters = studentSemestersRepository.findByStudentId(sp.getStudentId());


            List<StudentSemestersDTO> semesterDTOs = semesters.stream().map(s -> {

                StudentSemestersDTO dto = new StudentSemestersDTO();
                dto.setStudentSemesterId(s.getId());

                dto.setStudentSemester(s.getSemester());

                dto.setStudentSemesterStatus(s.getSemesterStatus());

                dto.setCreatedAt(s.getCreatedAt());

                dto.setUpdatedAt(s.getUpdatedAt());

                dto.setStudentSemesterActivity(s.isActive());
                return dto;

            }).collect(Collectors.toList());



            StudentWithSemesterAndProgramDTO dto = new StudentWithSemesterAndProgramDTO();

            dto.setStudentId(student.getId());

            dto.setFirstName(student.getFirstName());
            dto.setLastName(student.getLastName());
            dto.setEmail(student.getEmail());
            dto.setEnrollmentNumber(student.getEnrollmentNumber());
            dto.setIsActive(student.getIsActive());

            dto.setStudentProgramId(sp.getId());
            dto.setProgramName(program.getName());
            dto.setProgramCode(program.getProgramCode());
            dto.setEnrolmentStatus(sp.getEnrollmentStatus());
            dto.setProgramStatus(sp.isActive());
            dto.setCreatedAt(sp.getCreatedAt());
            dto.setUpdatedAt(sp.getUpdatedAt());

            if(sc!=null&&sc.getStatus()){
                dto.setStudentSectionId(sc.getId());
                dto.setStudentFullSection(sc.getSectionName()+sc.getProgramCode());
            }

            dto.setStudentSemestersDTOList(semesterDTOs);

            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<StudentProfileDTO> saveOrUpdateUserProfile(StudentProfileDTO dto) {

        StudentProfile entity;
        Optional<StudentProfile> optional = studentProfileRepository.findByUserId(dto.getUserId());

        if (optional.isPresent()) {
            entity = optional.get();
            entity.setUpdatedAt(new Date());
        } else {
            entity = new StudentProfile();
            entity.setUserId(dto.getUserId());
            entity.setIsActive(true);
            entity.setCreatedAt(new Date());
            entity.setUpdatedAt(new Date());
        }

        entity.setDateOfBirth(dto.getDateOfBirth() != null ? dto.getDateOfBirth() : entity.getDateOfBirth());
        entity.setGender(dto.getGender() != null ? dto.getGender() : entity.getGender());
        entity.setAddress(dto.getAddress() != null ? dto.getAddress() : entity.getAddress());
        entity.setCity(dto.getCity() != null ? dto.getCity() : entity.getCity());
        entity.setState(dto.getState() != null ? dto.getState() : entity.getState());
        entity.setPincode(dto.getPincode() != null ? dto.getPincode() : entity.getPincode());
        entity.setGuardianContact(dto.getGuardianContact() != null ? dto.getGuardianContact() : entity.getGuardianContact());
        entity.setGuardianName(dto.getGuardianName() != null ? dto.getGuardianName() : entity.getGuardianName());
        entity.setGuardianRelation(dto.getGuardianRelation() != null ? dto.getGuardianRelation() : entity.getGuardianRelation());
        entity.setProfilePictureUrl(dto.getProfilePictureUrl() != null ? dto.getProfilePictureUrl() : entity.getProfilePictureUrl());
        entity.setBloodGroup(dto.getBloodGroup() != null ? dto.getBloodGroup() : entity.getBloodGroup());
        entity.setAlternateContact(dto.getAlternateContact() != null ? dto.getAlternateContact() : entity.getAlternateContact());
        entity.setTenthPercentage(dto.getTenthPercentage() != null ? dto.getTenthPercentage() : entity.getTenthPercentage());
        entity.setTenthBoard(dto.getTenthBoard() != null ? dto.getTenthBoard() : entity.getTenthBoard());
        entity.setTenthYearOfPassing(dto.getTenthYearOfPassing() != null ? dto.getTenthYearOfPassing() : entity.getTenthYearOfPassing());
        entity.setTwelfthPercentage(dto.getTwelfthPercentage() != null ? dto.getTwelfthPercentage() : entity.getTwelfthPercentage());
        entity.setTwelfthBoard(dto.getTwelfthBoard() != null ? dto.getTwelfthBoard() : entity.getTwelfthBoard());
        entity.setTwelfthYearOfPassing(dto.getTwelfthYearOfPassing() != null ? dto.getTwelfthYearOfPassing() : entity.getTwelfthYearOfPassing());

        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : entity.getIsActive());

        StudentProfile saved = studentProfileRepository.save(entity);

        dto.setId(saved.getId());
        dto.setCreatedAt(saved.getCreatedAt());
        dto.setUpdatedAt(saved.getUpdatedAt());

        return ResponseEntity.ok(dto);
    }


    @Override
    public ResponseEntity<StudentProfileDTO> getUserProfileById(UUID id) {

        Optional<StudentProfile> optional = studentProfileRepository.findByUserId(id);

        User user = userRepository.findById(id).orElse(null);

        if (optional.isEmpty()) {

            StudentProfileDTO dto = new StudentProfileDTO();
            dto.setUserId(id);

            if (user != null) {
                dto.setEnrollmentNumber(user.getEnrollmentNumber());
                dto.setFirstName(user.getFirstName());
                dto.setLastName(user.getLastName());
                dto.setEmail(user.getEmail());
                dto.setMobileNumber(user.getMobileNumber());
                dto.setUserCreatedAt(user.getCreatedAt());
            }

            return ResponseEntity.ok(dto);
        }

        StudentProfile entity = optional.get();
        StudentProfileDTO dto = new StudentProfileDTO();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getId());

        if (user != null) {
            dto.setEnrollmentNumber(user.getEnrollmentNumber());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setMobileNumber(user.getMobileNumber());
            dto.setUserCreatedAt(user.getCreatedAt());
        }

        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setGender(entity.getGender());
        dto.setAddress(entity.getAddress());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setPincode(entity.getPincode());
        dto.setGuardianContact(entity.getGuardianContact());
        dto.setGuardianName(entity.getGuardianName());
        dto.setGuardianRelation(entity.getGuardianRelation());
        dto.setProfilePictureUrl(entity.getProfilePictureUrl());
        dto.setBloodGroup(entity.getBloodGroup());
        dto.setAlternateContact(entity.getAlternateContact());
        dto.setTenthPercentage(entity.getTenthPercentage());
        dto.setTenthBoard(entity.getTenthBoard());
        dto.setTenthYearOfPassing(entity.getTenthYearOfPassing());
        dto.setTwelfthPercentage(entity.getTwelfthPercentage());
        dto.setTwelfthBoard(entity.getTwelfthBoard());
        dto.setTwelfthYearOfPassing(entity.getTwelfthYearOfPassing());
        dto.setIsActive(entity.getIsActive());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return ResponseEntity.ok(dto);

    }


    public List<StudentProfile>getListOfStudentsInfo(){

        List<StudentProfile>list=new ArrayList<>();

        List<StudentProfile>getStudentsInfo=studentProfileRepository.findAll();

        if(getStudentsInfo!=null &&!getStudentsInfo.isEmpty()){
            getStudentsInfo.stream().forEach(entity -> {
                ;
                StudentProfile dto=new StudentProfile();

                dto.setId(entity.getId());
                dto.setUserId(entity.getUser().getId());

                dto.setDateOfBirth(entity.getDateOfBirth());
                dto.setGender(entity.getGender());
                dto.setAddress(entity.getAddress());
                dto.setCity(entity.getCity());
                dto.setState(entity.getState());
                dto.setPincode(entity.getPincode());
                dto.setGuardianContact(entity.getGuardianContact());
                dto.setGuardianName(entity.getGuardianName());
                dto.setGuardianRelation(entity.getGuardianRelation());
                dto.setProfilePictureUrl(entity.getProfilePictureUrl());
                dto.setBloodGroup(entity.getBloodGroup());
                dto.setAlternateContact(entity.getAlternateContact());
                dto.setTenthPercentage(entity.getTenthPercentage());
                dto.setTenthBoard(entity.getTenthBoard());
                dto.setTenthYearOfPassing(entity.getTenthYearOfPassing());
                dto.setTwelfthPercentage(entity.getTwelfthPercentage());
                dto.setTwelfthBoard(entity.getTwelfthBoard());
                dto.setTwelfthYearOfPassing(entity.getTwelfthYearOfPassing());
                dto.setIsActive(entity.getIsActive());
                dto.setCreatedAt(entity.getCreatedAt());
                dto.setUpdatedAt(entity.getUpdatedAt());


                list.add(dto);
            });
        }

        return list;
    }

    public TimeTableDetailsDto setTimeTablesForSemestersAndHolidaysExams(TimeTableDetailsDto timeTableDetailsDto){

        TimeTableDetailsDto timeTableDetailsDt=new TimeTableDetailsDto();
        if(timeTableDetailsDto.getTimeTableType()!=null){
            timeTableDetailsDt.setStatus("pls check there is no time table type like...SYLLABUS,EXAM,HOLIDAYS");
            return timeTableDetailsDt;
        }

        if(timeTableDetailsDto.getTimeTableType().equals("SYLLABUS")){

            TimeTableDetails tmTaDetails=timeTableDetailsRepo.findByDepartmentIdAndSemesterIdAndSubjectName(timeTableDetailsDto.getDepartmentId(),timeTableDetailsDto.getSemesterId(),timeTableDetailsDto.getSubjectName());

            if(tmTaDetails!=null){
                tmTaDetails.setPeriodTime(timeTableDetailsDto.getPeriodTime());
                tmTaDetails.setUpdatedAt(new Date());
                tmTaDetails.setPeriodStartToEnd(timeTableDetailsDto.getPeriodStartToEnd());

            }else {
                tmTaDetails=new TimeTableDetails();
                tmTaDetails.setDepartmentId(timeTableDetailsDto.getDepartmentId());
                tmTaDetails.setSemesterId(timeTableDetailsDto.getSemesterId());
                tmTaDetails.setTimeTableType(timeTableDetailsDto.getTimeTableType());
                tmTaDetails.setPeriodTime(timeTableDetailsDto.getPeriodTime());
                tmTaDetails.setPeriodStartToEnd(timeTableDetailsDto.getPeriodStartToEnd());

             tmTaDetails.setSubjectName(timeTableDetailsDto.getSubjectName());
             tmTaDetails.setCreatedAt(new Date());

            }

            timeTableDetailsRepo.save(tmTaDetails);

        }else {
            if(timeTableDetailsDto.getTimeTableType().equals("EXAMS")){

                ExamesTimeTable examTime=examesTimeTableRepo.findByDepartmentIdAndSemesterIdAndExamSubject(timeTableDetailsDto.getDepartmentId(),timeTableDetailsDto.getSemesterId(),timeTableDetailsDto.getExamSubject());

                if(examTime!=null){

                    examTime.setExamTime(timeTableDetailsDto.getExamTime());
                    examTime.setExamType(timeTableDetailsDto.getExamType());
                    examTime.setUpdatedAt(new Date());
                    examTime.setExamCenter(timeTableDetailsDto.getExamCenter());
                    examTime.setExameDate(timeTableDetailsDto.getExameDate());
                }else {

                    examTime=new ExamesTimeTable();

                    examTime.setExamTime(timeTableDetailsDto.getExamTime());
                    examTime.setExamType(timeTableDetailsDto.getExamType());
                    examTime.setExamCenter(timeTableDetailsDto.getExamCenter());
                    examTime.setExameDate(timeTableDetailsDto.getExameDate());
                    examTime.setCreatedAt(new Date());
                    examTime.setSemesterId(timeTableDetailsDto.getSemesterId());
                    examTime.setDepartmentId(timeTableDetailsDto.getDepartmentId());
                    examTime.setExamSubject(timeTableDetailsDto.getExamSubject());
                    examTime.setTimeTableType(timeTableDetailsDto.getTimeTableType());
                }

                examesTimeTableRepo.save(examTime);


            }else{
                if(timeTableDetailsDto.getTimeTableType().equals("HOLIDAYS")){
                    HolidaysTimeTable holidays=new HolidaysTimeTable();
                    holidays.setHolidayType(timeTableDetailsDto.getHolidayType());
                    holidays.setCreatedAt(new Date());
                    holidays.setTimeTableType(timeTableDetailsDto.getTimeTableType());
                    holidays.setHolidaysStartDate(timeTableDetailsDto.getHolidaysStartDate());
                    holidays.setHolidaysEndDate(timeTableDetailsDto.getHolidaysEndDate());

                }
            }

        }

        return timeTableDetailsDt;
    }

    public List<TimeTableDetailsDto>getSylabusTimeTableOnDepartAndSemesterId(UUID departmentId,UUID semesterId){

        List<TimeTableDetailsDto>listGetTimeTable=new ArrayList<>();

        List<TimeTableDetails>tmTaDetails=timeTableDetailsRepo.findByDepartmentIdAndSemesterId(departmentId,semesterId);

        if(tmTaDetails==null ||tmTaDetails.isEmpty()){
            return null;
        }

        tmTaDetails.stream().forEach(a->{

            TimeTableDetailsDto timeTableDetailsDto=new TimeTableDetailsDto();

            timeTableDetailsDto.setDepartmentId(a.getDepartmentId());
            timeTableDetailsDto.setSemesterId(a.getSemesterId());
            timeTableDetailsDto.setTimeTableType(a.getTimeTableType());
            timeTableDetailsDto.setPeriodTime(a.getPeriodTime());
            timeTableDetailsDto.setPeriodStartToEnd(a.getPeriodStartToEnd());
            timeTableDetailsDto.setSubjectName(a.getSubjectName());

            listGetTimeTable.add(timeTableDetailsDto);

        });

        return listGetTimeTable;
    }

}

