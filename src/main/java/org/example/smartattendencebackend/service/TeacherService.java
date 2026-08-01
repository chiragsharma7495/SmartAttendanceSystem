package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.CreateTeacherRequest;
import org.example.smartattendencebackend.dto.request.UpdateTeacherRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.TeacherResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeacherService {

    TeacherResponse createTeacher(CreateTeacherRequest request);

    TeacherResponse getTeacherById(Long id);

    PagedResponse<TeacherResponse> getAllTeachers(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    TeacherResponse updateTeacher(Long id , UpdateTeacherRequest request);

    void deleteTeacher(Long id);
}
