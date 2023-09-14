package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.cartitem.CartItemCreateRequestDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.dto.cartitem.CartItemUpdateRequestDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart();

    CartItemDto addCartItem(CartItemCreateRequestDto request);

    CartItemDto updateCartItem(Long cartItemId, CartItemUpdateRequestDto request);

    void deleteCartItem(Long cartItemId);
}
