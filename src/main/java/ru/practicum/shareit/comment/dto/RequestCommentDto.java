package ru.practicum.shareit.comment.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class RequestCommentDto {
    @NotNull
    @NotBlank
    private String text;
}
