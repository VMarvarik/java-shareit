package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.exception.InvalidRequestException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.request.RequestController.REQUEST_HEADER;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> addBooking(@RequestHeader(REQUEST_HEADER) Long userId,
                                             @RequestBody @Valid RequestBookingDto requestBookingDto) {
        return bookingClient.addBooking(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> changeBookingStatus(@RequestHeader(REQUEST_HEADER) Long userId,
                                                      @PathVariable Long bookingId,
                                                      @RequestParam(name = "approved") Boolean approved) {
        return bookingClient.updateStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> findBookingById(@RequestHeader(REQUEST_HEADER) Long userId,
                                                  @PathVariable Long bookingId) {
        return bookingClient.findById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getAllBookingsByBooker(@RequestHeader(REQUEST_HEADER) Long userId,
                                                         @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                         @RequestParam(value = "from",
                                                                 defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(value = "size",
                                                                 defaultValue = "10") @Positive Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new EntityNotFoundException("Неизестное состояние: " + state));
        return bookingClient.getAllByBooker(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getAllBookingsByOwner(@RequestHeader(REQUEST_HEADER) Long userId,
                                                        @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                        @RequestParam(value = "from",
                                                                defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(value = "size",
                                                                defaultValue = "10") @Positive Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        BookingState bookingState = BookingState.from(state)
                .orElseThrow(() -> new EntityNotFoundException("Неизестное состояние: " + state));
        return bookingClient.getAllByOwner(userId, bookingState, from, size);
    }
}