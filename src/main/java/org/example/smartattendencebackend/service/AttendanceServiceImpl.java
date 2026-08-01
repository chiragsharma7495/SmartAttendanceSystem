package org.example.smartattendencebackend.service;

import org.example.smartattendencebackend.dto.request.AttendanceFilterRequest;
import org.example.smartattendencebackend.dto.request.StudentAttendanceCountProjection;
import org.example.smartattendencebackend.dto.request.UpdateAttendanceRequest;
import org.example.smartattendencebackend.dto.response.PagedResponse;
import org.example.smartattendencebackend.dto.response.StudentAttendanceReportResponse;
import org.example.smartattendencebackend.entity.*;
import org.example.smartattendencebackend.specifications.AttendanceSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional; // <-- Add this
import lombok.RequiredArgsConstructor;
import org.example.smartattendencebackend.dto.request.CreateAttendanceRequest;
import org.example.smartattendencebackend.dto.response.AttendanceResponse;
import org.example.smartattendencebackend.exception.*;
import org.example.smartattendencebackend.repository.AttendanceRepository;
import org.example.smartattendencebackend.repository.StudentRepository;
import org.example.smartattendencebackend.repository.SubjectRepository;
import org.example.smartattendencebackend.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService{

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    private static final int MAX_PAGE_SIZE = 100;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "attendanceDate",
            "status"
    );


    @Override
    @Transactional
    public AttendanceResponse createAttendance(CreateAttendanceRequest request) {

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + request.getStudentId()));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(()-> new SubjectNotFoundException("Subject with this ID not found" + request.getSubjectId()));

        Teacher teacher = teacherRepository.findById((request.getTeacherId()))
                .orElseThrow(()-> new TeacherNotFoundException("Teacher with this ID not found : " + request.getTeacherId()));

        boolean isDuplicate = attendanceRepository.existsByStudentAndSubjectAndAttendanceDate(student , subject , request.getAttendanceDate());
        if(isDuplicate){
            throw new DuplicateAttendanceException("Attendance already marked for this student, subject, and date.");
        }

        // 5. (Optional) Verify teacher can teach subject
        if (subject.getTeacher() != null && !subject.getTeacher().getId().equals(teacher.getId())) {
            throw new IllegalArgumentException("Teacher is not authorized to teach this subject.");
        }

        // create Attendance Entity
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setSubject(subject);
        attendance.setTeacher(teacher);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());

        // 7. Save
        Attendance savedAttendance = attendanceRepository.save(attendance);

        return mapToResponse(savedAttendance);

    }

    @Override
    public AttendanceResponse getAttendanceById(Long id) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(()-> new AttendanceNotFoundException("Attendance not found with this ID : " + id));

        return mapToResponse(attendance);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AttendanceResponse> getAllAttendance(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection) {

        if (pageNumber < 0) {
            throw new InvalidPaginationException("Page number cannot be negative");
        }

        if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
            throw new InvalidPaginationException("page size must be between 1 and " + MAX_PAGE_SIZE);
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!sortDirection.equalsIgnoreCase("asc")
                && !sortDirection.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Sort direction must be 'asc' or 'desc'"
            );
    }

        Sort sort = sortDirection.equalsIgnoreCase("desc")? Sort.by(sortBy).descending(): Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber , pageSize , sort);
        Page<Attendance> attendancePage = attendanceRepository.findAll(pageable);

        return buildPagedResponse(attendancePage);

    }

    @Override
    @Transactional
    public AttendanceResponse updateAttendance(Long id, UpdateAttendanceRequest request) {
        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(()-> new AttendanceNotFoundException("Attendance not found with this ID : " + id));

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(()-> new StudentNotFoundException("Student with this ID not found : " + request.getStudentId()));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(()-> new TeacherNotFoundException("Teacher not found with this ID : " + request.getTeacherId()));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException("Subject not found with ID: " + request.getSubjectId()));

        boolean isDuplicate = attendanceRepository.existsByStudentAndSubjectAndAttendanceDateAndIdNot(
                student,
                subject,
                request.getAttendanceDate(),
                id
        );
        if (isDuplicate) {
            throw new DuplicateAttendanceException(
                    "Attendance already marked for student: " + student.getFirstName() + " " + student.getLastName() +
                            ", subject: " + subject.getName() +
                            ", date: " + request.getAttendanceDate()
            );
        }

        // Step 2: Teacher Validation
        if (subject.getTeacher() != null && !subject.getTeacher().getId().equals(teacher.getId())) {
            throw new IllegalArgumentException("Teacher is not authorized to teach this subject.");
        }
        // Step 3: Update the Existing Entity (modify fields of the fetched object)
        attendance.setStudent(student);
        attendance.setSubject(subject);
        attendance.setTeacher(teacher);
        attendance.setAttendanceDate(request.getAttendanceDate());
        attendance.setStatus(request.getStatus());

        // Step 4: Save the changes
        Attendance updatedAttendance = attendanceRepository.save(attendance);

        // Step 5: Map and return
        return mapToResponse(updatedAttendance);

    }


    @Override
    @Transactional
    public void DeleteAttendance(Long id) {
        if(!attendanceRepository.existsById(id)){
            throw new AttendanceNotFoundException("Attendance record not found with ID: " + id);
        }
        attendanceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AttendanceResponse> getAttendanceByStudent(Long studentId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with this ID " + studentId));

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Attendance> attendancePage = attendanceRepository.findByStudentId(student.getId(), pageable);

        return buildPagedResponse(attendancePage);

    }

    @Override
    public PagedResponse<AttendanceResponse> getAttendanceBySubject(Long subjectId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new SubjectNotFoundException(
                                "Subject not found with ID: " + subjectId
                        )
                );

        Pageable pageable =
                createPageable(pageNumber, pageSize, sortBy, sortDirection);

        Page<Attendance> attendancePage =
                attendanceRepository.findBySubjectId(subject.getId(), pageable);

        return buildPagedResponse(attendancePage);
    }

    @Override
    public PagedResponse<AttendanceResponse> getAttendanceByTeacher(Long teacherId, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with this ID" + teacherId));

        Pageable pageable = createPageable(pageNumber , pageSize , sortBy , sortDirection);

        Page<Attendance> attendancePage = attendanceRepository.findByTeacherId(teacher.getId() , pageable);

        return buildPagedResponse(attendancePage);
    }

    @Override
    public PagedResponse<AttendanceResponse> getAttendanceByStatus(AttendanceStatus status, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Pageable pageable =
                createPageable(pageNumber, pageSize, sortBy, sortDirection);

        Page<Attendance> attendancePage =
                attendanceRepository.findByStatus(status, pageable);

        return buildPagedResponse(attendancePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AttendanceResponse> getAttendanceByDate(
            LocalDate attendanceDate,
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection) {

        Pageable pageable =
                createPageable(pageNumber, pageSize, sortBy, sortDirection);

        Page<Attendance> attendancePage =
                attendanceRepository.findByAttendanceDate(
                        attendanceDate,
                        pageable
                );

        return buildPagedResponse(attendancePage);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<AttendanceResponse> filterAttendance(AttendanceFilterRequest filter, int pageNumber, int pageSize, String sortBy, String sortDirection) {

        if (filter.getStartDate() != null
                && filter.getEndDate() != null
                && filter.getStartDate().isAfter(filter.getEndDate())) {

            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }

        Pageable pageable = createPageable(pageNumber , pageSize , sortBy, sortDirection);

        Specification<Attendance> specification = AttendanceSpecifications.buildSpecifications(filter);

        Page<Attendance> attendancePage = attendanceRepository.findAll(specification , pageable);

        return buildPagedResponse(attendancePage);
    }

    @Override
    public StudentAttendanceReportResponse getStudentAttendanceReport(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with this ID " + studentId));

        StudentAttendanceCountProjection counts =
                attendanceRepository.getStudentAttendanceCounts(
                        studentId,
                        AttendanceStatus.PRESENT,
                        AttendanceStatus.ABSENT
                );

        long totalClasses = counts.getTotalClasses();
        long presentClasses = counts.getPresentClasses();
        long absentClasses = counts.getAbsentClasses();

        double attendancePercentage = totalClasses == 0
                ? 0.0
                : (presentClasses * 100.0) / totalClasses;

        attendancePercentage =
                Math.round(attendancePercentage * 100.0) / 100.0;

        return StudentAttendanceReportResponse.builder()
                .studentId(student.getId())
                .studentName(
                        student.getFirstName() + " " + student.getLastName()
                )
                .totalClasses(totalClasses)
                .presentClasses(presentClasses)
                .absentClasses(absentClasses)
                .attendancePercentage(attendancePercentage)
                .build();
    }


    // below Methods are helper methods
    private AttendanceResponse mapToResponse(Attendance attendance) {
        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setStudentId(attendance.getStudent().getId());
        response.setStudentName(attendance.getStudent().getFirstName() + " " + attendance.getStudent().getLastName());
        response.setRollNo(attendance.getStudent().getRollNumber());
        response.setSubjectId(attendance.getSubject().getId());
        response.setSubjectName(attendance.getSubject().getName());
        response.setSubjectCode(attendance.getSubject().getCode());
        response.setTeacherId(attendance.getTeacher().getId());
        response.setTeacherName(attendance.getTeacher().getFirstName() + " " + attendance.getTeacher().getLastName());
        response.setAttendanceDate(attendance.getAttendanceDate());
        response.setStatus(attendance.getStatus());
        return response;
    }

    private PagedResponse<AttendanceResponse> buildPagedResponse(
            Page<Attendance> attendancePage) {

        List<AttendanceResponse> content = attendancePage
                .getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return PagedResponse.<AttendanceResponse>builder()
                .content(content)
                .pageNumber(attendancePage.getNumber())
                .pageSize(attendancePage.getSize())
                .totalElements(attendancePage.getTotalElements())
                .totalPages(attendancePage.getTotalPages())
                .first(attendancePage.isFirst())
                .last(attendancePage.isLast())
                .build();
    }

    private Pageable createPageable(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection) {

        validatePagination(pageNumber, pageSize, sortBy, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    private void validatePagination(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDirection) {

        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    "Page size must be between 1 and " + MAX_PAGE_SIZE
            );
        }

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy
            );
        }

        if (!sortDirection.equalsIgnoreCase("asc")
                && !sortDirection.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException(
                    "Sort direction must be either 'asc' or 'desc'"
            );
        }
    }
}
