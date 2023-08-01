package ru.practicum.shareit.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class ItemRequestFoundException extends NotFoundException {

    public ItemRequestFoundException(Long requestId) {
        super(MessageFormat.format("Запрос %d не был найден", requestId));
    }

    public static Supplier<ItemRequestFoundException> itemRequestFoundException(Long id) {
        return () -> new ItemRequestFoundException(id);
    }
}
