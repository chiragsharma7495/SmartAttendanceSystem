package org.example.smartattendencebackend.repository;

import org.example.smartattendencebackend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByEmail(String email);

    boolean existsByRollNumber(String rollNumber);

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByRollNumberAndIdNot(
            String rollNumber,
            Long id
    );
}
