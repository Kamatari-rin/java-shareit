package ru.practicum.shareit.item.repository.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Comparators.orderByCreatedDesc;
import static ru.practicum.shareit.util.Comparators.orderByStartDateAsc;
import static ru.practicum.shareit.util.Comparators.orderByStartDateDesc;

@UtilityClass
public class ItemMapper {

    List<Long> list = new ArrayList<>();

    public Item mapToItem(ItemRequestDto itemRequestDto) {
        return Item.builder()
                .id(itemRequestDto.getId())
                .name(itemRequestDto.getName())
                .description(itemRequestDto.getDescription())
                .available(itemRequestDto.getAvailable())
                .build();
    }

    public ItemShortDto mapToItemShortDto(Item item) {
        return ItemShortDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public ItemResponseDto mapToItemResponseDto(Item item) {
        SortedSet<CommentResponseDto> comments = new TreeSet<>(orderByCreatedDesc);

        if (item.getComments() != null) {
            comments.addAll(item.getComments()
                    .stream()
                    .map(CommentMapper::mapToCommentResponseDto)
                    .collect(Collectors.toSet()));
        }

        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments)
                .build();
    }

    public ItemResponseDto mapToItemResponseWithBookingDto(Item item) {
        LocalDateTime now = LocalDateTime.now();
        ItemResponseDto itemResponseDto = mapToItemResponseDto(item);
        Set<Booking> bookings = item.getBookings();

        Booking lastBooking = bookings.stream()
                .sorted(orderByStartDateDesc)
                .filter(t -> t.getStartBooking().isBefore(now)
                        && t.getStatus().equals(BookingStatus.APPROVED))
                .findFirst()
                .orElse(null);

        Booking nextBooking = bookings.stream()
                .sorted(orderByStartDateAsc)
                .filter(t -> t.getStartBooking().isAfter(now)
                        && t.getStatus().equals(BookingStatus.APPROVED))
                .findFirst()
                .orElse(null);

        itemResponseDto.setLastBooking(BookingMapper.mapToBookingShortDto(lastBooking));
        itemResponseDto.setNextBooking(BookingMapper.mapToBookingShortDto(nextBooking));
        return itemResponseDto;
    }
}
