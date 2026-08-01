package org.example.smartattendencebackend.repository;

import org.example.smartattendencebackend.dto.request.StudentAttendanceCountProjection;
import org.example.smartattendencebackend.entity.Attendance;
import org.example.smartattendencebackend.entity.AttendanceStatus;
import org.example.smartattendencebackend.entity.Student;
import org.example.smartattendencebackend.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance , Long> , JpaSpecificationExecutor<Attendance> {

    boolean existsByStudentAndSubjectAndAttendanceDate(Student student, Subject subject, LocalDate date);
    boolean existsByStudentAndSubjectAndAttendanceDateAndIdNot(
            Student student,
            Subject subject,
            LocalDate date,
            Long id
    );

    Page<Attendance> findByStudentId(Long studentId, Pageable pageable);

    Page<Attendance> findBySubjectId(Long subjectId, Pageable pageable);

    Page<Attendance> findByTeacherId(Long teacherId, Pageable pageable);

    Page<Attendance> findByStatus(
            AttendanceStatus status,
            Pageable pageable
    );

    Page<Attendance> findByAttendanceDate(
            LocalDate attendanceDate,
            Pageable pageable
    );

    Long countByStudentId(Long StudentId);

    long countByStudentIdAndStatus(
            Long studentId,
            AttendanceStatus status
    );

    @Query("""
        SELECT
            COUNT(a.id) AS totalClasses,
            COALESCE(SUM(
                CASE
                    WHEN a.status = :presentStatus THEN 1
                    ELSE 0
                END
            ), 0) AS presentClasses,
            COALESCE(SUM(
                CASE
                    WHEN a.status = :absentStatus THEN 1
                    ELSE 0
                END
            ), 0) AS absentClasses
        FROM Attendance a
        WHERE a.student.id = :studentId
        """)
    StudentAttendanceCountProjection getStudentAttendanceCounts(
            @Param("studentId") Long studentId,
            @Param("presentStatus") AttendanceStatus presentStatus,
            @Param("absentStatus") AttendanceStatus absentStatus
    );
}
