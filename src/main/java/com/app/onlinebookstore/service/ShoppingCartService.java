package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.cartitem.CartItemCreateRequestDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.dto.cartitem.CartItemUpdateRequestDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.model.ShoppingCart;
import java.util.Optional;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemDto addCartItem(CartItemCreateRequestDto request);

    CartItemDto updateCartItem(Long cartItemId, CartItemUpdateRequestDto request);

    void deleteCartItem(Long cartItemId);

    Optional<ShoppingCart> findShoppingCartByAuthenticatedUser();

    void clear(ShoppingCart shoppingCart);
}
