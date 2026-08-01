package org.example.smartattendencebackend.exception;

public class DepartmentInUseException extends RuntimeException{
    public DepartmentInUseException(String message){
        super(message);
    }
}
