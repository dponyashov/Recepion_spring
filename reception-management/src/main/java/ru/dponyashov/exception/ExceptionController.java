package ru.dponyashov.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NoFoundDirectoryElementException.class)
    public String handlerNoFoundDirectoryElementException(NoFoundDirectoryElementException exception, Model model,
                                                HttpServletResponse response, Locale locale){
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                exception.getMessage());
        return "errors/404";
    }
}
