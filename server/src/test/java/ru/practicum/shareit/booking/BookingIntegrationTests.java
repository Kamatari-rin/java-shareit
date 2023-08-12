package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class BookingIntegrationTests {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemRepository itemRepository;
    private final EntityManager entityManager;

    private User userOne = User.builder().name("user").email("user@email.com").build();
    private User userTwo = User.builder().name("userTwo").email("userTwo@email.com").build();
    private Item item = Item.builder()
            .name("item")
            .description("item_description")
            .owner(userOne)
            .available(true)
            .build();
    private ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("item_description")
            .name("item")
            .available(true)
            .ownerId(1L)
            .build();

    @Test
    @DirtiesContext
    void getUserBookings_whenInvoked_thenReturnListOfBookings() {
        userService.create(userOne);
        userService.create(userTwo);
        itemRepository.save(item);
        bookingService.create(1L, LocalDateTime.MIN, LocalDateTime.MAX, 2L);

        List<Booking> bookings = bookingService.getUserBookings(2L, "ALL", 0, 2);

        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getBooker().getId(), 2L);
    }

    @Test
    @DirtiesContext
    void getOwnerBookings_whenInvoked_thenReturnListOfBookings() {
        userService.create(userOne);
        userService.create(userTwo);
        itemRepository.save(item);
        bookingService.create(1L, LocalDateTime.MIN, LocalDateTime.MAX, 2L);

        List<Booking> bookings = bookingService.getOwnerBookings(1L, "ALL", 0, 2);
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.get(0).getItem().getOwner().getId(), 1L);
    }

    @Test
    @DirtiesContext
    void getBookingByIdOwnerOrBookerOnly_whenInvoked_thenReturnBooking() {
        userService.create(userOne);
        userService.create(userTwo);
        itemRepository.save(item);
        bookingService.create(1L, LocalDateTime.MIN, LocalDateTime.MAX, 2L);

        Booking booking = bookingService.getBookingByIdOwnerOrBookerOnly(1L, 1L);
        assertEquals(booking.getItem().getOwner().getId(), 1L);

        Booking booking2 = bookingService.getBookingByIdOwnerOrBookerOnly(2L, 1L);
        assertEquals(booking2.getBooker().getId(), 2L);
    }

    @Test
    @DirtiesContext
    void create_whenValid_thenSaveBooking() {
        userService.create(userOne);
        userService.create(userTwo);
        itemRepository.save(item);
        bookingService.create(1L, LocalDateTime.MIN, LocalDateTime.MAX, 2L);

        Booking booking = entityManager.find(Booking.class, 1L);
        assertEquals(booking.getBooker().getId(), 2);
        assertEquals(booking.getStartBooking(), LocalDateTime.MIN);
        assertEquals(booking.getEndBooking(), LocalDateTime.MAX);
        assertEquals(booking.getStatus(), BookingStatus.WAITING);
    }

    @Test
    @DirtiesContext
    void approveBooking_whenValid_thenChangeBookingStatus() {
        userService.create(userOne);
        userService.create(userTwo);
        itemRepository.save(item);
        bookingService.create(1L, LocalDateTime.MIN, LocalDateTime.MAX, 2L);

        Booking booking = bookingService.approveBooking(1L, 1L, false);
        assertEquals(booking.getStatus(), BookingStatus.REJECTED);
    }

    @Test
    @DirtiesContext
    void getBookingByItemIdAndStatusNotInAndStartBookingBefore_whenValid_thenReturnListOfBookings() {
        userService.create(userOne);
        userService.create(userTwo);
        itemRepository.save(item);
        bookingService.create(1L, LocalDateTime.now().minusDays(1), LocalDateTime.MAX, 2L);

        List<Booking> bookings = bookingService.getBookingByItemIdAndStatusNotInAndStartBookingBefore(1L,
                        BookingStatus.REJECTED,
                        LocalDateTime.now());

        assertEquals(bookings.size(), 1);
    }
}
