package org.example.smartattendencebackend.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateDepartmentRequest {

    @Column(nullable = false)
    private String name;
}
