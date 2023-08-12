package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;
import ru.practicum.shareit.user.dto.UserCreateDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> save(@Validated({OnCreate.class}) @RequestBody UserCreateDto user) {
        return userClient.save(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId,
                                         @Validated({OnUpdate.class}) @RequestBody UserCreateDto user) {
        return userClient.update(user, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        return userClient.findById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return userClient.findAll();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteById(@PathVariable Long userId) {
        return userClient.deleteById(userId);
    }
}
