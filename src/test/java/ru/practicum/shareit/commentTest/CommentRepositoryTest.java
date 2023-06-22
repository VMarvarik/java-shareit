package ru.practicum.shareit.commentTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DataJpaTest
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@Sql("classpath:tests/data.sql")
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Test
    void shouldFindCommentByItemId() {
        List<Comment> results = commentRepository.findAllByItemId(2L);
        assertFalse(results.isEmpty());
        assertThat(results.get(0).getText()).isEqualTo("Эти грабли идеально мне подходят");
        assertThat(results.get(0).getAuthor().getId()).isEqualTo(3L);
    }
}
