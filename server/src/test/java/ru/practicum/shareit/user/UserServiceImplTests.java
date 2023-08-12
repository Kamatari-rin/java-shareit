package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.AlreadyExistsException;
import ru.practicum.shareit.exception.UserFoundException;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    private User user;
    private final Long userId = 0L;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(userId)
                .name("Test_name")
                .email("test@email.com")
                .build();
    }

    @Test
    void getById_whenUserFound_thenReturnedUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponseDto expectedUser = UserMapper.mapToUserResponseDto(user);
        UserResponseDto actualUser = userService.getById(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getById_whenUserNotFound_thenUserNotFoundExceptionThrown() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(UserFoundException.class,
                () -> userService.getById(userId));
    }

    @Test
    void getAll_whenUsersFound_thenReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        assertEquals(userService.getAll().size(), 1);
        verify(userRepository).findAll();
    }

    @Test
    void getAll_whenUserNotFound_thenReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(userService.getAll().size(), 0);
    }

    @Test
    void create_whenUserEmailValid_thenSavedUser() {
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDto expectedUser = UserMapper.mapToUserResponseDto(user);
        UserResponseDto actualUser = userService.create(user);

        assertEquals(expectedUser, actualUser);
        verify(userRepository).save(user);
    }

    @Test
    void create_whenUserEmailNotValid_thenNotSaveUser() {
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);
        assertThrows(AlreadyExistsException.class,
                () -> userService.create(user));
        verify(userRepository).save(user);
    }

    @Test
    void delete_whenDelete_thenDelete() {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void update_whenUserFound_thenUpdatedOnlyAvailableFields() {
        User updatedUser = User.builder().id(0L).name("Newname").email("new@email.com").build();

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(updatedUser);

        UserResponseDto actualUser = userService.update(userId, updatedUser);

        verify(userRepository).saveAndFlush(userArgumentCaptor.capture());
        UserResponseDto savedUser = UserMapper.mapToUserResponseDto(userArgumentCaptor.getValue());

        assertEquals(actualUser, savedUser);
    }

    @Test
    void update_whenUserEmailNotValid_thenAlreadyExistsException() {
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));
        when(userRepository.saveAndFlush(any(User.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(AlreadyExistsException.class,
                () -> userService.update(userId, user));
        verify(userRepository).saveAndFlush(user);
    }
}
