package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> addUser(User user);

    Optional<User> getUser(long id);

    Collection<User> getUsers();

    Optional<User> updateUser(User user, long id);

    void deleteUser(long id);
}
