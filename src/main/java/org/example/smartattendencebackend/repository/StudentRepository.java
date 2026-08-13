package org.example.smartattendencebackend.repository;

import org.example.smartattendencebackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRollNumber(String rollNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByRollNumberAndIdNot(
            String rollNumber,
            Long id
    );
}
