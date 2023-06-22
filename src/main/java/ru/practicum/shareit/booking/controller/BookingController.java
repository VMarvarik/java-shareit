package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.InvalidRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseBookingDto addBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                         @RequestBody @Valid RequestBookingDto requestBookingDto) {
        return bookingService.addBooking(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseBookingDto changeBookingStatus(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam(name = "approved") Boolean approved) {
        return bookingService.updateStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseBookingDto findBookingById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                              @PathVariable Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResponseBookingDto> getAllBookingsByBooker(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                           @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                           @RequestParam(value = "from",
                                                                   defaultValue = "0") @PositiveOrZero Integer from,
                                                           @RequestParam(value = "size",
                                                                   defaultValue = "10") @Positive Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        return bookingService.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResponseBookingDto> getAllBookingsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                          @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                          @RequestParam(value = "from",
                                                                  defaultValue = "0") @PositiveOrZero Integer from,
                                                          @RequestParam(value = "size",
                                                                  defaultValue = "10") @Positive Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        return bookingService.getAllByOwner(userId, state, from, size);
    }
}
