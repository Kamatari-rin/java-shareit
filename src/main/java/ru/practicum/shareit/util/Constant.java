package ru.practicum.shareit.util;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentResponseDto;

import java.util.Comparator;

public class Constant {
    public static final String REQUEST_HEADER_USER_ID = "X-Sharer-User-Id";
    public static final Sort SORT_BY_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
    public static final Sort SORT_BY_START_DATE_DESC = Sort.by(Sort.Direction.DESC, "startBooking");

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
