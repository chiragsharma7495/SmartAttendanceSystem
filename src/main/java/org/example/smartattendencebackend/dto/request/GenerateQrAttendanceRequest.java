package org.example.smartattendencebackend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenerateQrAttendanceRequest {

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Validity is required")
    @Min(value = 1, message = "Validity must be at least 1 minute")
    @Max(value = 120, message = "Validity cannot exceed 120 minutes")
    private Integer validForMinutes = 10;
}
