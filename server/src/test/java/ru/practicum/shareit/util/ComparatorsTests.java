package ru.practicum.shareit.util;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentResponseDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparatorsTests {

    @Test
    void orderBookingByStartDateDescTests_whenInvoked_thenReturnZero() {
        Booking bookingOne = Booking.builder().startBooking(LocalDateTime.MIN).build();
        Booking bookingTwo = Booking.builder().startBooking(LocalDateTime.MIN).build();

        assertEquals(0, Comparators.orderBookingByStartDateDesc.compare(bookingOne, bookingTwo));
    }

    @Test
    void orderBookingByStartDateDescTests_whenInvoked_thenReturnOne() {
        Booking bookingOne = Booking.builder().startBooking(LocalDateTime.now().minusDays(1)).build();
        Booking bookingTwo = Booking.builder().startBooking(LocalDateTime.now()).build();

        assertEquals(1, Comparators.orderBookingByStartDateDesc.compare(bookingOne, bookingTwo));
    }

    @Test
    void orderBookingByStartDateDescTests_whenInvoked_thenReturnMinusOne() {
        Booking bookingOne = Booking.builder().startBooking(LocalDateTime.now()).build();
        Booking bookingTwo = Booking.builder().startBooking(LocalDateTime.now().minusDays(1)).build();

        assertEquals(-1, Comparators.orderBookingByStartDateDesc.compare(bookingOne, bookingTwo));
    }

    @Test
    void orderBookingByStartDateAscTests_whenInvoked_thenReturnZero() {
        Booking bookingOne = Booking.builder().startBooking(LocalDateTime.MIN).build();
        Booking bookingTwo = Booking.builder().startBooking(LocalDateTime.MIN).build();

        assertEquals(0, Comparators.orderBookingByStartDateAsc.compare(bookingOne, bookingTwo));
    }

    @Test
    void orderBookingByStartDateAscTests_whenInvoked_thenReturnOne() {
        Booking bookingOne = Booking.builder().startBooking(LocalDateTime.now()).build();
        Booking bookingTwo = Booking.builder().startBooking(LocalDateTime.now().minusDays(1)).build();

        assertEquals(1, Comparators.orderBookingByStartDateAsc.compare(bookingOne, bookingTwo));
    }

    @Test
    void orderBookingByStartDateAscTests_whenInvoked_thenReturnMinusOne() {
        Booking bookingOne = Booking.builder().startBooking(LocalDateTime.now().minusDays(1)).build();
        Booking bookingTwo = Booking.builder().startBooking(LocalDateTime.now()).build();

        assertEquals(-1, Comparators.orderBookingByStartDateAsc.compare(bookingOne, bookingTwo));
    }

    @Test
    void orderCommentByCreatedDescTests_whenInvoked_thenReturnZero() {
        CommentResponseDto commentOne = CommentResponseDto.builder().created(LocalDateTime.MIN).build();
        CommentResponseDto commentTwo = CommentResponseDto.builder().created(LocalDateTime.MIN).build();

        assertEquals(0, Comparators.orderCommentByCreatedDesc.compare(commentOne, commentTwo));
    }

    @Test
    void orderCommentByCreatedDescTests_whenInvoked_thenReturnOne() {
        CommentResponseDto commentOne = CommentResponseDto.builder().created(LocalDateTime.now()).build();
        CommentResponseDto commentTwo = CommentResponseDto.builder().created(LocalDateTime.now().minusDays(1)).build();

        assertEquals(1, Comparators.orderCommentByCreatedDesc.compare(commentOne, commentTwo));
    }

    @Test
    void orderCommentByCreatedDescTests_whenInvoked_thenReturnMinusOne() {
        CommentResponseDto commentOne = CommentResponseDto.builder().created(LocalDateTime.now().minusDays(1)).build();
        CommentResponseDto commentTwo = CommentResponseDto.builder().created(LocalDateTime.now()).build();

        assertEquals(-1, Comparators.orderCommentByCreatedDesc.compare(commentOne, commentTwo));
    }
}
