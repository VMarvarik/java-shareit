package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "You cant self reference an entity")
public class SelfReferenceException extends RuntimeException {
    public SelfReferenceException(String message) {
        super(message);
    }
}
