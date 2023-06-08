package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.StateOfBookingForRequest;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AccessDenied;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.SelfReferenceException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            ItemRepository itemRepository,
            UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Booking create(RequestBookingDto requestBookingDto, long bookerId) {
        Booking booking = BookingMapper.toBookingFromRequestBookingDto(requestBookingDto);
        Item item = itemRepository.findById(requestBookingDto.getItemId()).orElseThrow(
                () -> new EntityNotFoundException("Вещи нет в базе.")
        );
        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Вещь не доступна для бронирования.");
        }
        booking.setItem(item);
        if (item.getOwner().getId() == bookerId) {
            throw new SelfReferenceException("Невозможно забронировать свою же вещь.");
        }
        booking.setBooker(userRepository.findById(bookerId).orElseThrow(
                () -> new EntityNotFoundException("Пользователь не найден.")
        ));
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking approve(long bookingId, long ownerId, boolean approved) {
        Booking booking = bookingRepository.findByIdAndItemOwnerId(bookingId, ownerId).orElseThrow(
                () -> new EntityNotFoundException("Бронирование не найдено или вы не являетесь владельцем вещи.")
        );
        if (booking.getStatus() == BookingStatus.APPROVED && approved) {
            throw new IllegalArgumentException("Бронирование уже одобрено.");
        } else {
            booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
            return bookingRepository.save(booking);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Booking findById(long bookingId, long ownerOrBookerId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new EntityNotFoundException("Бронирование не найдено.")
        );
        if (booking.getItem().getOwner().getId() != ownerOrBookerId && booking.getBooker().getId() != ownerOrBookerId) {
            throw new AccessDenied("Вы не являетесь владельцем вещи или создателем брони.");
        } else {
            return booking;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Booking> findAllByUser(long userId, StateOfBookingForRequest state) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь не найден.");
        }
        switch (state) {
            case ALL:
                return new ArrayList<>(bookingRepository.findAllByBookerIdOrderByStartDesc(userId));
            case CURRENT:
                LocalDateTime now = LocalDateTime.now();
                return new ArrayList<>(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, now, now));
            case PAST:
                return new ArrayList<>(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()));
            case FUTURE:
                return new ArrayList<>(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()));
            case WAITING:
                return new ArrayList<>(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING));
            case REJECTED:
                return new ArrayList<>(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED));
        }
        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<Booking> findAllByOwner(long ownerId, StateOfBookingForRequest state) {
        if (!userRepository.existsById(ownerId)) {
            throw new EntityNotFoundException("Пользователь не найден.");
        }
        switch (state) {
            case ALL:
                return new ArrayList<>(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId));
            case CURRENT:
                LocalDateTime now = LocalDateTime.now();
                return new ArrayList<>(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now, now));
            case PAST:
                return new ArrayList<>(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now()));
            case FUTURE:
                return new ArrayList<>(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now()));
            case WAITING:
                return new ArrayList<>(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING));
            case REJECTED:
                return new ArrayList<>(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED));
        }
        return new ArrayList<>();
    }
}
