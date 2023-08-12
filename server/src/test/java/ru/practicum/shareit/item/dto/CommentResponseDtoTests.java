package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentResponseDtoTests {

    @Autowired
    private JacksonTester<CommentResponseDto> commentResponseDtoJacksonTester;

    @Test
    void bookingShortDtoTest() throws IOException {
        CommentResponseDto comment = CommentResponseDto.builder()
                .id(1L)
                .text("test")
                .authorName("name")
                .created(LocalDateTime.MIN)
                .build();

        JsonContent<CommentResponseDto> jsonContent = commentResponseDtoJacksonTester.write(comment);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id")
                        .isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.text")
                .isEqualTo("test");
        assertThat(jsonContent).extractingJsonPathStringValue("$.created")
                .isEqualTo("-999999999-01-01T00:00:00");
        assertThat(jsonContent).extractingJsonPathStringValue("$.authorName")
                .isEqualTo("name");
    }
}
