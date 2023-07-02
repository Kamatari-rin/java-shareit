package ru.practicum.shareit.user.repository.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> userMap;
    private final Set<String> emails;
    private Long id;

    public UserRepositoryImpl() {
        this.userMap = new HashMap<>();
        this.emails = new HashSet<>();
        this.id = 1L;
    }

    @Override
    public Optional<User> create(User user) {
        if (emails.contains(user.getEmail())) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует.");
        }
        user.setId(generatedNewId());
        userMap.put(user.getId(), user);
        emails.add(user.getEmail());
        return getUserById(user.getId());
    }

    @Override
    public User update(Long userId, User user) {
        final String userEmail = userMap.get(userId).getEmail();
        final String updatedUserEmail = user.getEmail();

        if (emails.contains(updatedUserEmail) && !userEmail.equals(updatedUserEmail)) {
            throw new AlreadyExistsException("Пользователь с таким Email уже существует.");
        }

        emails.remove(userEmail);
        emails.add(updatedUserEmail);
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
        emails.remove(userMap.get(id).getEmail());
        userMap.remove(id);
    }

    private Long generatedNewId() {
        return id++;
    }
}
