package org.example.smartattendencebackend.exception;

public class InvalidPaginationException extends RuntimeException{
    public InvalidPaginationException(String message){
        super(message);
    }
}
