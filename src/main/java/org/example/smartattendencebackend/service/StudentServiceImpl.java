package org.example.smartattendencebackend.service;

import lombok.*;
import org.example.smartattendencebackend.dto.request.CreateStudentRequest;
import org.example.smartattendencebackend.dto.request.UpdateStudentRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.StudentResponse;
import org.example.smartattendencebackend.entity.Department;
import org.example.smartattendencebackend.entity.Role;
import org.example.smartattendencebackend.entity.Session;
import org.example.smartattendencebackend.entity.Student;
import org.example.smartattendencebackend.exception.*;
import org.example.smartattendencebackend.repository.DepartmentRepository;
import org.example.smartattendencebackend.repository.SessionRepository;
import org.example.smartattendencebackend.repository.StudentRepository;
import org.example.smartattendencebackend.util.PaginationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Set<String> STUDENT_SORT_FIELDS =
            Set.of("id", "firstName", "lastName", "email", "rollNumber");



    @Override
    public StudentResponse createStudent(CreateStudentRequest request) {

        if(studentRepository.existsByEmail(request.getEmail())){
            throw new StudentAlreadyExistException("Student with email " + request.getEmail() + " already exists");
        }

        if(studentRepository.existsByRollNumber(request.getRollNumber())){
            throw new StudentAlreadyExistException("Student with roll number " + request.getRollNumber() + " already exists");
        }

        // 2. Fetch existing Department and Session from database (Example using repositories)

        Department department  = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Session session = sessionRepository.findById(request.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));


    // 3. Request DTO
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setPassword(passwordEncoder.encode(request.getPassword()));
        student.setRollNumber(request.getRollNumber());
        student.setDepartment(department); // set database-managed department
        student.setSession(session);
        student.setRole(Role.STUDENT);

        // Save Entity
            Student savedStudent = studentRepository.save(student);

        return mapToResponse(savedStudent);
    }

    @Override
    public StudentResponse getStudentsById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Studen not found with this ID: " + id));

        return mapToResponse(student);
    }

    @Override
    public PagedResponse<StudentResponse> getAllStudents(
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
                STUDENT_SORT_FIELDS
        );

        Page<StudentResponse> studentPage =
                studentRepository.findAll(pageable)
                        .map(this::mapToResponse);

        return PaginationUtils.toPagedResponse(studentPage);
    }

    @Override
    public StudentResponse updateStudents(Long id, UpdateStudentRequest request) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with this ID : " + id));

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with ID: "
                                        + request.getDepartmentId()
                        )
                );

        Session session = sessionRepository
                .findById(request.getSessionId())
                .orElseThrow(() ->
                        new SessionNotFoundException(
                                "Session not found with ID: "
                                        + request.getSessionId()
                        )
                );

        if(studentRepository.existsByEmailAndIdNot(request.getEmail(), id)){
            throw  new IllegalArgumentException("Email is already in use by other candidate");
        }

        if(studentRepository.existsByRollNumberAndIdNot(request.getRollNumber(), id)){
            throw new IllegalArgumentException("This RollNumber is assigned to another candidate");
        }

        student.setFirstName(request.getFirstName().trim());
        student.setLastName(request.getLastName().trim());
        student.setEmail(request.getEmail().trim());
        student.setRollNumber(request.getRollNumber().trim());
        student.setDepartment(department);
        student.setSession(session);

        Student updatedStudent = studentRepository.save(student);

        return mapToResponse(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with this ID" + id));

        try{
            studentRepository.delete(student);
            studentRepository.flush();
        } catch(DataIntegrityViolationException exception){
            throw new StudentInUseException("Student cannot be deleted because attendance records are linked to this student");
        }
    }

    private StudentResponse mapToResponse(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setEmail(student.getEmail());
        response.setRollNumber(student.getRollNumber());
        if (student.getDepartment() != null) {
            response.setDepartmentName(student.getDepartment().getName());
        }
        if (student.getSession() != null) {
            String sessionString = student.getSession().getStartYear() + "-" + student.getSession().getEndYear();
            response.setSession(sessionString);
        }
        return response;
    }

}
