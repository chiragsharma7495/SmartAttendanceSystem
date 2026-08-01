package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.CreateStudentRequest;
import org.example.smartattendencebackend.dto.request.UpdateStudentRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.StudentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

        StudentResponse createStudent(CreateStudentRequest request);

        StudentResponse getStudentsById(Long id);

        PagedResponse<StudentResponse> getAllStudents(
                int page,
                int size,
                String sortBy,
                String sortDirection
        );

        StudentResponse updateStudents(Long id , UpdateStudentRequest request);

        void deleteStudent(Long id);
}
