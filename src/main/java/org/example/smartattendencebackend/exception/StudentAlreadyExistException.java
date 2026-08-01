package org.example.smartattendencebackend.exception;

public class StudentAlreadyExistException extends ResourceAlreadyExistException {
    public StudentAlreadyExistException(String message) {
        super(message);
    }
}
