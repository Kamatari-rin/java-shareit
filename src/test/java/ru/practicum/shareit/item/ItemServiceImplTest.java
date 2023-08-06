package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ItemFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserFoundException;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.repository.mapper.ItemMapper;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Mock
    private BookingService bookingService;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    private Item item;
    private ItemRequestDto itemRequestDto;
    private User user;
    private UserResponseDto userResponseDto;
    private Booking booking;
    private Comment comment;
    private final Long id = 1L;

    @BeforeEach
    void init() {
        user = User.builder().id(id).name("Name").email("test@email.com").build();
        userResponseDto = UserResponseDto.builder().id(id).name("User_DTO").email("dto@user.com").build();

        booking = Booking.builder()
                .id(id)
                .startBooking(LocalDateTime.now())
                .endBooking(LocalDateTime.MAX)
                .item(item)
                .status(BookingStatus.APPROVED)
                .build();

        HashSet<Booking> bookings = new HashSet<>();
        bookings.add(booking);

        item = Item.builder().bookings(bookings)
                .id(id).name("Item_Name").available(true).description("Item_description").owner(user).build();
        itemRequestDto = ItemRequestDto.builder()
                .ownerId(id).name("DTO_Item").description("DTO_description").available(true).build();
        comment = Comment.builder().id(id).text("comment text").item(item).author(user).build();
    }

    @Test
    void create_whenItemValid_thenSaveItem() {
        when(userService.getById(id)).thenReturn(userResponseDto);
        when(itemRepository.save(any())).thenReturn(item);

        ItemResponseDto actualItem = itemService.create(itemRequestDto);
        assertEquals(actualItem.getName(), "Item_Name");
        assertEquals(actualItem.getDescription(), "Item_description");
        assertEquals(actualItem.getAvailable(), true);
        verify(itemRepository).save(any());
    }

    @Test
    void create_whenOwnerNotFound_thenUserFoundExceptionThrown() {
        when(userService.getById(id)).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> itemService.create(itemRequestDto));
        verify(userService).getById(any());
        verify(itemRepository, never()).save(any());
    }

    @Test
    void update_whenItemValid_thenUpdateItem() {
        Item updateItem = Item.builder().id(id).name("Item_update").description("description_update").available(true).build();

        ItemRequestDto updatedItemRequest = ItemRequestDto.builder()
                .ownerId(id).name("Item_update").description("description_update").available(true).build();

        when(itemRepository.findByIdWithOwner(anyLong())).thenReturn(Optional.ofNullable(item));
        when(itemRepository.saveAndFlush(any())).thenReturn(updateItem);

        ItemResponseDto actualItem = itemService.update(id, updatedItemRequest);

        verify(itemRepository).saveAndFlush(itemArgumentCaptor.capture());
        ItemResponseDto savedItem = ItemMapper.mapToItemResponseDto(itemArgumentCaptor.getValue());
        assertEquals(actualItem, savedItem);
    }

    @Test
    void update_whenItemNotFound_thenItemFoundExceptionThrown() {
        when(itemRepository.findByIdWithOwner(anyLong())).thenThrow(ItemFoundException.class);
        assertThrows(ItemFoundException.class,
                () -> itemService.update(id, itemRequestDto));
        verify(itemRepository, never()).saveAndFlush(any());
    }

    @Test
    void getItemById_whenItemFound_thenReturnItem() {
       when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
       when(itemRepository.findByIdWithOwner(anyLong())).thenReturn(Optional.ofNullable(item));

       ItemResponseDto actualItem = itemService.getItemById(id, id);
       ItemResponseDto expectedItem = ItemMapper.mapToItemResponseWithBookingDto(item);
       assertEquals(actualItem, expectedItem);
       verify(itemRepository).findByIdWithOwner(id);
       verify(userService).getById(id);
    }

    @Test
    void getItemById_whenItemNotFound_thenItemNotFoundExceptionThrown() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.findByIdWithOwner(anyLong())).thenThrow(ItemFoundException.class);

        assertThrows(ItemFoundException.class,
                () -> itemService.getItemById(id, id));
        verify(userService).getById(id);
    }

    @Test
    void getAllItemsByUserId_whenItemsFound_thenReturnListOfItems() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.findAllByOwnerIdWithBookings(anyLong(), any())).thenReturn(List.of(item));

        List<ItemResponseDto> actualItems = itemService.getAllItemsByUserId(id);
        assertEquals(actualItems.size(), 1);
        verify(userService).getById(id);
        verify(itemRepository).findAllByOwnerIdWithBookings(anyLong(), any());
    }

    @Test
    void getAllItemsByUserId_whenItemsNotFound_thenReturnEmptyList() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.findAllByOwnerIdWithBookings(anyLong(), any())).thenReturn(Collections.emptyList());

        List<ItemResponseDto> actualItems = itemService.getAllItemsByUserId(id);
        assertEquals(actualItems.size(), 0);
        verify(userService).getById(id);
        verify(itemRepository).findAllByOwnerIdWithBookings(anyLong(), any());
    }

    @Test
    void getAllItemsByUserId_whenUserNotFound_thenUserFoundExceptionThrown() {
        when(userService.getById(anyLong())).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> itemService.getAllItemsByUserId(id));
        verify(userService).getById(id);
    }

    @Test
    void search_whenItemFound_thenReturnListOfItems() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.search(anyString())).thenReturn(List.of(item));
        List<ItemResponseDto> items = itemService.search(id, "test");
        assertEquals(items.size(), 1);
        verify(userService).getById(id);
        verify(itemRepository).search(anyString());
    }

    @Test
    void search_whenTextIsBlank_thenReturnEmptyList() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.search(anyString())).thenReturn(Collections.emptyList());
        List<ItemResponseDto> items = itemService.search(id, "test");
        assertEquals(items.size(), 0);
        verify(userService).getById(id);
        verify(itemRepository).search(anyString());
    }

    @Test
    void search_whenUserNotFound_thenUserFoundExceptionThrown() {
        when(userService.getById(anyLong())).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> itemService.search(id, "test"));
        verify(userService).getById(id);
    }

    @Test
    void createComment_whenCommentValid_thenSaveComment() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.findByIdWithOwner(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingService.getUserBookings(anyLong(), any(), anyInt(), anyInt())).thenReturn(List.of(booking));
        when(bookingService.getBookingByItemIdAndStatusNotInAndStartBookingBefore(
                anyLong(), any(), any())).thenReturn(List.of(booking));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment actualComment = itemService.createComment(id, id, "comment text");

        assertEquals(actualComment.getText(), "comment text");
        assertEquals(actualComment.getItem(), item);
        assertEquals(actualComment.getAuthor(), user);
        verify(itemRepository).findByIdWithOwner(anyLong());
        verify(bookingService).getUserBookings(anyLong(), any(), anyInt(), anyInt());
        verify(userService).getById(id);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void createComment_whenCommentIsEmpty_thenValidationExceptionThrown() {
        assertThrows(ValidationException.class,
                () -> itemService.createComment(id, id, ""));
    }

    @Test
    void createComment_whenUserDoesNotHaveOpenBookings_thenNotFoundExceptionThrown() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.findByIdWithOwner(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingService.getUserBookings(anyLong(), any(), anyInt(), anyInt())).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class,
                () -> itemService.createComment(id, id, "comment text"));
    }
}
