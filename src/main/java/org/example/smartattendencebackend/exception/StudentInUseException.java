package org.example.smartattendencebackend.exception;

public class StudentInUseException extends RuntimeException{
    public StudentInUseException(String message){
        super(message);
    }

}
