package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
           "from Booking as b " +
           "join fetch b.booker " +
           "join fetch b.item as i " +
           "join fetch i.owner " +
           "where b.id = :id " +
           "order by b.startBooking desc ")
    @NotNull Optional<Booking> findById(@Param("id") Long id);

    @Query("select b " +
           "from Booking as b " +
           "join fetch b.booker " +
           "join fetch  b.item " +
           "where b.booker = :booker ")
    List<Booking> findByBooker(@Param("booker") User booker, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking as b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = :booker " +
            "and b.startBooking < :time " +
            "and b.endBooking > :time ")
    List<Booking> findByBookerCurrent(@Param("booker") User booker,
                                      @Param("time") LocalDateTime currentTime, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = :user " +
            "   and b.endBooking < :time ")
    List<Booking> findByBookerPast(@Param("user") User booker, @Param("time") LocalDateTime currentTime, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = :user " +
            "   and b.startBooking > :time ")
    List<Booking> findByBookerFuture(@Param("user") User booker, @Param("time") LocalDateTime currentTime, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item " +
            "where b.booker = :user " +
            "   and b.status = :status ")
    List<Booking> findByBookerAndStatus(@Param("user") User booker, @Param("status") BookingStatus status, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = :user ")
    List<Booking> findByItemOwner(@Param("user") User itemOwner, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = :user " +
            "   and b.startBooking < :time " +
            "   and b.endBooking > :time ")
    List<Booking> findByItemOwnerCurrent(@Param("user") User itemOwner,
                                         @Param("time") LocalDateTime currentTime, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = :user " +
            "   and b.endBooking < :time ")
    List<Booking> findByItemOwnerPast(@Param("user") User itemOwner,
                                      @Param("time") LocalDateTime currentTime, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = :user " +
            "   and b.startBooking > :time ")
    List<Booking> findByItemOwnerFuture(@Param("user") User itemOwner,
                                        @Param("time") LocalDateTime currentTime, Sort sortByStartDateDesc);

    @Query("select b " +
            "from Booking b " +
            "join fetch b.booker " +
            "join fetch b.item i " +
            "where i.owner = :user " +
            "   and b.status = :status ")
    List<Booking> findByItemOwnerAndStatus(@Param("user") User itemOwner, @Param("status") BookingStatus status, Sort sortByStartDateDesc);

    List<Booking> findBookingByItemIdAndStatusNotInAndStartBookingBefore(
            long itemId, List<BookingStatus> statuses, LocalDateTime startBooking);
}
