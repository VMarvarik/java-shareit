package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> userMap = new HashMap<>();
    private long id = 1L;

    @Override
    public Optional<User> addUser(User user) {
        if (getUsers().stream().map(User::getEmail).noneMatch(email -> email.equals(user.getEmail()))) {
            user.setId(id++);
            userMap.put(user.getId(), user);
            return Optional.of(userMap.get(user.getId()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        if (userMap.containsKey(id)) {
            return Optional.of(userMap.get(id));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Collection<User> getUsers() {
        return userMap.values().stream().sorted(Comparator.comparingLong(User::getId)).collect(Collectors.toList());
    }

    @Override
    public Optional<User> updateUser(User user, long id) {
        if (!userMap.containsKey(id)) {
            throw new EntityNotFoundException(String.format("Ошибка получения: пользователь с id=%d не найден.", id));

        }

        if (userMap.values().stream()
                .map(User::getEmail)
                .filter(email -> !email.equals(userMap.get(id).getEmail()))
                .noneMatch(email -> email.equals(user.getEmail()))) {
            User userUpdate = userMap.get(id);
            Optional.ofNullable(user.getName()).ifPresent(userUpdate::setName);
            Optional.ofNullable(user.getEmail()).ifPresent(userUpdate::setEmail);
            return Optional.of(userUpdate);
        }
        return Optional.empty();
    }

    @Override
    public void deleteUser(long id) {
        userMap.remove(id);
    }
}
