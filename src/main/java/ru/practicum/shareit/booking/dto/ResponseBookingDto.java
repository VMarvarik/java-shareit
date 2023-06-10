package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ResponseBookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Booker booker;
    private Item item;
    private Status status;

    @Data
    @Builder
    @AllArgsConstructor
    public static class Item {
        private Long id;
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class Booker {
        private Long id;
        private String name;
    }

}
