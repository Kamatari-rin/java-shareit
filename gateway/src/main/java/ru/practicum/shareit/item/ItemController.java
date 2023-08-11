package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
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
                                       @Validated(OnCreate.class) @RequestBody ItemRequestDto item) {
        return new ResponseEntity<>(itemClient.save(userId, item), HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @Validated(OnUpdate.class) @RequestBody ItemRequestDto item,
                                         @PathVariable Long itemId) {
        return new ResponseEntity<>(itemClient.update(userId, itemId, item), HttpStatus.OK);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(HEADER_USER_ID) Long userId,
                                          @PathVariable Long itemId) {
        return new ResponseEntity<>(itemClient.getById(userId, itemId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsByUserId(@RequestHeader(HEADER_USER_ID) Long userId,
                                                   @RequestParam(defaultValue = DEFAULT_ELEMENT_INDEX) int from,
                                                   @RequestParam(defaultValue = LIMIT_OF_PAGES_DEFAULT) int size) {
        return new ResponseEntity<>(itemClient.getItemsByUserId(userId, from, size), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> saveComment(@RequestHeader(HEADER_USER_ID) Long userId,
                                              @PathVariable Long itemId,
                                              @RequestBody CommentRequestDto comment) {
        return new ResponseEntity<>(itemClient.saveComment(userId, itemId, comment), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @RequestParam(name = "text") String text,
                                         @RequestParam(defaultValue = DEFAULT_ELEMENT_INDEX) @PositiveOrZero int from,
                                         @RequestParam(defaultValue = LIMIT_OF_PAGES_DEFAULT) @Positive int size) {
        if (text.isBlank()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        return new ResponseEntity<>(itemClient.search(userId, text, from, size), HttpStatus.OK);
    }
}
