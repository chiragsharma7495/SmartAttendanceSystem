package org.example.smartattendencebackend.exception;

public class SessionAlreadyExistException extends ResourceAlreadyExistException {
    public SessionAlreadyExistException(String message) {
        super(message);
    }
}
