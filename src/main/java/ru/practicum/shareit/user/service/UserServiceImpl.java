package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User getUser(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
        return userRepository.getById(id);
    }

    @Transactional
    @Override
    public Collection<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User updateUser(User user, long id) {
        User userCheck = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
        if (userCheck.getId() != id) {
            throw new EntityNotFoundException("Пользователь не найден.");
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteUser(long id) {
        User userCheck = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден."));
        userRepository.deleteById(id);
    }
}
