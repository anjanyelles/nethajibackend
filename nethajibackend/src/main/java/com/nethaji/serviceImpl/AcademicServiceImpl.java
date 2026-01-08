package com.nethaji.serviceImpl;

import com.nethaji.Enums.ProgramLevel;
import com.nethaji.dto.DepartmentDTO;
import com.nethaji.dto.*;
import com.nethaji.entity.*;
import com.nethaji.repositories.*;
import com.nethaji.service.AcademicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicServiceImpl implements AcademicService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProgramsRepository programsRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartMentSemestersRepo departMentSemestersRepo;

    @Override
    public ResponseEntity<DepartmentDTO> createAndUpdateDepartment(DepartmentDTO dto) {

        Department department;
        DepartmentDTO res=new DepartmentDTO();

        String code = dto.getDepartmentCode().trim().toUpperCase();

        if(dto.getProgramId()!=null) {
            Optional<Programs> checkProgram = programsRepository.findById(dto.getProgramId());

            if(checkProgram==null || checkProgram.isEmpty()){

                res.setStatus(false);
                res.setMessage("pls check program id is not present");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
            }
        }

        Optional<Department> alreadyPresent=departmentRepository.findbyDepartMentCode(code);
        if (alreadyPresent.isPresent() && dto.getId() == null ) {
            res.setStatus(false);
            res.setMessage("Department with code '" + code + "' already exists.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        if (dto.getId() != null) {

            department = departmentRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Department not found with ID: " + dto.getId()));

            department.setDepartmentCode(code);
            department.setDepartmentName(dto.getDepartmentName());


            department.setUpdatedAt(new Date());
        }

        else {

            department = new Department();
            department.setDepartmentCode(code);
            department.setDepartmentName(dto.getDepartmentName());
            department.setCreatedAt(new Date());
            department.setUpdatedAt(new Date());
        }


        Department saved = departmentRepository.save(department);

        res.setId(saved.getId());
        res.setDepartmentCode(saved.getDepartmentCode());
        res.setDepartmentName(saved.getDepartmentName());

        res.setCreatedAt(saved.getCreatedAt());
        res.setUpdatedAt(saved.getUpdatedAt());

        res.setStatus(true);
        res.setMessage(dto.getId() != null ? "Department updated successfully." : "Department created successfully.");

        return ResponseEntity.ok(res);
    }

    @Override
    public  ResponseEntity<List<DepartmentDTO>> getAllDepartment(UUID id){

        List<Department> res=null;
        if(id!=null){
            Department dept=departmentRepository.findById(id).orElse(null);
            res = dept != null ? List.of(dept) : new ArrayList<>();

        }
        else {

            res = departmentRepository.findAll();
        }

        List<DepartmentDTO> finalResult= res.stream().map(department -> {

            DepartmentDTO dto=new DepartmentDTO();
            dto.setId(department.getId());
            dto.setDepartmentCode(department.getDepartmentCode());
            dto.setDepartmentName(department.getDepartmentName());

            dto.setCreatedAt(department.getCreatedAt());
            dto.setUpdatedAt(department.getUpdatedAt());

            List<DepartMentSemesters> departMentSemesters=departMentSemestersRepo.findByDepartmentId1(department.getId());

            List<DepartMentSemestersDto> semestersDtoList = departMentSemesters.stream().map(sem -> {
                DepartMentSemestersDto semDto = new DepartMentSemestersDto();

                semDto.setId(sem.getId());
                semDto.setSemester(sem.getSemester());
                semDto.setSemesterStartDate(sem.getSemesterStartDate());
                semDto.setSemesterEndDate(sem.getSemesterEndDate());
                semDto.setSemesterYear(sem.getSemesterYear());
                semDto.setSemesterSubjects(sem.getSemesterSubjects());
                semDto.setSemesterTotalLabs(sem.getSemesterTotalLabs());
                semDto.setDepartmentId(sem.getDepartmentId());

                // If needed you can fetch courseListInfo here:
                // List<CourseListInfo> courseList = courseRepo.findBySemesterId(sem.getId());
                // semDto.setCourseListInfo(courseList);

                return semDto;
            }).collect(Collectors.toList());



            dto.setDepartMentSemestersDtoList(semestersDtoList);

            return dto;

        }).collect(Collectors.toList());


        return ResponseEntity.ok(finalResult);
    }



//add graduation types
    @Override
    public ResponseEntity<ProgramDTO> createOrUpdateProgram(ProgramDTO dto) {
        ProgramDTO res = new ProgramDTO();

        String code = dto.getProgramCode().trim().toUpperCase();

        Optional<Programs> alreadyPresent=programsRepository.findByCode(code);
        if(alreadyPresent.isPresent() && dto.getId() == null){
            res.setStatus(false);
            res.setMessage("Program with code '" + code + "' already exists.");
            return ResponseEntity.ok(res);
        }

        Programs program;

        if (dto.getId() != null) {

            program = programsRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Program not found with ID: " + dto.getId()));program.setName(dto.getName());
           program.setProgramCode(code);
           program.setLevel(dto.getLevel());
           program.setDurationYears(dto.getDurationYears());
           program.setIsActive(dto.getIsActive());

            program.setUpdatedAt(new Date());
        }

        else {

            program = new Programs();
            program.setName(dto.getName());
            program.setProgramCode(code);
            program.setLevel(dto.getLevel());
            program.setDurationYears(dto.getDurationYears());
            program.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
            program.setCreatedAt(new Date());
            program.setUpdatedAt(new Date());
        }

        Programs saved = programsRepository.save(program);


        res.setId(saved.getId());
        res.setName(saved.getName());
        res.setProgramCode(saved.getProgramCode()  );
        res.setLevel(saved.getLevel());
        res.setDurationYears(saved.getDurationYears());
        res.setIsActive(saved.getIsActive());
        res.setCreatedAt(saved.getCreatedAt());
        res.setUpdatedAt(saved.getUpdatedAt());

        res.setStatus(true);
        res.setMessage(dto.getId() != null ? "Program updated successfully." : "Program created successfully.");


        return ResponseEntity.ok(res);
    }


    public ResponseEntity<DepartMentSemestersDto> setSemesteresDepartMentWise(DepartMentSemestersDto dto) {

        DepartMentSemestersDto response = new DepartMentSemestersDto();

        if (dto.getDepartmentId() == null) {
            response.setStatus(false);
            response.setMessage("departmentId cannot be null");
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Department> department = departmentRepository.findById(dto.getDepartmentId());

        if (department.isEmpty()) {
            response.setStatus(false);
            response.setMessage("No department found with id: " + dto.getDepartmentId());
            return ResponseEntity.badRequest().body(response);
        }

        DepartMentSemesters entity;

        if (dto.getId() != null) {
            entity = departMentSemestersRepo.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Semester not found: " + dto.getId()));
        }
        else {
            entity = new DepartMentSemesters();
        }

        entity.setSemester(dto.getSemester());
        entity.setSemesterYear(dto.getSemesterYear());
        entity.setSemesterStartDate(dto.getSemesterStartDate());
        entity.setSemesterEndDate(dto.getSemesterEndDate());
        entity.setSemesterTotalLabs(dto.getSemesterTotalLabs());
        entity.setSemesterSubjects(dto.getSemesterSubjects());
        entity.setDepartmentId(dto.getDepartmentId());

        DepartMentSemesters saved = departMentSemestersRepo.save(entity);

        response.setId(saved.getId());
        response.setSemester(saved.getSemester());
        response.setSemesterYear(saved.getSemesterYear());
        response.setSemesterStartDate(saved.getSemesterStartDate());
        response.setSemesterEndDate(saved.getSemesterEndDate());
        response.setSemesterTotalLabs(saved.getSemesterTotalLabs());
        response.setSemesterSubjects(saved.getSemesterSubjects());
        response.setDepartmentId(saved.getDepartmentId());
        response.setStatus(true);
        response.setMessage(dto.getId() != null?"updated successfully":"created successfully");

        return ResponseEntity.ok(response);
    }



    @Override
    public List<DepartMentSemestersDto> getBySemesterType(String departMentType, Integer semester) {

        List<DepartMentSemestersDto> getSemestersInfo = new ArrayList<>();

        if (departMentType != null) {

            Department dep = departmentRepository.findByDepartMentCode(departMentType);

            if (dep != null) {
                DepartMentSemesters depSem = departMentSemestersRepo.findByDepartmentIdAndSemester(dep.getId(), semester);

                if (depSem != null) {

                    DepartMentSemestersDto departMentSemestersDto = new DepartMentSemestersDto();

                    // set semester details
                    departMentSemestersDto.setDepartmentId(dep.getId());
                    departMentSemestersDto.setSemester(depSem.getSemester());
                    departMentSemestersDto.setSemesterYear(depSem.getSemesterYear());
                    departMentSemestersDto.setSemesterSubjects(depSem.getSemesterSubjects());
                    departMentSemestersDto.setSemesterTotalLabs(depSem.getSemesterTotalLabs());
                    departMentSemestersDto.setSemesterStartDate(depSem.getSemesterStartDate());
                    departMentSemestersDto.setSemesterEndDate(depSem.getSemesterEndDate());

                    // fetch courses
                    List<Course> subjects = courseRepository.findByDepartmentSemesterId(depSem.getId());
                    List<CourseListInfo> courseListInfoo = new ArrayList<>();

                    if (subjects != null && !subjects.isEmpty()) {
                        subjects.forEach(b -> {
                            CourseListInfo courseListInf = new CourseListInfo();
                            courseListInf.setCourseType(b.getCourseType());
                            courseListInf.setCourseCode(b.getCourseCode());
                            courseListInf.setName(b.getName());
                            courseListInf.setSyllabusPdfUrl(b.getSyllabusPdfUrl());
                            courseListInf.setDescription(b.getDescription());
                            courseListInfoo.add(courseListInf);
                        });
                    }

                    // attach courses to semester DTO
                    departMentSemestersDto.setCourseListInfo(courseListInfoo);

                    // add semester DTO to final list
                    getSemestersInfo.add(departMentSemestersDto);
                }
            }
        }

        return getSemestersInfo;
    }

    public ResponseEntity<List<ProgramResponseDTO>> getAllPrograms(UUID id) {
        List<Programs> programs;

        if (id != null) {
           Programs pro = programsRepository.findById(id).orElse(null);

           programs=pro!=null?List.of(pro):new ArrayList<>();
        }
        else {
            programs = programsRepository.findAll();
        }

        List<ProgramResponseDTO> response = programs.stream().map(program -> {
            ProgramResponseDTO dto = new ProgramResponseDTO();
            dto.setId(program.getId());
            dto.setName(program.getName());
            dto.setProgramCode(program.getProgramCode());
            dto.setLevel(program.getLevel().name());
            dto.setDurationYears(program.getDurationYears());

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<CourseDTO> createOrUpdateCourse(CourseDTO dto) {

        CourseDTO res = new CourseDTO();
        String code = dto.getCourseCode().trim().toUpperCase();
if(dto.getDepartmentSemesterId()==null){
    res.setStatus(false);
    res.setMessage("pls check departmentSemester id is null");
}
DepartMentSemesters depSem=departMentSemestersRepo.findById(dto.getDepartmentSemesterId()).orElseThrow(()
        -> new RuntimeException("no records with this departmentsemester id"));

        Course course;

        if (dto.getId() != null) {
            course = courseRepository.findById(dto.getId()).orElseThrow(() -> new RuntimeException("Course not found with ID: " + dto.getId()));

            course.setName(dto.getName());
            course.setCourseCode(code);
            course.setDescription(dto.getDescription());
            course.setCredits(dto.getCredits());
            course.setDepartmentSemesterId(dto.getDepartmentSemesterId());
            course.setIsElective(dto.getIsElective());
            course.setCourseType(dto.getCourseType());
            course.setSyllabusPdfUrl(dto.getSyllabusPdfUrl());
            course.setIsActive(dto.getIsActive());
            course.setUpdatedAt(new Date());
        }


        else {
            course = new Course();
            course.setName(dto.getName());
            course.setCourseCode(code);
            course.setDescription(dto.getDescription());
            course.setCredits(dto.getCredits());
            course.setDepartmentSemesterId(dto.getDepartmentSemesterId());
            course.setIsElective(dto.getIsElective() != null ? dto.getIsElective() : false);
            course.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
            course.setSyllabusPdfUrl(dto.getSyllabusPdfUrl());
            course.setCourseType(dto.getCourseType());
            course.setCreatedAt(new Date());
            course.setUpdatedAt(new Date());
        }

        Course saved = courseRepository.save(course);


        res.setId(saved.getId());
        res.setName(saved.getName());
        res.setCourseCode(saved.getCourseCode());
        res.setDescription(saved.getDescription());
        res.setCredits(saved.getCredits());
        res.setDepartmentSemesterId(saved.getDepartmentSemesterId());
        res.setCourseType(saved.getCourseType());
        res.setIsElective(saved.getIsElective());
        res.setSyllabusPdfUrl(saved.getSyllabusPdfUrl());
        res.setIsActive(saved.getIsActive());
        res.setCreatedAt(saved.getCreatedAt());
        res.setUpdatedAt(saved.getUpdatedAt());


        res.setStatus(true);
        res.setMessage(dto.getId()!=null?"Course updated successfully":"Course saved successfully");

        return ResponseEntity.ok(res);
    }


    public ResponseEntity<List<CourseDTO>> getAllCoursesAdmin(UUID id) {

        List<Course> courses;

        if (id != null) {
            Course course = courseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
            courses = List.of(course);
        }
        else {
            courses = courseRepository.findAll();
        }

        List<CourseDTO> dtoList = courses.stream().map(course -> {
            CourseDTO dto = new CourseDTO();
            dto.setId(course.getId());
            dto.setName(course.getName());
            dto.setCourseCode(course.getCourseCode());
            dto.setDescription(course.getDescription());
            dto.setCredits(course.getCredits());
            dto.setCourseType(course.getCourseType());
          //  dto.setProgramId(course.getProgramId());
          //  dto.setDepartmentId(course.getDepartmentId());
            dto.setIsElective(course.getIsElective());
            dto.setIsActive(course.getIsActive());
            dto.setSyllabusPdfUrl(course.getSyllabusPdfUrl());
            dto.setCreatedAt(course.getCreatedAt());
            dto.setUpdatedAt(course.getUpdatedAt());

            DepartMentSemesters departMentSemesters=departMentSemestersRepo.findById(course.getDepartmentSemesterId()).orElse(null);


                dto.setSemester(departMentSemesters!=null?departMentSemesters.getSemester():null);
                Department department=departmentRepository.findById(departMentSemesters.getDepartmentId()).orElse(null);
                dto.setDepartmentName(department!=null?department.getDepartmentName():null);
                dto.setDepartmentCode(department!=null?department.getDepartmentCode():null);
                Programs programs=programsRepository.findById(department.getProgramId()).orElse(null);
                dto.setProgramName(programs!=null?programs.getName():null);
                dto.setProgramCode(programs!=null?programs.getProgramCode():null);


            /*if (course.getProgram() != null) {
                dto.setProgramName(course.getProgram().getName());
            }
            if (course.getDepartment() != null) {
                dto.setDepartmentName(course.getDepartment().getDepartmentName());
            }*/
            return dto;
        }).toList();

        return ResponseEntity.ok(dtoList);
    }





    public List<ProgramDepaSemCourseList> getAllProgramBasedDetails(ProgramLevel programLevel) {

        List<ProgramDepaSemCourseList> arrayList = new ArrayList<>();

        if (programLevel != null) {


            String prog=programLevel.name();
            Programs pro = programsRepository.findByProgramLevel(prog);

            if (pro != null) {
                ProgramDepaSemCourseList programDepaSemCourseList = new ProgramDepaSemCourseList();

                // Set program details
                programDepaSemCourseList.setProgramCode(pro.getProgramCode());
                programDepaSemCourseList.setName(pro.getName());
                programDepaSemCourseList.setLevel(pro.getLevel());

                List<DepartMentInfoList> departMentInfoList = new ArrayList<>();

                // Fetch departments
                List<Department> dep = departmentRepository.findBYProgramId(pro.getId());

                if (dep != null && !dep.isEmpty()) {
                    dep.forEach(a -> {
                        DepartMentInfoList departMentInfoLis = new DepartMentInfoList();
                        departMentInfoLis.setProgramId(a.getProgramId());
                        departMentInfoLis.setDepartmentName(a.getDepartmentName());
                        departMentInfoLis.setDepartmentCode(a.getDepartmentCode());

                        List<DepartMentSemistersListInfo> departMentSemistersListInfo = new ArrayList<>();

                        // Fetch semesters
                        List<DepartMentSemesters> depSem = departMentSemestersRepo.findByDepartmentId(a.getId());
                        if (depSem != null && !depSem.isEmpty()) {
                            depSem.forEach(b -> {
                                DepartMentSemistersListInfo list = new DepartMentSemistersListInfo();
                                list.setSemesterYear(b.getSemesterYear());
                                list.setSemesterSubjects(b.getSemesterSubjects());
                                list.setSemesterTotalLabs(b.getSemesterTotalLabs());
                                list.setSemester(b.getSemester());
                                list.setSemesterStartDate(b.getSemesterStartDate());
                                list.setSemesterEndDate(b.getSemesterEndDate());

                                List<CourseListInfo> courseListInfo = new ArrayList<>();

                                // Fetch courses
                                List<Course> course = courseRepository.findByDepartmentSemesterId(b.getId());
                                if (course != null && !course.isEmpty()) {
                                    course.forEach(c -> {
                                        CourseListInfo courseListInf = new CourseListInfo();
                                        courseListInf.setCourseCode(c.getCourseCode());
                                        courseListInf.setName(c.getName());
                                        courseListInf.setDescription(c.getDescription());
                                        courseListInf.setSyllabusPdfUrl(c.getSyllabusPdfUrl());
                                        courseListInf.setCourseType(c.getCourseType());
                                        courseListInfo.add(courseListInf);
                                    });
                                }

                                // attach courses into semester
                                list.setCourseListInfo(courseListInfo);
                                departMentSemistersListInfo.add(list);
                            });
                        }

                        // attach semesters into department
                        departMentInfoLis.setDepartMentSemistersListInfo(departMentSemistersListInfo);
                        departMentInfoList.add(departMentInfoLis);
                    });
                }

                // attach departments into program
                programDepaSemCourseList.setDepartMentInfoList(departMentInfoList);
                arrayList.add(programDepaSemCourseList);
            }
        }

        return arrayList;
    }


    public ResponseEntity<List<ProgramDTO>> getProgramHierarchy() {

        List<Programs> programs = programsRepository.findAll();

        List<ProgramDTO> finalResult = programs.stream().map(program -> {

            ProgramDTO programDTO = new ProgramDTO();
            programDTO.setId(program.getId());
            programDTO.setName(program.getName());
            programDTO.setProgramCode(program.getProgramCode());

            List<Department> departments = departmentRepository.findByProgramId(program.getId());

            List<DepartmentDTO> deptDTOList = departments.stream().map(dept -> {

                DepartmentDTO deptDTO = new DepartmentDTO();
                deptDTO.setId(dept.getId());
                deptDTO.setDepartmentCode(dept.getDepartmentCode());
                deptDTO.setDepartmentName(dept.getDepartmentName());

                List<DepartMentSemesters> sems = departMentSemestersRepo.findByDepartmentId1(dept.getId());

                List<DepartMentSemestersDto> semDTOList = sems.stream().map(sem -> {

                    DepartMentSemestersDto semDTO = new DepartMentSemestersDto();
                    semDTO.setId(sem.getId());
                    semDTO.setSemesterStartDate(sem.getSemesterStartDate());
                    semDTO.setSemesterEndDate(sem.getSemesterEndDate());
                    semDTO.setSemester(sem.getSemester());
                    semDTO.setSemesterYear(sem.getSemesterYear());
                    semDTO.setSemesterSubjects(sem.getSemesterSubjects());
                    semDTO.setSemesterTotalLabs(sem.getSemesterTotalLabs());
                    semDTO.setDepartmentId(sem.getDepartmentId());

                    List<Course> courses = courseRepository.findByDepartmentSemesterId(sem.getId());

                    List<CourseListInfo> courseDTOs = courses.stream().map(course -> {
                        CourseListInfo c = new CourseListInfo();
                        c.setId(course.getId());
                        c.setName(course.getName());
                        c.setCourseCode(course.getCourseCode());
                        c.setCourseType(course.getCourseType());
                        c.setCredits(course.getCredits());
                        c.setIsElective(course.getIsElective());
                        c.setIsActive(course.getIsActive());
                        c.setSyllabusPdfUrl(course.getSyllabusPdfUrl());
                        c.setCreatedAt(course.getCreatedAt());
                        c.setUpdatedAt(course.getUpdatedAt());
                        return c;
                    }).toList();

                    semDTO.setCourseListInfo(courseDTOs);

                    return semDTO;
                }).toList();

                deptDTO.setDepartMentSemestersDtoList(semDTOList);
                return deptDTO;
            }).toList();

            programDTO.setDepartments(deptDTOList);
            return programDTO;

        }).toList();

        return ResponseEntity.ok(finalResult);
    }




}
