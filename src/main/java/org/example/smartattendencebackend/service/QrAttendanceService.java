package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.ClaimQrAttendanceRequest;
import org.example.smartattendencebackend.dto.request.GenerateQrAttendanceRequest;
import org.example.smartattendencebackend.dto.response.AttendanceResponse;
import org.example.smartattendencebackend.dto.response.QrAttendanceSessionResponse;

public interface QrAttendanceService {

    QrAttendanceSessionResponse generate(GenerateQrAttendanceRequest request, String email);

    AttendanceResponse claim(ClaimQrAttendanceRequest request, String email);

    QrAttendanceSessionResponse close(Long sessionId, String email);
}
