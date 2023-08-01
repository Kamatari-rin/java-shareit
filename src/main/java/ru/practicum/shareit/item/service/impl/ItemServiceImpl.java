package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ItemFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constant.SORT_BY_ID_ASC;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    private final UserService userService;
    private final BookingService bookingService;
    private final RequestService requestService;


    @Override
    public ItemResponseDto create(ItemRequestDto itemRequestDto) {
        final Long requestId = itemRequestDto.getRequestId();
        final Long ownerId = itemRequestDto.getOwnerId();

        User owner = getUserOrThrowException(ownerId);
        Item item = ItemMapper.mapToItem(itemRequestDto);
        item.setOwner(owner);

        if (requestId != null) {
            item.setRequest(getRequestOrThrowException(requestId));
        }

        return ItemMapper.mapToItemResponseDto(itemRepository.save(item));
    }

    @Override
    public ItemResponseDto update(Long itemId, ItemRequestDto itemRequestDto) {
        Item updatedItem = getItemOrThrowException(itemId);

        final Long ownerId = updatedItem.getOwner().getId();
        final Long userId = itemRequestDto.getOwnerId();
        final String userName = itemRequestDto.getName();
        final String description = itemRequestDto.getDescription();

        if (!Objects.equals(ownerId, userId)) {
            throw new ItemFoundException(itemId);
        }

        if (userName != null && !userName.isBlank()) {
            updatedItem.setName(userName);
        }

        if (description != null && !description.isBlank()) {
            updatedItem.setDescription(description);
        }

        if (itemRequestDto.getAvailable() != null) {
            updatedItem.setAvailable(itemRequestDto.getAvailable());
        }

        return ItemMapper.mapToItemResponseDto(itemRepository.saveAndFlush(updatedItem));
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

        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        if (items.get(0).getBookings() != null && Objects.equals(items.get(0).getOwner().getId(), userId)) {
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

        if (bookingService.getUserBookings(userId, "ALL", 0, 1).isEmpty()) {
            throw new NotFoundException("У пользователя нет бронирований.");
        }

        List<Booking> bookings = bookingService.getBookingByItemIdAndStatusNotInAndStartBookingBefore(
                itemId, BookingStatus.REJECTED, LocalDateTime.now());

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
        return UserMapper.mapToUserFromResponseDto(userService.getById(userId));
    }

    private Item getItemOrThrowException(Long itemId) {
        return itemRepository.findByIdWithOwner(itemId).orElseThrow(
                () -> new ItemFoundException(itemId));
    }

    private Request getRequestOrThrowException(Long requestId) {
        return requestService.getOneOrThrowException(requestId);
    }
}
