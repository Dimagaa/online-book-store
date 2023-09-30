package com.app.onlinebookstore.repository.shoppingcart;

import com.app.onlinebookstore.config.SpringSecurityTestConfig;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.CartItem;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.model.Role;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.model.User;
import java.math.BigDecimal;
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
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(SpringSecurityTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartItemRepositoryTest {
    private static final String HARRY_EMAIL = "harry@example.com";
    private static final String FRODO_EMAIL = "frodo@example.com";
    private static final String INSERT_BOOKS_AND_CATEGORIES_SQL =
            "classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql";
    private static final String INSERT_USERS_SQL =
            "classpath:sql-scripts/users/InsertUsers.sql";
    private static final String INSERT_SHOPPING_CARTS_AND_ITEMS =
            "classpath:sql-scripts/shoppingcarts/InsertShoppingCartsAndCartItems.sql";
    private static ShoppingCart frodoShoppingCart;
    private static ShoppingCart harryShoppingCart;
    @Autowired
    private CartItemRepository cartItemRepository;

    @BeforeAll
    static void beforeAll() {
        Role roleUser = new Role(Role.RoleName.ROLE_USER);

        User userHarry = new User(1L,
                "harry@example.com",
                "$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK",
                "Harry",
                "Potter",
                "4 Privet Drive, Little Whinging, England",
                Set.of(roleUser),
                false
        );
        User userFrodo = new User(2L,
                "frodo@example.com",
                "$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK",
                "Frodo",
                "Baggins",
                "Bag End, Hobbiton, The Shire",
                Set.of(roleUser),
                false
        );
        harryShoppingCart = new ShoppingCart(userHarry);
        frodoShoppingCart = new ShoppingCart(userFrodo);

        harryShoppingCart.setId(1L);
        frodoShoppingCart.setId(2L);

        Category classic = new Category(1L,
                "Classic novel",
                "A classic novel",
                false
        );
        Category dystopian = new Category(2L,
                "Dystopian novel",
                "A dystopian novel",
                false
        );
        Category romantic = new Category(3L,
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
                Set.of(classic)
        );
        Book book2 = new Book(2L,
                "1984",
                "George Orwell",
                "9780451524935",
                BigDecimal.valueOf(10.54),
                "A dystopian novel",
                "/book2.jpg",
                false,
                Set.of(dystopian, additional)
        );
        Book book3 = new Book(3L,
                "Pride and Prejudice",
                "Jane Austen",
                "9780486284736",
                BigDecimal.valueOf(7.99),
                "A romantic novel",
                "/images/book3.jpg",
                false,
                Set.of(romantic, additional)
        );
        CartItem cartItem1 = new CartItem(1L,
                harryShoppingCart,
                book1, 3,
                false
        );
        CartItem cartItem2 = new CartItem(2L,
                harryShoppingCart,
                book3,
                2,
                false);
        CartItem cartItem3 = new CartItem(3L,
                frodoShoppingCart,
                book1,
                1,
                false
        );
        CartItem cartItem4 = new CartItem(4L,
                frodoShoppingCart,
                book2,
                5,
                false
        );
        CartItem cartItem5 = new CartItem(5L,
                frodoShoppingCart,
                book3,
                3,
                false
        );
        harryShoppingCart.setCartItems(Set.of(cartItem1, cartItem2));
        frodoShoppingCart.setCartItems(Set.of(cartItem3, cartItem4, cartItem5));
    }

    @TestFactory
    @DisplayName("findByIdForCurrentUser: When CartItem Exists, Return Optional of CartItem")
    @WithUserDetails(HARRY_EMAIL)
    @Sql(scripts = {INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_ITEMS},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:sql-scripts/shoppingcarts/DeleteShoppingCartsAndItems.sql",
            "classpath:sql-scripts/users/DeleteUsers.sql",
            "classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> findByIdForCurrentUser_WhenCartItemExistsForHarry_ReturnOptionalItem() {
        return harryShoppingCart.getCartItems().stream()
                .map(expected -> DynamicTest.dynamicTest(
                        "Find for Harry by id: " + expected.getId(),
                        () -> {
                            Optional<CartItem> actual = cartItemRepository
                                    .findByIdForCurrentUser(expected.getId());

                            Assertions.assertTrue(actual.isPresent(),
                                    "Expect item is present by id: "
                                            + expected.getId());

                            Assertions.assertEquals(expected, actual.get());
                        }
                ));
    }

    @TestFactory
    @DisplayName("findByIdForCurrentUser: When CartItem Exists, Return Optional of CartItem")
    @WithUserDetails(FRODO_EMAIL)
    @Sql(scripts = {"classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql",
            "classpath:sql-scripts/users/InsertUsers.sql",
            "classpath:sql-scripts/shoppingcarts/InsertShoppingCartsAndCartItems.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:sql-scripts/shoppingcarts/DeleteShoppingCartsAndItems.sql",
            "classpath:sql-scripts/users/DeleteUsers.sql",
            "classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> findByIdForCurrentUser_WhenCartItemExistsForFrodo_ReturnOptionalItem() {
        return frodoShoppingCart.getCartItems().stream()
                .map(expected -> DynamicTest.dynamicTest(
                        "Find for Frodo by id: " + expected.getId(),
                        () -> {
                            Optional<CartItem> actual = cartItemRepository
                                    .findByIdForCurrentUser(expected.getId());

                            Assertions.assertTrue(actual.isPresent(),
                                    "Expect item is present by id: "
                                            + expected.getId());

                            Assertions.assertEquals(expected, actual.get());
                        }
                ));
    }

    @Test
    @DisplayName("findByIdForCurrentUser:"
            + " When CartItem Does Not Exist For Current User, Return Empty Optional")
    @WithUserDetails(FRODO_EMAIL)
    void findByIdForCurrentUser_WhenCartItemDoesNotExistForCurrentUser_ReturnEmptyOptional() {
        Long unownedCartItemId = harryShoppingCart.getCartItems().stream()
                .map(CartItem::getId)
                .findFirst()
                .orElseThrow();
        long nonExistentId = -99L;

        Optional<CartItem> actualForUnowned = cartItemRepository
                .findByIdForCurrentUser(unownedCartItemId);
        Optional<CartItem> actualForNonExisted = cartItemRepository
                .findByIdForCurrentUser(nonExistentId);

        Assertions.assertTrue(actualForUnowned.isEmpty(),
                "Expect Optional.empty() for item with id: "
                        + unownedCartItemId);

        Assertions.assertTrue(actualForNonExisted.isEmpty(),
                "");
    }

}
