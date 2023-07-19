package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.UserFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserFoundException(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует.");
        }
    }

    @Override
    public User update(Long id, User user) {
        User updatedUser = userRepository.findById(id).orElseThrow(
                () -> new UserFoundException(id));

        if (user.getName() != null && !user.getName().isBlank()) {
            updatedUser.setName(user.getName());
        }
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            updatedUser.setEmail(user.getEmail());
        }

        try {
            return userRepository.saveAndFlush(updatedUser);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует.");
        }
    }

    @Override
    public void delete(Long id) {
        isUserExist(id);
        userRepository.deleteById(id);
    }

    private void isUserExist(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new UserFoundException(id));
    }
}
