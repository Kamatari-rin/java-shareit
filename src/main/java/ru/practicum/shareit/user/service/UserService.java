package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUserById(Long id);

    List<UserDto> getAllUser();

    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    void deleteUser(Long userId);
}
