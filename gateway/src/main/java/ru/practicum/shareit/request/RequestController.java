package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {
    public static final String REQUEST_HEADER = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> addItemRequest(@RequestHeader(REQUEST_HEADER) Long userId,
                                                 @RequestBody @Valid RequestDto requestDto) {
        return requestClient.addRequest(userId, requestDto);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(REQUEST_HEADER) Long userId,
                                                     @PathVariable Long requestId) {
        return requestClient.getRequestByUserId(userId, requestId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(REQUEST_HEADER) Long userId) {
        return requestClient.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Object> getItemRequestsByOtherUsers(@RequestHeader(REQUEST_HEADER) Long userId,
                                                              @RequestParam(value = "from",
                                                                      defaultValue = "0", required = false) @PositiveOrZero Integer from,
                                                              @RequestParam(value = "size",
                                                                      defaultValue = "10", required = false) @Positive Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        return requestClient.getRequestsByOtherUsers(userId, from, size);
    }
}
