package org.example.smartattendencebackend.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateSessionRequest {

    @Column(nullable = false)
    private Long startYear;

    @Column(nullable = false)
    private Long endYear;
}
