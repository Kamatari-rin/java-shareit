package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.mapper.RequestMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RequestMapperTests {

    @Test
    void mapToRequestResponseDto() {
        User owner = User.builder().id(1L).email("test@email.com").name("owner").build();
        User requestMaker = User.builder().id(1L).email("test@@email.com").name("requestMaker").build();
        Item item = Item.builder().id(1L).name("item").description("desc").owner(owner).available(true).build();

        List<Item> items = new ArrayList<>();
        items.add(item);

        Request request = Request.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.MIN)
                .created(LocalDateTime.MAX)
                .requestMaker(requestMaker)
                .items(items)
                .build();

        RequestResponseDto requestResponseDto = RequestMapper.mapToRequestResponseDto(request);

        assertThat(requestResponseDto.getId(), equalTo(request.getId()));
        assertThat(requestResponseDto.getDescription(), equalTo(request.getDescription()));
        assertThat(requestResponseDto.getCreated(), equalTo(request.getCreated()));
        assertThat(requestResponseDto.getItems(),
                equalTo(request.getItems()
                        .stream()
                        .map(ItemMapper::mapToItemResponseDto)
                        .collect(Collectors.toList())));
    }
}
