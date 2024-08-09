package ru.dponyashov.exception;

public class NoFoundDirectoryElementException extends RuntimeException{
    public NoFoundDirectoryElementException(String message){
        super(message);
    }
}
