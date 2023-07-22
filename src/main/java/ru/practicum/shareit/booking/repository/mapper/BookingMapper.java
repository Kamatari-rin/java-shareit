package ru.practicum.shareit.booking.repository.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.user.repository.mapper.UserMapper;

@UtilityClass
public class BookingMapper {

    public BookingResponseDto mapToBookingDto(Booking booking) {
        return BookingResponseDto.builder()
                .id(booking.getId())
                .start(booking.getStartBooking())
                .end(booking.getEndBooking())
                .status(booking.getStatus())
                .booker(UserMapper.mapToUserShortDto(booking.getBooker()))
                .item(ItemMapper.mapToItemShortDto(booking.getItem()))
                .build();
    }

    public BookingShortDto mapToBookingShortDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return BookingShortDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker() != null ? booking.getBooker().getId() : null)
                .build();
    }
}
