package ru.practicum.shareit.bookingTest;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.practicum.shareit.booking.controller.BookingController;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
//    @Autowired
//    MockMvc mvc;
//
//    @MockBean
//    BookingService bookingService;
//
//    @Autowired
//    ObjectMapper mapper;
//
//    String userHeader = "X-Sharer-User-Id";
//
////    @Test
////    void getAllBookingsByBookerCorrectPageableParameters() throws Exception {
////        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
////                .id(10L)
////                .start(LocalDateTime.now().plusDays(10))
////                .end(LocalDateTime.now().plusDays(30))
////                .bookingStatus(BookingStatus.APPROVED)
////                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
////                .item(new ResponseBookingDto.Item(1L, "Лопата"))
////                .build();
////
////        when(bookingService.getAllByBooker(anyLong(), anyString(), anyInt(), anyInt()))
////                .thenReturn(List.of(bookingResponse));
////
////        mvc.perform(MockMvcRequestBuilders.get("/bookings")
////                        .header(userHeader, 1)
////                        .param("from", "1")
////                        .param("size", "1")
////                        .param("state", "ALL"))
////                .andDo(print())
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(BookingStatus.APPROVED.toString()))
////                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.name").value("Варвара"))
////                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Лопата"));
////    }
//
//    @Test
//    void getAllBookingsByBookerIncorrectPageableParameters() throws Exception {
//        doThrow(InvalidRequestException.class)
//                .when(bookingService)
//                .getAllByBooker(anyLong(), anyString(), anyInt(), anyInt());
//
//        mvc.perform(MockMvcRequestBuilders.get("/bookings")
//                        .header(userHeader, 1)
//                        .param("from", "-1")
//                        .param("size", "-1")
//                        .param("state", "ALL"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist());
//    }
//
////    @Test
////    void getAllBookingsByOwnerCorrectPageableParameters() throws Exception {
////        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
////                .id(10L)
////                .start(LocalDateTime.now().plusDays(10))
////                .end(LocalDateTime.now().plusDays(30))
////                .bookingStatus(BookingStatus.APPROVED)
////                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
////                .item(new ResponseBookingDto.Item(1L, "Лопата"))
////                .build();
////
////        when(bookingService.getAllByOwner(anyLong(), anyString(), anyInt(), anyInt()))
////                .thenReturn(List.of(bookingResponse));
////
////        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
////                        .header(userHeader, 1)
////                        .param("from", "1")
////                        .param("size", "1")
////                        .param("state", "ALL"))
////                .andDo(print())
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value(BookingStatus.APPROVED.toString()))
////                .andExpect(MockMvcResultMatchers.jsonPath("$[0].booker.name").value("Варвара"))
////                .andExpect(MockMvcResultMatchers.jsonPath("$[0].item.name").value("Лопата"));
////    }
//
//    @Test
//    void getAllBookingsByOwnerIncorrectPageableParameters() throws Exception {
//        doThrow(InvalidRequestException.class)
//                .when(bookingService)
//                .getAllByOwner(anyLong(), anyString(), anyInt(), anyInt());
//
//        mvc.perform(MockMvcRequestBuilders.get("/bookings/owner")
//                        .header(userHeader, 1)
//                        .param("from", "-1")
//                        .param("size", "-1")
//                        .param("state", "ALL"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").doesNotExist());
//    }
//
//    @Test
//    void addBookingStatusNotFoundIfItemDoesNotExist() throws Exception {
//        RequestBookingDto requestBookingDto = RequestBookingDto.builder()
//                .itemId(1L)
//                .start(LocalDateTime.now().plusDays(10))
//                .end(LocalDateTime.now().plusDays(30))
//                .build();
//
//        doThrow(EntityNotFoundException.class)
//                .when(bookingService).addBooking(anyLong(), any(RequestBookingDto.class));
//
//        mvc.perform(post("/bookings")
//                        .header(userHeader, 1)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(mapper.writeValueAsString(requestBookingDto)))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
//    }
//
//    @Test
//    void addBookingShouldReturnBadRequestIfStatusIsNotAvailable() throws Exception {
//        RequestBookingDto bookingRequest = RequestBookingDto.builder()
//                .itemId(1L)
//                .start(LocalDateTime.now().plusDays(10))
//                .end(LocalDateTime.now().plusDays(30))
//                .build();
//
//        doThrow(InvalidRequestException.class)
//                .when(bookingService).addBooking(anyLong(), any(RequestBookingDto.class));
//
//        mvc.perform(post("/bookings")
//                        .header(userHeader, 1)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(mapper.writeValueAsString(bookingRequest)))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
//    }
//
//    @Test
//    void addBookingShouldReturnBadRequestIfTimeIsIncorrect() throws Exception {
//        RequestBookingDto bookingRequest = RequestBookingDto.builder()
//                .itemId(1L)
//                .start(LocalDateTime.now().plusDays(10))
//                .end(LocalDateTime.now().minusDays(5))
//                .build();
//
//        doThrow(InvalidRequestException.class)
//                .when(bookingService).addBooking(anyLong(), any(RequestBookingDto.class));
//
//        mvc.perform(post("/bookings")
//                        .header(userHeader, 1)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(mapper.writeValueAsString(bookingRequest)))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
//    }
//
//    @Test
//    void addBookingShouldReturnCreated() throws Exception {
//        RequestBookingDto bookingRequest = RequestBookingDto.builder()
//                .itemId(1L)
//                .start(LocalDateTime.now().plusDays(10))
//                .end(LocalDateTime.now().plusDays(30))
//                .build();
//
//        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
//                .id(10L)
//                .start(LocalDateTime.now().plusDays(10))
//                .end(LocalDateTime.now().plusDays(30))
//                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
//                .item(new ResponseBookingDto.Item(1L, "Лопата"))
//                .build();
//
//        when(bookingService.addBooking(anyLong(), any(RequestBookingDto.class)))
//                .thenReturn(bookingResponse);
//
//        mvc.perform(post("/bookings")
//                        .header(userHeader, 1)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(mapper.writeValueAsString(bookingRequest)))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("Варвара"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Лопата"));
//    }
//
//    @Test
//    void tryChangeBookingStatusStatusNotFound() throws Exception {
//
//        doThrow(EntityNotFoundException.class)
//                .when(bookingService).updateStatus(anyLong(), anyBoolean(), anyLong());
//
//        mvc.perform(patch("/bookings/1")
//                        .header(userHeader, 1)
//                        .param("approved", "true"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
//    }
//
//    @Test
//    void tryChangeBookingStatusStatusBadRequest() throws Exception {
//
//        doThrow(InvalidRequestException.class)
//                .when(bookingService).updateStatus(anyLong(), anyBoolean(), anyLong());
//
//        mvc.perform(patch("/bookings/1")
//                        .header(userHeader, 1)
//                        .param("approved", "true"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
//    }
//
////    @Test
////    void tryChangeBookingStatusStatusOk() throws Exception {
////        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
////                .id(10L)
////                .start(LocalDateTime.now().plusDays(10))
////                .end(LocalDateTime.now().plusDays(30))
////                .bookingStatus(BookingStatus.APPROVED)
////                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
////                .item(new ResponseBookingDto.Item(1L, "Лопата"))
////                .build();
////
////        when(bookingService.updateStatus(anyLong(), anyBoolean(), anyLong()))
////                .thenReturn(bookingResponse);
////
////        mvc.perform(patch("/bookings/1")
////                        .header(userHeader, 1)
////                        .param("approved", "true"))
////                .andDo(print())
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(BookingStatus.APPROVED.toString()))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("Варвара"))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Лопата"));
////    }
//
////    @Test
////    void tryGetBookingIfBookingExists() throws Exception {
////        ResponseBookingDto bookingResponse = ResponseBookingDto.builder()
////                .id(10L)
////                .start(LocalDateTime.now().plusDays(10))
////                .end(LocalDateTime.now().plusDays(30))
////                .bookingStatus(BookingStatus.APPROVED)
////                .booker(new ResponseBookingDto.Booker(1L, "Варвара"))
////                .item(new ResponseBookingDto.Item(1L, "Лопата"))
////                .build();
////
////        when(bookingService.findById(anyLong(), anyLong()))
////                .thenReturn(bookingResponse);
////
////        mvc.perform(MockMvcRequestBuilders.get("/bookings/1")
////                        .header(userHeader, 1))
////                .andDo(print())
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(BookingStatus.APPROVED.toString()))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.booker.name").value("Варвара"))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.item.name").value("Лопата"));
////    }
//
//    @Test
//    void tryGetBookingIfBookingDoesNotExist() throws Exception {
//        doThrow(EntityNotFoundException.class)
//                .when(bookingService).findById(anyLong(), anyLong());
//
//        mvc.perform(MockMvcRequestBuilders.get("/bookings/1")
//                        .header(userHeader, 1))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isNotFound())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist());
//    }

