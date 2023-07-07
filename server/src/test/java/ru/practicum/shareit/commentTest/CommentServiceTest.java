package ru.practicum.shareit.commentTest;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentServiceTest {
//    @Autowired
//    CommentServiceImpl commentService;
//
//    @MockBean
//    CommentRepository commentRepository;
//
//    User user1;
//    User user2;
//    Request request;
//
//    Item item;
//
//    Comment comment;
//
//    @BeforeEach
//    void beforeEach() {
//        user1 = User.builder().id(1L).name("Варвара").email("varvara@gmail.com").build();
//        user2 = User.builder().id(2L).name("Семен").email("semyon@gmail.com").build();
//        request = Request.builder().id(10L)
//                .created(LocalDateTime.of(2023, 4, 4, 17, 10, 0))
//                .description("Мне нужна лопата")
//                .requestor(user2)
//                .build();
//
//        item = Item.builder().id(1L)
//                .name("Лопата")
//                .description("Лопата садовая")
//                .owner(user1)
//                .available(true)
//                .request(request)
//                .build();
//
//        comment = Comment.builder()
//                .id(1L)
//                .text("хорошо")
//                .author(user2)
//                .item(item)
//                .created(LocalDateTime.of(2023, 2, 10, 17, 10, 5).plusDays(10))
//                .build();
//    }
//
//    @Test
//    void shouldSaveComment() {
//        commentService.save(comment);
//
//        verify(commentRepository, times(1)).save(any(Comment.class));
//    }
//
//    @Test
//    void getCommentsByItemId() {
//        when(commentRepository.findAllByItemId(anyLong()))
//                .thenReturn(List.of(comment));
//
//        List<ResponseCommentDto> result = commentService.getAllByItemId(1L);
//
//        verify(commentRepository, times(1)).findAllByItemId(anyLong());
//        assertFalse(result.isEmpty());
//        assertEquals(comment.getText(), result.get(0).getText());
//        assertEquals(comment.getAuthor().getName(), result.get(0).getAuthorName());
//    }
//
//    @Test
//    void getCommentsIfNoComments() {
//        when(commentRepository.findAllByItemId(anyLong()))
//                .thenReturn(List.of());
//
//        List<ResponseCommentDto> result = commentService.getAllByItemId(1L);
//
//        verify(commentRepository, times(1)).findAllByItemId(anyLong());
//        assertTrue(result.isEmpty());
//    }
}
