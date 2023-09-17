package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderItemDto;
import com.app.onlinebookstore.dto.order.OrderPlaceRequestDto;
import com.app.onlinebookstore.dto.order.OrderUpdateRequestDto;
import com.app.onlinebookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order API", description = "Manage user orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Place an order",
            description = "Place a new order with the provided shipping address"
    )
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public OrderDto placeOrder(@RequestBody @Valid OrderPlaceRequestDto request) {
        return orderService.placeOrder(request);
    }

    @Operation(
            summary = "Retrieve user's order history",
            description = "Retrieve a list of user's order history"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public List<OrderDto> getOrderHistory(Pageable pageable) {
        return orderService.getHistory(pageable);
    }

    @Operation(
            summary = "Update order status",
            description = "Update the status of a specific order"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    public OrderDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderUpdateRequestDto request) {
        return orderService.updateStatus(orderId, request);
    }

    @Operation(
            summary = "Retrieve all OrderItems for a specific order",
            description = "Retrieve a list of all order items within a specific order"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getAllOrderItems(@PathVariable Long orderId, Pageable pageable) {
        return orderService.getAllOrderItems(orderId, pageable);
    }

    @Operation(
            summary = "Retrieve a specific OrderItem within an order",
            description = "Retrieve details of a specific order item within a specific order"
    )
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId,
                                         @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }
}
