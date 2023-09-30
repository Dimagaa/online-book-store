package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.book.BookDto;
import com.app.onlinebookstore.dto.book.BookWithoutCategoryIdsDto;
import com.app.onlinebookstore.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    public static final String INSERT_IMMUTABLE_BOOKS_AND_CATEGORIES_SQL =
            "sql-scripts/books/InsertImmutableBooksAndCategories.sql";
    public static final String DELETE_IMMUTABLE_BOOKS_AND_CATEGORIES_SQL =
            "sql-scripts/books/DeleteImmutableBooksAndCategories.sql";
    public static final String INSERT_MUTABLE_BOOKS_SQL =
            "classpath:sql-scripts/books/InsertMutableBooks.sql";
    public static final String DELETE_MUTABLE_BOOKS_SQL =
            "classpath:sql-scripts/books/DeleteMutableBooks.sql";
    protected static MockMvc mockMvc;
    private static Map<Long, BookDto> responseDtos;
    private static Map<Long, BookWithoutCategoryIdsDto> responseWithOutCategoriesDtos;
    private static Map<Long, CreateBookRequestDto> requestDtos;
    private static Map<Long, BookDto> createResponseDtos;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        deleteData(dataSource);
        insertData(dataSource);

        Long classic = 1L;
        Long dystopian = 2L;
        Long romantic = 3L;
        Long additional = 4L;

        BookDto bookDto1 = new BookDto(1L,
                "To Kill a Mockingbird",
                "Harper Lee",
                "9780061120084",
                BigDecimal.valueOf(9.99),
                "A classic novel",
                "/book1.jpg",
                Set.of(classic));

        BookDto bookDto2 = new BookDto(2L,
                "1984",
                "George Orwell",
                "9780451524935",
                BigDecimal.valueOf(10.54),
                "A dystopian novel",
                "/book2.jpg",
                Set.of(dystopian, additional));

        BookDto bookDto3 = new BookDto(3L,
                "Pride and Prejudice",
                "Jane Austen",
                "9780486284736",
                BigDecimal.valueOf(7.99),
                "A romantic novel",
                "/images/book3.jpg",
                Set.of(romantic, additional));

        responseDtos = Map.of(1L, bookDto1, 2L, bookDto2, 3L, bookDto3);

        BookWithoutCategoryIdsDto bookWithoutCategoryIdsDto1 = new BookWithoutCategoryIdsDto(
                1L,
                "To Kill a Mockingbird",
                "Harper Lee",
                "9780061120084",
                BigDecimal.valueOf(9.99),
                "A classic novel",
                "/book1.jpg");

        BookWithoutCategoryIdsDto bookWithoutCategoryIdsDto2 = new BookWithoutCategoryIdsDto(
                2L,
                "1984",
                "George Orwell",
                "9780451524935",
                BigDecimal.valueOf(10.54),
                "A dystopian novel",
                "/book2.jpg");

        BookWithoutCategoryIdsDto bookWithoutCategoryIdsDto3 = new BookWithoutCategoryIdsDto(
                3L,
                "Pride and Prejudice",
                "Jane Austen",
                "9780486284736",
                BigDecimal.valueOf(7.99),
                "A romantic novel",
                "/images/book3.jpg");

        responseWithOutCategoriesDtos = Map.of(
                1L, bookWithoutCategoryIdsDto1,
                2L, bookWithoutCategoryIdsDto2,
                3L, bookWithoutCategoryIdsDto3
        );

        CreateBookRequestDto createBookRequestDto1 = new CreateBookRequestDto(
                "The Martian",
                "Andy Weir",
                "9780553418026",
                BigDecimal.valueOf(11.99),
                "A science fiction novel about an astronaut "
                        + "stranded on Mars and his fight for survival.",
                "https://www.example.com/images/book25.jpg",
                Set.of(1L));

        CreateBookRequestDto createBookRequestDto2 = new CreateBookRequestDto(
                "Educated",
                "Tara Westover",
                "9780399590504",
                BigDecimal.valueOf(13.50),
                "A memoir about a woman who grows up in a strict and"
                        + " abusive household but eventually escapes to earn a Ph.D.",
                "https://www.example.com/images/book26.jpg",
                Set.of(2L, 4L));

        CreateBookRequestDto createBookRequestDto3 = new CreateBookRequestDto(
                "The Nightingale",
                "Kristin Hannah",
                "9780312577223",
                BigDecimal.valueOf(14.75),
                "A historical novel set in Nazi-occupied France,"
                        + "following the lives of two sisters and their struggle for survival.",
                "https://www.example.com/images/book27.jpg",
                Set.of(3L, 4L));

        requestDtos = Map.of(
                101L, createBookRequestDto1,
                102L, createBookRequestDto2,
                103L, createBookRequestDto3
        );

        BookDto createdBook1 = new BookDto(
                101L,
                "The Martian",
                "Andy Weir",
                "9780553418026",
                BigDecimal.valueOf(11.99),
                "A science fiction novel about an astronaut "
                        + "stranded on Mars and his fight for survival.",
                "https://www.example.com/images/book25.jpg",
                Set.of(1L));

        BookDto createdBook2 = new BookDto(
                102L,
                "Educated",
                "Tara Westover",
                "9780399590504",
                BigDecimal.valueOf(13.50),
                "A memoir about a woman who grows up in a strict"
                        + " and abusive household but eventually escapes to earn a Ph.D.",
                "https://www.example.com/images/book26.jpg",
                Set.of(2L, 4L));

        BookDto createdBook3 = new BookDto(
                103L,
                "The Nightingale",
                "Kristin Hannah",
                "9780312577223",
                BigDecimal.valueOf(14.75),
                "A historical novel set in Nazi-occupied France,"
                        + "following the lives of two sisters and their struggle for survival.",
                "https://www.example.com/images/book27.jpg",
                Set.of(3L, 4L));

        createResponseDtos = Map.of(
                101L, createdBook1,
                102L, createdBook2,
                103L, createdBook3);
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        deleteData(dataSource);
    }

    @WithMockUser(username = "user")
    @SneakyThrows
    @Test
    @DisplayName("getAll: When Books Exist, Return List of BookDtos")
    void getAll_WhenBooksExist_ReturnListOfBookDtos() {
        List<BookDto> expected = responseDtos.values().stream()
                .sorted(Comparator.comparingLong(BookDto::id))
                .toList();

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), new TypeReference<>() {}
        );
        actual.sort(Comparator.comparingLong(BookDto::id));

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @TestFactory
    @DisplayName("getById: When Book Exists, Return BookDto")
    Stream<DynamicTest> getById_WhenBookExists_ReturnBookDto() {
        return responseDtos.values().stream()
                .map((expected) -> DynamicTest.dynamicTest(
                        "Get BookDto by id: " + expected.id(),
                        () -> {
                            MvcResult mvcResult = mockMvc.perform(
                                            MockMvcRequestBuilders.get(
                                                            "/books/" + expected.id())
                                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(MockMvcResultMatchers.status().isOk())
                                    .andReturn();

                            BookDto actual = objectMapper.readValue(
                                    mvcResult.getResponse().getContentAsByteArray(),
                                    BookDto.class);

                            Assertions.assertEquals(expected, actual);
                        }
                ));

    }

    @WithMockUser(username = "user")
    @SneakyThrows
    @Test
    @DisplayName("getById: When Book Does Not Exist, Return 404 NotFound")
    void getById_WhenBookDoesNotExist_Return404NotFound() {
        long id = -999L;

        mockMvc.perform(
                        MockMvcRequestBuilders.get(
                                        "/books/" + id)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @TestFactory
    @DisplayName("create: When Valid Book Request, Return Created BookDto")
    @Sql(scripts = DELETE_MUTABLE_BOOKS_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> create_WhenValidBookRequest_ReturnCreatedBookDto() {
        return requestDtos.entrySet().stream()
                .map(entry -> DynamicTest.dynamicTest(
                        "Creating book with isbn: " + entry.getValue().isbn(),
                        () -> {
                            String request = objectMapper.writeValueAsString(entry.getValue());
                            BookDto expected = responseDtos.get(entry.getKey());

                            MvcResult result = mockMvc.perform(
                                            MockMvcRequestBuilders.post("/books")
                                                    .content(request)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                    )
                                    .andExpect(MockMvcResultMatchers.status().isCreated())
                                    .andReturn();

                            BookDto actual = objectMapper.readValue(
                                    result.getResponse().getContentAsByteArray(),
                                    BookDto.class);

                            Assertions.assertNotNull(actual);
                            Assertions.assertNotNull(actual.id());
                            EqualsBuilder.reflectionEquals(expected, actual, "id");
                        }
                ));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @TestFactory
    @DisplayName("update: When Valid Book Request and Book Exists, Return Updated BookDto")
    @Sql(scripts = INSERT_MUTABLE_BOOKS_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_MUTABLE_BOOKS_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> update_WhenValidBookRequestAndBookExists_ReturnUpdatedBookDto() {
        return requestDtos.entrySet().stream()
                .map((entry) -> DynamicTest.dynamicTest(
                        "Update book with isbn: " + entry.getValue().isbn(),
                        () -> {
                            CreateBookRequestDto modifiedRequest = modifyRequestDto(
                                    entry.getValue());
                            String request = objectMapper.writeValueAsString(modifiedRequest);
                            BookDto expected = modifyResponseDto(
                                    createResponseDtos.get(entry.getKey()));

                            MvcResult result = mockMvc.perform(
                                            MockMvcRequestBuilders.put(
                                                            "/books/" + entry.getKey())
                                                    .content(request)
                                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(MockMvcResultMatchers.status().isOk())
                                    .andReturn();

                            BookDto actual = objectMapper.readValue(
                                    result.getResponse().getContentAsByteArray(),
                                    BookDto.class);

                            Assertions.assertEquals(expected, actual);
                            Assertions.assertEquals(expected.categories(), actual.categories());
                        })
                );
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("update: When Valid Book Request and Book Does Not Exist, Return 404 NotFound")
    void update_WhenValidBookRequestAndBookDoesNotExist_Return404NotFound() throws Exception {
        long id = 999L;
        String request = objectMapper.writeValueAsString(requestDtos.get(101L));

        mockMvc.perform(MockMvcRequestBuilders.put("/books/" + id)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @TestFactory
    @DisplayName("deleteById: When Book Exists, Return 204 NoContent")
    @Sql(scripts = INSERT_MUTABLE_BOOKS_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    Stream<DynamicTest> deleteById_WhenBookExists_Return204NoContent() {
        return requestDtos.keySet().stream()
                .map(id -> DynamicTest.dynamicTest(
                        "Delete by id: " + id,
                        () -> mockMvc.perform(MockMvcRequestBuilders
                                        .delete("/books/" + id
                                        ))
                                .andExpect(MockMvcResultMatchers
                                        .status()
                                        .isNoContent())
                ));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @SneakyThrows
    @Test
    @DisplayName("deleteById: When Book Does Not Exist, Return 404 NotFound")
    void deleteById_WhenBookDoesNotExist_Return404NotFound() {
        long id = 999L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/books/" + id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("search: When Books Match Search Criteria, Return List of BookDtos")
    void search_WhenBooksMatchSearchCriteria_ReturnListOfBookDtos() throws Exception {
        TypeReference<List<BookWithoutCategoryIdsDto>> typeReference = new TypeReference<>() {
        };

        List<BookWithoutCategoryIdsDto> expectedByTitle = List.of(
                responseWithOutCategoriesDtos.get(1L),
                responseWithOutCategoriesDtos.get(3L));
        List<BookWithoutCategoryIdsDto> expectedByTitleAndAuthor = List.of(
                responseWithOutCategoriesDtos.get(1L));
        List<BookWithoutCategoryIdsDto> expectedByTitleAndAuthorAndIsbn = List.of(
                responseWithOutCategoriesDtos.get(2L));

        MvcResult searchByTitle = mockMvc.perform(
                        (MockMvcRequestBuilders.get("/books/search"))
                                .param("title", "a")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult searchByTitleAndAuthor = mockMvc.perform(
                        (MockMvcRequestBuilders.get("/books/search"))
                                .param("title", "bird")
                                .param("author", "Harper Lee")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult searchByTitleAndAuthorAndIsbn = mockMvc.perform(
                        (MockMvcRequestBuilders.get("/books/search"))
                                .param("title", "98")
                                .param("author", "George Orwell")
                                .param("isbn", "9780451524935")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<BookWithoutCategoryIdsDto> actualByTitle = objectMapper.readValue(
                searchByTitle.getResponse().getContentAsByteArray(),
                typeReference
        );

        List<BookWithoutCategoryIdsDto> actualByTitleAndAuthor = objectMapper.readValue(
                searchByTitleAndAuthor.getResponse().getContentAsByteArray(),
                typeReference);

        List<BookWithoutCategoryIdsDto> actualByTitleAndAuthorAndIsbn = objectMapper.readValue(
                searchByTitleAndAuthorAndIsbn.getResponse().getContentAsByteArray(),
                typeReference);

        Assertions.assertIterableEquals(expectedByTitle, actualByTitle);
        Assertions.assertIterableEquals(expectedByTitleAndAuthor, actualByTitleAndAuthor);
        Assertions.assertIterableEquals(expectedByTitleAndAuthorAndIsbn,
                actualByTitleAndAuthorAndIsbn);
    }

    @SneakyThrows
    private static void insertData(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    INSERT_IMMUTABLE_BOOKS_AND_CATEGORIES_SQL
            ));
        }
    }

    @SneakyThrows
    private static void deleteData(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    DELETE_IMMUTABLE_BOOKS_AND_CATEGORIES_SQL
            ));
        }
    }

    private CreateBookRequestDto modifyRequestDto(CreateBookRequestDto requestDto) {
        String title = requestDto.title().toUpperCase();
        String author = requestDto.author().toUpperCase();
        String isbn = requestDto.isbn();
        BigDecimal price = requestDto.price();
        String description = requestDto.description().toUpperCase();
        String coverImage = requestDto.coverImage().toUpperCase();
        Set<Long> categories = Set.of(1L, 2L, 3L, 4L);
        return new CreateBookRequestDto(title, author, isbn,
                price, description, coverImage, categories);
    }

    private BookDto modifyResponseDto(BookDto bookDto) {
        String title = bookDto.title().toUpperCase();
        String author = bookDto.author().toUpperCase();
        String isbn = bookDto.isbn();
        BigDecimal price = bookDto.price();
        String description = bookDto.description().toUpperCase();
        String coverImage = bookDto.coverImage().toUpperCase();
        Set<Long> categories = Set.of(1L, 2L, 3L, 4L);
        return new BookDto(bookDto.id(), title, author, isbn,
                price, description, coverImage, categories);
    }
}
