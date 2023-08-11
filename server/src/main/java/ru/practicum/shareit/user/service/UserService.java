package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserResponseDto getById(Long id);

    List<UserResponseDto> getAll();

    UserResponseDto create(User user);

    UserResponseDto update(Long id, User user);

    void delete(Long userId);
}
