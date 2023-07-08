package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public RequestDto mapToDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .requestorId(request.getRequestor().getId())
                .items(new ArrayList<>())
                .build();
    }

    public Request mapToModel(RequestDto requestDto) {
        return Request.builder()
                .description(requestDto.getDescription())
                .created(null)
                .requestor(null)
                .build();
    }

    public List<RequestDto> mapToDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
