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

import static ru.practicum.shareit.util.Comparators.orderCommentByCreatedDesc;
import static ru.practicum.shareit.util.Comparators.orderBookingByStartDateAsc;
import static ru.practicum.shareit.util.Comparators.orderBookingByStartDateDesc;

@UtilityClass
public class ItemMapper {

    public Item mapToItem(ItemRequestDto itemRequestDto) {
        return Item.builder()
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
        SortedSet<CommentResponseDto> comments = new TreeSet<>(orderCommentByCreatedDesc);

        if (item.getComments() != null) {
            comments.addAll(item.getComments()
                    .stream()
                    .map(CommentMapper::mapToCommentResponseDto)
                    .collect(Collectors.toSet()));
        }

        Long requestId = null;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }

        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(requestId)
                .comments(comments)
                .build();
    }

    public ItemResponseDto mapToItemResponseWithBookingDto(Item item) {
        LocalDateTime now = LocalDateTime.now();
        ItemResponseDto itemResponseDto = mapToItemResponseDto(item);
        Set<Booking> bookings = item.getBookings();

        Booking lastBooking = bookings.stream()
                .sorted(orderBookingByStartDateDesc)
                .filter(t -> t.getStartBooking().isBefore(now)
                        && t.getStatus().equals(BookingStatus.APPROVED))
                .findFirst()
                .orElse(null);

        Booking nextBooking = bookings.stream()
                .sorted(orderBookingByStartDateAsc)
                .filter(t -> t.getStartBooking().isAfter(now)
                        && t.getStatus().equals(BookingStatus.APPROVED))
                .findFirst()
                .orElse(null);

        itemResponseDto.setLastBooking(BookingMapper.mapToBookingShortDto(lastBooking));
        itemResponseDto.setNextBooking(BookingMapper.mapToBookingShortDto(nextBooking));
        return itemResponseDto;
    }
}
