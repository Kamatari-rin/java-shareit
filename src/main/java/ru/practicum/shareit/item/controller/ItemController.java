package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.mapper.CommentMapper;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
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
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Validated(OnCreate.class)
    public ResponseEntity<ItemResponseDto> create(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                  @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Запрос на создание вещи: [user ID: {}], [Item: {}].", userId, itemRequestDto.toString());
        Item newItem = ItemMapper.mapToItem(itemRequestDto);
        return new ResponseEntity<>(ItemMapper.mapToItemResponseDto(
                itemService.create(userId, newItem)), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> update(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                 @RequestBody @Validated(OnUpdate.class) ItemRequestDto itemRequestDto,
                                                 @PathVariable Long itemId) {
        log.info("Запрос на обновление вещи: [item ID: {}], [Owner ID: {}], [Updated Item: {}].",
                itemId, userId, itemRequestDto.toString());
        Item updatedItem = ItemMapper.mapToItem(itemRequestDto);
        return new ResponseEntity<>(ItemMapper.mapToItemResponseDto(
                itemService.update(userId, itemId, updatedItem)), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                      @PathVariable Long itemId) {
        log.info("Пользователь {} запросил вещь {}.", userId, itemId);
        return new ResponseEntity<>(itemService.getItemById(userId, itemId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItemsByUserId(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId) {
        log.info("Запрос всех вещей пользователя {}.", userId);
        return new ResponseEntity<>(itemService.getAllItemsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDto>> search(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                       @RequestParam String text) {
        log.info("Пользователь {} выполнил поиск по запросу: [{}]", userId, text);
        return new ResponseEntity<>(itemService.search(userId, text), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> createdComment(@RequestHeader(REQUEST_HEADER_USER_ID) Long userId,
                                                             @PathVariable Long itemId,
                                                             @RequestBody CommentRequestDto comment) {
        CommentResponseDto responseComment = CommentMapper.mapToCommentResponseDto(
                itemService.createComment(userId, itemId, comment.getText()));
        return new ResponseEntity<>(responseComment, HttpStatus.OK);
    }
}
