package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@JsonTest
public class ItemRequestDtoTests {

    @Autowired
    private JacksonTester<ItemRequestDto> itemRequestDtoJacksonTester;

    @Test
    void itemRequestDtoTests() throws IOException {
        ItemRequestDto item = ItemRequestDto.builder()
                .ownerId(1L)
                .name("name")
                .description("description")
                .available(true)
                .requestId(2L)
                .build();

        JsonContent<ItemRequestDto> jsonContent = itemRequestDtoJacksonTester.write(item);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerId")
                .isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name")
                .isEqualTo("name");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(item.getAvailable());
        assertThat(jsonContent).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(2);
    }
}
