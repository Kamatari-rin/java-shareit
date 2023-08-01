package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    List<Booking> getUserBookings(Long userId, String state, int from, int size);

    List<Booking> getOwnerBookings(Long userId, String state, int from, int size);

    Booking getBookingByIdOwnerOrBookerOnly(Long userId, Long bookingId);

    Booking create(Long itemId, LocalDateTime bookingStart, LocalDateTime bookingEnd, Long userId);

    Booking approveBooking(Long userId, Long bookingId, Boolean approved);

    List<Booking> getBookingByItemIdAndStatusNotInAndStartBookingBefore(Long itemId, BookingStatus status, LocalDateTime date);
}
