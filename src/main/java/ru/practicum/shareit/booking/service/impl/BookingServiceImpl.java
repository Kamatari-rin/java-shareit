package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.State;
import ru.practicum.shareit.exception.ItemFoundException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UserFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.OffsetBasedPageRequest;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.util.Constant.SORT_BY_START_DATE_DESC;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getUserBookings(Long userId, String requestState, int from, int size) {
        User user = getUserOrThrowException(userId);
        State state = State.valueOf(requestState.toUpperCase());
        LocalDateTime timeNow = LocalDateTime.now();

        Pageable page = new OffsetBasedPageRequest(from, size, SORT_BY_START_DATE_DESC);

        List<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(user.getId(), page);
                log.info(bookings.toString());
                break;
            case PAST:
                bookings = bookingRepository.findByBookerPast(user, timeNow, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerCurrent(user, timeNow, page);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerFuture(user, timeNow, page);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerAndStatus(user, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerAndStatus(user, BookingStatus.REJECTED, page);
                break;
            default: bookings = Collections.emptyList();
        }
        return bookings;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getOwnerBookings(Long userId, String requestState, int from, int size) {
        User user = getUserOrThrowException(userId);
        State state;
        state = State.valueOf(requestState.toUpperCase());
        LocalDateTime timeNow = LocalDateTime.now();

        Pageable page = new OffsetBasedPageRequest(from, size, SORT_BY_START_DATE_DESC);

        List<Booking> bookings;

        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItemOwner(user, page);
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerPast(user, timeNow, page);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerCurrent(user, timeNow, page);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerFuture(user, timeNow, page);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerAndStatus(user, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerAndStatus(user, BookingStatus.REJECTED, page);
                break;
            default: bookings = Collections.emptyList();
        }
        return bookings;
    }

    @Override
    public Booking getBookingByIdOwnerOrBookerOnly(Long userId, Long bookingId) {
        Booking booking = getBookingOrThrowException(bookingId);

        getUserOrThrowException(userId);

        final Long bookerId = booking.getBooker().getId();
        final Long ownerId = booking.getItem().getOwner().getId();

        if (!Objects.equals(bookerId, userId) && !Objects.equals(ownerId, userId)) {
            throw new NotFoundException("Данный запрос может отправить только владелец вещи" +
                    " или человек осуществлявший бронирование.");
        }
        return booking;
    }

    @Override
    public Booking create(Long itemId, LocalDateTime bookingStart, LocalDateTime bookingEnd, Long bookerId) {
        User user = getUserOrThrowException(bookerId);

        if (bookingEnd.isBefore(bookingStart) || bookingEnd.equals(bookingStart)) {
            throw new ValidationException("Дата окончания бронирования не может быть раньше или равна дате бронирования.");
        }

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ItemFoundException(itemId));

        if (item.getOwner().getId().equals(bookerId)) {
            throw new NotFoundException("Вещь не может быть забронирована владельцем.");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования2.");
        }

        if (bookingEnd.isBefore(LocalDateTime.now()) || bookingStart.equals(bookingEnd)) {
            throw new ValidationException(
                    "Время окончания бронирования не может быть раньше времени начала бронирования вещи3.");
        }


        Booking booking = Booking.builder()
                .startBooking(bookingStart)
                .endBooking(bookingEnd)
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();

        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveBooking(Long userId, Long bookingId, Boolean state) {
        Booking booking = getBookingOrThrowException(bookingId);

        getUserOrThrowException(userId);

        if (booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException(String.format("У пользователя %s нет доступных бронирований.", userId));
        }

        if (!booking.getItem().getOwner().getId().equals(userId)
                || !booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new ValidationException("Не возможно обновить статус. " +
                    "Для обновления статуса вы должны являться владельцем вещи," +
                    " а так же запрос должен ожидать подтверждения.");
        }

        booking.setStatus(state ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookingByItemIdAndStatusNotInAndStartBookingBefore(Long itemId,
                                                                               BookingStatus status,
                                                                               LocalDateTime date) {
        return bookingRepository.findBookingByItemIdAndStatusNotInAndStartBookingBefore(
                itemId, List.of(status), date);
    }

    private Booking getBookingOrThrowException(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(
                () -> new NotFoundException("Запрос на бронирования не найден."));
    }

    private User getUserOrThrowException(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserFoundException(userId));
    }
}
