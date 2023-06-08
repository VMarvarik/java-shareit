package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Access denied")
public class AccessDenied extends RuntimeException {
    public AccessDenied(String message) {
        super(message);
    }
}
