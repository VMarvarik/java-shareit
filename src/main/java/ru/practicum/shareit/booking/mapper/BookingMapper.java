package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookingMapper {

    public static ResponseBookingDto mapToDto(Booking booking) {
        ResponseBookingDto.Item itemDto = ResponseBookingDto.Item.builder()
                .id(booking.getItem().getId())
                .name(booking.getItem().getName())
                .build();

        ResponseBookingDto.Booker booker = ResponseBookingDto.Booker.builder()
                .id(booking.getBooker().getId())
                .build();

        return ResponseBookingDto.builder()
                .id(booking.getId())
                .booker(booker)
                .item(itemDto)
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public static Booking mapToModel(RequestBookingDto bookingDto) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }


    public static List<ResponseBookingDto> mapToDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
