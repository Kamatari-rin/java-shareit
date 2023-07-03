package ru.practicum.shareit.exception;

public class ItemFoundException extends NotFoundException {
    public ItemFoundException(Long userId, Long itemId) {
        super(String.format("Пользователь %d, не является владельцем вещи %d.", userId, itemId));
    }
}
