package ru.dponyashov.exception;

public class NotFoundRecordException extends RuntimeException {
    public NotFoundRecordException() {
        super("Запись не найдена");
    }
}
