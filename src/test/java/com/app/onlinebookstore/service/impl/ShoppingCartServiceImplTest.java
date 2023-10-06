package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.cartitem.CartItemCreateDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.CartItemMapper;
import com.app.onlinebookstore.mapper.ShoppingCartMapper;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.CartItem;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.model.Role;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.model.User;
import com.app.onlinebookstore.repository.book.BookRepository;
import com.app.onlinebookstore.repository.shoppingcart.CartItemRepository;
import com.app.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.app.onlinebookstore.service.UserService;
import java.math.BigDecimal;
import java.util.HashSet;
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

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static User user;
    private static ShoppingCart shoppingCart;
    private static ShoppingCartDto shoppingCartDto;
    private static Map<Long, CartItem> cartItems;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserService userService;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    static void beforeAll() {
        Role role = new Role();
        role.setName(Role.RoleName.ROLE_USER);

        user = new User(1L,
                "test@email",
                "test1password",
                "Bob",
                "Smith",
                "65300",
                Set.of(role),
                false
        );

        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);

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
                BigDecimal.valueOf(10.59),
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
        CartItem cartItem1 = new CartItem(1L,
                shoppingCart,
                book1,
                2,
                false
        );
        CartItem cartItem2 = new CartItem(2L,
                shoppingCart,
                book2,
                3,
                false
        );
        CartItem cartItem3 = new CartItem(3L,
                shoppingCart,
                book3,
                1,
                false
        );
        cartItems = Map.of(1L, cartItem1, 2L, cartItem2, 3L, cartItem3);

        CartItemDto cartItemDto1 = new CartItemDto(1L,
                book1.getId(),
                book1.getTitle(),
                cartItem1.getQuantity()
        );
        CartItemDto cartItemDto2 = new CartItemDto(2L,
                book2.getId(),
                book2.getTitle(),
                cartItem2.getQuantity()
        );
        CartItemDto cartItemDto3 = new CartItemDto(3L,
                book3.getId(),
                book3.getTitle(),
                cartItem3.getQuantity()
        );
        Map<Long, CartItemDto> cartItemDtos = Map.of(
                1L, cartItemDto1,
                2L, cartItemDto2,
                3L, cartItemDto3
        );
        shoppingCart.setCartItems(new HashSet<>(cartItems.values()));

        shoppingCartDto = new ShoppingCartDto(
                shoppingCart.getId(),
                shoppingCart.getUser().getId(),
                new HashSet<>(cartItemDtos.values())
        );
    }

    @Test
    @DisplayName("getShoppingCart: When Shopping Cart Exists for User, Return ShoppingCartDto")
    void getShoppingCart_WhenShoppingCartExistsForUser_ReturnShoppingCartDto() {
        final ShoppingCartDto expected = shoppingCartDto;

        Mockito.when(shoppingCartMapper.toDto(shoppingCart))
                .thenReturn(shoppingCartDto);
        Mockito.when(shoppingCartRepository.findForCurrentUserWithCartItemsAndBooks())
                .thenReturn(Optional.of(shoppingCart));

        ShoppingCartDto actual = shoppingCartService.getShoppingCart();

        Mockito.verifyNoMoreInteractions(shoppingCartRepository);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getShoppingCart: When No Shopping Cart Exists, Return new ShoppingCartDto")
    void getShoppingCart_WhenNoShoppingCartExistsForUser_CreateAndReturnShoppingCartDto() {
        final ShoppingCartDto expected = shoppingCartDto;

        Mockito.when(shoppingCartMapper.toDto(shoppingCart))
                .thenReturn(shoppingCartDto);
        Mockito.when(shoppingCartRepository.findForCurrentUserWithCartItemsAndBooks())
                .thenReturn(Optional.empty());
        Mockito.when(userService.getAuthenticatedUser())
                .thenReturn(user);
        Mockito.when(shoppingCartRepository.save(ArgumentMatchers.any()))
                .thenReturn(shoppingCart);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart();

        Mockito.verify(shoppingCartRepository, Mockito.times(1))
                .save(ArgumentMatchers.any());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("addCartItem: When Book Exists in Cart, Update CartItem and Return CartItemDto")
    void addCartItem_WhenBookExistsInCart_UpdateCartItemAndReturnCartItemDto() {
        CartItem cartItem = cartItems.get(1L);
        CartItemCreateDto requestDto = new CartItemCreateDto(
                cartItem.getId(), 3);
        ShoppingCart mutableCart = new ShoppingCart();
        mutableCart.setId(1L);
        mutableCart.setUser(user);
        CartItem mutableCartItem = new CartItem(
                cartItem.getId(),
                cartItem.getShoppingCart(),
                cartItem.getBook(),
                cartItem.getQuantity(),
                cartItem.isDeleted()
        );
        mutableCart.setCartItems(Set.of(mutableCartItem));

        final CartItemDto expected = new CartItemDto(
                cartItem.getId(),
                cartItem.getBook().getId(),
                cartItem.getBook().getTitle(),
                cartItem.getQuantity() + requestDto.quantity()
        );
        Mockito.when(shoppingCartRepository.findForCurrentUserWithCartItemsAndBooks())
                .thenReturn(Optional.of(mutableCart));
        Mockito.when(cartItemRepository.save(mutableCartItem))
                .thenReturn(mutableCartItem);
        Mockito.when(cartItemMapper.toDto(mutableCartItem))
                .thenReturn(expected);

        CartItemDto actual = shoppingCartService.addCartItem(requestDto);

        Assertions.assertEquals(expected, actual);
        Mockito.verifyNoInteractions(bookRepository);
        Mockito.verify(shoppingCartRepository, Mockito.times(1))
                .findForCurrentUserWithCartItemsAndBooks();
        Mockito.verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName("addCartItem: When Book Does Not Exist in Cart, Return new CartItemDto")
    void addCartItem_WhenBookDoesNotExistInCart_CreateCartItemAndReturnCartItemDto() {
        CartItem cartItem = cartItems.get(1L);
        CartItemCreateDto requestDto = new CartItemCreateDto(
                cartItem.getId(), 3);
        ShoppingCart mutableCart = new ShoppingCart();
        mutableCart.setId(1L);
        mutableCart.setUser(user);
        CartItem mutableCartItem = new CartItem(
                cartItem.getId(),
                cartItem.getShoppingCart(),
                cartItem.getBook(),
                requestDto.quantity(),
                cartItem.isDeleted()
        );
        final CartItemDto expected = new CartItemDto(
                cartItem.getId(),
                cartItem.getBook().getId(),
                cartItem.getBook().getTitle(),
                requestDto.quantity()
        );
        Mockito.when(shoppingCartRepository.findForCurrentUserWithCartItemsAndBooks())
                .thenReturn(Optional.of(mutableCart));
        Mockito.when(cartItemRepository.save(ArgumentMatchers.any()))
                .thenReturn(mutableCartItem);
        Mockito.when(cartItemMapper.toDto(mutableCartItem))
                .thenReturn(expected);
        Mockito.when(bookRepository.findById(requestDto.bookId()))
                .thenReturn(Optional.of(cartItem.getBook()));

        CartItemDto actual = shoppingCartService.addCartItem(requestDto);

        Mockito.verify(shoppingCartRepository, Mockito.times(1))
                .findForCurrentUserWithCartItemsAndBooks();
        Mockito.verify(bookRepository, Mockito.times(1))
                .findById(cartItem.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("updateCartItem: When CartItem Exists for User, Return Updated CartItemDto")
    void updateCartItem_WhenCartItemExistsForUser_UpdateAndReturnUpdatedCartItemDto() {
        Book book = new Book();
        book.setId(4L);
        book.setTitle("To Kill a Mockingbird");
        CartItem cartItem = new CartItem(4L,
                shoppingCart,
                book,
                6,
                false
        );
        shoppingCart.getCartItems().add(cartItem);

        CartItemUpdateDto request = new CartItemUpdateDto(3);

        final CartItemDto expected = new CartItemDto(cartItem.getId(),
                cartItem.getBook().getId(),
                cartItem.getBook().getTitle(),
                request.quantity()
        );
        Mockito.when(cartItemRepository.findByIdForCurrentUser(cartItem.getId()))
                .thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem))
                .thenReturn(cartItem);
        Mockito.when(cartItemMapper.toDto(cartItem))
                .thenReturn(expected);

        CartItemDto actual = shoppingCartService.updateCartItem(cartItem.getId(), request);

        Assertions.assertEquals(expected, actual);

        shoppingCart.getCartItems().remove(cartItem);
    }

    @Test
    @DisplayName("updateCartItem: When CartItem Does Not Exist, Throw EntityNotFoundException")
    void updateCartItem_WhenCartItemDoesNotExistForUser_ThrowEntityNotFoundException() {
        long id = -99L;
        CartItemUpdateDto request = new CartItemUpdateDto(3);

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.updateCartItem(id, request));

        Assertions.assertEquals(
                "Can't find CartItem by id: " + id,
                exception.getMessage()
        );
    }

    @TestFactory
    @DisplayName("deleteCartItem: When CartItem Exists for User, Delete CartItem")
    Stream<DynamicTest> deleteCartItem_WhenCartItemExistsForUser_DeleteCartItem() {
        return cartItems.keySet().stream()
                .map(id -> DynamicTest.dynamicTest(
                        "Delete by id: " + id,
                        () -> {
                            Mockito.when(shoppingCartRepository
                                            .findForCurrentUserWithCartItemsAndBooks())
                                    .thenReturn(Optional.of(shoppingCart));

                            shoppingCartService.deleteCartItem(id);

                            Mockito.verify(cartItemRepository, Mockito.times(1))
                                    .deleteById(id);
                        }
                ));
    }

    @Test
    @DisplayName("deleteCartItem: When CartItem Does Not Exist=, Throw EntityNotFoundException")
    void deleteCartItem_WhenCartItemDoesNotExistForUser_ThrowEntityNotFoundException() {
        long id = -99L;

        Mockito.when(shoppingCartRepository.findForCurrentUserWithCartItemsAndBooks())
                .thenReturn(Optional.of(shoppingCart));

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> shoppingCartService.deleteCartItem(id)
        );
        Assertions.assertEquals(
                "Can't find CartItem by id: " + id,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("clear: When CartItems Exist, Delete CartItems and Clear ShoppingCart")
    void clear_WhenCartItemsExist_DeleteCartItemsAndClearShoppingCart() {
        shoppingCartService.clear(shoppingCart);
        Mockito.verify(cartItemRepository, Mockito.times(1))
                .deleteAll(ArgumentMatchers.any());
    }
}
