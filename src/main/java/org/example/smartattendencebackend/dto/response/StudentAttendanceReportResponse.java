package org.example.smartattendencebackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAttendanceReportResponse {

    private Long studentId;
    private String studentName;
    private long totalClasses;
    private long presentClasses;
    private long absentClasses;
    private double attendancePercentage;
}
