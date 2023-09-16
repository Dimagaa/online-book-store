package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderItemDto;
import com.app.onlinebookstore.dto.order.OrderPlaceRequestDto;
import com.app.onlinebookstore.dto.order.OrderUpdateRequestDto;
import com.app.onlinebookstore.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderDto placeOrder(@RequestBody @Valid OrderPlaceRequestDto request) {
        return orderService.placeOrder(request);
    }

    @GetMapping
    public List<OrderDto> getOrderHistory(Pageable pageable) {
        return orderService.getHistory(pageable);
    }

    @PatchMapping("/{orderId}")
    public OrderDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderUpdateRequestDto request) {
        return orderService.updateStatus(orderId, request);
    }

    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getAllOrderItems(@PathVariable Long orderId, Pageable pageable) {
        return orderService.getAllOrderItems(orderId, pageable);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId,
                                         @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }
}
