package org.example.smartattendencebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.CreateSubjectRequest;

import org.example.smartattendencebackend.dto.request.UpdateSubjectRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.SubjectResponse;
import org.example.smartattendencebackend.entity.Attendance;
import org.example.smartattendencebackend.entity.Department;
import org.example.smartattendencebackend.entity.Subject;
import org.example.smartattendencebackend.entity.Teacher;
import org.example.smartattendencebackend.exception.*;
import org.example.smartattendencebackend.repository.AttendanceRepository;
import org.example.smartattendencebackend.repository.DepartmentRepository;
import org.example.smartattendencebackend.repository.SubjectRepository;
import org.example.smartattendencebackend.repository.TeacherRepository;
import org.example.smartattendencebackend.util.PaginationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;
    private final TeacherRepository teacherRepository;

    private static final Set<String> SUBJECT_SORT_FIELDS =
            Set.of("id", "name", "code");

    @Override
    public SubjectResponse createSubject(CreateSubjectRequest request) {

        if (subjectRepository.existsByCode(request.getCode())) {
            throw new SubjectAlreadyExistException(
                    "Subject with code " + request.getCode() + " already exists"
            );
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department not found with ID: " + request.getDepartmentId()
                        )
                );

        Teacher teacher = teacherRepository.findByEmployeeId(request.getTeacherId())
                .orElseThrow(() ->
                        new TeacherNotFoundException(
                                "Teacher not found with employee ID: " + request.getTeacherId()
                        )
                );

        if (!teacher.getDepartment().getId().equals(department.getId())) {
            throw new IllegalArgumentException(
                    "Teacher does not belong to the selected department"
            );
        }

        Subject subject = new Subject();
        subject.setName(request.getName());
        subject.setCode(request.getCode());
        subject.setDepartment(department);
        subject.setTeacher(teacher);

        Subject savedSubject = subjectRepository.save(subject);

        return mapToResponse(savedSubject);
    }

    @Override
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with this ID " + id));

        return mapToResponse(subject);
    }

    @Override
    public PagedResponse<SubjectResponse> getAllSubjects(
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
                SUBJECT_SORT_FIELDS
        );

        Page<SubjectResponse> subjectPage =
                subjectRepository.findAll(pageable)
                        .map(this::mapToResponse);

        return PaginationUtils.toPagedResponse(subjectPage);
    }

    @Override
    @Transactional
    public SubjectResponse updateSubject(
            Long id,
            UpdateSubjectRequest request
    ) {

        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(
                        "Subject not found with ID: " + id
                ));

        Department department = departmentRepository.findById(
                        request.getDepartmentId()
                )
                .orElseThrow(() -> new DepartmentNotFoundException(
                        "Department not found with ID: "
                                + request.getDepartmentId()
                ));

        Teacher teacher = teacherRepository.findById(
                        request.getTeacherId()
                )
                .orElseThrow(() -> new TeacherNotFoundException(
                        "Teacher not found with ID: "
                                + request.getTeacherId()
                ));

        if (subjectRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new SubjectAlreadyExistException(
                    "Subject with name '" + request.getName() + "' already exists"
            );
        }

        if (subjectRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new SubjectAlreadyExistException(
                    "Subject with code '" + request.getCode() + "' already exists"
            );
        }

        if (!teacher.getDepartment().getId()
                .equals(department.getId())) {

            throw new IllegalArgumentException(
                    "Teacher does not belong to the selected department"
            );
        }

        subject.setName(request.getName());
        subject.setCode(request.getCode());
        subject.setDepartment(department);
        subject.setTeacher(teacher);

        Subject updatedSubject = subjectRepository.save(subject);

        return mapToResponse(updatedSubject);
    }

    @Override
    public void DeleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new SubjectNotFoundException(
                        "Subject not found with ID: " + id
                ));

        try {
            subjectRepository.delete(subject);
            subjectRepository.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new SubjectInUseException(
                    "Subject cannot be deleted because attendance records are linked to it"
            );
        }
    }



    private SubjectResponse mapToResponse(Subject subject) {

        SubjectResponse response = new SubjectResponse();

        response.setId(subject.getId());
        response.setName(subject.getName());
        response.setCode(subject.getCode());
        response.setDepartmentName(subject.getDepartment().getName());
        response.setTeacherName(
                subject.getTeacher().getFirstName()
                        + " "
                        + subject.getTeacher().getLastName()
        );

        return response;
    }
}
