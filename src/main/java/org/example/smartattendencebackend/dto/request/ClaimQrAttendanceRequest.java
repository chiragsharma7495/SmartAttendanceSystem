package org.example.smartattendencebackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClaimQrAttendanceRequest {

    @NotBlank(message = "QR token is required")
    private String token;
}
