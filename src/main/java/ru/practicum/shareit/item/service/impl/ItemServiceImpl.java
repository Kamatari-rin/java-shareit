package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto itemDto, Long userId) {
        User owner = userRepository.getUserById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь %d не найден.", userId)));

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(owner);

        return ItemMapper.mapToItemDto(itemRepository.createItem(item).get());
    }

    @Override
    public ItemDto update(ItemDto item, Long itemId, Long userId) {
        Item updatedItem = itemRepository.getItemById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь не найдена: %s.", item)));

        if (!Objects.equals(updatedItem.getOwner().getId(), userId)) {
            throw new ItemNotFoundException(String
                    .format("Пользователь %d, не является владельцем вещи %s.", userId, item));
        }

        if (item.getName() != null && !item.getName().isBlank()) {
            updatedItem.setName(item.getName());
        }

        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updatedItem.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }

        return ItemMapper.mapToItemDto(itemRepository.updateItem(updatedItem).get());
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {
        userRepository.getUserById(userId);
        final ItemDto item = ItemMapper.mapToItemDto(itemRepository.getItemById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Вещь не найдена: %s.", itemId))));
        return item;
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        userRepository.getUserById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь %d не был найден.", userId)));

        return itemRepository.getAllItemsByUserId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(Long userId, String text) {
        userRepository.getUserById(userId);

        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.searchByText(text)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }
}
