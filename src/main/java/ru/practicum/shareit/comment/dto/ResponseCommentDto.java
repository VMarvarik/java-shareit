package ru.practicum.shareit.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseCommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
