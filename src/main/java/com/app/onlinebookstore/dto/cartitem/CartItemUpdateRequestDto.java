package com.app.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record CartItemUpdateRequestDto(@Positive int quantity) {
}
