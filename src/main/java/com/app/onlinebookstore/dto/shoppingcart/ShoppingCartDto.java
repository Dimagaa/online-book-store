package com.app.onlinebookstore.dto.shoppingcart;

import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import java.util.Set;

public record ShoppingCartDto(Long id, Long userId, Set<CartItemDto> cartItems) {
}
