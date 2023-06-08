package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StateOfBookingForRequest;

import java.util.Collection;

public interface BookingService {
    Booking create(RequestBookingDto requestBookingDto, long bookerId);
    //ResponseBookingDto

    Booking approve(long bookingId, long bookerId, boolean approved);

    Booking findById(long bookingId, long ownerOrBookerId);

    Collection<Booking> findAllByUser(long userId, StateOfBookingForRequest state);

    Collection<Booking> findAllByOwner(long ownerId, StateOfBookingForRequest state);
}
