package org.example.smartattendencebackend.repository;

import org.example.smartattendencebackend.entity.QrAttendanceSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QrAttendanceSessionRepository extends JpaRepository<QrAttendanceSession, Long> {

    Optional<QrAttendanceSession> findByToken(String token);

    List<QrAttendanceSession> findBySubjectIdAndTeacherIdAndActiveTrue(Long subjectId, Long teacherId);
}
