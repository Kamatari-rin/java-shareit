package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.marker.OnCreate;
import ru.practicum.shareit.marker.OnUpdate;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        log.info("Запрос на получение пользователя {}.", userId);
        return new ResponseEntity<UserDto>(UserMapper.mapToUserDto(
                userService.getById(userId)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUser() {
        log.info("Запрос на получение всех пользователей.");
        return new ResponseEntity<>(userService.getAll()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PostMapping
    @Validated(OnCreate.class)
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Запрос на создание нового пользователя: {}.", userDto.toString());
        User newUser = userService.create(UserMapper.mapToUser(userDto));
        return new ResponseEntity<UserDto>(UserMapper.mapToUserDto(newUser), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId,
                                              @RequestBody @Validated(OnUpdate.class) UserDto userDto) {
        log.info("Запрос на обновление данных пользователя {}. Новые данные пользователя: {}.",
                userId, userDto.toString());
        User updatedUser = userService.update(userId, UserMapper.mapToUser(userDto));
        return new ResponseEntity<UserDto>(UserMapper
                .mapToUserDto(userService.update(userId, updatedUser)), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable Long userId) {
        log.info("Запрос на удаление пользователя {}.", userId);
        userService.delete(userId);
    }
}
