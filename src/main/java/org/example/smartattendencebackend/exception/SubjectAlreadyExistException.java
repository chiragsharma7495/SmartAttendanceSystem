package org.example.smartattendencebackend.exception;

public class SubjectAlreadyExistException extends ResourceAlreadyExistException{
    public SubjectAlreadyExistException(String message) {
        super(message);
    }
}
