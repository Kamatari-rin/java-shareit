package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.mapToUserDto(userRepository.getUserById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь %d не найден.", id))));
    }

    @Override
    public List<UserDto> getAllUser() {
        return userRepository.getAllUser()
                .stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto create(UserDto userDto) {
        Set<String> emails = getAllEmails();

        if (emails.contains(userDto.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует.");
        }

        User newUser = userRepository.create(UserMapper.mapToUser(userDto)).get();
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        User updatedUser = userRepository.getUserById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Пользователь %d не найден.", id)));

        final String userEmail = userRepository.getUserById(id).get().getEmail();
        final String updatedUserEmail = userDto.getEmail();

        Set<String> emails = getAllEmails();

        if (emails.contains(updatedUserEmail) && !userEmail.equals(updatedUserEmail)) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует.");
        }

        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            updatedUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            updatedUser.setEmail(userDto.getEmail());
        }

        return UserMapper.mapToUserDto(userRepository.update(id, updatedUser));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.getUserById(userId);
        userRepository.delete(userId);
    }

    private Set<String> getAllEmails() {
        return userRepository.getAllUser()
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());
    }
}
