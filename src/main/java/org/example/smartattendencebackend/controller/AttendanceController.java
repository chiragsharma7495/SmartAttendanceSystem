package org.example.smartattendencebackend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.AttendanceFilterRequest;
import org.example.smartattendencebackend.dto.request.CreateAttendanceRequest;
import org.example.smartattendencebackend.dto.request.UpdateAttendanceRequest;
import org.example.smartattendencebackend.dto.response.AttendanceResponse;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.StudentAttendanceReportResponse;
import org.example.smartattendencebackend.entity.AttendanceStatus;
import org.example.smartattendencebackend.repository.AttendanceRepository;
import org.example.smartattendencebackend.service.AttendanceService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/attendance")
@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponse> createAttendance(@Valid @RequestBody CreateAttendanceRequest request) {
        AttendanceResponse response = attendanceService.createAttendance(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<AttendanceResponse> getAttendanceById(@PathVariable Long id) {
        AttendanceResponse response = attendanceService.getAttendanceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<AttendanceResponse>> getAllAttendance(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PagedResponse<AttendanceResponse> response =
                attendanceService.getAllAttendance(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDirection
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponse> updateAttendance(
            @PathVariable Long id,
            @RequestBody UpdateAttendanceRequest request) {

        AttendanceResponse response = attendanceService.updateAttendance(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(@PathVariable Long id) {
        attendanceService.DeleteAttendance(id);
        return ResponseEntity.ok("Attendance record deleted successfully.");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<PagedResponse<AttendanceResponse>> getAttendanceByStudent(
            @PathVariable Long studentId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "Desc") String sortDirection) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByStudent(studentId, pageNumber, pageSize, sortBy, sortDirection)
        );
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<PagedResponse<AttendanceResponse>> getAttendanceBySubject(
            @PathVariable Long subjectId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        PagedResponse<AttendanceResponse> response =
                attendanceService.getAttendanceBySubject(
                        subjectId,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDirection
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<PagedResponse<AttendanceResponse>> getAttendanceByTeacher(
            @PathVariable Long teacherId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByTeacher(
                        teacherId,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<PagedResponse<AttendanceResponse>> getAttendanceByStatus(
            @PathVariable AttendanceStatus status,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByStatus(
                        status,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/date/{attendanceDate}")
    public ResponseEntity<PagedResponse<AttendanceResponse>> getAttendanceByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate attendanceDate,

            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceByDate(
                        attendanceDate,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<PagedResponse<AttendanceResponse>> filterAttendance(
            @ModelAttribute AttendanceFilterRequest filter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "attendanceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        return ResponseEntity.ok(
                attendanceService.filterAttendance(
                        filter,
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDirection
                )
        );
    }

    @GetMapping("/reports/student/{studentId}")
    public ResponseEntity<StudentAttendanceReportResponse> getStudentAttendanceReport(
            @PathVariable Long studentId
    ){
        StudentAttendanceReportResponse response = attendanceService.getStudentAttendanceReport(studentId);
        return ResponseEntity.ok(response);
    }
}
