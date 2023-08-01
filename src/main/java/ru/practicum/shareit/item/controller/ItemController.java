package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.repository.mapper.CommentMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;

import javax.validation.Valid;

import java.util.List;

import static ru.practicum.shareit.util.Constant.HEADER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Validated(OnCreate.class)
    public ResponseEntity<ItemResponseDto> create(@RequestHeader(HEADER_USER_ID) Long userId,
                                                  @RequestBody @Valid ItemRequestDto itemRequestDto) {
        itemRequestDto.setOwnerId(userId);
        return new ResponseEntity<>(itemService.create(itemRequestDto), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> update(@RequestHeader(HEADER_USER_ID) Long userId,
                                                 @RequestBody @Validated(OnUpdate.class) ItemRequestDto itemRequestDto,
                                                 @PathVariable Long itemId) {
        itemRequestDto.setOwnerId(userId);
        return new ResponseEntity<>(itemService.update(itemId, itemRequestDto), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@RequestHeader(HEADER_USER_ID) Long userId,
                                                      @PathVariable Long itemId) {
        return new ResponseEntity<>(itemService.getItemById(userId, itemId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> getAllItemsByUserId(@RequestHeader(HEADER_USER_ID) Long userId) {
        return new ResponseEntity<>(itemService.getAllItemsByUserId(userId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemResponseDto>> search(@RequestHeader(HEADER_USER_ID) Long userId,
                                                       @RequestParam String text) {
        return new ResponseEntity<>(itemService.search(userId, text), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentResponseDto> createdComment(@RequestHeader(HEADER_USER_ID) Long userId,
                                                             @PathVariable Long itemId,
                                                             @RequestBody CommentRequestDto comment) {
        CommentResponseDto responseComment = CommentMapper.mapToCommentResponseDto(
                itemService.createComment(userId, itemId, comment.getText()));
        return new ResponseEntity<>(responseComment, HttpStatus.OK);
    }
}
