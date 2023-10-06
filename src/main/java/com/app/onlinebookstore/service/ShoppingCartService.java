package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.cartitem.CartItemCreateDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.dto.cartitem.CartItemUpdateDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.model.ShoppingCart;
import java.util.Optional;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemDto addCartItem(CartItemCreateDto request);

    CartItemDto updateCartItem(Long cartItemId, CartItemUpdateDto request);

    void deleteCartItem(Long cartItemId);

    Optional<ShoppingCart> findShoppingCartByAuthenticatedUser();

    void clear(ShoppingCart shoppingCart);
}
