package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ResponseBookingDto addBooking(Long bookerId, RequestBookingDto request) {
        User user = userService.findUserById(bookerId);

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Вещи нет в базе."));

        if (!item.getAvailable()) {
            throw new InvalidRequestException("Вещь не доступна для бронирования.");
        }

        if (request.getEnd().isBefore(request.getStart()) ||
                request.getEnd().equals(request.getStart())) {
            throw new InvalidRequestException("Время ьбронирования не отвечает требованиям.");
        }

        if (Objects.equals(item.getOwner().getId(), bookerId))
            throw new EntityNotFoundException("Невозможно забронировать свою же вещь.");

        Booking booking = BookingMapper.mapToModel(request);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.mapToDto(savedBooking);
    }

    @Override
    @Transactional
    public ResponseBookingDto updateStatus(Long id, Boolean approved, Long userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Букинга нет в базе"));

        if (!Objects.equals(booking.getItem().getOwner().getId(), userId))
            throw new EntityNotFoundException("У вас нет доступа к данному букингу.");

        if (!booking.getStatus().equals(Status.WAITING))
            throw new InvalidRequestException("Ошибка статуса букинга.");

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

        Booking savedBooking = bookingRepository.save(booking);
        return BookingMapper.mapToDto(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseBookingDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Букинга нет в базе"));

        if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new EntityNotFoundException("У вас нет доступа к данному букингу.");
        }

        return BookingMapper.mapToDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseBookingDto> getAllByBooker(Long userId, String state, Integer from, Integer size) {
        userService.checkIfUserExists(userId);
        int page = 0;
        if (from != 0) {
            page = from / size;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());
        LocalDateTime dateNow = LocalDateTime.now();
        State bookingState = State.toStateFromString(state)
                .orElseThrow(() -> new InvalidRequestException("Unknown state: " + state));

        List<Booking> bookings;
        switch (bookingState) {
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, dateNow, dateNow, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, dateNow, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, dateNow, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterAndStatusIs(userId, dateNow, pageable, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterAndStatusIs(userId, dateNow, pageable, Status.REJECTED);
                break;
            default:
                bookings = bookingRepository.findAllByBookerId(userId, pageable);
                break;
        }
        return BookingMapper.mapToDtoList(bookings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseBookingDto> getAllByOwner(Long ownerId, String state, Integer from, Integer size) {
        User user = userService.findUserById(ownerId);
        int page = 0;
        if (from != 0) {
            page = from / size;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("start").descending());
        LocalDateTime dateNow = LocalDateTime.now();
        State bookingState = State.toStateFromString(state)
                .orElseThrow(() -> new InvalidRequestException("Unknown state: " + state));
        List<Long> itemIdList = itemRepository.findAllByOwnerId(user.getId())
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<Booking> bookings;
        switch (bookingState) {
            case CURRENT:
                bookings = bookingRepository.findByItemIdInAndStartIsBeforeAndEndIsAfter(itemIdList, dateNow, dateNow, pageable);
                break;
            case PAST:
                bookings = bookingRepository.findByItemIdInAndEndIsBefore(itemIdList, dateNow, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemIdInAndStartIsAfter(itemIdList, dateNow, pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemIdInAndStartIsAfterAndStatusIs(itemIdList, dateNow, pageable, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemIdInAndStartIsAfterAndStatusIs(itemIdList, dateNow, pageable, Status.REJECTED);
                break;
            default:
                bookings = bookingRepository.findAllByItemIdIn(itemIdList, pageable);
                break;
        }
        return BookingMapper.mapToDtoList(bookings);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllByItemId(Long id) {
        return bookingRepository.findAllByItemId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllByItemIdIn(List<Long> itemIds) {
        return bookingRepository.findAllByItemIdIn(itemIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllByItemIdAndTime(Long itemId, LocalDateTime created) {
        return bookingRepository.findByItemIdAndEndIsBefore(itemId, created);
    }
}
