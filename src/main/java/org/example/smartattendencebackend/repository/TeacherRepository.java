package org.example.smartattendencebackend.repository;

import org.example.smartattendencebackend.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByEmail(String email);
    boolean existsByEmployeeId(String employeeId);
    boolean existsByEmailAndIdNot(String email , Long id);
    boolean existsByEmployeeIdAndIdNot(String employeeId , Long id);

    Optional<Teacher> findByEmployeeId(String employeeId);
}
