package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mvc;

    private UserResponseDto userCreateDtoOne;

    private List<UserResponseDto> users;

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
        userCreateDtoOne = UserResponseDto.builder().id(0L).name("Name_One").email("email.one@test.com").build();
        users = List.of(userCreateDtoOne);
    }

    @Test
    void getUserById_whenInvoked_thenResponseStatusOkWithUserInBody() {
        Long userId = userCreateDtoOne.getId();
        Mockito.when(userService.getById(userId)).thenReturn(userCreateDtoOne);
        ResponseEntity<UserResponseDto> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), userCreateDtoOne);
        verify(userService).getById(userId);
    }

    @Test
    void getAllUser_whenInvoked_thenResponseStatusOkWithUsersListInBody() {
        Mockito.when(userService.getAll()).thenReturn(users);

        ResponseEntity<List<UserResponseDto>> response = userController.getAllUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).size(), 1);
        assertEquals(response.getBody(), users);
        verify(userService).getAll();
    }

    @Test
    void createUser_whenInvoked_thenResponseStatusOkWithUserInBody() {
        UserCreateDto newUser = UserCreateDto.builder().email("email.one@test.com").name("Name_One").build();
        Mockito.when(userService.create(any(User.class))).thenReturn(userCreateDtoOne);
        ResponseEntity<UserResponseDto> response = userController.createUser(newUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getName(), newUser.getName());
        assertEquals(response.getBody().getEmail(), newUser.getEmail());
        verify(userService).create(any(User.class));
    }

    @Test
    void updateUser_whenInvoked_thenResponseStatusOkWithUserInBody() {
        Long userId = userCreateDtoOne.getId();
        UserCreateDto updateUser = UserCreateDto.builder().email("email.one@test.com").name("Name_One").build();
        Mockito.when(userService.update(anyLong(), any(User.class))).thenReturn(userCreateDtoOne);
        ResponseEntity<UserResponseDto> response = userController.updateUser(userId, updateUser);

        assertEquals(Objects.requireNonNull(response.getBody()).getName(), updateUser.getName());
        assertEquals(response.getBody().getEmail(), updateUser.getEmail());
        assertEquals(response.getBody().getId(), userId);
        verify(userService).update(anyLong(), any(User.class));
    }

    @Test
    void deleteById_whenInvoked_thenDeleteUser() throws Exception {
        mvc.perform(delete("/users/{userId}", 1))
                .andExpect(status().isOk());
    }
}