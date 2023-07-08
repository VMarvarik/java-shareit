package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestBookingDto {
    @NotNull
    private Long itemId;

    @FutureOrPresent(message = "Время начала не может быть в прошлом")
    @NotNull(message = "Время начала не может быть пустым")
    private LocalDateTime start;

    @Future(message = "Время окончания не может быть в прошлом")
    @NotNull(message = "Время окончания не может быть пустым")
    private LocalDateTime end;
}
