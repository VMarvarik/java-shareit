package ru.practicum.shareit.requestTest;


import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RequestServiceTest {
//    @Autowired
//    RequestServiceImpl requestService;
//
//    @MockBean
//    RequestRepository requestRepository;
//
//    @MockBean
//    ItemService itemService;
//
//    @MockBean
//    UserService userService;
//
//    User user = User.builder().id(1L).name("Варвара").email("varvara@gmail.com").build();
//    User user2 = User.builder().id(2L).name("Михаил").email("michael@gmail.com").build();
//
//    Request request = Request.builder().id(10L)
//            .created(LocalDateTime.of(2023, 2, 10, 17, 10, 5))
//            .description("Мне нужна лопата")
//            .requestor(user2)
//            .build();
//
//    Item item = Item.builder().id(1L)
//            .name("Лопата")
//            .description("Лопата для сада")
//            .owner(user)
//            .available(true)
//            .build();
//
//    @Test
//    void addRequestShouldBeOk() {
//        RequestDto request = RequestDto.builder()
//                .description("Мне нужна лопата")
//                .build();
//
//        when(userService.findUserById(anyLong())).thenReturn(user2);
//        when(requestRepository.save(any(Request.class))).thenReturn(this.request);
//
//        requestService.addRequest(request, 1L);
//
//        verify(requestRepository, times(1)).save(any(Request.class));
//    }
//
//    @Test
//    void getRequestById() {
//        when(requestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(request));
//
//        Request result = requestService.getRequestById(1L);
//
//        verify(requestRepository, times(1)).findById(anyLong());
//
//        assertNotNull(result);
//        assertThat(result.getDescription()).isEqualTo("Мне нужна лопата");
//    }
//
//    @Test
//    void getRequestByIdIfRequestIdIsIncorrect() {
//        doThrow(EntityNotFoundException.class)
//                .when(requestRepository).findById(anyLong());
//
//        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestById(100L));
//    }
//
////    @Test
////    void findAllByRequestorIdShouldBeOk() {
////        doNothing()
////                .when(userService)
////                .checkIfUserExists(anyLong());
////
////        when(itemService.getOwnerItems(any(), Pageable.unpaged()))
////                .thenReturn(anyList());
////
////        requestService.getAllRequestsByUserId(1L);
////
////        verify(requestRepository, times(1)).findAllByRequestorId(anyLong());
////    }
//
//    @Test
//    void getAllRequestByUserId_IfUserIdIsIncorrect() {
//        doThrow(EntityNotFoundException.class)
//                .when(userService).checkIfUserExists(anyLong());
//
//        assertThrows(EntityNotFoundException.class, () -> requestService.getAllRequestsByUserId(anyLong()));
//    }
//
//    @Test
//    void getRequestByUserIdIfUserIdIsIncorrect() {
//        doThrow(EntityNotFoundException.class)
//                .when(userService).checkIfUserExists(anyLong());
//
//        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestByUserId(100L, 1L));
//    }
//
//    @Test
//    void getRequestsOtherUsersIfUserIdIncorrect() {
//        doThrow(EntityNotFoundException.class)
//                .when(userService).checkIfUserExists(anyLong());
//
//        assertThrows(EntityNotFoundException.class, () -> requestService.getRequestsByOtherUsers(anyLong(), 0, 10));
//    }
//
//    @Test
//    void getRequestsOtherUsersShouldBeOk() {
//        doNothing()
//                .when(userService)
//                .checkIfUserExists(anyLong());
//
//        when(requestRepository.findAllByRequestorId(anyLong(), any(Pageable.class)))
//                .thenReturn(List.of(request));
//
//        when(itemService.findByRequestId(anyLong()))
//                .thenReturn(item);
//
//        requestService.getRequestsByOtherUsers(2L, 0, 10);
//
//        verify(requestRepository, times(1)).findAllByRequestorIdNot(anyLong(), any(Pageable.class));
//    }
//
//    @Test
//    void getRequestsOtherUsersIncorrectPageable() {
//        doNothing()
//                .when(userService)
//                .checkIfUserExists(anyLong());
//
//        assertThrows(IllegalArgumentException.class, () -> requestService.getRequestsByOtherUsers(1L, -1, -1));
//    }
}