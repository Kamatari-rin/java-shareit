package ru.practicum.shareit.item.repository.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> itemMap;
    private final Map<Long, Map<Long, Item>> itemsByUsers;
    private Long id;

    public ItemRepositoryImpl() {
        this.itemMap = new HashMap<>();
        this.itemsByUsers = new HashMap<>();
        this.id = 1L;
    }

    @Override
    public Optional<Item> createItem(Item item) {
        item.setId(generatedNewId());

        final Long itemId = item.getId();
        final Long ownerId = item.getOwner().getId();

        itemMap.put(itemId, item);
        itemsByUsers.computeIfAbsent(ownerId, k -> new HashMap<>())
                .put(itemId, item);

        return getItemById(itemId);
    }

    @Override
    public Optional<Item> updateItem(Item item) {
        final Long itemId = item.getId();
        final Long ownerId = item.getOwner().getId();

        itemMap.put(itemId, item);
        itemsByUsers.get(ownerId).put(itemId, item);

        return getItemById(itemId);
    }

    @Override
    public Optional<Item> getItemById(long itemId) {
        return Optional.ofNullable(itemMap.get(itemId));
    }

    @Override
    public List<Item> getAllItemsByUserId(long userId) {
        return List.copyOf(itemsByUsers.get(userId).values());
    }

    @Override
    public List<Item> searchByText(String text) {
        String searchRequest = text.toLowerCase();

        return itemMap
                .values()
                .stream()
                .filter(k -> k.getAvailable()
                        && (k.getDescription().toLowerCase().contains(searchRequest))
                        || k.getName().toLowerCase().contains(searchRequest))
                .collect(Collectors.toList());
    }

    private Long generatedNewId() {
        return id++;
    }
}
