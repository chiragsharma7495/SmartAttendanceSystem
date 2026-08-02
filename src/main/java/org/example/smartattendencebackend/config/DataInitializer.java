package org.example.smartattendencebackend.config;

import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.entity.Role;
import org.example.smartattendencebackend.entity.User;
import org.example.smartattendencebackend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createFirstAdmin() {

        return args -> {

            String adminEmail = "admin@attendance.com";

            if (userRepository.existsByEmail(adminEmail)) {
                System.out.println("Admin already exists");
                return;
            }

            User admin = new User();

            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(
                    passwordEncoder.encode("Admin@123")
            );
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);

            System.out.println("First admin created successfully");
        };
    }
}
