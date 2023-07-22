package ru.practicum.shareit.util;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentResponseDto;

import java.util.Comparator;

public class Comparators {

    public static final Comparator<Booking> orderByStartDateDesc = (a, b) -> {
        if (a.getStartBooking().isAfter(b.getStartBooking())) {
            return -1;
        } else if (a.getStartBooking().isBefore(b.getStartBooking())) {
            return 1;
        } else {
            return 0;
        }
    };

    public static final Comparator<Booking> orderByStartDateAsc = (a, b) -> {
        if (a.getStartBooking().isAfter(b.getStartBooking())) {
            return 1;
        } else if (a.getStartBooking().isBefore(b.getStartBooking())) {
            return -1;
        } else {
            return 0;
        }
    };

    public static final Comparator<CommentResponseDto> orderByCreatedDesc = (a, b) -> {
        if (a.getCreated().isAfter(b.getCreated())) {
            return 1;
        } else if (a.getCreated().isBefore(b.getCreated())) {
            return -1;
        } else {
            return 0;
        }
    };
}
