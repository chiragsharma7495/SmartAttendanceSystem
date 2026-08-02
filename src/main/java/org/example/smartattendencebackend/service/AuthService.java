package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.LoginRequest;
import org.example.smartattendencebackend.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    LoginResponse getCurrentUser(String email);
}
