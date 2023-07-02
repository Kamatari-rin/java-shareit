package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    private Long id;
    private LocalDateTime startBooking;
    private LocalDateTime endBooking;
    private Long itemId;
    private Long userIdWhoReservedItem;
    private Boolean isConfirmed;
}
