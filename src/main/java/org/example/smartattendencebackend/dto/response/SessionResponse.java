package org.example.smartattendencebackend.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionResponse {

    private Long id;

    private Long startYear;

    private Long endYear;
}
