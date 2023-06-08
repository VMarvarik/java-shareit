package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByIdAndItemOwnerId(Long id, Long ownerId);

    List<Booking> findAllByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeOrderByStartDesc(Long bookerId, Long itemId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId, LocalDateTime now, LocalDateTime now2);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId, LocalDateTime now, LocalDateTime now2);

    Optional<Booking> findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(Long itemId, Long ownerId, LocalDateTime now, BookingStatus status);

    Optional<Booking> findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(Long itemId, Long ownerId, LocalDateTime now, BookingStatus status);

    List<Booking> findAllByItemIdInAndItemOwnerIdAndStatus(List<Long> itemIds, Long ownerId, BookingStatus status);
}
