package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class UserIntegrationTests {

    private final UserService userService;
    private final EntityManager entityManager;
    private static User user;

    @BeforeEach
    void init() {
        user = User.builder().name("Test").email("test@email.com").build();
    }

    @Test
    @DirtiesContext
    void getById_whenUserFound_thenReturnUser() {
        userService.create(user);
        UserResponseDto savedUser = userService.getById(1L);
        savedUser.setId(1L);
        assertEquals(savedUser, UserMapper.mapToUserResponseDto(user));
    }

    @Test
    @DirtiesContext
    void getAll_whenUsersFound_thenReturnListOfUser() {
        userService.create(user);
        List<UserResponseDto> users = userService.getAll();
        assertEquals(users.size(), 1);
        user.setId(1L);
        assertEquals(users.get(0), UserMapper.mapToUserResponseDto(user));
    }

    @Test
    @DirtiesContext
    void create_whenUserEmailValid_thenSaveAndReturnUser() {
        userService.create(user);
        User savedUser = entityManager.find(User.class, 1L);
        user.setId(1L);
        assertEquals(savedUser, user);
    }

    @Test
    @DirtiesContext
    void update_whenUserEmailValid_thenUpdateAndReturnUser() {
        userService.create(user);
        user.setEmail("update@email.com");
        userService.update(1L, user);
        User updatedUser = entityManager.find(User.class, 1L);
        assertEquals(updatedUser.getEmail(), "update@email.com");
        assertEquals(updatedUser.getName(), user.getName());
    }

    @Test
    @DirtiesContext
    void delete_whenUserFound_thenDeleteUser() {
        userService.create(user);
        assertEquals(userService.getAll().size(), 1);
        userService.delete(1L);
        assertEquals(userService.getAll().size(), 0);
    }
}
