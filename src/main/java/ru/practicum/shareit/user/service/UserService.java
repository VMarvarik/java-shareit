package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {

    User addUser(User user);

    User getUser(long id);

    Collection<User> getUsers();

    User updateUser(User user, long id);

    void deleteUser(long id);
}