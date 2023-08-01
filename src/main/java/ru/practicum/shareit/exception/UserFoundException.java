package ru.practicum.shareit.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class UserFoundException extends NotFoundException {
    public UserFoundException(Long id) {
        super(MessageFormat.format("Пользователь {0} не найден.", id));
    }

    public static Supplier<UserFoundException> userFoundException(Long id) {
        return () -> new UserFoundException(id);
    }
}
