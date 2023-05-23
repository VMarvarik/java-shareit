package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> addUser(User user) {
        return Optional.of(userRepository.addUser(user)
                .orElseThrow(() -> new DuplicateEmailException("Ошибка добавления пользователя: такой email уже существует.")));
    }

    @Override
    public Optional<User> getUser(long id) {
        if (userRepository.getUser(id).isPresent()) {
            return userRepository.getUser(id);
        } else {
            throw new UserNotFoundException(String.format("Ошибка получения: пользователь с id=%d не найден.", id));
        }
    }

    @Override
    public Collection<User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public Optional<User> updateUser(User user, long id) {
        return Optional.of(userRepository.updateUser(user, id)
                .orElseThrow(() -> new DuplicateEmailException("Ошибка добавления пользователя: такой email уже существует.")));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }
}
