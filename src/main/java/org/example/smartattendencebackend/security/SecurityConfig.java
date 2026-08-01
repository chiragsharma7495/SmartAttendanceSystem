package org.example.smartattendencebackend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                ))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint
                        )
                        .accessDeniedHandler(
                                customAccessDeniedHandler
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Public authentication endpoints
                        .requestMatchers("/api/auth/**").permitAll()

                        // Attendance management
                        .requestMatchers("/api/attendance/**")
                        .hasAnyRole("TEACHER", "ADMIN")

                        // Student management
                        .requestMatchers("/api/student/**")
                        .hasAnyRole("TEACHER", "ADMIN")

                        // Subject management
                        .requestMatchers("/api/subject/**")
                        .hasAnyRole("TEACHER", "ADMIN")

                        // Admin-only management
                        .requestMatchers("/api/teacher/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/department/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/session/**")
                        .hasRole("ADMIN")

                        // Everything else needs login
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
