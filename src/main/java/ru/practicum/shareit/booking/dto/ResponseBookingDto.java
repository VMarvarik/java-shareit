package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoForBooking;
import ru.practicum.shareit.user.dto.UserDtoForBooking;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseBookingDto {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UserDtoForBooking user;
    private ItemDtoForBooking item;
    private BookingStatus status;
}
