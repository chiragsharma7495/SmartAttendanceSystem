package org.example.smartattendencebackend.exception;

public class TeacherAlreadyExistException extends ResourceAlreadyExistException{

    public TeacherAlreadyExistException(String message){
        super(message);
    }
}
