package ru.practicum.shareit.request.repository.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public RequestResponseDto mapToRequestResponseDto(Request request) {
        List<ItemResponseDto> items = new ArrayList<>();

        if (request.getItems() != null) {
            items.addAll(request.getItems()
                    .stream()
                    .map(ItemMapper::mapToItemResponseDto)
                    .collect(Collectors.toList()));
        }

        return RequestResponseDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(items)
                .build();
    }
}
