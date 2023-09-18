package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.cartitem.CartItemCreateRequestDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.dto.cartitem.CartItemUpdateRequestDto;
import com.app.onlinebookstore.dto.shoppingcart.ShoppingCartDto;
import com.app.onlinebookstore.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart API", description = "Manage the user's shopping cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Get the user's shopping cart",
            description = "Retrieve the user's shopping cart"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ShoppingCartDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @Operation(
            summary = "Add a new item to the shopping cart",
            description = "Add a new item to the user's shopping cart"
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public CartItemDto addCartItem(@RequestBody @Valid CartItemCreateRequestDto request) {
        return shoppingCartService.addCartItem(request);
    }

    @Operation(
            summary = "Update a cart item by ID",
            description = "Update an existing cart item in the user's shopping cart"
    )
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{cartItemId}")
    public CartItemDto updateCartItem(@PathVariable Long cartItemId,
                                      @RequestBody @Valid CartItemUpdateRequestDto request) {
        return shoppingCartService.updateCartItem(cartItemId, request);
    }

    @Operation(
            summary = "Delete a cart item by ID",
            description = "Delete an item from the user's shopping cart by its unique identifier"
    )
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
