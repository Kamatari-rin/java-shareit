package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.UserFoundException.userFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getById(Long id) {
        return UserMapper.mapToUserResponseDto(getUserOrThrowException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto create(User user) {
        try {
            return UserMapper.mapToUserResponseDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует.");
        }
    }

    @Override
    public UserResponseDto update(Long id, User user) {
        User updatedUser = getUserOrThrowException(id);

        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }

        try {
            return UserMapper.mapToUserResponseDto(userRepository.saveAndFlush(updatedUser));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует.");
        }
    }

    @Override
    public void delete(Long id) {
        getUserOrThrowException(id);
        userRepository.deleteById(id);
    }

    private User getUserOrThrowException(Long id) {
        return userRepository.findById(id).orElseThrow(userFoundException(id));
    }
}
