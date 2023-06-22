package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DataJpaTest
@Sql("classpath:tests/data.sql")
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void findByEmailOne() {
        Optional<User> resultOptional = userRepository.findByEmail("varvara@gmail.com");
        assertTrue(resultOptional.isPresent());
        assertThat(resultOptional.get().getId()).isNotNull();
        assertThat(resultOptional.get().getName()).isEqualTo("Варвара");
        assertThat(resultOptional.get().getEmail()).isEqualTo("varvara@gmail.com");
    }

    @Test
    void findByEmailTwo() {
        Optional<User> resultOptional = userRepository.findByEmail("semyon@gmail.com");
        assertTrue(resultOptional.isPresent());
        assertThat(resultOptional.get().getId()).isNotNull();
        assertThat(resultOptional.get().getName()).isEqualTo("Семен");
        assertThat(resultOptional.get().getEmail()).isEqualTo("semyon@gmail.com");
    }

    @Test
    void findByEmailThree() {
        Optional<User> resultOptional = userRepository.findByEmail("michael@gmail.com");
        assertTrue(resultOptional.isPresent());
        assertThat(resultOptional.get().getId()).isNotNull();
        assertThat(resultOptional.get().getName()).isEqualTo("Михаил");
        assertThat(resultOptional.get().getEmail()).isEqualTo("michael@gmail.com");
    }

}
