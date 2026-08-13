package org.example.smartattendencebackend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.ClaimQrAttendanceRequest;
import org.example.smartattendencebackend.dto.request.GenerateQrAttendanceRequest;
import org.example.smartattendencebackend.dto.response.AttendanceResponse;
import org.example.smartattendencebackend.dto.response.QrAttendanceSessionResponse;
import org.example.smartattendencebackend.service.QrAttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qr-attendance")
public class QrAttendanceController {

    private final QrAttendanceService qrAttendanceService;

    @PostMapping("/generate")
    public ResponseEntity<QrAttendanceSessionResponse> generate(
            @Valid @RequestBody GenerateQrAttendanceRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(qrAttendanceService.generate(request, authentication.getName()));
    }

    @PostMapping("/claim")
    public ResponseEntity<AttendanceResponse> claim(
            @Valid @RequestBody ClaimQrAttendanceRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(qrAttendanceService.claim(request, authentication.getName()));
    }

    @PostMapping("/{sessionId}/close")
    public ResponseEntity<QrAttendanceSessionResponse> close(
            @PathVariable Long sessionId,
            Authentication authentication) {
        return ResponseEntity.ok(qrAttendanceService.close(sessionId, authentication.getName()));
    }
}
