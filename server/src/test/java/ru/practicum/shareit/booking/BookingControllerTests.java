package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTests {

    @InjectMocks
    BookingController bookingController;

    @Mock
    BookingService bookingService;
    private Booking booking;
    private CreateBookingRequestDto createBookingRequestDto;
    private User user;
    private Item item;

    @BeforeEach
    void init() {
        user = User.builder().id(1L).name("test").email("test@email.com").build();
        item = Item.builder().id(1L).name("itemName").description("item").available(true).owner(user).build();

        booking = Booking.builder()
                .startBooking(LocalDateTime.MIN)
                .endBooking(LocalDateTime.MAX)
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();

        createBookingRequestDto = CreateBookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.MIN)
                .end(LocalDateTime.MAX)
                .build();
    }

    @Test
    void getUserBookings() {
        when(bookingService.getUserBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));

        ResponseEntity<List<BookingResponseDto>> response = bookingController
                .getUserBookings(1L, 0, 1, "ALL");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).size(), 1);
        verify(bookingService).getUserBookings(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    void getBookingById() {
        Mockito.when(bookingService.getBookingByIdOwnerOrBookerOnly(anyLong(), anyLong())).thenReturn(booking);
        ResponseEntity<BookingResponseDto> response = bookingController.getBookingById(1L, 1L);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getStart(), LocalDateTime.MIN);
        assertEquals(response.getBody().getEnd(), LocalDateTime.MAX);
        assertEquals(response.getBody().getStatus(), BookingStatus.WAITING);
        verify(bookingService).getBookingByIdOwnerOrBookerOnly(anyLong(), anyLong());
    }

    @Test
    void getOwnerBookings() {
        when(bookingService.getOwnerBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(booking));

        ResponseEntity<List<BookingResponseDto>> response = bookingController
                .getOwnerBookings(1L, 0, 1, "ALL");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).size(), 1);
        verify(bookingService).getOwnerBookings(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    void create() {
        when(bookingService.create(anyLong(), any(), any(), anyLong()))
                .thenReturn(booking);

        ResponseEntity<BookingResponseDto> response = bookingController
                .create(1L, createBookingRequestDto);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).getStart(), LocalDateTime.MIN);
        assertEquals(response.getBody().getEnd(), LocalDateTime.MAX);
        assertEquals(response.getBody().getStatus(), BookingStatus.WAITING);
        verify(bookingService).create(anyLong(), any(), any(), anyLong());
    }

    @Test
    void approveBooking() {
        when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(booking);

        ResponseEntity<BookingResponseDto> response = bookingController
                .updateAvailableStatus(1L, 1L, true);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).getStart(), LocalDateTime.MIN);
        assertEquals(response.getBody().getEnd(), LocalDateTime.MAX);
        assertEquals(response.getBody().getStatus(), BookingStatus.WAITING);
        verify(bookingService).approveBooking(anyLong(), anyLong(), anyBoolean());
    }
}
