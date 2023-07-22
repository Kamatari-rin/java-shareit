package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    List<Booking> getUserBookings(Long userId, String state);

    List<Booking> getOwnerBookings(Long userId, String state);

    Booking getBookingByIdOwnerOrBookerOnly(Long userId, Long bookingId);

    Booking create(Long itemId, LocalDateTime bookingStart, LocalDateTime bookingEnd, Long userId);

    Booking approveBooking(Long userId, Long bookingId, Boolean approved);
}
