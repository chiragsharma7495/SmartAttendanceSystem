package org.example.smartattendencebackend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class QrAttendanceSessionResponse {

    private Long id;
    private String token;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private Long teacherId;
    private String teacherName;
    private Instant createdAt;
    private Instant expiresAt;
    private boolean active;
}
