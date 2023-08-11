package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.validation.ValuesAllowedConstraint;

import javax.validation.Valid;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(HEADER_USER_ID) Long userId,
                                       @Valid @RequestBody CreateBookingRequestDto booking) {
        return new ResponseEntity<>(bookingClient.save(userId, booking), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateAvailableStatus(@RequestHeader(HEADER_USER_ID) Long userId,
                                                        @PathVariable Long bookingId,
                                                        @RequestParam(required = false) Boolean approved) {
        return new ResponseEntity<>(bookingClient.updateAvailableStatus(userId, bookingId, approved), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                 @PathVariable Long bookingId) {
        return new ResponseEntity<>(bookingClient.getBookingById(userId, bookingId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUserId(@RequestHeader(HEADER_USER_ID) Long userId,
                                                      @ValuesAllowedConstraint(
                                                              propName = "state",
                                                              values = {"all",
                                                                        "current",
                                                                        "past",
                                                                        "future",
                                                                        "waiting",
                                                                        "rejected"},
                                                              message = "Unknown state: UNSUPPORTED_STATUS")
                                                      @RequestParam(defaultValue = "all") String state,
                                                      @RequestParam(defaultValue = DEFAULT_ELEMENT_INDEX) int from,
                                                      @RequestParam(defaultValue = LIMIT_OF_PAGES_DEFAULT) int size) {
        return new ResponseEntity<>(bookingClient.getBookingsByUserId(userId, state, from, size), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBooking(@RequestHeader(HEADER_USER_ID) Long userId,
                                                  @ValuesAllowedConstraint(
                                                          propName = "state",
                                                          values = {"all",
                                                                    "current",
                                                                    "past",
                                                                    "future",
                                                                    "waiting",
                                                                    "rejected"},
                                                          message = "Unknown state: UNSUPPORTED_STATUS")
                                                  @RequestParam(defaultValue = "all") String state,
                                                  @RequestParam(defaultValue = DEFAULT_ELEMENT_INDEX) int from,
                                                  @RequestParam(defaultValue = LIMIT_OF_PAGES_DEFAULT) int size) {
        return new ResponseEntity<>(bookingClient.getOwnerBookings(userId, state, from, size), HttpStatus.OK);
    }
}
