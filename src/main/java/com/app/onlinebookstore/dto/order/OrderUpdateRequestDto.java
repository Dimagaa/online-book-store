package com.app.onlinebookstore.dto.order;

import com.app.onlinebookstore.model.Order;

public record OrderUpdateRequestDto(Order.Status status) {
}
