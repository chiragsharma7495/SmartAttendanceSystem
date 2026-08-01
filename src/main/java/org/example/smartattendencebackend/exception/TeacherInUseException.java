package org.example.smartattendencebackend.exception;

public class TeacherInUseException extends RuntimeException{
    public TeacherInUseException(String message){
        super(message);
    }
}
