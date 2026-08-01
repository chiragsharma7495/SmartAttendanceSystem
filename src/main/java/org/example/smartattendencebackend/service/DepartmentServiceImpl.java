package org.example.smartattendencebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.CreateDepartmentRequest;
import org.example.smartattendencebackend.dto.request.UpdateDepartmentRequest;
import org.example.smartattendencebackend.dto.response.AttendanceResponse;
import org.example.smartattendencebackend.dto.response.DepartmentResponse;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.entity.Attendance;
import org.example.smartattendencebackend.entity.Department;
import org.example.smartattendencebackend.exception.DepartmentAlreadyExistException;
import org.example.smartattendencebackend.exception.DepartmentInUseException;
import org.example.smartattendencebackend.exception.DepartmentNotFoundException;
import org.example.smartattendencebackend.repository.DepartmentRepository;
import org.example.smartattendencebackend.util.PaginationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private static final Set<String> DEPARTMENT_SORT_FIELDS =
            Set.of("id", "name");

    @Override
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        // Implementation for creating a department
        //Business Validation

        if(departmentRepository.existsByName(request.getName())) {
            throw new DepartmentAlreadyExistException("Department already exists");
        }

        //RequestDto
        Department department  = new Department();
        department.setName(request.getName());

        // Save Entity
        Department SavedDepartment = departmentRepository.save(department);

        //Response Dto
        DepartmentResponse response = new DepartmentResponse();
        response.setId(SavedDepartment.getId());
        response.setName(SavedDepartment.getName());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department Not found with this ID :" + id));

        return mapToDepartmentResponse(department);
    }


    @Override
    public PagedResponse<DepartmentResponse> getAllDepartments(
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
                DEPARTMENT_SORT_FIELDS
        );

        Page<DepartmentResponse> departmentPage =
                departmentRepository.findAll(pageable)
                        .map(this::mapToDepartmentResponse);

        return PaginationUtils.toPagedResponse(departmentPage);
    }

    @Override
    public DepartmentResponse updateDepartment(Long id, UpdateDepartmentRequest request) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Deaprtment Not Found With this ID : " + id));

        if(departmentRepository.existsByName(request.getName())
            && !department.getName()
                .equalsIgnoreCase(request.getName())){

            throw new DepartmentAlreadyExistException("Department Already ExistException : " + request.getName());
        }

        department.setName(request.getName().trim());

        Department updatedDepartment = departmentRepository.save(department);

        return mapToDepartmentResponse(updatedDepartment);
    }

    @Override
    public void deleteDepartment(Long id) {

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department Not Found With this ID : " + id));

        try{
            departmentRepository.delete(department);
            departmentRepository.flush();
        } catch(DataIntegrityViolationException exception){
            throw new DepartmentInUseException("Department cannot be deleted because it is currently in use");
        }

    }

    private DepartmentResponse mapToDepartmentResponse(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setName(department.getName());
        // set other fields of DepartmentResponse here...
        return response;
    }

}
