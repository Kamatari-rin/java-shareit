package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(userClient.save(user), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Long userId,
                                         @Validated({OnUpdate.class}) @RequestBody UserCreateDto user) {
        return new ResponseEntity<>(userClient.update(user, userId), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        return new ResponseEntity<>(userClient.findById(userId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return new ResponseEntity<>(userClient.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteById(@PathVariable Long userId) {
        try {
            userClient.deleteById(userId);
            return new ResponseEntity<>("Пользователь был успешно удален.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Что-то пошло не так.", HttpStatus.BAD_REQUEST);
        }
    }
}
