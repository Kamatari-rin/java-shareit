package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CommentRequestDtoTests {

    @Autowired
    private JacksonTester<CommentRequestDto> commentRequestDtoJacksonTester;

    @Test
    void bookingShortDtoTest() throws IOException {
        CommentRequestDto comment = CommentRequestDto.builder().text("test").build();

        JsonContent<CommentRequestDto> jsonContent = commentRequestDtoJacksonTester.write(comment);

        assertThat(jsonContent).extractingJsonPathStringValue("$.text")
                .isEqualTo("test");
    }
}
