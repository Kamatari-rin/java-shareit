package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Long userId, Item item);

    Item update(Long userId, Long itemId, Item item);

    ItemResponseDto getItemById(Long userId, Long itemId);

    List<ItemResponseDto> getAllItemsByUserId(Long userId);

    List<ItemResponseDto> search(Long userId, String text);

    Comment createComment(Long userId, Long itemId, String text);
}
