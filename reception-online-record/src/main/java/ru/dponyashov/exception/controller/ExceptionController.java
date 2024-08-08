package ru.dponyashov.exception.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.dponyashov.exception.*;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NameIsInvalidException.class)
    public ResponseEntity<String> nameIsInvalidExceptionHandler(NameIsInvalidException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NotFoundRecordException.class)
    public ResponseEntity<String> notFoundRecordExceptionHandler(NotFoundRecordException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(PhoneIsInvalidException.class)
    public ResponseEntity<String> phoneIsInvalidExceptionHandler(PhoneIsInvalidException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RecordInWorkException.class)
    public ResponseEntity<String> recordInWorkExceptionHandler(RecordInWorkException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> invalidDataExceptionHandler(RecordInWorkException ex){
        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }
}
