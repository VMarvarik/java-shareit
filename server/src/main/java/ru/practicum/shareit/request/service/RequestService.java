package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.List;

public interface RequestService {

    RequestDto addRequest(RequestDto requestDto, Long userId);

    Request getRequestById(Long id);

    RequestDto getRequestByUserId(Long userId, Long requestId);

    List<RequestDto> getAllRequestsByUserId(Long userId);

    List<RequestDto> getRequestsByOtherUsers(Long userId, Integer from, Integer size);
}
