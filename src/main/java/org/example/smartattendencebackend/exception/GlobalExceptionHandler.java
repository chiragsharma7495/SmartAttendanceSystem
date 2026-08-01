package org.example.smartattendencebackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.example.smartattendencebackend.dto.response.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            StudentNotFoundException.class,
            SubjectNotFoundException.class,
            TeacherNotFoundException.class,
            AttendanceNotFoundException.class,
            DepartmentNotFoundException.class,
            SessionNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex , HttpServletRequest request){
         ErrorResponse errorResponse = new ErrorResponse();
         errorResponse.setTimestamp(LocalDateTime.now());
         errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
         errorResponse.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
         errorResponse.setMessage(ex.getMessage());
         errorResponse.setPath(request.getRequestURI());

         return new ResponseEntity<>(errorResponse , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, Object> errors = new HashMap<>();

        // 1. Collect validation errors into map
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        // 2. Create the response object
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value()); // 400
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase()); // "Bad Request"
        errorResponse.setMessage("Validation failed");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setErrors(errors); // Set validation map

        // 3. Return the response (This is what was missing!)
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceAlreadyExistException.class , DuplicateAttendanceException.class})
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistException(
            ResourceAlreadyExistException ex,
            HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.CONFLICT.value()); // 409
        errorResponse.setError(HttpStatus.CONFLICT.getReasonPhrase()); // "Conflict"
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPaginationException.class )
    public ResponseEntity<ErrorResponse> handleInvalidPaginationException(InvalidPaginationException ex , HttpServletRequest request){
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.CONFLICT.value()); // 409
        errorResponse.setError(HttpStatus.CONFLICT.getReasonPhrase()); // "Conflict"
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(
            {DepartmentInUseException.class, SessionInUseException.class,
            StudentInUseException.class , TeacherInUseException.class,
                    SubjectInUseException.class})
    public ResponseEntity<Map<String, Object>> handleDepartmentInUseException(
            DepartmentInUseException exception
    ) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("status", HttpStatus.CONFLICT.value());
        errorResponse.put("error", HttpStatus.CONFLICT.getReasonPhrase());
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("timestamp", LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException exception, HttpServletRequest request){

        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid Credentials Error",                // 1. ErrorResponse (String)
                LocalDateTime.now(),                        // 2. Timestamp
                HttpStatus.UNAUTHORIZED.value(),             // 3. Status
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),   // 4. Error
                exception.getMessage(),                     // 5. Message
                request.getRequestURI(),                    // 6. Path
                null                                        // 7. Errors (Map<String, Object>)
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    }
}
