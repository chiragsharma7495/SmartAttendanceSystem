package org.example.smartattendencebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.smartattendencebackend.entity.AttendanceStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private Long id;

    private Long studentId;
    private String studentName;
    private String rollNo;

    private Long subjectId;
    private String subjectName;
    private String SubjectCode;

    private Long teacherId;
    private String teacherName;

    private AttendanceStatus status;

    private LocalDate attendanceDate;
}
