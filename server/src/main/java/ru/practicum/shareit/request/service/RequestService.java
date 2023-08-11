package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    RequestResponseDto create(RequestCreateDto request);

    List<RequestResponseDto> getUserRequests(Long userId);

    List<RequestResponseDto> getRequests(Long userId, Integer from, Integer limit);

    RequestResponseDto getOne(Long userId, Long requestId);

    Request getOneOrThrowException(Long requestId);
}
