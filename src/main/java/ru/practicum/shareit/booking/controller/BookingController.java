package ru.practicum.shareit.booking.controller;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.StateOfBookingForRequest;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseBookingDto create(
            @Valid @RequestBody RequestBookingDto requestBookingDto,
            @RequestHeader("X-Sharer-User-Id") long bookerId
    ) {
        if (requestBookingDto.getStartTime().isAfter(requestBookingDto.getEndTime())
                || requestBookingDto.getStartTime().isEqual(requestBookingDto.getEndTime())) {
            throw new IllegalArgumentException("Начало бронирования должно быть раньше окончания.");
        }
        return BookingMapper.toResponseBookingDto(bookingService.create(requestBookingDto, bookerId));
    }

    @PatchMapping("{bookingId}")
    public ResponseBookingDto approve(
            @PathVariable long bookingId,
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam("approved") boolean approved
    ) {
        return BookingMapper.toResponseBookingDto(bookingService.approve(bookingId, ownerId, approved));
    }

    @GetMapping("{bookingId}")
    public ResponseBookingDto findById(
            @PathVariable long bookingId,
            @RequestHeader("X-Sharer-User-Id") long ownerOrBookerId
    ) {
        return BookingMapper.toResponseBookingDto(bookingService.findById(bookingId, ownerOrBookerId));
    }

    @GetMapping
    public Collection<ResponseBookingDto> findAllByUser(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(defaultValue = "ALL") StateOfBookingForRequest state
    ) {
        return bookingService.findAllByUser(userId, state).stream().map(BookingMapper::toResponseBookingDto).collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public Collection<ResponseBookingDto> findAllByOwner(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(defaultValue = "ALL") StateOfBookingForRequest state
    ) {
        return bookingService.findAllByOwner(ownerId, state).stream().map(BookingMapper::toResponseBookingDto).collect(Collectors.toList());
    }
}
