package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> addUser(User user);

    Optional<User> getUser(long id);

    Collection<User> getUsers();

    Optional<User> updateUser(User user, long id);

    void deleteUser(long id);
}