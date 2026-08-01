package org.example.smartattendencebackend.exception;

public class DepartmentAlreadyExistException extends ResourceAlreadyExistException {

    public DepartmentAlreadyExistException(String message) {
        super(message);
    }
}
