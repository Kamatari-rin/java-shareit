package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingResponseDtoTests {

    @Autowired
    private JacksonTester<BookingResponseDto> bookingResponseDtoJacksonTester;

    @Test
    void bookingResponseDtoTest() throws IOException {
        BookingResponseDto booking = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.MIN)
                .end(LocalDateTime.MAX)
                .status(BookingStatus.WAITING)
                .booker(null)
                .build();

        JsonContent<BookingResponseDto> jsonContent = bookingResponseDtoJacksonTester.write(booking);

        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo("+999999999-12-31T23:59:59.999999999");
        assertThat(jsonContent).extractingJsonPathStringValue("$.item")
                .isEqualTo(booking.getItem());
        assertThat(jsonContent).extractingJsonPathStringValue("$.booker")
                .isEqualTo(booking.getBooker());
        assertThat(jsonContent).extractingJsonPathStringValue("$.status")
                .isEqualTo(String.valueOf(booking.getStatus()));
    }
}
