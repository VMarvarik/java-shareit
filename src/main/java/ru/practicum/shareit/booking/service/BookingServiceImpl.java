package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
    public ResponseBookingDto findById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Букинга нет в базе"));

        if (!Objects.equals(booking.getBooker().getId(), userId) && !Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new EntityNotFoundException("У вас нет доступа к данному букингу.");
        }

        return BookingMapper.mapToDto(booking);
    }

    @Override
    public List<ResponseBookingDto> getAllByBooker(Long userId, String state) {
        userService.checkIfUserExists(userId);

        LocalDateTime dateNow = LocalDateTime.now();
        Sort sort = Sort.by("start").descending();
        State bookingState = State.toStateFromString(state)
                .orElseThrow(() -> new InvalidRequestException("Unknown state: " + state));

        List<Booking> bookings;
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByBookerId(userId, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, dateNow, dateNow, sort);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, dateNow, sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, dateNow, sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterAndStatusIs(userId, dateNow, sort, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStartIsAfterAndStatusIs(userId, dateNow, sort, Status.REJECTED);
                break;
            default:
                return List.of();
        }
        return BookingMapper.mapToDtoList(bookings);
    }

    @Override
    public List<ResponseBookingDto> getAllByOwner(Long ownerId, String state) {
        User user = userService.findUserById(ownerId);
        LocalDateTime dateNow = LocalDateTime.now();
        Sort sort = Sort.by("start").descending();
        State bookingState = State.toStateFromString(state)
                .orElseThrow(() -> new InvalidRequestException("Unknown state: " + state));
        List<Long> itemIdList = itemRepository.findAllByOwnerId(user.getId())
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<Booking> bookings;
        switch (bookingState) {
            case ALL:
                bookings = bookingRepository.findAllByItemIdIn(itemIdList, sort);
                break;
            case CURRENT:
                bookings = bookingRepository.findByItemIdInAndStartIsBeforeAndEndIsAfter(itemIdList, dateNow, dateNow, sort);
                break;
            case PAST:
                bookings = bookingRepository.findByItemIdInAndEndIsBefore(itemIdList, dateNow, sort);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemIdInAndStartIsAfter(itemIdList, dateNow, sort);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemIdInAndStartIsAfterAndStatusIs(itemIdList, dateNow, sort, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemIdInAndStartIsAfterAndStatusIs(itemIdList, dateNow, sort, Status.REJECTED);
                break;
            default:
                return List.of();
        }
        return BookingMapper.mapToDtoList(bookings);
    }

    @Override
    public List<Booking> getAllByItemId(Long id) {
        return bookingRepository.findAllByItemId(id);
    }

    @Override
    public List<Booking> getAllByItemIdIn(List<Long> itemIds) {
        return bookingRepository.findAllByItemIdIn(itemIds);
    }

    @Override
    public List<Booking> getAllByItemIdAndTime(Long itemId, LocalDateTime created) {
        return bookingRepository.findByItemIdAndEndIsBefore(itemId, created);
    }
}
