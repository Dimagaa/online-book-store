package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.cartitem.CartItemCreateRequestDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.dto.cartitem.CartItemUpdateRequestDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return null;
    }

    @PostMapping
    public CartItemDto addCartItem(@RequestBody @Valid CartItemCreateRequestDto request) {
        return null;
    }

    @PutMapping("/car-items/{cartItemId}")
    public CartItemDto updateCartItem(@PathVariable Long cartItemId,
                               @RequestBody CartItemUpdateRequestDto request) {
        return null;
    }

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId) {

    }
}
