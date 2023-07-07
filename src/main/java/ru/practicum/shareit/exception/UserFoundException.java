package ru.practicum.shareit.exception;

public class UserFoundException extends NotFoundException {
    public UserFoundException(Long id) {
        super(String.format("Пользователь %d не найден.", id));
    }
}
