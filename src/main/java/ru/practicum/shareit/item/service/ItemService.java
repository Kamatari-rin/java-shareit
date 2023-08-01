package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {

    ItemResponseDto create(ItemRequestDto itemRequestDto);

    ItemResponseDto update(Long itemId, ItemRequestDto itemRequestDto);

    ItemResponseDto getItemById(Long userId, Long itemId);

    List<ItemResponseDto> getAllItemsByUserId(Long userId);

    List<ItemResponseDto> search(Long userId, String text);

    Comment createComment(Long userId, Long itemId, String text);
}
