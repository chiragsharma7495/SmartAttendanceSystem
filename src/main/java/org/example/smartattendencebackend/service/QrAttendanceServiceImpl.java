package org.example.smartattendencebackend.service;

import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.ClaimQrAttendanceRequest;
import org.example.smartattendencebackend.dto.request.CreateAttendanceRequest;
import org.example.smartattendencebackend.dto.request.GenerateQrAttendanceRequest;
import org.example.smartattendencebackend.dto.response.AttendanceResponse;
import org.example.smartattendencebackend.dto.response.QrAttendanceSessionResponse;
import org.example.smartattendencebackend.entity.*;
import org.example.smartattendencebackend.exception.QrAttendanceException;
import org.example.smartattendencebackend.exception.StudentNotFoundException;
import org.example.smartattendencebackend.exception.SubjectNotFoundException;
import org.example.smartattendencebackend.exception.TeacherNotFoundException;
import org.example.smartattendencebackend.repository.QrAttendanceSessionRepository;
import org.example.smartattendencebackend.repository.StudentRepository;
import org.example.smartattendencebackend.repository.SubjectRepository;
import org.example.smartattendencebackend.repository.TeacherRepository;
import org.example.smartattendencebackend.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class QrAttendanceServiceImpl implements QrAttendanceService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final QrAttendanceSessionRepository qrSessionRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final AttendanceService attendanceService;

    @Override
    @Transactional
    public QrAttendanceSessionResponse generate(GenerateQrAttendanceRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Authenticated user was not found."));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException(
                        "Subject not found with ID: " + request.getSubjectId()));

        Teacher teacher;
        if (user.getRole() == Role.ADMIN) {
            teacher = subject.getTeacher();
        } else if (user.getRole() == Role.TEACHER) {
            teacher = teacherRepository.findByEmail(email)
                    .orElseThrow(() -> new TeacherNotFoundException(
                            "Teacher account was not found for: " + email));
            if (!subject.getTeacher().getId().equals(teacher.getId())) {
                throw new AccessDeniedException("You can generate a QR code only for your own subject.");
            }
        } else {
            throw new AccessDeniedException("Only teachers and administrators can generate attendance QR codes.");
        }

        qrSessionRepository.findBySubjectIdAndTeacherIdAndActiveTrue(subject.getId(), teacher.getId())
                .forEach(existing -> existing.setActive(false));

        Instant now = Instant.now();
        QrAttendanceSession qrSession = new QrAttendanceSession();
        qrSession.setToken(generateToken());
        qrSession.setSubject(subject);
        qrSession.setTeacher(teacher);
        qrSession.setCreatedAt(now);
        qrSession.setExpiresAt(now.plus(Duration.ofMinutes(request.getValidForMinutes())));
        qrSession.setActive(true);

        return mapToResponse(qrSessionRepository.save(qrSession));
    }

    @Override
    @Transactional
    public AttendanceResponse claim(ClaimQrAttendanceRequest request, String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student account was not found for: " + email));
        QrAttendanceSession qrSession = qrSessionRepository.findByToken(request.getToken().trim())
                .orElseThrow(() -> new QrAttendanceException("This QR code is invalid."));

        if (!qrSession.isActive()) {
            throw new QrAttendanceException("This QR attendance session has been closed.");
        }
        if (!Instant.now().isBefore(qrSession.getExpiresAt())) {
            throw new QrAttendanceException("This QR code has expired. Ask your teacher to generate a new one.");
        }
        if (!student.getDepartment().getId().equals(qrSession.getSubject().getDepartment().getId())) {
            throw new AccessDeniedException("This QR code is not for your department.");
        }

        CreateAttendanceRequest attendanceRequest = new CreateAttendanceRequest(
                student.getId(),
                qrSession.getSubject().getId(),
                qrSession.getTeacher().getId(),
                LocalDate.now(),
                AttendanceStatus.PRESENT
        );

        return attendanceService.createAttendance(attendanceRequest);
    }

    @Override
    @Transactional
    public QrAttendanceSessionResponse close(Long sessionId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("Authenticated user was not found."));
        QrAttendanceSession qrSession = qrSessionRepository.findById(sessionId)
                .orElseThrow(() -> new QrAttendanceException("QR attendance session was not found."));

        if (user.getRole() != Role.ADMIN && !qrSession.getTeacher().getEmail().equalsIgnoreCase(email)) {
            throw new AccessDeniedException("You can close only your own QR attendance session.");
        }

        qrSession.setActive(false);
        return mapToResponse(qrSession);
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private QrAttendanceSessionResponse mapToResponse(QrAttendanceSession qrSession) {
        Teacher teacher = qrSession.getTeacher();
        Subject subject = qrSession.getSubject();

        return QrAttendanceSessionResponse.builder()
                .id(qrSession.getId())
                .token(qrSession.getToken())
                .subjectId(subject.getId())
                .subjectName(subject.getName())
                .subjectCode(subject.getCode())
                .teacherId(teacher.getId())
                .teacherName(teacher.getFirstName() + " " + teacher.getLastName())
                .createdAt(qrSession.getCreatedAt())
                .expiresAt(qrSession.getExpiresAt())
                .active(qrSession.isActive() && Instant.now().isBefore(qrSession.getExpiresAt()))
                .build();
    }
}
