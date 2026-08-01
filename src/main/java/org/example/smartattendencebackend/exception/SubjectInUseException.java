package org.example.smartattendencebackend.exception;

public class SubjectInUseException extends RuntimeException{
    public SubjectInUseException (String message){
        super(message);
    }
}
