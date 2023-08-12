package ru.practicum.shareit.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message,Object... arg) {
        super(MessageFormat.format(message, arg));
    }

    public static Supplier<NotFoundException> notFoundException(String message) {
        return () -> new NotFoundException(message);
    }
}
