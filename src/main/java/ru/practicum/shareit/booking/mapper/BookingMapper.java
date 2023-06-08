package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {
    public static ResponseBookingDto toResponseBookingDto(Booking booking) {
        return new ResponseBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                UserMapper.mapToDtoForBooking(booking.getBooker()),
                ItemMapper.mapToDtoForBooking(booking.getItem()),
                booking.getStatus());
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getBooker() == null ? null : booking.getBooker().getId());
    }

    public static Booking toBookingFromRequestBookingDto(RequestBookingDto requestBookingDto) {
        return new Booking(
                requestBookingDto.getStartTime(),
                requestBookingDto.getEndTime());
    }
}
