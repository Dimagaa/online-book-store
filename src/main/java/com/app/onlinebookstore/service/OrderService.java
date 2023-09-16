package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderItemDto;
import com.app.onlinebookstore.dto.order.OrderPlaceRequestDto;
import com.app.onlinebookstore.dto.order.OrderUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(OrderPlaceRequestDto request);

    List<OrderDto> getHistory(Pageable pageable);

    OrderDto updateStatus(Long orderId, OrderUpdateRequestDto request);

    List<OrderItemDto> getAllOrderItems(Long orderId, Pageable pageable);

    OrderItemDto getOrderItemById(Long orderId, Long itemId);
}
