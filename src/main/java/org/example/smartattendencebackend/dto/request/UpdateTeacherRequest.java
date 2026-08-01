package org.example.smartattendencebackend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeacherRequest {

    @NotBlank(message = "EmployeeId is required")
    private String employeeId;

    @NotBlank(message = "FirstName is required")
    private String firstName;

    @NotBlank(message = "LastName is required")
    private String lastName;

    @Email(message = "Email must be Unique")
    @NotBlank(message = "Email is necessary")
    private String email;

    @NotNull(message = "TeacherId is required")
    private  String teacherId;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}
