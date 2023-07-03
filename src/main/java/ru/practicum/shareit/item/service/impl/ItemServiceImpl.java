package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemFoundException;
import ru.practicum.shareit.exception.UserFoundException;
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
    public ItemDto create(Long userId, ItemDto itemDto) {
        User owner = userRepository.getUserById(userId);

        Item item = ItemMapper.mapToItem(itemDto);
        item.setOwner(owner);

        return ItemMapper.mapToItemDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto item) {
        Item updatedItem = itemRepository.getItemById(userId, itemId);

        if (!Objects.equals(updatedItem.getOwner().getId(), userId)) {
            throw new ItemFoundException(userId, itemId);
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

        return ItemMapper.mapToItemDto(itemRepository.updateItem(updatedItem));
    }

    @Override
    public ItemDto getItemById(Long userId, Long itemId) {
        userRepository.getUserById(userId);
        return ItemMapper.mapToItemDto(itemRepository.getItemById(userId, itemId));
    }

    @Override
    public List<ItemDto> getAllItemsByUserId(Long userId) {
        userRepository.getUserById(userId);

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
