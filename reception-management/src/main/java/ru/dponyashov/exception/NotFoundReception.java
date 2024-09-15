package ru.dponyashov.exception;

public class NotFoundReception extends RuntimeException{
    public NotFoundReception(){
        super("Запись не найдена");
    }
}
