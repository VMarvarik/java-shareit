package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.exception.InvalidRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.request.RequestController.USER_ID_HEADER;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getAllBookingsByBooker(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                         @RequestParam(name = "state", defaultValue = "all") String state,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new InvalidRequestException("Unknown state: " + state));
        return bookingClient.getAllByBooker(userId, bookingState, from, size);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> addBooking(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                             @RequestBody @Valid RequestBookingDto request) {
        if (request.getEnd().isBefore(request.getStart()) ||
                request.getEnd().equals(request.getStart())) {
            throw new InvalidRequestException("Неверная дата брони");
        }
        return bookingClient.addBooking(userId, request);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findBookingById(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                  @PathVariable Long bookingId) {
        return bookingClient.findById(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> changeBookingStatus(@RequestHeader(name = USER_ID_HEADER) Long ownerId,
                                                      @PathVariable Long bookingId,
                                                      @RequestParam Boolean approved) {
        return bookingClient.updateStatus(ownerId, bookingId, approved);
    }

    @GetMapping("/owner")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                        @RequestParam(name = "state", defaultValue = "all") String state,
                                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new InvalidRequestException("Unknown state: " + state));

        return bookingClient.getAllByOwner(userId, bookingState, from, size);
    }
}
