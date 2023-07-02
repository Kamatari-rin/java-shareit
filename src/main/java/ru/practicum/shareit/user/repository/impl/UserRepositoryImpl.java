package ru.practicum.shareit.user.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> userMap;
    private Long id;

    public UserRepositoryImpl() {
        this.userMap = new HashMap<>();
        this.id = 1L;
    }

    @Override
    public Optional<User> create(User user) {
        user.setId(generatedNewId());
        userMap.put(user.getId(), user);
        return getUserById(user.getId());
    }

    @Override
    public User update(Long userId, User user) {
        userMap.put(userId, user);
        return getUserById(userId).get();
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public List<User> getAllUser() {
        return List.copyOf(userMap.values());
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
    }

    private Long generatedNewId() {
        return id++;
    }
}
