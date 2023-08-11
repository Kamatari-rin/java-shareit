package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;

import javax.validation.Valid;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class RequestController {

    private final RequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(HEADER_USER_ID) Long userId,
                                       @Valid @RequestBody RequestCreateDto itemRequest) {
        return new ResponseEntity<>(itemRequestClient.save(userId, itemRequest), HttpStatus.OK);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                     @PathVariable Long requestId) {
        return new ResponseEntity<>(itemRequestClient.getItemRequestsById(userId, requestId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByUserId(@RequestHeader(HEADER_USER_ID) Long userId) {
        return new ResponseEntity<>(itemRequestClient.getItemRequestsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @RequestParam(defaultValue = DEFAULT_ELEMENT_INDEX) int from,
                                         @RequestParam(defaultValue = LIMIT_OF_PAGES_DEFAULT) int size) {
        return new ResponseEntity<>(itemRequestClient.getAll(userId, from, size), HttpStatus.OK);
    }
}
