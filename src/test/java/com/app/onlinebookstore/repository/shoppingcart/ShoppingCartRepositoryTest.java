package com.app.onlinebookstore.repository.shoppingcart;

import com.app.onlinebookstore.config.SpringSecurityTestConfig;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.CartItem;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.model.Role;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.model.User;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(SpringSecurityTestConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    private static final String INSERT_BOOKS_AND_CATEGORIES_SQL =
            "classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql";
    private static final String INSERT_USERS_SQL =
            "classpath:sql-scripts/users/InsertUsers.sql";
    private static final String INSERT_SHOPPING_CARTS_AND_ITEMS_SQL =
            "classpath:sql-scripts/shoppingcarts/InsertShoppingCartsAndCartItems.sql";
    private static final String DELETE_USERS_SQL =
            "classpath:sql-scripts/users/DeleteUsers.sql";
    private static final String DELETE_SHOPPING_CARTS_AND_ITEMS_SQL =
            "classpath:sql-scripts/shoppingcarts/DeleteShoppingCartsAndItems.sql";
    private static final String DELETE_BOOKS_AND_CATEGORIES_SQL =
            "classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql";
    private static final String HARRY_EMAIL = "harry@example.com";
    private static final String FRODO_EMAIL = "frodo@example.com";
    private static ShoppingCart harryShoppingCart;
    private static ShoppingCart frodoShoppingCart;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

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
                "1984", "George Orwell",
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
                book1,
                3,
                false
        );
        CartItem cartItem2 = new CartItem(2L,
                harryShoppingCart,
                book3, 2,
                false
        );
        CartItem cartItem3 = new CartItem(3L,
                frodoShoppingCart,
                book1, 1,
                false
        );
        CartItem cartItem4 = new CartItem(4L,
                frodoShoppingCart,
                book2,
                5,
                false
        );
        CartItem cartItem5 = new CartItem(
                5L,
                frodoShoppingCart,
                book3, 3,
                false
        );

        harryShoppingCart.setCartItems(Set.of(cartItem1, cartItem2));
        frodoShoppingCart.setCartItems(Set.of(cartItem3, cartItem4, cartItem5));
    }

    @Test
    @DisplayName("findForCurrentUserWithCartItemsAndBooks:"
            + "When ShoppingCart Exists, Return Optional of ShoppingCart")
    @WithUserDetails(HARRY_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_ITEMS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_SHOPPING_CARTS_AND_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES_SQL
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findForCurrentUserWithCartItemsAndBooks_ShoppingCartForHarry_ReturnOptional() {
        final ShoppingCart expected = harryShoppingCart;
        final User expectedUser = harryShoppingCart.getUser();
        final List<CartItem> expectedItems = harryShoppingCart.getCartItems().stream()
                .sorted(Comparator.comparingLong(CartItem::getId))
                .toList();

        Optional<ShoppingCart> actual = shoppingCartRepository
                .findForCurrentUserWithCartItemsAndBooks();

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
        Assertions.assertEquals(expectedUser, actual.get().getUser());
        List<CartItem> actualItems = actual.get().getCartItems().stream()
                .sorted(Comparator.comparingLong(CartItem::getId))
                .toList();
        Assertions.assertIterableEquals(expectedItems, actualItems);
    }

    @Test
    @DisplayName("findForCurrentUserWithCartItemsAndBooks:"
            + "When ShoppingCart Exists, Return Optional of ShoppingCart")
    @WithUserDetails(FRODO_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_ITEMS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_SHOPPING_CARTS_AND_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES_SQL
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findForCurrentUserWithCartItemsAndBooks_ShoppingCartForFrodo_ReturnOptional() {
        ShoppingCart expected = frodoShoppingCart;
        User expectedUser = frodoShoppingCart.getUser();
        List<CartItem> expectedItems = frodoShoppingCart.getCartItems().stream()
                .sorted(Comparator.comparingLong(CartItem::getId))
                .toList();

        Optional<ShoppingCart> actual = shoppingCartRepository
                .findForCurrentUserWithCartItemsAndBooks();

        Assertions.assertTrue(actual.isPresent());
        Assertions.assertEquals(expected, actual.get());
        Assertions.assertEquals(expectedUser, actual.get().getUser());
        List<CartItem> actualItems = actual.get().getCartItems().stream()
                .sorted(Comparator.comparingLong(CartItem::getId))
                .toList();
        Assertions.assertIterableEquals(expectedItems, actualItems);
    }

    @Test
    @DisplayName("findForCurrentUserWithCartItemsAndBooks:"
            + " When No ShoppingCart Exists, Return Empty Optional")
    @WithUserDetails(HARRY_EMAIL)
    void findForCurrentUserWithCartItemsAndBooks_WhenNoShoppingCartExists_ReturnEmptyOptional() {
        Optional<ShoppingCart> actual = shoppingCartRepository
                .findForCurrentUserWithCartItemsAndBooks();

        Assertions.assertTrue(
                actual.isEmpty(),
                "Expect Optional.empty() if there is no shopping cart for current user"
        );
    }
}
