package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.AttendanceFilterRequest;
import org.example.smartattendencebackend.dto.request.CreateAttendanceRequest;
import org.example.smartattendencebackend.dto.request.UpdateAttendanceRequest;
import org.example.smartattendencebackend.dto.response.AttendanceResponse;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.StudentAttendanceReportResponse;
import org.example.smartattendencebackend.entity.AttendanceStatus;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {

    public AttendanceResponse createAttendance(CreateAttendanceRequest request);

    AttendanceResponse getAttendanceById(Long id);
    PagedResponse<AttendanceResponse> getAllAttendance(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    );
    public AttendanceResponse updateAttendance(Long id , UpdateAttendanceRequest request);
    public void DeleteAttendance(Long id);

    PagedResponse<AttendanceResponse> getAttendanceByStudent(
            Long studentId,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    );

    PagedResponse<AttendanceResponse> getAttendanceBySubject(
            Long subjectId,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    );

    PagedResponse<AttendanceResponse> getAttendanceByTeacher(
            Long teacherId,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    );

    PagedResponse<AttendanceResponse> getAttendanceByStatus(
            AttendanceStatus status,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    );

    PagedResponse<AttendanceResponse> getAttendanceByDate(
            LocalDate attendanceDate,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    );

    PagedResponse<AttendanceResponse> filterAttendance(
            AttendanceFilterRequest filter,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection
    );

    StudentAttendanceReportResponse getStudentAttendanceReport(Long studentId);
}
