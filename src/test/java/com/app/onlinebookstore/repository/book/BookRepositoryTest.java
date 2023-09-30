package com.app.onlinebookstore.repository.book;

import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.Category;
import java.math.BigDecimal;
import java.util.Collection;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static Map<Long, Category> categories;
    private static Map<Long, Book> books;
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll() {
        Category category1 = new Category(1L, "Classic novel", "A classic novel", false);
        Category category2 = new Category(2L, "Dystopian novel", "A dystopian novel", false);
        Category category3 = new Category(3L, "Romantic novel", "A romantic novel", false);
        Category additional = new Category(4L, "Additional", "Additional category", false);

        categories = Map.of(1L, category1, 2L, category2, 3L, category3, 4L, additional);

        Book book1 = new Book(1L,
                "To Kill a Mockingbird",
                "Harper Lee",
                "9780061120084",
                BigDecimal.valueOf(9.99),
                "A classic novel",
                "/book1.jpg",
                false,
                Set.of(category1));

        Book book2 = new Book(2L,
                "1984",
                "George Orwell",
                "9780451524935",
                BigDecimal.valueOf(10.54),
                "A dystopian novel",
                "/book2.jpg",
                false,
                Set.of(category2, additional));

        Book book3 = new Book(3L,
                "Pride and Prejudice",
                "Jane Austen",
                "9780486284736",
                BigDecimal.valueOf(7.99),
                "A romantic novel",
                "/images/book3.jpg",
                false,
                Set.of(category3, additional));

        books = Map.of(1L, book1, 2L, book2, 3L, book3);
    }

    @Test
    @DisplayName("Find All Books by Category ID: When Category Exists, Return List of Books")
    @Sql(scripts = {"classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_WhenCategoryExists_ReturnListOfBooks() {
        Pageable pageable = Pageable.ofSize(10);
        Category category1 = categories.get(1L);
        Category category2 = categories.get(2L);
        Category category3 = categories.get(3L);
        Category category4 = categories.get(4L);
        Book expectedForCategory1 = books.get(1L);
        Book expectedForCategory2And4 = books.get(2L);
        Book expectedForCategory3And4 = books.get(3L);

        List<Book> actualForCategory1 = bookRepository
                .findAllByCategoryId(category1.getId(), pageable);
        List<Book> actualForCategory2 = bookRepository
                .findAllByCategoryId(category2.getId(), pageable);
        List<Book> actualForCategory3 = bookRepository
                .findAllByCategoryId(category3.getId(), pageable);
        List<Book> actualForCategory4 = bookRepository
                .findAllByCategoryId(category4.getId(), pageable);

        Assertions.assertEquals(expectedForCategory1, actualForCategory1.get(0));
        Assertions.assertEquals(expectedForCategory2And4, actualForCategory2.get(0));
        Assertions.assertEquals(expectedForCategory3And4, actualForCategory3.get(0));
        Assertions.assertTrue(actualForCategory4.size() > 1);
        actualForCategory4.forEach(actual -> Assertions
                .assertEquals(actual, books.get(actual.getId())));
    }

    @Test
    @DisplayName("Find All Books by Category ID: When Category Does Not Exist, Return Empty List")
    void findAllByCategoryId_WhenCategoryDoesNotExist_ReturnEmptyList() {
        Pageable pageable = Pageable.ofSize(10);
        Long categoryId = 999L;
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId, pageable);
        Assertions.assertTrue(actual.isEmpty());
    }

    @TestFactory
    @DisplayName("Find Book by ID with Categories: When Book Exists, Return Optional<Book>")
    @Sql(scripts = {"classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> findByIdWithCategories_WhenBookExists_ReturnOptionalOfBook() {
        return books.entrySet().stream()
                .map((entry) ->
                        DynamicTest.dynamicTest("Find by id: " + entry.getKey(),
                                () -> {
                                    Book expected = entry.getValue();
                                    Optional<Book> actual = bookRepository
                                            .findByIdWithCategories(expected.getId());
                                    Assertions.assertTrue(actual.isPresent(),
                                            "Actual result is present");
                                    Assertions.assertEquals(expected, actual.get());

                                    List<Category> expectedCategories = sort(
                                            expected.getCategories(),
                                            Comparator.comparingLong(Category::getId));

                                    List<Category> actualCategories = sort(
                                            actual.get().getCategories(),
                                            Comparator.comparingLong(Category::getId));
                                    Assertions.assertIterableEquals(expectedCategories,
                                            actualCategories);
                                }));
    }

    @Test
    @DisplayName("Find Book by ID with Categories: When Book Does Not Exist, Return Empty Optional")
    void findByIdWithCategories_WhenBookDoesNotExist_ReturnEmptyOptional() {
        Long bookId = 999L;
        Optional<Book> actual = bookRepository.findByIdWithCategories(bookId);
        Assertions.assertTrue(actual.isEmpty(),
                "Actual result is empty");
    }

    @Test
    @DisplayName("Find All Books with Categories: Return List of Books with Categories")
    @Sql(scripts = {"classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllWithCategories_WhenCategoriesExist_ReturnListOfBooks() {
        Pageable pageable = Pageable.ofSize(10);
        List<Book> expected = sort(books.values(), Comparator.comparingLong(Book::getId));

        List<Book> actual = sort(bookRepository.findAllWithCategories(pageable),
                Comparator.comparingLong(Book::getId));

        Assertions.assertIterableEquals(expected, actual);
        actual.forEach((actualBook) -> {
                    Book expectedBook = books.get(actualBook.getId());
                    List<Category> expectedCategories = sort(expectedBook.getCategories(),
                            Comparator.comparingLong(Category::getId));

                    List<Category> actualCategories = sort(actualBook.getCategories(),
                            Comparator.comparingLong(Category::getId));

                    Assertions.assertIterableEquals(expectedCategories, actualCategories);
                }
        );
    }

    @Test
    @DisplayName("Find All Books with Categories: When No Books Exist, Return Empty List")
    void findAllWithCategories_WhenNoCategoriesExist_ReturnEmptyList() {
        Pageable pageable = Pageable.ofSize(10);

        List<Book> actual = bookRepository.findAllWithCategories(pageable);

        Assertions.assertTrue(actual.isEmpty(), "Expect empty list");
    }

    @TestFactory
    @DisplayName("Find Book by ISBN: When Book Exists, Return Optional<Book>")
    @Sql(scripts = {"classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> findByIsbn_WhenBookExists_ReturnOptionalOfBook() {
        return books.values().stream()
                .map(expected -> DynamicTest.dynamicTest(
                        "Find by ISBN: " + expected.getIsbn(),
                        () -> {
                            String isbn = expected.getIsbn();

                            Optional<Book> actual = bookRepository.findByIsbn(isbn);

                            Assertions.assertTrue(actual.isPresent(), "Book is present");
                            Assertions.assertEquals(expected, actual.get());
                        }
                ));
    }

    @Test
    @DisplayName("Find Book by ISBN: When Book Does Not Exist, Return Empty Optional")
    void findByIsbn_WhenBookDoesNotExist_ReturnEmptyOptional() {
        String isbnToFind = "non_existent_isbn";

        Optional<Book> actual = bookRepository.findByIsbn(isbnToFind);

        Assertions.assertTrue(actual.isEmpty(),
                "Expect Optional of Book is empty for isbn: " + isbnToFind);
    }

    private <T> List<T> sort(Collection<T> collection, Comparator<T> comparator) {
        return collection.stream().sorted(comparator).toList();
    }
}
