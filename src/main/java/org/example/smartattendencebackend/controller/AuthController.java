package org.example.smartattendencebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.LoginRequest;
import org.example.smartattendencebackend.dto.request.CreateStudentRequest;
import org.example.smartattendencebackend.dto.request.CreateTeacherRequest;
import org.example.smartattendencebackend.dto.response.LoginResponse;
import org.example.smartattendencebackend.dto.response.StudentResponse;
import org.example.smartattendencebackend.dto.response.TeacherResponse;
import org.example.smartattendencebackend.service.AuthService;
import org.example.smartattendencebackend.service.StudentService;
import org.example.smartattendencebackend.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponse> getCurrentUser(Authentication authentication){
        return ResponseEntity.ok(authService.getCurrentUser(authentication.getName()));
    }

    @PostMapping("/register/student")
    public ResponseEntity<StudentResponse> registerStudent(
            @Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.createStudent(request));
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<TeacherResponse> registerTeacher(
            @Valid @RequestBody CreateTeacherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teacherService.createTeacher(request));
    }
}
