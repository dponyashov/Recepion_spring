package ru.dponyashov.exception;

public class PhoneIsInvalidException extends RuntimeException {
    public PhoneIsInvalidException(){
        super("Телефон должен быть не менее 10 символов");
    }
}
