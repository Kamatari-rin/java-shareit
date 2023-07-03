package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto item);

    ItemDto getItemById(Long userId, Long itemId);

    List<ItemDto> getAllItemsByUserId(Long userId);

    List<ItemDto> search(Long userId, String text);
}
