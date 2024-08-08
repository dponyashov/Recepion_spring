package ru.dponyashov.exception;

public class RecordInWorkException extends RuntimeException {
    public RecordInWorkException(){
        super("Запись с такими параметрами уже в работе");
    }
}
