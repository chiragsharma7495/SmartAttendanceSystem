package org.example.smartattendencebackend.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String ErrorResponse;

    private LocalDateTime Timestamp;

    private Integer Status;

    private String Error;

    private String Message;

    private String Path;

    private Map<String, Object> Errors;
}
