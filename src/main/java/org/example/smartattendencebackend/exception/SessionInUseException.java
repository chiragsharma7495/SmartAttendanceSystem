package org.example.smartattendencebackend.exception;

import org.example.smartattendencebackend.entity.Session;

public class SessionInUseException extends RuntimeException{
    public SessionInUseException(String message){
        super(message);
    }
}
