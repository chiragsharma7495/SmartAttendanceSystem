package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.CreateDepartmentRequest;
import org.example.smartattendencebackend.dto.request.UpdateDepartmentRequest;
import org.example.smartattendencebackend.dto.response.DepartmentResponse;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DepartmentService {

   DepartmentResponse createDepartment(CreateDepartmentRequest request);

   DepartmentResponse getDepartmentById(Long id);

   PagedResponse<DepartmentResponse> getAllDepartments(
           int page,
           int size,
           String sortBy,
           String sortDirection
   );

   DepartmentResponse updateDepartment(
           Long id,
           UpdateDepartmentRequest request
   );

   void deleteDepartment(Long id);
}
