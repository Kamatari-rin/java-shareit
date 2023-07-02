package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;

import javax.validation.Valid;

import java.util.List;

import static ru.practicum.shareit.util.Constant.REQUEST_HEADER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Validated(OnCreate.class)
    public ResponseEntity<ItemDto> create(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                          @RequestBody @Valid ItemDto itemDto) {
        return new ResponseEntity<ItemDto>(itemService.create(itemDto, userId), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> update(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                          @RequestBody @Validated(OnUpdate.class) ItemDto itemDto,
                                          @PathVariable Long itemId) {
        return new ResponseEntity<ItemDto>(itemService.update(itemDto, itemId, userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        return new ResponseEntity<>(itemService.getAllItemsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                               @PathVariable Long itemId) {
        return new ResponseEntity<ItemDto>(itemService.getItemById(itemId, userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> search(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                @RequestParam String text) {
        return new ResponseEntity<>(itemService.search(userId, text), HttpStatus.OK);
    }
}
