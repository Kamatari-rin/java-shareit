package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ItemRequestFoundException;
import ru.practicum.shareit.exception.UserFoundException;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.impl.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTests {

    @InjectMocks
    private RequestServiceImpl requestService;

    @Mock
    private UserService userService;

    @Mock
    private RequestRepository requestRepository;

    private User user;
    private Request request;
    private RequestCreateDto requestCreateDto;
    private RequestResponseDto requestResponseDto;
    private final Long id = 1L;

    @BeforeEach
    void init() {
        user = User.builder()
                .id(id).name("Name").email("test@email.com").build();
        request = Request.builder()
                .id(id).description("test").created(LocalDateTime.now()).requestMaker(user).build();
        requestCreateDto = RequestCreateDto.builder()
                .userId(id).description("test").build();
        requestResponseDto = RequestResponseDto.builder()
                .id(id).description("test").created(LocalDateTime.now()).items(new ArrayList<>()).build();
    }

    @Test
    void create_whenArgumentsValid_thenSaveItemRequest() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(requestRepository.save(any(Request.class))).thenReturn(request);

        RequestResponseDto actualRequestItem = requestService.create(requestCreateDto);

        assertEquals(actualRequestItem.getId(), requestResponseDto.getId());
        assertEquals(actualRequestItem.getDescription(), requestResponseDto.getDescription());
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void create_whenUserNotFound_thenUserFoundExceptionThrows() {
        when(userService.getById(anyLong())).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> requestService.create(requestCreateDto));
        verify(userService).getById(id);
    }

    @Test
    void getUserRequests_whenArgumentsValid_thenReturnListOfItemRequests() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(requestRepository.findRequestsByRequestMakerIdOrderByCreatedDesc(anyLong())).thenReturn(List.of(request));

        List<RequestResponseDto> actualRequests = requestService.getUserRequests(id);
        assertEquals(actualRequests.size(), 1);
        assertEquals(actualRequests.get(0).getDescription(), "test");
        verify(requestRepository).findRequestsByRequestMakerIdOrderByCreatedDesc(anyLong());
        verify(userService).getById(id);
    }

    @Test
    void getUserRequests_whenUserNotFound_thenUserFoundExceptionThrows() {
        when(userService.getById(anyLong())).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> requestService.getUserRequests(id));
        verify(userService).getById(id);
    }

    @Test
    void getRequests_whenArgumentsValid_thenReturnListOfItemRequests() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(requestRepository.findAllByRequestMakerIsNot(any(User.class), any())).thenReturn(List.of(request));

        List<RequestResponseDto> actualRequests = requestService.getRequests(id, 1, 1);
        assertEquals(actualRequests.size(), 1);
        assertEquals(actualRequests.get(0).getDescription(), "test");
        verify(requestRepository).findAllByRequestMakerIsNot(any(User.class), any());
        verify(userService).getById(id);
    }

    @Test
    void getRequests_whenUserNotFound_thenUserFoundExceptionThrows() {
        when(userService.getById(anyLong())).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> requestService.getRequests(id, 1, 1));
        verify(userService).getById(id);
    }

    @Test
    void getOne_whenArgumentsValid_thenReturnItemRequest() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(request));

        RequestResponseDto actualRequest = requestService.getOne(id, id);
        assertEquals(actualRequest.getDescription(), "test");
        assertEquals(actualRequest.getId(), id);
        verify(userService).getById(id);
    }

    @Test
    void getOne_whenItemRequestNotFound_thenItemRequestFoundExceptionThrows() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(requestRepository.findById(anyLong())).thenThrow(ItemRequestFoundException.class);
        assertThrows(ItemRequestFoundException.class,
                () -> requestService.getOne(id, id));
        verify(userService).getById(id);
        verify(requestRepository).findById(anyLong());
    }

    @Test
    void getOne_whenUserNotFound_thenUserFoundExceptionThrows() {
        when(userService.getById(anyLong())).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> requestService.getOne(id, id));
        verify(userService).getById(id);
    }
}
