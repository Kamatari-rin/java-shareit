package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ItemFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserFoundException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constant.SORT_BY_ID_ASC;
import static ru.practicum.shareit.util.Constant.SORT_BY_START_DATE_DESC;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Item create(Long userId, Item item) {
        User owner = getUserOrThrowException(userId);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Long userId, Long itemId, Item item) {
        Item updatedItem = getItemOrThrowException(itemId);

        if (!Objects.equals(updatedItem.getOwner().getId(), userId)) {
            throw new ItemFoundException(itemId);
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

        return itemRepository.saveAndFlush(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponseDto getItemById(Long userId, Long itemId) {
        getUserOrThrowException(userId);
        Item item = getItemOrThrowException(itemId);

        if (Objects.equals(item.getOwner().getId(), userId)) {
            return ItemMapper.mapToItemResponseWithBookingDto(item);
        } else {
            return ItemMapper.mapToItemResponseDto(item);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemResponseDto> getAllItemsByUserId(Long userId) {
        getUserOrThrowException(userId);
        List<Item> items = itemRepository.findAllByOwnerIdWithBookings(userId, SORT_BY_ID_ASC);

        if (!items.isEmpty() && Objects.equals(items.get(0).getOwner().getId(), userId)) {
            return items.stream()
                    .map(ItemMapper::mapToItemResponseWithBookingDto)
                    .collect(Collectors.toList());
        } else {
            return items.stream()
                    .map(ItemMapper::mapToItemResponseDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<ItemResponseDto> search(Long userId, String text) {
        getUserOrThrowException(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return itemRepository.search(text)
                .stream()
                .map(ItemMapper::mapToItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment createComment(Long userId, Long itemId, String text) {
        if (text.isBlank()) {
            throw new ValidationException("Комментарий не может быть пуст.");
        }

        User user = getUserOrThrowException(userId);
        Item item = getItemOrThrowException(itemId);

        if (bookingRepository.findByBooker(user, SORT_BY_START_DATE_DESC).isEmpty()) {
            throw new NotFoundException("У пользователя нет бронирований.");
        }

        List<Booking> bookings = bookingRepository.findBookingByItemIdAndStatusNotInAndStartBookingBefore(
                itemId, List.of(BookingStatus.REJECTED), LocalDateTime.now());

        if (bookings == null || bookings.isEmpty()) {
            throw new ValidationException(
                    "Для того что бы оставить комментарий, требуется вначале забронировать вещь.");
        }

        Comment comment = Comment.builder()
                .text(text)
                .created(LocalDateTime.now())
                .item(item)
                .author(user)
                .build();

        return commentRepository.save(comment);
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserFoundException(userId));
    }

    private Item getItemOrThrowException(Long itemId) {
        return itemRepository.findByIdWithOwner(itemId).orElseThrow(
                () -> new ItemFoundException(itemId));
    }
}
