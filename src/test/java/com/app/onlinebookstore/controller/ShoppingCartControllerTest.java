package com.app.onlinebookstore.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.onlinebookstore.config.SecurityConfig;
import com.app.onlinebookstore.dto.cartitem.CartItemCreateDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(SecurityConfig.class)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static final String HARRY_EMAIL = "harry@example.com";
    private static final String FRODO_EMAIL = "frodo@example.com";
    private static final String INSERT_BOOKS_AND_CATEGORIES_SQL =
            "classpath:sql-scripts/books/InsertImmutableBooksAndCategories.sql";
    private static final String INSERT_USERS_SQL =
            "classpath:sql-scripts/users/InsertUsers.sql";
    private static final String INSERT_SHOPPING_CARTS_AND_CART_ITEMS_SQL =
            "classpath:sql-scripts/shoppingcarts/InsertShoppingCartsAndCartItems.sql";
    private static final String DELETE_SHOPPING_CARTS_AND_CART_ITEMS_SQL =
            "classpath:sql-scripts/shoppingcarts/DeleteShoppingCartsAndItems.sql";
    private static final String DELETE_USERS_SQL =
            "classpath:sql-scripts/users/DeleteUsers.sql";
    private static final String DELETE_BOOKS_AND_CATEGORIES =
            "classpath:sql-scripts/books/DeleteImmutableBooksAndCategories.sql";
    private static final String DELETE_FRODO_SHOPPING_CART_SQL =
            "classpath:sql-scripts/shoppingcarts/DeleteFrodoShoppingCart.sql";
    private static final String DELETE_HARRY_SHOPPING_CART_AND_ITEMS_SQL =
            "classpath:sql-scripts/shoppingcarts/DeleteHarryShoppingCartAndItems.sql";
    private static ShoppingCartDto harryShoppingCart;
    private static ShoppingCartDto frodoShoppingCart;
    private static Map<Long, CartItemDto> cartItemDtos;
    private static Map<Long, CartItemCreateDto> cartItemCreateDtos;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        CartItemCreateDto cartItemCreateDto1 = new CartItemCreateDto(1L, 3);
        CartItemCreateDto cartItemCreateDto2 = new CartItemCreateDto(3L, 2);
        CartItemCreateDto cartItemCreateDto3 = new CartItemCreateDto(1L, 1);
        CartItemCreateDto cartItemCreateDto4 = new CartItemCreateDto(2L, 5);
        CartItemCreateDto cartItemCreateDto5 = new CartItemCreateDto(3L, 3);

        cartItemCreateDtos = Map.of(
                1L, cartItemCreateDto1,
                2L, cartItemCreateDto2,
                3L, cartItemCreateDto3,
                4L, cartItemCreateDto4,
                5L, cartItemCreateDto5
        );
        CartItemDto cartItemDto1 = new CartItemDto(
                1L, 1L, "To Kill a Mockingbird", 3
        );
        CartItemDto cartItemDto2 = new CartItemDto(
                2L, 3L, "Pride and Prejudice", 2
        );
        CartItemDto cartItemDto3 = new CartItemDto(
                3L, 1L, "To Kill a Mockingbird", 1
        );
        CartItemDto cartItemDto4 = new CartItemDto(
                4L, 2L, "1984", 5
        );
        CartItemDto cartItemDto5 = new CartItemDto(
                5L, 3L, "Pride and Prejudice", 3
        );
        cartItemDtos = Map.of(
                1L, cartItemDto1,
                2L, cartItemDto2,
                3L, cartItemDto3,
                4L, cartItemDto4,
                5L, cartItemDto5
        );

        harryShoppingCart = new ShoppingCartDto(
                1L, 1L, Set.of(cartItemDto1, cartItemDto2)
        );
        frodoShoppingCart = new ShoppingCartDto(
                2L, 2L, Set.of(cartItemDto3, cartItemDto4, cartItemDto5)
        );
    }

    @SneakyThrows
    @Test
    @DisplayName("getShoppingCart: When User Requests Shopping Cart, Return ShoppingCartDto")
    @WithUserDetails(HARRY_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_CART_ITEMS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_SHOPPING_CARTS_AND_CART_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getShoppingCart_WhenUserRequestsShoppingCart_ReturnShoppingCartDto() {
        ShoppingCartDto expected = harryShoppingCart;
        List<CartItemDto> expectedItems = expected.cartItems().stream()
                .sorted(Comparator.comparingLong(CartItemDto::id))
                .toList();

        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                ShoppingCartDto.class
        );
        List<CartItemDto> actualItems = actual.cartItems().stream()
                .sorted(Comparator.comparingLong(CartItemDto::id))
                .toList();

        Assertions.assertEquals(expected, actual);
        Assertions.assertIterableEquals(expectedItems, actualItems);
    }

    @Test
    @DisplayName(
            "getShoppingCart: When User Requests Shopping Cart"
                    + " and None Exists, Return Empty ShoppingCartDto"
    )
    @SneakyThrows
    @WithUserDetails(FRODO_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_FRODO_SHOPPING_CART_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getShoppingCart_WhenUserRequestsShoppingCartAndNoneExists_ReturnEmptyShoppingCartDto() {
        Long expectedUserId = frodoShoppingCart.userId();

        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(),
                ShoppingCartDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.userId());
        Assertions.assertEquals(expectedUserId, actual.userId());
        Assertions.assertTrue(actual.cartItems().isEmpty(),
                "Expect there are no cart items");
    }

    @TestFactory
    @DisplayName("addCartItem: When Valid Cart Item Create Request, Return Created CartItemDto")
    @WithUserDetails(HARRY_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_HARRY_SHOPPING_CART_AND_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> addCartItem_WhenValidCartItemCreateRequest_ReturnCreatedCartItemDto() {
        return cartItemCreateDtos.entrySet().stream()
                .map(entry -> DynamicTest.dynamicTest(
                        "Add cart item: " + entry.getValue(),
                        () -> {
                            String request = objectMapper.writeValueAsString(entry.getValue());
                            CartItemDto expected = cartItemDtos.get(entry.getKey());

                            MockHttpServletResponse response = mockMvc.perform(
                                            post("/cart")
                                                    .content(request)
                                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isCreated())
                                    .andReturn()
                                    .getResponse();

                            CartItemDto actual = objectMapper.readValue(
                                    response.getContentAsByteArray(),
                                    CartItemDto.class
                            );
                            Assertions.assertNotNull(actual);
                            Assertions.assertNotNull(actual.id());
                            EqualsBuilder.reflectionEquals(expected, actual, "id");
                        }
                ));
    }

    @TestFactory
    @DisplayName("addCartItem: When Valid Cart Item Create Request, Return Created CartItemDto")
    @WithUserDetails(HARRY_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_CART_ITEMS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_SHOPPING_CARTS_AND_CART_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> addCartItem_WhenCartItemWithTheSameBookExist_ReturnCreatedCartItemDto() {
        return cartItemCreateDtos.entrySet().stream()
                .map(entry -> DynamicTest.dynamicTest(
                        "Add cart item: " + entry.getValue(),
                        () -> {
                            String request = objectMapper.writeValueAsString(entry.getValue());
                            CartItemDto cartItemDto = cartItemDtos.get(entry.getKey());
                            CartItemDto expected = new CartItemDto(
                                    cartItemDto.id(),
                                    cartItemDto.bookId(),
                                    cartItemDto.bookTitle(),
                                    cartItemDto.quantity() + entry.getValue().quantity()
                            );

                            MockHttpServletResponse response = mockMvc.perform(
                                            post("/cart")
                                                    .content(request)
                                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isCreated())
                                    .andReturn()
                                    .getResponse();

                            CartItemDto actual = objectMapper.readValue(
                                    response.getContentAsByteArray(),
                                    CartItemDto.class
                            );
                            Assertions.assertNotNull(actual);
                            Assertions.assertNotNull(actual.id());
                            EqualsBuilder.reflectionEquals(expected, actual, "id");
                        }
                ));
    }

    @SuppressWarnings("DataFlowIssue")
    @SneakyThrows
    @Test
    @DisplayName("addCartItem: When Invalid Cart Item Create Request, Return Bad Request")
    @WithUserDetails(HARRY_EMAIL)
    @Sql(scripts = {
            INSERT_USERS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_HARRY_SHOPPING_CART_AND_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addCartItem_WhenInvalidCartItemCreateRequest_ReturnBadRequest() {
        CartItemCreateDto cartItemWithoutBook = new CartItemCreateDto(null, 4);
        CartItemCreateDto cartItemWithNotExistentBook = new CartItemCreateDto(-99L, 3);
        CartItemCreateDto cartItemWithNegativeQuantity = new CartItemCreateDto(1L, -30);
        CartItemCreateDto cartItemWithZeroQuantity = new CartItemCreateDto(1L, 0);

        String requestWithoutBook = objectMapper.writeValueAsString(cartItemWithoutBook);
        String requestNotExistentBook = objectMapper.writeValueAsString(
                cartItemWithNotExistentBook);
        String requestWithNegativeQuantity = objectMapper.writeValueAsString(
                cartItemWithNegativeQuantity);
        String requestWithZeroQuantity = objectMapper.writeValueAsString(
                cartItemWithZeroQuantity);

        mockMvc.perform(
                        post("/cart")
                                .content(requestWithoutBook)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        post("/cart")
                                .content(requestNotExistentBook)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        post("/cart")
                                .content(requestWithNegativeQuantity)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/cart")
                        .content(requestWithZeroQuantity)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @TestFactory
    @WithUserDetails(FRODO_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_CART_ITEMS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_SHOPPING_CARTS_AND_CART_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("updateCartItem: When Valid Cart Item Update Request, Return Updated CartItemDto")
    Stream<DynamicTest> updateCartItem_WhenValidCartItemUpdateRequest_ReturnUpdatedCartItemDto() {
        Map<Long, CartItemUpdateDto> updateRequest = Map.of(
                3L, new CartItemUpdateDto(345),
                4L, new CartItemUpdateDto(567),
                5L, new CartItemUpdateDto(987)
        );
        return updateRequest.entrySet().stream()
                .map(entry -> DynamicTest.dynamicTest(
                        "Update Cart Item by Id: " + entry.getKey(),
                        () -> {
                            String request = objectMapper.writeValueAsString(entry.getValue());
                            CartItemDto cartItemDto = cartItemDtos.get(entry.getKey());
                            CartItemDto expected = new CartItemDto(
                                    cartItemDto.id(),
                                    cartItemDto.bookId(),
                                    cartItemDto.bookTitle(),
                                    entry.getValue().quantity()
                            );
                            MockHttpServletResponse response = mockMvc.perform(
                                            put("/cart/cart-items/" + entry.getKey())
                                                    .content(request)
                                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse();

                            CartItemDto actual = objectMapper.readValue(
                                    response.getContentAsByteArray(), CartItemDto.class);

                            Assertions.assertEquals(expected, actual);
                        }
                ));
    }

    @SneakyThrows
    @Test
    @DisplayName("updateCartItem: When Cart Item Does Not Exist, Return Not Found")
    @WithUserDetails(FRODO_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_CART_ITEMS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_SHOPPING_CARTS_AND_CART_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCartItem_WhenCartItemDoesNotExist_ReturnNotFound() {
        String request = objectMapper.writeValueAsString(new CartItemUpdateDto(13));
        long nonExistentCardId = -99L;
        Long unownedCardId = harryShoppingCart.cartItems().stream()
                .map(CartItemDto::id)
                .findFirst()
                .orElseThrow();

        mockMvc.perform(put("/cart/cart-items/" + nonExistentCardId)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/cart/cart-items/" + unownedCardId)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @TestFactory
    @DisplayName("deleteCartItem: When User Deletes Cart Item, Return 204 No Content")
    @WithUserDetails(FRODO_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_CART_ITEMS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_SHOPPING_CARTS_AND_CART_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    Stream<DynamicTest> deleteCartItem_WhenUserDeletesCartItem_Return204NoContent() {
        return frodoShoppingCart.cartItems().stream()
                .map(cartItemDto -> DynamicTest.dynamicTest(
                        "Delete by id: " + cartItemDto.bookId(),
                        () -> mockMvc.perform(
                                        delete("/cart/cart-items/" + cartItemDto.id())
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent())
                ));
    }

    @SneakyThrows
    @Test
    @DisplayName("deleteCartItem: When Cart Item Does Not Exist, Return Not Found")
    @WithUserDetails(FRODO_EMAIL)
    @Sql(scripts = {
            INSERT_BOOKS_AND_CATEGORIES_SQL,
            INSERT_USERS_SQL,
            INSERT_SHOPPING_CARTS_AND_CART_ITEMS_SQL
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            DELETE_SHOPPING_CARTS_AND_CART_ITEMS_SQL,
            DELETE_USERS_SQL,
            DELETE_BOOKS_AND_CATEGORIES
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCartItem_WhenCartItemDoesNotExist_ReturnNotFound() {
        long nonExistentCardId = -99L;
        Long unownedCardId = harryShoppingCart.cartItems().stream()
                .map(CartItemDto::id)
                .findFirst()
                .orElseThrow();

        mockMvc.perform(delete("/cart/cart-items/" + nonExistentCardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/cart/cart-items/" + unownedCardId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
