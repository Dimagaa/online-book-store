package com.app.onlinebookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemCreateRequestDto(
        @NotNull Long bookId,
        @Positive int quantity) {
}
