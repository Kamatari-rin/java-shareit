package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.mapper.RequestMapper;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemRequestIntegrationTests {

    private final UserService userService;
    private final RequestService itemRequestService;
    private final EntityManager entityManager;

    private User user;
    private RequestCreateDto requestCreateDto;

    private final Long id = 1L;

    @BeforeEach
    void init() {
        user = User.builder().id(1L).name("Name").email("test@email.com").build();
        requestCreateDto = RequestCreateDto.builder().description("test").userId(id).build();
    }

    @Test
    @DirtiesContext
    void create_whenValid_thenSaveItemRequest() {
        userService.create(user);
        RequestResponseDto actualItemRequest = itemRequestService.create(requestCreateDto);
        RequestResponseDto expectedItemRequest = RequestMapper
                .mapToRequestResponseDto(entityManager.find(Request.class, 1L));
        expectedItemRequest.setId(id);
        assertEquals(actualItemRequest, expectedItemRequest);
    }

    @Test
    @DirtiesContext
    void getUserRequests_whenUserIdValid_thenReturnListOfItemRequests() {
        userService.create(user);
        itemRequestService.create(requestCreateDto);
        List<RequestResponseDto> actualItemRequests = itemRequestService.getUserRequests(id);
        assertEquals(actualItemRequests.size(), 1);
    }

    @Test
    @DirtiesContext
    void getRequests_whenArgumentsValid_thenReturnListOfItemRequests() {
        User userTwo = User.builder().name("test").email("test2@email.com").build();
        userService.create(user);
        userService.create(userTwo);
        itemRequestService.create(requestCreateDto);
        List<RequestResponseDto> actualItemRequests = itemRequestService.getRequests(2L, 0, 1);
        assertEquals(actualItemRequests.size(), 1);
    }

    @Test
    @DirtiesContext
    void getOne_whenArgumentsValid_thenReturnItemRequest() {
        userService.create(user);
        itemRequestService.create(requestCreateDto);
        RequestResponseDto actualItemRequest = itemRequestService.getOne(id, id);
        RequestResponseDto expectedItemRequest = RequestMapper
                .mapToRequestResponseDto(entityManager.find(Request.class, 1L));
        expectedItemRequest.setId(id);
        assertEquals(actualItemRequest, expectedItemRequest);
    }
}
