package org.example.smartattendencebackend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .cors(cors -> {})
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
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        )
                        .permitAll()

                        .requestMatchers("/actuator/health").permitAll()

                        // Attendance
                        .requestMatchers(HttpMethod.GET , "/api/attendance/**")
                        .hasAnyRole("STUDENT" , "TEACHER" , "ADMIN")

                        .requestMatchers(HttpMethod.POST , "/api/attendance/**")
                        .hasAnyRole("TEACHER" , "ADMIN")

                        .requestMatchers(HttpMethod.PUT , "/api/attendance/**")
                        .hasAnyRole("TEACHER" , "ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/attendance/**")
                        .hasRole("ADMIN")

                        // Students
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/student/**"
                        )
                        .hasAnyRole("TEACHER", "ADMIN")

                        .requestMatchers(
                                "/api/student/**"
                        )
                        .hasRole("ADMIN")

                        // Teachers
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/teacher/**"
                        )
                        .hasAnyRole("TEACHER", "ADMIN")

                        .requestMatchers(
                                "/api/teacher/**"
                        )
                        .hasRole("ADMIN")

                        // Subjects
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/subject/**"
                        )
                        .hasAnyRole("STUDENT", "TEACHER", "ADMIN")

                        .requestMatchers(
                                "/api/subject/**"
                        )
                        .hasAnyRole("TEACHER", "ADMIN")

                        // Department and session
                        .requestMatchers("/api/department/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/session/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/api/auth/me").authenticated()

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