//    @Nested
//    class ValidationBookingTest {
//        Validator validator;
//
//        @BeforeEach
//        void beforeEach() {
//            try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
//                validator = validatorFactory.getValidator();
//            }
//        }
//
//        @Test
//        void notValidIfNameIsNull() {
//            RequestBookingDto test = RequestBookingDto.builder()
//                    .itemId(null)
//                    .start(LocalDateTime.now().plusDays(1))
//                    .end(LocalDateTime.now().plusDays(1))
//                    .build();
//
//            List<ConstraintViolation<RequestBookingDto>> validationSet = new ArrayList<>(validator.validate(test));
//            assertAll(
//                    () -> assertEquals(1, validationSet.size()),
//                    () -> assertEquals("must not be null", validationSet.get(0).getMessage())
//            );
//        }
//
//        @Test
//        void notValidIfStartTimeInThePast() {
//            RequestBookingDto test = RequestBookingDto.builder()
//                    .itemId(1L)
//                    .start(LocalDateTime.now().minusDays(1))
//                    .end(LocalDateTime.now().plusDays(1))
//                    .build();
//
//            List<ConstraintViolation<RequestBookingDto>> validationSet = new ArrayList<>(validator.validate(test));
//            assertAll(
//                    () -> assertEquals(1, validationSet.size()),
//                    () -> assertEquals("Время начала не может быть в прошлом", validationSet.get(0).getMessage())
//            );
//        }
//
//        @Test
//        void notValidIfStartTimeIsEmpty() {
//            RequestBookingDto test = RequestBookingDto.builder()
//                    .itemId(1L)
//                    .start(null)
//                    .end(LocalDateTime.now().plusDays(1))
//                    .build();
//
//            List<ConstraintViolation<RequestBookingDto>> validationSet = new ArrayList<>(validator.validate(test));
//            assertAll(
//                    () -> assertEquals(1, validationSet.size()),
//                    () -> assertEquals("Время начала не может быть пустым", validationSet.get(0).getMessage())
//            );
//        }
//
//        @Test
//        void notValidIfEndTimeIsEmpty() {
//            RequestBookingDto test = RequestBookingDto.builder()
//                    .itemId(1L)
//                    .start(LocalDateTime.now().plusDays(1))
//                    .end(null)
//                    .build();
//
//            List<ConstraintViolation<RequestBookingDto>> validationSet = new ArrayList<>(validator.validate(test));
//            assertAll(
//                    () -> assertEquals(1, validationSet.size()),
//                    () -> assertEquals("Время окончания не может быть пустым", validationSet.get(0).getMessage())
//            );
//        }
//
//        @Test
//        void notValidIfEndTimeIsInThePast() {
//            RequestBookingDto test = RequestBookingDto.builder()
//                    .itemId(1L)
//                    .start(LocalDateTime.now().plusDays(2))
//                    .end(LocalDateTime.now().minusDays(1))
//                    .build();
//
//            List<ConstraintViolation<RequestBookingDto>> validationSet = new ArrayList<>(validator.validate(test));
//            assertAll(
//                    () -> assertEquals(1, validationSet.size()),
//                    () -> assertEquals("Время окончания не может быть в прошлом", validationSet.get(0).getMessage())
//            );
//        }
//    }
}
