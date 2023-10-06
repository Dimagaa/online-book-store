package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.book.BookDto;
import com.app.onlinebookstore.dto.book.BookSearchParameters;
import com.app.onlinebookstore.dto.book.BookWithoutCategoryIdsDto;
import com.app.onlinebookstore.dto.book.CreateBookRequestDto;
import com.app.onlinebookstore.exception.EntityAlreadyExistsException;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.exception.EntityProcessingException;
import com.app.onlinebookstore.mapper.BookMapper;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.repository.book.BookRepository;
import com.app.onlinebookstore.repository.book.BookSpecificationBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static final Pageable PAGEABLE = Pageable.ofSize(10);
    private static Map<Long, Book> books;
    private static Map<Long, BookDto> bookResponses;
    private static Map<Long, CreateBookRequestDto> bookRequests;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryServiceImpl categoryService;
    @Mock
    private BookSpecificationBuilder specificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeAll
    static void beforeAll() {
        Category category1 = new Category(1L,
                "Classic novel",
                "A classic novel",
                false
        );
        Category category2 = new Category(2L,
                "Dystopian novel",
                "A dystopian novel",
                false
        );
        Category category3 = new Category(3L,
                "Romantic novel",
                "A romantic novel",
                false
        );
        Category additional = new Category(4L,
                "Additional",
                "Additional category",
                false
        );
        Book book1 = new Book(1L,
                "To Kill a Mockingbird",
                "Harper Lee",
                "9780061120084",
                BigDecimal.valueOf(9.99),
                "A classic novel",
                "/book1.jpg",
                false,
                Set.of(category1)
        );
        Book book2 = new Book(2L,
                "1984",
                "George Orwell",
                "9780451524935",
                BigDecimal.valueOf(10.50).setScale(2, RoundingMode.HALF_UP),
                "A dystopian novel",
                "/book2.jpg",
                false,
                Set.of(category2, additional)
        );
        Book book3 = new Book(3L,
                "Pride and Prejudice",
                "Jane Austen",
                "9780486284736",
                BigDecimal.valueOf(7.99),
                "A romantic novel",
                "/images/book3.jpg",
                false,
                Set.of(category3, additional)
        );
        books = Map.of(1L, book1, 2L, book2, 3L, book3);

        BookDto bookDto1 = new BookDto(1L,
                "To Kill a Mockingbird",
                "Harper Lee",
                "9780061120084",
                BigDecimal.valueOf(9.99),
                "A classic novel",
                "/book1.jpg",
                Set.of(1L)
        );
        BookDto bookDto2 = new BookDto(2L,
                "1984",
                "George Orwell",
                "9780451524935",
                BigDecimal.valueOf(10.50),
                "A dystopian novel",
                "/book2.jpg",
                Set.of(2L, 4L)
        );
        BookDto bookDto3 = new BookDto(3L,
                "Pride and Prejudice",
                "Jane Austen",
                "9780486284736",
                BigDecimal.valueOf(7.99),
                "A romantic novel",
                "/images/book3.jpg",
                Set.of(3L, 4L)
        );
        bookResponses = Map.of(1L, bookDto1, 2L, bookDto2, 3L, bookDto3);

        CreateBookRequestDto requestDto1 = new CreateBookRequestDto(
                "To Kill a Mockingbird",
                "Harper Lee",
                "9780061120084",
                BigDecimal.valueOf(9.99),
                "A classic novel",
                "/book1.jpg",
                Set.of(1L)
        );
        CreateBookRequestDto requestDto2 = new CreateBookRequestDto(
                "1984",
                "George Orwell",
                "9780451524935",
                BigDecimal.valueOf(10.50),
                "A dystopian novel",
                "/book2.jpg",
                Set.of(2L, 4L)
        );
        CreateBookRequestDto requestDto3 = new CreateBookRequestDto(
                "Pride and Prejudice",
                "Jane Austen",
                "9780486284736",
                BigDecimal.valueOf(7.99),
                "A romantic novel",
                "/images/book3.jpg",
                Set.of(3L, 4L)
        );
        bookRequests = Map.of(1L, requestDto1, 2L, requestDto2, 3L, requestDto3);
    }

    @TestFactory
    @DisplayName("save: When Book ISBN Does Not Exist, Save and Return BookDto")
    Stream<DynamicTest> save_WhenBookIsbnDoesNotExist_SaveAndReturnBookDto() {
        return bookRequests.entrySet().stream()
                .map((entry) -> DynamicTest.dynamicTest("Save book: " + entry.getValue().isbn(),
                        () -> {
                            CreateBookRequestDto requestDto = entry.getValue();
                            Book book = books.get(entry.getKey());
                            BookDto expected = bookResponses.get(entry.getKey());

                            Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);
                            Mockito.when(bookRepository.findByIsbn(book.getIsbn()))
                                    .thenReturn(Optional.empty());
                            Mockito.when(categoryService.findAllById(requestDto.categories()))
                                    .thenReturn(book.getCategories());
                            Mockito.when(bookRepository.save(book)).thenReturn(book);
                            Mockito.when(bookMapper.toDto(book)).thenReturn(expected);

                            BookDto actual = bookService.save(requestDto);

                            Assertions.assertEquals(actual, expected);
                        }));
    }

    @Test
    @DisplayName("save: When Book ISBN Already Exists, Throw BookProcessingException")
    void save_WhenBookIsbnAlreadyExists_ThrowBookProcessingException() {
        CreateBookRequestDto requestDto = bookRequests.get(1L);
        Book book = books.get(1L);
        Mockito.when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));

        Exception exception = Assertions.assertThrows(EntityAlreadyExistsException.class,
                () -> bookService.save(requestDto));
        Assertions.assertEquals(
                "Book creation failed. A book with ISBN already exists: "
                        + book.getIsbn(),
                exception.getMessage());
    }

    @Test
    @DisplayName("save: When Categories Not Found, Throw BookProcessingException")
    void save_WhenCategoriesNotFound_ThrowBookProcessingException() {
        CreateBookRequestDto requestDto = bookRequests.get(1L);
        Book book = books.get(1L);

        Mockito.when(bookRepository.findByIsbn(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityProcessingException.class,
                () -> bookService.save(requestDto)
        );
        Assertions.assertEquals(
                "Book processing failed. There are not categories with id: "
                        + book.getId(),
                exception.getMessage());
    }

    @Test
    @DisplayName("findAll: When Books Exist, Return List of BookDtos")
    void findAll_WhenBooksExist_ReturnListOfBookDtos() {
        final List<BookDto> expected = bookResponses.values().stream()
                .sorted(Comparator.comparingLong(BookDto::id))
                .toList();

        Mockito.when(bookRepository.findAllWithCategories(PAGEABLE))
                .thenReturn(books.values().stream().toList());
        books.values().forEach((book) -> Mockito.when(bookMapper.toDto(book))
                .thenReturn(bookResponses.get(book.getId())));

        List<BookDto> actual = bookService.findAll(PAGEABLE);
        actual.sort(Comparator.comparingLong(BookDto::id));

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("findAll: When No Books Exist, Return Empty List")
    void findAll_WhenNoBooksExist_ReturnEmptyList() {
        Mockito.when(bookRepository.findAllWithCategories(PAGEABLE))
                .thenReturn(List.of());

        List<BookDto> actual = bookService.findAll(PAGEABLE);

        Assertions.assertTrue(actual.isEmpty(), "Expect empty list");
    }

    @TestFactory
    @DisplayName("getById: When Book Exists, Return BookDto")
    Stream<DynamicTest> getById_WhenBookExists_ReturnBookDto() {
        return bookRequests.keySet().stream()
                .map((id) -> DynamicTest.dynamicTest("Get by id: " + id,
                        () -> {
                            BookDto expected = bookResponses.get(id);
                            Book book = books.get(id);
                            Mockito.when(bookRepository.findByIdWithCategories(id))
                                    .thenReturn(Optional.of(book));
                            Mockito.when(bookMapper.toDto(book))
                                    .thenReturn(expected);

                            BookDto actual = bookService.getById(id);

                            Assertions.assertEquals(expected, actual);
                            Assertions.assertIterableEquals(
                                    expected.categories(), actual.categories()
                            );
                        })
                );
    }

    @Test
    @DisplayName("getById: When Book Does Not Exist, Throw EntityNotFoundException")
    void getById_WhenBookDoesNotExist_ThrowEntityNotFoundException() {
        Long id = 999L;
        Mockito.when(bookRepository.findByIdWithCategories(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.getById(id)
        );
        Assertions.assertEquals(
                "Can't find book with id: " + id,
                exception.getMessage()
        );
    }

    @TestFactory
    @DisplayName("update: When Book Exists, Update and Return BookDto")
    Stream<DynamicTest> update_WhenBookExists_UpdateAndReturnBookDto() {
        return bookRequests.entrySet().stream()
                .map((entry) -> DynamicTest.dynamicTest(
                        "Update book by id: " + entry.getKey(),
                        () -> {
                            Long id = entry.getKey();
                            CreateBookRequestDto requestDto = entry.getValue();
                            Book book = books.get(id);
                            BookDto expected = bookResponses.get(id);

                            Mockito.when(bookRepository.findById(id))
                                    .thenReturn(Optional.of(book));
                            Mockito.when(bookMapper.toModel(requestDto))
                                    .thenReturn(book);
                            Mockito.when(categoryService.findAllById(requestDto.categories()))
                                    .thenReturn(book.getCategories());
                            Mockito.when(bookRepository.save(book)).thenReturn(book);
                            Mockito.when(bookMapper.toDto(book)).thenReturn(expected);

                            BookDto actual = bookService.update(id, requestDto);

                            Assertions.assertEquals(expected, actual);
                        })
                );
    }

    @Test
    @DisplayName("update: When Book Does Not Exist, Throw EntityNotFoundException")
    void update_WhenBookDoesNotExist_ThrowEntityNotFoundException() {
        Long id = 1L;
        CreateBookRequestDto requestDto = bookRequests.get(id);
        Mockito.when(bookRepository.findById(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.update(id, requestDto)
        );
        Assertions.assertEquals(
                "Can't update: Not found book with id: " + id,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("update: When Categories Not Found, Throw BookProcessingException")
    void update_WhenCategoriesNotFound_ThrowBookProcessingException() {
        CreateBookRequestDto requestDto = bookRequests.get(1L);
        Book book = books.get(1L);

        Mockito.when(bookRepository.findById(ArgumentMatchers.any()))
                .thenReturn(Optional.of(book));
        Mockito.when(bookMapper.toModel(requestDto)).thenReturn(book);

        Exception exception = Assertions.assertThrows(
                EntityProcessingException.class,
                () -> bookService.update(book.getId(), requestDto)
        );
        Assertions.assertEquals(
                "Book processing failed. There are not categories with id: "
                        + book.getId(),
                exception.getMessage());
    }

    @Test
    @DisplayName("search: When Books Match Search Criteria, Return List of BookDtos")
    void search_WhenBooksMatchSearchCriteria_ReturnListOfBookDtos() {
        Specification<Book> specification = (root, query, criteriaBuilder) -> null;
        BookSearchParameters bookSearchParameters = new BookSearchParameters(
                "Pride and ", new String[]{"Jane Austen"}, null);
        Book book = books.get(3L);
        BookWithoutCategoryIdsDto expected = new BookWithoutCategoryIdsDto(3L,
                "Pride and Prejudice",
                "Jane Austen",
                "9780486284736",
                BigDecimal.valueOf(7.99),
                "A romantic novel",
                "/images/book3.jpg");

        Mockito.when(specificationBuilder.build(bookSearchParameters))
                .thenReturn(specification);
        Mockito.when(bookRepository.findAll(specification, PAGEABLE))
                .thenReturn(new PageImpl<>(List.of(book)));
        Mockito.when(bookMapper.toDtoWithoutIds(book)).thenReturn(expected);

        List<BookWithoutCategoryIdsDto> actual = bookService.search(bookSearchParameters, PAGEABLE);

        Assertions.assertEquals(expected, actual.get(0));
    }

    @Test
    @DisplayName("getByCategoryId: When Books Exist for Category, Return List of BookDtos")
    void getByCategoryId_WhenBooksExistForCategory_ReturnListOfBookDtos() {
        Long categoryId = 1L;
        Book book = books.get(1L);
        BookWithoutCategoryIdsDto expected = new BookWithoutCategoryIdsDto(1L,
                "To Kill a Mockingbird",
                "Harper Lee",
                "9780061120084",
                BigDecimal.valueOf(9.99),
                "A classic novel",
                "/book1.jpg");
        Mockito.when(bookRepository.findAllByCategoryId(categoryId, PAGEABLE))
                .thenReturn(List.of(book));
        Mockito.when(bookMapper.toDtoWithoutIds(book)).thenReturn(expected);

        BookWithoutCategoryIdsDto actual = bookService.getByCategoryId(1L, PAGEABLE).get(0);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("deleteById: When Book Exists, Delete Successfully")
    void deleteById_WhenBookExists_DeleteSuccessfully() {
        Long id = 999L;
        Mockito.when(bookRepository.findById(ArgumentMatchers.any()))
                .thenReturn(Optional.of(books.get(1L)));

        bookService.deleteById(id);

        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(id);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }
}
