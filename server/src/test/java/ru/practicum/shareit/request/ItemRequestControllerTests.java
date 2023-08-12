package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Constant.HEADER_USER_ID;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTests {

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;


    private MockMvc mvc;
    private RequestResponseDto itemRequest;
    private RequestCreateDto itemRequestCreate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LocalDateTime time = LocalDateTime.now();
    private final Long id = 1L;
    private final String descr = "description";

    @BeforeEach
    void init() {
        itemRequest = RequestResponseDto.builder().id(id).description(descr).created(time).items(new ArrayList<>()).build();
        itemRequestCreate = RequestCreateDto.builder().description(descr).build();
        mvc = MockMvcBuilders.standaloneSetup(requestController).build();

    }

    @Test
    void create_whenInvoked_thenResponseStatusOkWithItemRequestInBody() throws Exception {
        when(requestService.create(any())).thenReturn(itemRequest);

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestCreate))
                        .header(HEADER_USER_ID, 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription()), String.class));
        verify(requestService).create(any(RequestCreateDto.class));
    }

    @Test
    void getAllRequestsByUserId_whenInvoked_thenResponseStatusOkWithListOfItemRequestInBody() throws Exception {
        List<RequestResponseDto> itemRequests = new ArrayList<>();
        itemRequests.add(itemRequest);
        when(requestService.getUserRequests(anyLong()))
                .thenReturn(itemRequests);

        mvc.perform(get("/requests")
                        .header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription()), String.class));
        verify(requestService).getUserRequests(anyLong());
    }

    @Test
    void getItemRequests_whenInvoked_thenResponseStatusOkWithListOfItemRequestInBody() throws Exception {
        when(requestService.getRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequest));

        mvc.perform(get("/requests/all").header(HEADER_USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is(itemRequest.getDescription()), String.class));
        verify(requestService).getRequests(anyLong(), anyInt(), anyInt());
    }

    @Test
    void getItemRequestById_whenInvoked_thenResponseStatusOkWithItemRequestInBody() throws Exception {
        when(requestService.getOne(anyLong(), anyLong()))
                .thenReturn(itemRequest);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header(HEADER_USER_ID, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequest.getDescription()), String.class));
    }
}
