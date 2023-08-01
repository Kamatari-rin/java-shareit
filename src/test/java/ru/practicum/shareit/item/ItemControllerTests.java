package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTests {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private ItemResponseDto item;

    @BeforeEach
    void init() {
        item = ItemResponseDto.builder().id(1L).name("name").description("description").available(true).build();
    }

    @Test
    void create_whenInvoked_thenResponseStatusOkWithItemInBody() {
        Mockito.when(itemService.create(any(ItemRequestDto.class))).thenReturn(item);
        ItemRequestDto testItem = ItemRequestDto.builder().name("test").description("test").available(true).build();
        ResponseEntity<ItemResponseDto> response = itemController.create(1L, testItem);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getName(), "name");
        assertEquals(response.getBody().getDescription(), "description");
        verify(itemService).create(any(ItemRequestDto.class));
    }

    @Test
    void update_whenInvoked_thenResponseStatusOkWithItemInBody() {
        ItemRequestDto updatedItem = ItemRequestDto.builder()
                .name("update").description("test").available(true).build();
        Mockito.when(itemService.update(anyLong(), any(ItemRequestDto.class))).thenReturn(item);
        ResponseEntity<ItemResponseDto> response = itemController.update(1L, updatedItem, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getName(), item.getName());
        assertEquals(response.getBody().getDescription(), item.getDescription());
        assertEquals(response.getBody().getAvailable(), item.getAvailable());
        verify(itemService).update(anyLong(), any(ItemRequestDto.class));
    }

    @Test
    void getItemById_whenInvoked_thenResponseStatusOkWithItemInBody() {
        Mockito.when(itemService.getItemById(anyLong(), anyLong())).thenReturn(item);
        ResponseEntity<ItemResponseDto> response = itemController.getItemById(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), item);
        verify(itemService).getItemById(anyLong(), anyLong());
    }

    @Test
    void getAllItemsByUserId_whenInvoked_thenResponseStatusOkWithListOfItemsInBody() {
        Mockito.when(itemService.getAllItemsByUserId(anyLong())).thenReturn(List.of(item));
        ResponseEntity<List<ItemResponseDto>> response = itemController.getAllItemsByUserId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody().size(), 1);
        verify(itemService).getAllItemsByUserId(anyLong());
    }

    @Test
    void search_whenInvoked_thenResponseStatusOkWithListOfItemsInBody() {
        Mockito.when(itemService.search(anyLong(), any(String.class))).thenReturn(List.of(item));
        ResponseEntity<List<ItemResponseDto>> response = itemController.search(1L, "test");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(itemService).search(anyLong(), any(String.class));
    }

    @Test
    void createComment_whenInvoked_thenResponseStatusOkWithItemsInBody() {
        CommentRequestDto commentRequestDto = new CommentRequestDto("test");
        User user = User.builder().id(0L).name("Name_One").email("email.one@test.com").build();
        Item itemForComment = Item.builder().description("test").name("test").build();
        Comment newComment = Comment.builder().item(itemForComment).author(user).text("test").build();

        Mockito.when(itemService.createComment(anyLong(), anyLong(), any(String.class))).thenReturn(newComment);
        ResponseEntity<CommentResponseDto> response = itemController
                .createdComment(1L, 1L, commentRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Objects.requireNonNull(response.getBody()).getText(), "test");
        verify(itemService).createComment(anyLong(), anyLong(), any(String.class));
    }
}
