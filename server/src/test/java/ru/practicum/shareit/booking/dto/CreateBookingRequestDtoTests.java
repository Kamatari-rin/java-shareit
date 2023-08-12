package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CreateBookingRequestDtoTests {

    @Autowired
    private JacksonTester<CreateBookingRequestDto> bookingShortDtoJacksonTester;

    @Test
    void bookingShortDtoTest() throws IOException {
        CreateBookingRequestDto booking = CreateBookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.MIN)
                .end(LocalDateTime.MAX)
                .build();

        JsonContent<CreateBookingRequestDto> jsonContent = bookingShortDtoJacksonTester.write(booking);

        assertThat(jsonContent).extractingJsonPathStringValue("$.start")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.end")
                .isEqualTo("+999999999-12-31T23:59:59.999999999");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(1);
    }
}
