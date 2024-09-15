package ru.dponyashov.exception;

public class NotFoundDirectoryElementException extends RuntimeException{
    public NotFoundDirectoryElementException(String message){
        super(message);
    }
}
