package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.RequestBookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseBookingDto addBooking(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                         @RequestBody @Valid RequestBookingDto requestBookingDto) {
        return bookingService.addBooking(userId, requestBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto changeBookingStatus(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                  @PathVariable Long bookingId,
                                                  @RequestParam(name = "approved") Boolean approved) {
        return bookingService.updateStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDto findBookingById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                              @PathVariable Long bookingId) {
        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public List<ResponseBookingDto> getAllBookingsByBooker(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                           @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllByBooker(userId, state);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getAllBookingsByOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                                          @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(userId, state);
    }
}
