package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemIntegrationTests {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private final EntityManager entityManager;

    private User userOne;
    private User userTwo;
    private ItemRequestDto requestItem;

    @BeforeEach
    void init() {
        userOne = User.builder()
                .name("Test").email("test@email.com").build();
        userTwo = User.builder()
                .name("usertow").email("email@two.com").build();
        requestItem = ItemRequestDto.builder()
                .ownerId(1L).name("Name").description("description").available(true).build();
    }

    @Test
    @DirtiesContext
    void create_whenItemValid_thenSaveItem() {
        userService.create(userOne);
        itemService.create(requestItem);
        Item actualItem = entityManager.find(Item.class, 1L);
        Item expectedItem = ItemMapper.mapToItem(requestItem);
        expectedItem.setId(1L);
        assertEquals(actualItem, expectedItem);
    }

    @Test
    @DirtiesContext
    void update_whenItemValid_thenUpdateItem() {
        userService.create(userOne);
        itemService.create(requestItem);

        ItemRequestDto updatedItem = ItemRequestDto.builder()
                .ownerId(1L).name("UpdatedName").description("UpdatedDescription").available(true).build();
        itemService.update(1L, updatedItem);

        Item expectedItem = ItemMapper.mapToItem(updatedItem);
        expectedItem.setId(1L);

        Item actualItem = entityManager.find(Item.class, 1L);

        assertEquals(expectedItem, actualItem);
    }

    @Test
    @DirtiesContext
    void getItemById_whenInvoked_thenReturnItem() {
        userService.create(userOne);
        userService.create(userTwo);
        itemService.create(requestItem);

        ItemResponseDto expectedItem = itemService.getItemById(2L, 1L);
        ItemResponseDto actualItem = ItemMapper.mapToItemResponseDto(entityManager.find(Item.class, 1L));
        assertEquals(expectedItem, actualItem);
    }

    @Test
    @DirtiesContext
    void getAllItemsByUserId_whenInvoked_thenReturnAllItemsByUser() {
        userService.create(userOne);
        itemService.create(requestItem);
        List<ItemResponseDto> responseItems = itemService.getAllItemsByUserId(1L);
        assertEquals(responseItems.size(), 1);
    }

    @Test
    @DirtiesContext
    void search_whenInvoked_thenReturnListOfItems() {
        userService.create(userOne);
        itemService.create(requestItem);
        List<ItemResponseDto> items = itemService.search(1L, "description");
        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getDescription(), "description");
    }

    @Test
    @DirtiesContext
    void createComment_whenInvoked_thenSaveComment() {
        userService.create(userOne);
        userService.create(userTwo);
        itemService.create(requestItem);
        bookingService.create(1L, LocalDateTime.now(), LocalDateTime.MAX, 2L);

        itemService.createComment(2L, 1L, "user_comment");

        Comment comment = entityManager.find(Comment.class, 1L);
        assertEquals(comment.getId(), 1L);
        assertEquals(comment.getText(), "user_comment");
    }
}
