package org.example.smartattendencebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.smartattendencebackend.entity.Role;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String email;
    private Role role;
}
