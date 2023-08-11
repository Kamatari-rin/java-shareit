package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.Collections;

import static ru.practicum.shareit.util.Constant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> save(@RequestHeader(HEADER_USER_ID) Long userId,
                                       @Validated(OnCreate.class) @RequestBody ItemCreateDto item) {
        return itemClient.save(userId, item);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @Validated(OnUpdate.class) @RequestBody ItemCreateDto item,
                                         @PathVariable Long itemId) {
        return itemClient.update(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(HEADER_USER_ID) Long userId,
                                          @PathVariable Long itemId) {
        return itemClient.getById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUserId(@RequestHeader(HEADER_USER_ID) Long userId,
                                                   @RequestParam(defaultValue = DEFAULT_ELEMENT_INDEX) int from,
                                                   @RequestParam(defaultValue = LIMIT_OF_PAGES_DEFAULT) int size) {
        return itemClient.getItemsByUserId(userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader(HEADER_USER_ID) Long userId,
                                              @PathVariable Long itemId,
                                              @RequestBody CommentRequestDto comment) {
        return itemClient.saveComment(userId, itemId, comment);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @RequestParam(name = "text") String text,
                                         @RequestParam(defaultValue = DEFAULT_ELEMENT_INDEX) @PositiveOrZero int from,
                                         @RequestParam(defaultValue = LIMIT_OF_PAGES_DEFAULT) @Positive int size) {
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return itemClient.search(userId, text, from, size);
    }
}
