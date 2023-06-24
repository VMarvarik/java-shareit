package ru.practicum.shareit.requestTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DataJpaTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Sql("classpath:tests/data.sql")
class RequestRepositoryTest {
    @Autowired
    RequestRepository requestRepository;

    @Test
    void findByRequestorIdCorrect() {
        List<Request> results = requestRepository.findAllByRequestorId(3L);

        assertFalse(results.isEmpty());
        assertThat(results.get(0).getDescription()).isEqualTo("Мне нужны грабли");
    }

    @Test
    void findByRequestorIdPageableCorrect() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("created").descending());

        List<Request> results = requestRepository.findAllByRequestorId(3L, pageable);

        assertFalse(results.isEmpty());
        assertThat(results.get(0).getDescription()).isEqualTo("Мне нужны грабли");
    }
}