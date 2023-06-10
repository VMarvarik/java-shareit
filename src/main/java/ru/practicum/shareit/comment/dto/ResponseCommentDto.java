package ru.practicum.shareit.comment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResponseCommentDto {
    private Long id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}

