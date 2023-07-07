package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.request.controller.RequestController.REQUEST_HEADER;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseBookingDto addBooking(@RequestHeader(REQUEST_HEADER) Long userId,
                                         @RequestBody RequestBookingDto requestBookingDto) {
        return bookingService.addBooking(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseBookingDto changeBookingStatus(@RequestHeader(REQUEST_HEADER) Long userId,
                                                  @PathVariable(name = "bookingId") Long bookingId,
                                                  @RequestParam(name = "approved") Boolean approved) {
        return bookingService.updateStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseBookingDto findBookingById(@RequestHeader(REQUEST_HEADER) Long userId,
                                              @PathVariable(name = "bookingId") Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResponseBookingDto> getAllBookingsByBooker(@RequestHeader(REQUEST_HEADER) Long userId,
                                                           @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                           @RequestParam(value = "from",
                                                                   defaultValue = "0") Integer from,
                                                           @RequestParam(value = "size",
                                                                   defaultValue = "10") Integer size) {
        return bookingService.getAllByBooker(userId, state, from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ResponseBookingDto> getAllBookingsByOwner(@RequestHeader(REQUEST_HEADER) Long userId,
                                                          @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                          @RequestParam(value = "from",
                                                                  defaultValue = "0") Integer from,
                                                          @RequestParam(value = "size",
                                                                  defaultValue = "10") Integer size) {
        return bookingService.getAllByOwner(userId, state, from, size);
    }
}
