package org.example.smartattendencebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.LoginRequest;
import org.example.smartattendencebackend.dto.response.LoginResponse;
import org.example.smartattendencebackend.entity.User;
import org.example.smartattendencebackend.exception.InvalidCredentialsException;
import org.example.smartattendencebackend.repository.UserRepository;
import org.example.smartattendencebackend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email and password"));

        if(!passwordEncoder.matches(request.getPassword() , user.getPassword())){
            throw new InvalidCredentialsException("Invalid email and password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(
                token , "Bearer", user.getId(), user.getEmail(), user.getRole()
        );
    }
}
