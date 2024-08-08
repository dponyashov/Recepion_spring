package ru.dponyashov.exception;

public class NameIsInvalidException extends RuntimeException {
    public NameIsInvalidException(){
        super("Имя не должно быть пустым");
    }
}
