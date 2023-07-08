package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.ResponseCommentDto;
import ru.practicum.shareit.comment.model.Comment;

import java.util.List;

public interface CommentService {
    Comment save(Comment comment);

    List<ResponseCommentDto> getAllByItemId(Long id);
}
