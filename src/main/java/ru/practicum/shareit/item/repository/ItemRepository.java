package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item createItem(Item item);

    Item updateItem(Item item);

    Item getItemById(Long userId, Long itemId);

    List<Item> getAllItemsByUserId(Long userId);

    List<Item> searchByText(String text);
}
