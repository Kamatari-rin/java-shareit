package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.exception.ItemFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTests {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @Captor
    private ArgumentCaptor<Booking> bookingArgumentCaptor;

    private Item item;
    private User user;
    private Booking booking;

    private final Long id = 1L;

    @BeforeEach
    void init() {
        user = User.builder().name("name").email("test@email.com").id(id).build();
        item = Item.builder().owner(user).description("description").available(true).id(id).name("test").build();
        booking = Booking.builder()
                .id(id).startBooking(LocalDateTime.MIN)
                .endBooking(LocalDateTime.MAX)
                .booker(user)
                .item(item)
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void getUserBookings_whenValidAndStateAll_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findAllByBookerId(anyLong(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getUserBookings(id, "ALL", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getUserBookings_whenValidAndStatePast_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByBookerPast(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getUserBookings(id, "PAST", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getUserBookings_whenValidAndStateCurrent_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByBookerCurrent(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getUserBookings(id, "CURRENT", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getUserBookings_whenValidAndStateFuture_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByBookerFuture(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getUserBookings(id, "FUTURE", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getUserBookings_whenValidAndStateWaiting_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByBookerAndStatus(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getUserBookings(id, "WAITING", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getUserBookings_whenValidAndStateRejected_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByBookerAndStatus(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getUserBookings(id, "REJECTED", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getUserBookings_whenUserNotFound_thenUserFoundExceptionThrows() {
        when(userService.getById(id)).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> bookingService.getUserBookings(id, "ALL", 0, 2));
        verify(userService).getById(any());
    }

    @Test
    void getOwnerBookings_whenValidAndStateAll_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByItemOwner(any(User.class), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getOwnerBookings(id, "ALL", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getOwnerBookings_whenValidAndStatePast_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByItemOwnerPast(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getOwnerBookings(id, "PAST", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getOwnerBookings_whenValidAndStateCurrent_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByItemOwnerCurrent(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getOwnerBookings(id, "CURRENT", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getOwnerBookings_whenValidAndStateFuture_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByItemOwnerFuture(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getOwnerBookings(id, "FUTURE", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getOwnerBookings_whenValidAndStateWaiting_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByItemOwnerAndStatus(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getOwnerBookings(id, "WAITING", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getOwnerBookings_whenValidAndStateRejected_thenReturnListOfBookings() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findByItemOwnerAndStatus(any(User.class), any(), any())).thenReturn(List.of(booking));

        List<Booking> actualBookings = bookingService.getOwnerBookings(id, "REJECTED", 0, 2);
        assertEquals(actualBookings.size(), 1);
        verify(userService).getById(any());
    }

    @Test
    void getOwnerBookings_whenUserNotFond_thenUserFoundExceptionThrows() {
        when(userService.getById(id)).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> bookingService.getOwnerBookings(id, "ALL", 0, 2));
        verify(userService).getById(any());
    }

    @Test
    void getBookingByIdOwnerOrBookerOnly_whenValid_thenReturnBooking() {
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));

        Booking actualBooking = bookingService.getBookingByIdOwnerOrBookerOnly(id, id);
        assertEquals(actualBooking, booking);
        verify(userService).getById(any());
    }

    @Test
    void getBookingByIdOwnerOrBookerOnly_whenBookingNotFound_thenNotFoundExceptionThrows() {
        when(bookingRepository.findById(anyLong())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingByIdOwnerOrBookerOnly(id, id));
        verify(bookingRepository).findById(any());
    }

    @Test
    void getBookingByIdOwnerOrBookerOnly_whenUserNotFound_thenUserFoundExceptionThrows() {
        when(userService.getById(id)).thenThrow(UserFoundException.class);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        assertThrows(UserFoundException.class,
                () -> bookingService.getBookingByIdOwnerOrBookerOnly(id, id));
        verify(userService).getById(any());
        verify(bookingRepository).findById(any());
    }

    @Test
    void getBookingByIdOwnerOrBookerOnly_whenUserNotOwnerAndNotBooker_thenNotFoundExceptionThrows() {
        User userTwo = User.builder().name("UserTwo").email("two@email.com").id(2L).build();
        Booking bookingTwo = booking = Booking.builder()
                .id(id).startBooking(LocalDateTime.MIN)
                .endBooking(LocalDateTime.MAX)
                .booker(userTwo)
                .item(item)
                .status(BookingStatus.WAITING)
                .build();


        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(bookingTwo));

        assertThrows(NotFoundException.class,
                () -> bookingService.getBookingByIdOwnerOrBookerOnly(3L, 1L));
        verify(userService).getById(any());
        verify(bookingRepository).findById(any());
    }

    @Test
    void create_whenValid_thenSaveBooking() {
        User userTwo = User.builder().name("UserTwo").email("two@email.com").id(2L).build();
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(userTwo));
        when(bookingRepository.save(any())).thenReturn(booking);

        Booking actualBooking = bookingService.create(id, LocalDateTime.MIN, LocalDateTime.MAX, 2L);

        assertEquals(booking, actualBooking);
        verify(userService).getById(any());
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void create_whenBookingEndIsBeforeBookingStartOrBookingEndEqualsBookingStart_thenValidationExceptionThrows() {
        User userTwo = User.builder().name("UserTwo").email("two@email.com").id(2L).build();
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(userTwo));

        assertThrows(ValidationException.class,
                () -> bookingService.create(id, LocalDateTime.MAX, LocalDateTime.MIN, 2L));
        verify(userService).getById(any());
    }

    @Test
    void create_whenItemNotFound_thenItemFoundExceptionThrows() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.findById(anyLong())).thenThrow(ItemFoundException.class);

        assertThrows(NotFoundException.class,
                () -> bookingService.create(id, LocalDateTime.MIN, LocalDateTime.MAX, id));
        verify(userService).getById(any());
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void create_whenOwnerIdEqualsBookerId_thenNotFoundExceptionThrows() {
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        assertThrows(NotFoundException.class,
                () -> bookingService.create(id, LocalDateTime.MIN, LocalDateTime.MAX, id));
        verify(userService).getById(any());
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void create_whenItemNotAvailable_thenValidationExceptionThrows() {
        item.setAvailable(false);
        User userTwo = User.builder().name("UserTwo").email("two@email.com").id(2L).build();

        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(userTwo));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));

        assertThrows(ValidationException.class,
                () -> bookingService.create(id, LocalDateTime.MIN, LocalDateTime.MAX, 2L));
        verify(userService).getById(any());
        verify(itemRepository).findById(anyLong());
    }

    @Test
    void approveBooking_whenValid_thenUpdateStateApproved() {
        User userTwo = User.builder().name("UserTwo").email("two@email.com").id(2L).build();
        booking.setBooker(userTwo);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));

        Booking actualBooking = bookingService.approveBooking(id, id, true);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();
        assertEquals(actualBooking, savedBooking);
        assertEquals(savedBooking.getStatus(), BookingStatus.APPROVED);
    }

    @Test
    void approveBooking_whenValid_thenUpdateStateRejected() {
        User userTwo = User.builder().name("UserTwo").email("two@email.com").id(2L).build();
        booking.setBooker(userTwo);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));

        Booking actualBooking = bookingService.approveBooking(id, id, false);
        verify(bookingRepository).save(bookingArgumentCaptor.capture());
        Booking savedBooking = bookingArgumentCaptor.getValue();

        assertEquals(actualBooking, savedBooking);
        assertEquals(savedBooking.getStatus(), BookingStatus.REJECTED);
    }

    @Test
    void approveBooking_whenBookingNotFound_thenNotFoundExceptionThrows() {
        when(bookingRepository.findById(anyLong())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(id, id, true));
        verify(bookingRepository).findById(anyLong());
    }

    @Test
    void approveBooking_whenUserNotFound_thenUserFoundExceptionThrows() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(userService.getById(id)).thenThrow(UserFoundException.class);
        assertThrows(UserFoundException.class,
                () -> bookingService.approveBooking(id, id, true));
        verify(userService).getById(anyLong());
    }

    @Test
    void approveBooking_whenBookerIdEqualsUserId_thenNotFoundExceptionThrows() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(userService.getById(id)).thenReturn(UserMapper.mapToUserResponseDto(user));
        assertThrows(NotFoundException.class,
                () -> bookingService.approveBooking(id, id, true));
        verify(userService).getById(anyLong());
    }

    @Test
    void approveBooking_whenOwnerIdEqualsUserIdOrBookingStatusNotWaiting_thenValidationExceptionThrows() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        when(userService.getById(anyLong())).thenReturn(UserMapper.mapToUserResponseDto(user));
        assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(2L, id, true));
        verify(userService).getById(anyLong());
    }

    @Test
    void getBookingByItemIdAndStatusNotInAndStartBookingBefore_whenValid_thenReturnListOfBooking() {
        when(bookingRepository.findBookingByItemIdAndStatusNotInAndStartBookingBefore(
                anyLong(), any(), any())).thenReturn(List.of(booking));
        List<Booking> actualBookings = bookingService
                .getBookingByItemIdAndStatusNotInAndStartBookingBefore(id, BookingStatus.APPROVED, LocalDateTime.now());

        assertEquals(actualBookings.size(), 1);
        verify(bookingRepository).findBookingByItemIdAndStatusNotInAndStartBookingBefore(anyLong(), any(), any());
    }

    @Test
    void getBookingByItemIdAndStatusNotInAndStartBookingBefore_whenBookingsNotFound_thenReturnEmptyList() {
        when(bookingRepository.findBookingByItemIdAndStatusNotInAndStartBookingBefore(
                anyLong(), any(), any())).thenReturn(Collections.emptyList());
        List<Booking> actualBookings = bookingService
                .getBookingByItemIdAndStatusNotInAndStartBookingBefore(id, BookingStatus.APPROVED, LocalDateTime.now());

        assertEquals(actualBookings.size(), 0);
        verify(bookingRepository).findBookingByItemIdAndStatusNotInAndStartBookingBefore(anyLong(), any(), any());
    }
}
