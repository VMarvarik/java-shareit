package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    private final RequestService requestService;

    public static final String REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public RequestDto addItemRequest(@RequestHeader(name = REQUEST_HEADER) Long userId,
                                     @RequestBody RequestDto requestDto) {
        return requestService.addRequest(requestDto, userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public RequestDto getItemRequestById(@RequestHeader(name = REQUEST_HEADER) Long userId,
                                         @PathVariable Long requestId) {
        return requestService.getRequestByUserId(userId, requestId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<RequestDto> getAllItemRequests(@RequestHeader(name = REQUEST_HEADER) Long userId) {
        return requestService.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public List<RequestDto> getItemRequestsByOtherUsers(@RequestHeader(name = REQUEST_HEADER) Long userId,
                                                        @RequestParam(value = "from",
                                                                defaultValue = "0") Integer from,
                                                        @RequestParam(value = "size",
                                                                defaultValue = "10") Integer size) {
        return requestService.getRequestsByOtherUsers(userId, from, size);
    }
}
