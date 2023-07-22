package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.List;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {

    @PostMapping
    public ResponseEntity<ItemRequestResponseDto> create(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                         @RequestBody @Valid ItemRequestDto request) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestResponseDto>> getAllRequestsByUserId(
            @RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return null;
    }

    @GetMapping("all")
    public ResponseEntity<List<ItemRequestResponseDto>> getItemRequests(
            @RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
            @RequestParam(defaultValue = FIRST_ELEMENT_INDEX) @PositiveOrZero int firstElementIndex,
            @RequestParam(defaultValue = COUNT_OF_PAGES) @Positive int count) {
        return null;
    }

    @GetMapping("{requestId}")
    public ResponseEntity<ItemRequestResponseDto> getItemrequestById(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                                     @PathVariable Long requestId) {
        return null;
    }
}
