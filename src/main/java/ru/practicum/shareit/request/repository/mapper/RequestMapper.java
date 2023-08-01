package ru.practicum.shareit.request.repository.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public RequestResponseDto mapToRequestResponseDto(Request request) {
        Set<ItemResponseDto> itemsSet = new HashSet<>();

        if (request.getItems() != null) {
            itemsSet.addAll(request.getItems()
                    .stream()
                    .map(ItemMapper::mapToItemResponseDto)
                    .collect(Collectors.toSet()));
        }

        return RequestResponseDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(itemsSet)
                .build();
    }
}
