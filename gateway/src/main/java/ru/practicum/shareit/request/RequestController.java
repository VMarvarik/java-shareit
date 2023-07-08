package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidRequestException;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                 @RequestBody @Valid RequestDto request) {
        return requestClient.addRequest(userId, request);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequests(@RequestHeader(name = USER_ID_HEADER) Long userId) {
        return requestClient.getAllRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequestsByOtherUsers(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                              @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                              @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size) {
        if (size <= 0 || from < 0) {
            throw new InvalidRequestException("Недопустимые значения параматеров size или from");
        }
        return requestClient.getRequestsByOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(name = USER_ID_HEADER) Long userId,
                                                     @PathVariable(name = "requestId") Long requestId) {
        return requestClient.getRequestByUserId(userId, requestId);
    }
}
