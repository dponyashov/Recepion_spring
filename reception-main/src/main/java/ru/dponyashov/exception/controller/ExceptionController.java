package ru.dponyashov.exception.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.dponyashov.dto.Response;
import ru.dponyashov.exception.MasterIsOccupiedException;
import ru.dponyashov.exception.NotFoundEntityException;
import ru.dponyashov.exception.RoomIsOccupiedException;
import ru.dponyashov.exception.StartFinishTimeException;

import java.util.Locale;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    private final MessageSource messageSource;

    @ExceptionHandler(StartFinishTimeException.class)
    public ResponseEntity<Response> startFinishTimeExceptionHandler(StartFinishTimeException ex){
        return new ResponseEntity<>(
                Response.builder()
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler({RoomIsOccupiedException.class, MasterIsOccupiedException.class})
    public ResponseEntity<Response> isOccupiedHandler(RuntimeException ex){
        return new ResponseEntity<>(
                Response.builder()
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(NotFoundEntityException.class)
    public ResponseEntity<Response> notFoundHandler(NotFoundEntityException ex){
        return new ResponseEntity<>(
                Response.builder()
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handlerBindExceptionMaster(BindException exception, Locale locale){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                messageSource.getMessage("errors.400.title", new Object[0],
                        "errors.400.title", locale)
        );
        problemDetail.setProperty("errors", exception.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .toList());
        return ResponseEntity.badRequest().body(problemDetail);
    }
}
