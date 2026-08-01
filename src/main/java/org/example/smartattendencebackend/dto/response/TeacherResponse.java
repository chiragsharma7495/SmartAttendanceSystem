package org.example.smartattendencebackend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TeacherResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String EmployeeId;
    private String departmentName;
}
