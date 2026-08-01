package org.example.smartattendencebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.CreateTeacherRequest;
import org.example.smartattendencebackend.dto.request.UpdateTeacherRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.TeacherResponse;
import org.example.smartattendencebackend.entity.Department;
import org.example.smartattendencebackend.entity.Role;
import org.example.smartattendencebackend.entity.Teacher;
import org.example.smartattendencebackend.exception.DepartmentNotFoundException;
import org.example.smartattendencebackend.exception.TeacherAlreadyExistException;
import org.example.smartattendencebackend.exception.TeacherInUseException;
import org.example.smartattendencebackend.exception.TeacherNotFoundException;
import org.example.smartattendencebackend.repository.DepartmentRepository;
import org.example.smartattendencebackend.repository.TeacherRepository;
import org.example.smartattendencebackend.util.PaginationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Set<String> TEACHER_SORT_FIELDS =
            Set.of("id", "firstName", "lastName", "email", "employeeId");

    @Override
    public TeacherResponse createTeacher(CreateTeacherRequest request) {

        if(teacherRepository.existsByEmail(request.getEmail())){
            throw new TeacherAlreadyExistException("Teacher with email " + request.getEmail() + " already exists");
        }

        if(teacherRepository.existsByEmployeeId(request.getEmployeeId())){
            throw new TeacherAlreadyExistException("Teacher with Employee ID " + request.getEmployeeId() + " already exist");
        }

        // fetch existing department from database
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(()-> new IllegalArgumentException("Department not found"));

        // Request DTO
        Teacher teacher = new Teacher();
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPassword(passwordEncoder.encode(request.getPassword()));
        teacher.setEmployeeId(request.getEmployeeId());
        teacher.setDepartment(department);
        teacher.setRole(Role.TEACHER);

        // save Teacher entity
        Teacher savedTeacher = teacherRepository.save(teacher);

        return mapToResponse(savedTeacher);
    }

    @Override
    public TeacherResponse getTeacherById(Long id) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with this ID" + id));

        return mapToResponse(teacher);
    }

    @Override
    public PagedResponse<TeacherResponse> getAllTeachers(
            int page,
            int size,
            String sortBy,
            String sortDirection
    ) {

        Pageable pageable = PaginationUtils.createPageable(
                page,
                size,
                sortBy,
                sortDirection,
                TEACHER_SORT_FIELDS
        );

        Page<TeacherResponse> teacherPage =
                teacherRepository.findAll(pageable)
                        .map(this::mapToResponse);

        return PaginationUtils.toPagedResponse(teacherPage);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherResponse updateTeacher(Long id, UpdateTeacherRequest request) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with this ID" + id));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with this ID" + request.getDepartmentId()));

        String updatedEmail = request.getEmail().trim();
        String updatedEmployeeId = request.getEmployeeId();

        if(teacherRepository.existsByEmailAndIdNot(updatedEmail , id)){
            throw  new IllegalArgumentException("Email already is use");
        }

        if(teacherRepository.existsByEmployeeIdAndIdNot(updatedEmployeeId , id)){
            throw new IllegalArgumentException("Employee ID is already used by another teacher");
        }

        teacher.setFirstName(request.getFirstName().trim());
        teacher.setLastName(request.getLastName().trim());
        teacher.setEmail(updatedEmail);
        teacher.setEmployeeId(String.valueOf(updatedEmployeeId));
        teacher.setDepartment(department);

        Teacher updatedTeacher = teacherRepository.save(teacher);

        return mapToResponse(updatedTeacher);

    }

    @Override
    @Transactional
    public void deleteTeacher(Long id) {

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with this ID" + id));
        try{
            teacherRepository.delete(teacher);
            teacherRepository.flush();
        } catch(DataIntegrityViolationException exception){
            throw new TeacherInUseException("Teacher cannot be deleted because subjects or attendance records are linked to this teacher");
        }
    }

    private TeacherResponse mapToResponse(Teacher teacher){
        TeacherResponse response = new TeacherResponse();
        response.setId(teacher.getId());
        response.setFirstName(teacher.getFirstName());
        response.setLastName(teacher.getLastName());
        response.setEmail(teacher.getEmail());
        response.setEmployeeId(teacher.getEmployeeId());
        response.setDepartmentName(teacher.getDepartment().getName());

        return response;
    }
}
