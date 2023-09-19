package com.app.onlinebookstore.dto.order;

public record OrderItemDto(Long id,
                           Long bookId,
                           int quantity) {
}
