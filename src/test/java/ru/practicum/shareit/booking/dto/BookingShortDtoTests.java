package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingShortDtoTests {

    @Autowired
    private JacksonTester<BookingShortDto> bookingShortDtoJacksonTester;

    @Test
    void bookingShortDtoTest() throws IOException {
        BookingShortDto booking = BookingShortDto.builder().id(1L).bookerId(2L).build();

        JsonContent<BookingShortDto> jsonContent = bookingShortDtoJacksonTester.write(booking);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(2);
    }
}
