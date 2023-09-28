package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.cartitem.CartItemCreateRequestDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.dto.cartitem.CartItemUpdateRequestDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.CartItemMapper;
import com.app.onlinebookstore.mapper.ShoppingCartMapper;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.CartItem;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.model.User;
import com.app.onlinebookstore.repository.book.BookRepository;
import com.app.onlinebookstore.repository.shoppingcart.CartItemRepository;
import com.app.onlinebookstore.repository.shoppingcart.ShoppingCartRepository;
import com.app.onlinebookstore.service.ShoppingCartService;
import com.app.onlinebookstore.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserService userService;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartMapper.toDto(findShoppingCartByAuthenticatedUser()
                .orElseGet(this::createNewShoppingCart));
    }

    @Override
    @Transactional
    public CartItemDto addCartItem(CartItemCreateRequestDto request) {
        ShoppingCart shoppingCart = findShoppingCartByAuthenticatedUser()
                .orElseGet(this::createNewShoppingCart);
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> request.bookId().equals(item.getBook().getId()))
                .peek(item -> item.setQuantity(item.getQuantity() + request.quantity()))
                .findFirst()
                .orElseGet(() -> createNewCartItem(shoppingCart, request));
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public CartItemDto updateCartItem(Long cartItemId, CartItemUpdateRequestDto request) {
        CartItem cartItem = cartItemRepository.findByIdForCurrentUser(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find CartItem by id: " + cartItemId
                ));
        cartItem.setQuantity(request.quantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        findShoppingCartByAuthenticatedUser()
                .stream()
                .flatMap(cart -> cart.getCartItems().stream())
                .filter(item -> cartItemId.equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find CartItem by id: " + cartItemId
                ));
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public Optional<ShoppingCart> findShoppingCartByAuthenticatedUser() {
        return shoppingCartRepository.findForCurrentUserWithCartItemsAndBooks();
    }

    @Override
    public void clear(ShoppingCart shoppingCart) {
        cartItemRepository.deleteAll(shoppingCart.getCartItems());
    }

    private ShoppingCart createNewShoppingCart() {
        User user = userService.getAuthenticatedUser();
        return shoppingCartRepository.save(new ShoppingCart(user));
    }

    private CartItem createNewCartItem(ShoppingCart shoppingCart,
                                       CartItemCreateRequestDto request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find Book by id: " + request.bookId()
                ));
        return new CartItem(shoppingCart, book, request.quantity());
    }
}
