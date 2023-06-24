package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;

    public static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public RequestDto addItemRequest(@RequestHeader(REQUEST_HEADER) Long userId,
                                     @RequestBody @Valid RequestDto requestDto) {
        return requestService.addRequest(requestDto, userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public RequestDto getItemRequestById(@RequestHeader(REQUEST_HEADER) Long userId,
                                         @PathVariable Long requestId) {
        return requestService.getRequestByUserId(userId, requestId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<RequestDto> getAllItemRequests(@RequestHeader(REQUEST_HEADER) Long userId) {
        return requestService.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RequestDto> getItemRequestsByOtherUsers(@RequestHeader(REQUEST_HEADER) Long userId,
                                                        @RequestParam(value = "from",
                                                                defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                        @RequestParam(value = "size",
                                                                defaultValue = "10", required = false) @Positive Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        return requestService.getRequestsByOtherUsers(userId, from, size);
    }
}
