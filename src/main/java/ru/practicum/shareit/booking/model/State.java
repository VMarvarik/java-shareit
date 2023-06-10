package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum State {
    ALL,
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED;

    public static Optional<State> toStateFromString(String state) {
        for (State bookingState : values()) {
            if (bookingState.name().equalsIgnoreCase(state))
                return Optional.of(bookingState);
        }
        return Optional.empty();
    }
}
