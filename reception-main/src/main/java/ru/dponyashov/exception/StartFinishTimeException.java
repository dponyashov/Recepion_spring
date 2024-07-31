package ru.dponyashov.exception;

public class StartFinishTimeException extends RuntimeException{
    public StartFinishTimeException() {
        super("Дата начала позже даты окончания!");
    }
}
