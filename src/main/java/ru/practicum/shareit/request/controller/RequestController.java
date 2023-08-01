package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.List;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestResponseDto> create(@RequestHeader(HEADER_USER_ID) Long userId,
                                                     @RequestBody @Valid RequestCreateDto request) {
        request.setUserId(userId);
        return new ResponseEntity<>(requestService.create(request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<RequestResponseDto>> getAllRequestsByUserId(
            @RequestHeader(HEADER_USER_ID) Long userId) {
        return new ResponseEntity<>(requestService.getUserRequests(userId), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<RequestResponseDto>> getItemRequests(
            @RequestHeader(HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = "0") @Min(value = 0) @Max(20) int from,
            @RequestParam(defaultValue = "20") @Min(value = 1) @Max(20) int size) {
        return new ResponseEntity<>(requestService.getRequests(userId, from, size), HttpStatus.OK);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<RequestResponseDto> getItemRequestById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                                 @PathVariable Long requestId) {
        return new ResponseEntity<>(requestService.getOne(userId, requestId), HttpStatus.OK);
    }
}
