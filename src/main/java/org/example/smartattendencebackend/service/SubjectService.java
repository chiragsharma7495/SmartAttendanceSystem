package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.CreateSubjectRequest;
import org.example.smartattendencebackend.dto.request.UpdateSubjectRequest;
import org.example.smartattendencebackend.dto.response.AttendanceResponse;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.SubjectResponse;
import org.example.smartattendencebackend.entity.Subject;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SubjectService {

    public SubjectResponse createSubject(CreateSubjectRequest request);

    SubjectResponse getSubjectById(Long id);

    PagedResponse<SubjectResponse> getAllSubjects(
            int page,
            int size,
            String sortBy,
            String sortDirection
    );

    SubjectResponse updateSubject(Long id , UpdateSubjectRequest request);

    void DeleteSubject(Long id);
}
