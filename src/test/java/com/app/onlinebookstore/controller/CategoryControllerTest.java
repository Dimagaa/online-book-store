package com.app.onlinebookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.onlinebookstore.dto.book.BookWithoutCategoryIdsDto;
import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CategoryRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final String INSERT_BOOKS_AND_CATEGORIES_SQL =
            "classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql";
    private static final String DELETE_BOOKS_AND_CATEGORIES_SQL =
            "classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql";
    private static final String DELETE_CATEGORIES_SQL =
            "classpath:sql-scripts/books/DeleteCategories.sql";
    private static final String INSERT_CATEGORIES_SQL =
            "classpath:sql-scripts/books/InsertCategories.sql";
    private static Map<Long, CategoryRequestDto> requestDtos;
    private static Map<Long, CategoryDto> categoryDtos;
    private static Map<Long, BookWithoutCategoryIdsDto> responseWithOutCategoriesDtos;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        CategoryRequestDto requestDto1 = new CategoryRequestDto(
                "Fantasy Adventure",
                "Fantasy adventure books"
        );

        CategoryRequestDto requestDto2 = new CategoryRequestDto(
                "Dystopian Fiction",
                "Novels in a dystopian setting"
        );

        CategoryRequestDto requestDto3 = new CategoryRequestDto(
                "Post-Apocalyptic",
                "Fiction, Post-apocalyptic novels"
        );
        requestDtos = Map.of(1L, requestDto1, 2L, requestDto2, 3L, requestDto3);

        CategoryDto categoryDto1 = new CategoryDto(
                1L,
                "Fantasy Adventure",
                "Fantasy adventure books"
        );
        CategoryDto categoryDto2 = new CategoryDto(
                2L,
                "Dystopian Fiction",
                "Novels in a dystopian setting"
        );
        CategoryDto categoryDto3 = new CategoryDto(
                3L,
                "Post-Apocalyptic",
                "Fiction, Post-apocalyptic novels"
        );
        categoryDtos = Map.of(1L, categoryDto1, 2L, categoryDto2, 3L, categoryDto3);

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
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @TestFactory
    @DisplayName("create: When Valid Category Create Request, Return Created CategoryDto")
    @Sql(scripts = DELETE_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> create_WhenValidCategoryCreateRequest_ReturnCreatedCategoryDto() {
        return requestDtos.entrySet().stream()
                .map((entry -> DynamicTest.dynamicTest(
                        "Create category: " + entry.getValue().name(),
                        () -> {
                            String request = objectMapper.writeValueAsString(entry.getValue());
                            CategoryDto expected = categoryDtos.get(entry.getKey());
                            MockHttpServletResponse response = mockMvc.perform(
                                            post("/categories")
                                                    .content(request)
                                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isCreated())
                                    .andReturn().getResponse();

                            CategoryDto actual = objectMapper.readValue(
                                    response.getContentAsByteArray(),
                                    CategoryDto.class
                            );

                            EqualsBuilder.reflectionEquals(expected, actual, "id");
                        })));
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("create: When Category with Same Name Exists, Return 409 Conflict")
    @Sql(scripts = DELETE_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create_WhenCategoryWithSameNameExists_Return409Conflict() {
        String request = objectMapper.writeValueAsString(requestDtos.get(1L));

        mockMvc.perform(post("/categories")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/categories")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("create: When Invalid Category Create Request, Return 400 Bad Request")
    void create_WhenInvalidCategoryCreateRequest_Return400BadRequest() {
        CategoryRequestDto invalidRequestDto1 = new CategoryRequestDto(
                "Fantasy Adventure", ""
        );
        CategoryRequestDto invalidRequestDto2 = new CategoryRequestDto(
                null, "Fantasy adventure books"
        );
        String invalidRequest1 = objectMapper.writeValueAsString(invalidRequestDto1);
        String invalidRequest2 = objectMapper.writeValueAsString(invalidRequestDto2);

        mockMvc.perform(post("/categories")
                        .content(invalidRequest1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/categories")
                        .content(invalidRequest2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @WithMockUser(username = "user")
    @Test
    @DisplayName("getAll: When Categories Exist, Return List of CategoryDtos")
    @Sql(scripts = INSERT_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_WhenCategoriesExist_ReturnListOfCategoryDtos() {
        List<CategoryDto> expected = categoryDtos.values()
                .stream()
                .sorted(Comparator.comparingLong(CategoryDto::id))
                .toList();

        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                new TypeReference<>() {
                }
        );
        actual.sort(Comparator.comparingLong(CategoryDto::id));

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertIterableEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @TestFactory
    @DisplayName("getById: When Category Exists, Return CategoryDto")
    @Sql(scripts = INSERT_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> getById_WhenCategoryExists_ReturnCategoryDto() {
        return categoryDtos.values().stream()
                .map((expected -> DynamicTest.dynamicTest(
                        "Get by id: " + expected.id(),
                        () -> {
                            MockHttpServletResponse response = mockMvc.perform(
                                            get("/categories/" + expected.id())
                                                    .contentType(MediaType.APPLICATION_JSON)
                                    )
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse();

                            CategoryDto actual = objectMapper.readValue(
                                    response.getContentAsByteArray(), CategoryDto.class
                            );

                            Assertions.assertEquals(expected, actual);
                        })));
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user")
    @DisplayName("getById: When Category Does Not Exist, Return 404 NotFound")
    void getById_WhenCategoryDoesNotExist_Return404NotFound() {
        long id = -999L;

        mockMvc.perform(get("/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @TestFactory
    @DisplayName("update: When Request Valid and Category Exists, Return Updated CategoryDto")
    @Sql(scripts = INSERT_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> update_WhenValidRequestAndCategoryExists_ReturnUpdatedCategoryDto() {
        return categoryDtos.values()
                .stream()
                .map(categoryDto -> DynamicTest.dynamicTest(
                        "Update category by id: " + categoryDto.id(),
                        () -> {
                            CategoryRequestDto requestDto = modifyRequestDto(
                                    requestDtos.get(categoryDto.id())
                            );
                            String request = objectMapper.writeValueAsString(requestDto);
                            CategoryDto expected = modifyCategoryDto(categoryDto);

                            MockHttpServletResponse response = mockMvc.perform(put(
                                            "/categories/" + categoryDto.id())
                                            .content(request)
                                            .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse();

                            CategoryDto actual = objectMapper.readValue(
                                    response.getContentAsByteArray(), CategoryDto.class
                            );

                            Assertions.assertEquals(expected, actual);
                        }));
    }

    @SneakyThrows
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("update: When Valid Request and Category Does Not Exist, Return 404 NotFound")
    @Sql(scripts = INSERT_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_WhenValidCategoryCreateRequestAndCategoryDoesNotExist_Return404NotFound() {
        long id = -999L;
        String request = objectMapper.writeValueAsString(requestDtos.get(1L));

        mockMvc.perform(put("/categories/" + id)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @TestFactory
    @DisplayName("delete: When Category Exists, Return 204 NoContent")
    @Sql(scripts = INSERT_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> delete_WhenCategoryExists_Return204NoContent() {
        return categoryDtos.keySet().stream()
                .map(id -> DynamicTest.dynamicTest(
                        "Delete by id: " + id,
                        () -> mockMvc.perform(delete(
                                        "/categories/" + id)
                                        .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent())));
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("delete: When Category Does Not Exist, Return 404 NotFound")
    void delete_WhenCategoryDoesNotExist_Return404NotFound() {
        long id = -999L;
        String request = objectMapper.writeValueAsString(requestDtos.get(1L));

        mockMvc.perform(delete("/categories/" + id)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user")
    @DisplayName(
            "getBooksByCategoryId: When Books Exist for Category, "
                    + "Return List of BookWithoutCategoryIdsDtos"
    )
    @Sql(scripts = INSERT_BOOKS_AND_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = DELETE_BOOKS_AND_CATEGORIES_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_WhenBooksExistForCategory_ReturnListOfBookDtos() {
        final List<BookWithoutCategoryIdsDto> expectedForClassic = List.of(
                responseWithOutCategoriesDtos.get(1L)
        );
        final List<BookWithoutCategoryIdsDto> expectedForDystopian = List.of(
                responseWithOutCategoriesDtos.get(2L)
        );
        final List<BookWithoutCategoryIdsDto> expectedForAdditional = List.of(
                responseWithOutCategoriesDtos.get(2L),
                responseWithOutCategoriesDtos.get(3L)
        );

        MockHttpServletResponse responseForClassic = mockMvc.perform(
                        get("/categories/1/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        MockHttpServletResponse responseForDystopian = mockMvc.perform(
                        get("/categories/2/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        MockHttpServletResponse responseForAdditional = mockMvc.perform(
                        get("/categories/4/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        List<BookWithoutCategoryIdsDto> actualForClassic = objectMapper.readValue(
                responseForClassic.getContentAsByteArray(), new TypeReference<>() {
                }
        );
        List<BookWithoutCategoryIdsDto> actualForDystopian = objectMapper.readValue(
                responseForDystopian.getContentAsByteArray(), new TypeReference<>() {
                }
        );
        List<BookWithoutCategoryIdsDto> actualForAdditional = objectMapper.readValue(
                responseForAdditional.getContentAsByteArray(), new TypeReference<>() {
                }
        );
        actualForAdditional.sort(Comparator.comparingLong(BookWithoutCategoryIdsDto::id));

        Assertions.assertEquals(expectedForClassic, actualForClassic);
        Assertions.assertEquals(expectedForDystopian, actualForDystopian);
        Assertions.assertEquals(expectedForAdditional, actualForAdditional);
    }

    private CategoryRequestDto modifyRequestDto(CategoryRequestDto requestDto) {
        return new CategoryRequestDto(
                requestDto.name().toUpperCase(),
                requestDto.description().toUpperCase()
        );
    }

    private CategoryDto modifyCategoryDto(CategoryDto categoryDto) {
        return new CategoryDto(
                categoryDto.id(),
                categoryDto.name().toUpperCase(),
                categoryDto.description().toUpperCase()
        );
    }
}
