package org.example.smartattendencebackend.repository;

//import org.hibernate.Session;
import org.example.smartattendencebackend.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

    boolean existsByStartYear(Long startYear);

    boolean existsBySessionName(String name);
}
