package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({NotFoundException.class, UserFoundException.class, ItemFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyExistsException(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(final Throwable e) {
        log.info(e.getMessage());
        return Map.of("error", e.getClass().toString());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            MissingServletRequestParameterException.class,
            ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleThrowable(final RuntimeException e) {
        log.info("Получен статус {} {}. Причина: {}",
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage());
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleThrowable(final ConstraintViolationException e) {
        log.debug("Получен статус {} {}. Причина: {}",
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage());
        return Map.of(
                "error", e.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessageTemplate)
                        .findFirst().orElse("No message")
        );
    }
}
