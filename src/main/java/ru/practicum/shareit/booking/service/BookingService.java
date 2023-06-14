package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    ResponseBookingDto addBooking(Long bookerId, RequestBookingDto requestBookingDto);

    ResponseBookingDto updateStatus(Long bookingId, Boolean approved, Long userId);

    ResponseBookingDto findById(Long bookingId, Long userId);

    List<ResponseBookingDto> getAllByBooker(Long userId, String state);

    List<ResponseBookingDto> getAllByOwner(Long ownerId, String state);

    List<Booking> getAllByItemId(Long id);

    List<Booking> getAllByItemIdIn(List<Long> itemIds);

    List<Booking> getAllByItemIdAndTime(Long itemId, LocalDateTime created);
}

