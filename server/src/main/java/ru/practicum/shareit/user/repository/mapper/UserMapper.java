package ru.practicum.shareit.user.repository.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class UserMapper {

    public User mapToUserFromCreateDto(UserCreateDto userCreateDto) {
        return User.builder()
                .name(userCreateDto.getName())
                .email(userCreateDto.getEmail())
                .build();
    }

    public User mapToUserFromResponseDto(UserResponseDto userResponseDto) {
        return User.builder()
                .id(userResponseDto.getId())
                .name(userResponseDto.getName())
                .email(userResponseDto.getEmail())
                .build();
    }

    public UserResponseDto mapToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public UserShortDto mapToUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .build();
    }
}
