package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.RequestCommentDto;
import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.model.Comment;

public class CommentMapper {
    public static ResponseCommentDto toResponseCommentDto(Comment comment) {
        return new ResponseCommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static Comment fromRequestCommentDtoToModel(RequestCommentDto requestCommentDto) {
        return new Comment(requestCommentDto.getText());
    }
}
