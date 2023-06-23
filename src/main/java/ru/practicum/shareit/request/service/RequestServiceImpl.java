package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserService userService;
    private final ItemService itemService;


    @Override
    @Transactional
    public RequestDto addRequest(RequestDto requestDto, Long userId) {
        Request request = RequestMapper.mapToModel(requestDto);
        userService.checkIfUserExists(userId);
        request.setRequestor(userService.findUserById(userId));
        request.setCreated(LocalDateTime.now());
        requestRepository.save(request);
        return RequestMapper.mapToDto(request);
    }

    @Override
    @Transactional(readOnly = true)
    public Request getRequestById(Long id) {
        return requestRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Запрос не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto getRequestByUserId(Long userId, Long requestId) {
        userService.checkIfUserExists(userId);
        Request request = getRequestById(requestId);
        Item item = itemService.findByRequestId(requestId);
        RequestDto requestDto = RequestMapper.mapToDto(request);
        ItemDtoForRequest itemDtoForRequest = ItemMapper.mapToDtoForRequest(item);
        requestDto.setItems(List.of(itemDtoForRequest));
        return requestDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getAllRequestsByUserId(Long userId) {
        userService.checkIfUserExists(userId);
        List<Request> requests = requestRepository.findAllByRequestorId(userId);
        List<Item> items = itemService.findAllByRequestIdIn(requests.stream().map(Request::getId).collect(Collectors.toList()));
        List<RequestDto> requestDtos = RequestMapper.mapToDto(requests);
        for (RequestDto requestDto : requestDtos) {
            List<ItemDtoForRequest> itemDtos = items.stream()
                    .filter(item -> Objects.equals(requestDto.getId(), item.getRequest().getId()))
                    .map(ItemMapper::mapToDtoForRequest)
                    .collect(Collectors.toList());
            requestDto.setItems(itemDtos);
        }
        return requestDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequestsByOtherUsers(Long userId, Integer from, Integer size) {
        userService.checkIfUserExists(userId);
        int page = 0;
        if (from != 0) {
            page = from / size;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        List<Request> requests = requestRepository.findAllByRequestorIdNot(userId, pageable);
        List<RequestDto> requestDtos = RequestMapper.mapToDto(requests);
        List<Item> items = itemService.findAllByRequestIdIn(requests.stream().map(Request::getId).collect(Collectors.toList()));

        for (RequestDto requestDto : requestDtos) {
            List<ItemDtoForRequest> itemDtos = items.stream()
                    .filter(item -> Objects.equals(requestDto.getId(), item.getRequest().getId()))
                    .map(ItemMapper::mapToDtoForRequest)
                    .collect(Collectors.toList());
            requestDto.setItems(itemDtos);
        }
        return requestDtos;
    }
}
