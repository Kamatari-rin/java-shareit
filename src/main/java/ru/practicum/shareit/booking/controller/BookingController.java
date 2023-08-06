package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.ValuesAllowedConstraint;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> getUserBookings(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = FIRST_ELEMENT_INDEX) @PositiveOrZero int from,
            @RequestParam(defaultValue = LIMIT_OF_PAGES) @Positive int size,
            @ValuesAllowedConstraint(propName = "state",
                                     values = {"all",
                                               "current",
                                               "past",
                                               "future",
                                               "waiting",
                                               "rejected"},
                                     message = "Unknown state: UNSUPPORTED_STATUS")
            @RequestParam(defaultValue = "all") String state) {
        List<BookingResponseDto> userBookings = bookingService.getUserBookings(userId, state, from, size)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userBookings, HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingResponseDto>> getOwnerBookings(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = FIRST_ELEMENT_INDEX) @PositiveOrZero int from,
            @RequestParam(defaultValue = LIMIT_OF_PAGES) @Positive int size,
            @ValuesAllowedConstraint(propName = "state",
                                     values = {"all",
                                               "current",
                                               "past",
                                               "future",
                                               "waiting",
                                               "rejected"},
                                     message = "Unknown state: UNSUPPORTED_STATUS")
            @RequestParam(defaultValue = "all") String state) {
        List<BookingResponseDto> ownerBookings = bookingService.getOwnerBookings(userId, state, from, size)
                .stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ownerBookings, HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBookingById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                             @PathVariable Long bookingId) {
        Booking booking = bookingService.getBookingByIdOwnerOrBookerOnly(userId, bookingId);
        return new ResponseEntity<>(BookingMapper.mapToBookingDto(booking), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> create(@RequestHeader(HEADER_USER_ID) Long userId,
                                                     @RequestBody @Valid CreateBookingRequestDto booking) {
        Booking newBooking = bookingService.create(
                booking.getItemId(),
                booking.getStart(),
                booking.getEnd(),
                userId);
        return new ResponseEntity<>(BookingMapper.mapToBookingDto(newBooking), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> approveBooking(@RequestHeader(HEADER_USER_ID) Long userId,
                                                             @PathVariable Long bookingId,
                                                             @RequestParam Boolean approved) {
        Booking booking = bookingService.approveBooking(userId, bookingId, approved);
        return new ResponseEntity<>(BookingMapper.mapToBookingDto(booking), HttpStatus.OK);
    }
}
