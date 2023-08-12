package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolation(final ConstraintViolationException e) {
        return Map.of(
                "error", e.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessageTemplate)
                        .findFirst().orElse("No message")
        );
    }
}
