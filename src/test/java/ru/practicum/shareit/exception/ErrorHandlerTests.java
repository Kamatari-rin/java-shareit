package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.ValidationException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTests {

    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    void handleNotFoundExceptionTest() {
        NotFoundException notFoundException = new NotFoundException("Not found");
        ResponseEntity<Map<String, String>> response = errorHandler.handleNotFoundException(notFoundException);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody().get("error"), "Not found");
    }

    @Test
    void handleUserFoundExceptionTest() {
        NotFoundException notFoundException = new UserFoundException(1L);
        ResponseEntity<Map<String, String>> response = errorHandler.handleNotFoundException(notFoundException);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody().get("error"), "Пользователь 1 не найден.");
    }

    @Test
    void handleItemFoundExceptionTest() {
        NotFoundException notFoundException = new ItemFoundException(1L);
        ResponseEntity<Map<String, String>> response = errorHandler.handleNotFoundException(notFoundException);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody().get("error"), "Вещь 1 не найдена.");
    }

    @Test
    void handleAlreadyExistsExceptionTest() {
        AlreadyExistsException alreadyExistsException = new AlreadyExistsException("Уже существует.");
        ResponseEntity<Map<String, String>> response = errorHandler.handleAlreadyExistsException(alreadyExistsException);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(response.getBody().get("error"), "Уже существует.");
    }

    @Test
    void handleThrowableTest() {
        Throwable throwable = new Throwable("Что-то пошло не так.");
        ResponseEntity<Map<String, String>> response = errorHandler.handleThrowable(throwable);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(response.getBody().get("error"), "Что-то пошло не так.");
    }

    @Test
    void handleValidationExceptionTest() {
        ValidationException validationException = new ValidationException("Ошибка валидации.");
        ResponseEntity<Map<String, String>> response = errorHandler.handleThrowable(validationException);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(response.getBody().get("error"), "Ошибка валидации.");
    }
}
