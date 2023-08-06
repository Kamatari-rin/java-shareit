package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.repository.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.ItemRequestFoundException.itemRequestFoundException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final UserService userService;
    private final RequestRepository requestRepository;

    @Override
    public RequestResponseDto create(RequestCreateDto request) {
        User user = getUserOrThrowException(request.getUserId());

        Request newRequest = Request.builder()
                .description(request.getDescription())
                .requestMaker(user)
                .created(LocalDateTime.now())
                .build();

        return RequestMapper.mapToRequestResponseDto(requestRepository.save(newRequest));
    }

    @Override
    public List<RequestResponseDto> getUserRequests(Long userId) {
        getUserOrThrowException(userId);

        return requestRepository.findRequestsByRequestMakerIdOrderByCreatedDesc(userId)
                .stream()
                .map(RequestMapper::mapToRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestResponseDto> getRequests(Long userId, Integer from, Integer limit) {
        User user = getUserOrThrowException(userId);
        Sort sortByCreatedDated = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, limit, sortByCreatedDated);

        return requestRepository.findAllByRequestMakerIsNot(user, page)
                .stream()
                .map(RequestMapper::mapToRequestResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestResponseDto getOne(Long userId, Long requestId) {
        getUserOrThrowException(userId);
        return RequestMapper.mapToRequestResponseDto(
                requestRepository.findById(requestId).orElseThrow(itemRequestFoundException(requestId)));
    }

    @Override
    public Request getOneOrThrowException(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(itemRequestFoundException(requestId));
    }

    private User getUserOrThrowException(Long userId) {
        return UserMapper.mapToUserFromResponseDto(userService.getById(userId));
    }
}
