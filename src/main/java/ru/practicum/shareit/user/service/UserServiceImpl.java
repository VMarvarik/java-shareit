package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDtoResponse addUser(UserDto userDto) {
        User newUser = UserMapper.mapToModel(userDto);
        return UserMapper.mapToDto(userRepository.save(newUser));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDtoResponse> getUsers() {
        return UserMapper.mapToDto(userRepository.findAll());
    }

    @Override
    @Transactional
    public UserDtoResponse updateUser(UserDto request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));

        if (request.getEmail() != null && !Objects.equals(request.getEmail(), user.getEmail())) {
            user.setEmail(request.getEmail());
        }

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        return UserMapper.mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDtoResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
        return UserMapper.mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
    }

    @Override
    public void checkIfUserExists(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Пользователь не найден.");
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
