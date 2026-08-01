package org.example.smartattendencebackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateSubjectRequest {

    @NotBlank(message = "Subject name is required")
    private String name;

    @NotBlank(message = "Subject Code is required")
    private String code;

    @NotNull(message = "DepartmentId is required")
    private Long departmentId;

    @NotNull(message = "TeacherId is required")
    private String teacherId;
}
