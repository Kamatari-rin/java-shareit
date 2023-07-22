package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User getById(Long id);

    List<User> getAll();

    User create(User user);

    User update(Long id, User user);

    void delete(Long userId);
}
