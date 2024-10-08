package ru.dponyashov.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler({NotFoundDirectoryElementException.class, NotFoundReception.class})
    public String handlerNoFoundException(RuntimeException exception, Model model,
                                                          HttpServletResponse response, Locale locale){
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                exception.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(Exception.class)
    public String handlerOtherException(Exception exception, Model model,
                                                          HttpServletResponse response, Locale locale){
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
                "Неизвестная ошибка: " + exception.getMessage());
        return "errors/404";
    }
}
