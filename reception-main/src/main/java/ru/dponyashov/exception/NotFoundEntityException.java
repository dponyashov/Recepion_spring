package ru.dponyashov.exception;

public class NotFoundEntityException extends RuntimeException {
    public NotFoundEntityException(String entityName, String fieldName, String fieldValue){
        super(String.format("%s c %s = %s не найден", entityName, fieldName, fieldValue));
    }
}
