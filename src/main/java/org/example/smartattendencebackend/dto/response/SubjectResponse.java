package org.example.smartattendencebackend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubjectResponse {

    private Long id;

    private String name;

    private String code;

    private String departmentName;

    private Long teacherId;

    private String teacherName;
}
