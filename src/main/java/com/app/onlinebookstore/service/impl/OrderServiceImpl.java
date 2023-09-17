package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderItemDto;
import com.app.onlinebookstore.dto.order.OrderPlaceRequestDto;
import com.app.onlinebookstore.dto.order.OrderUpdateRequestDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.OrderItemMapper;
import com.app.onlinebookstore.mapper.OrderMapper;
import com.app.onlinebookstore.model.Order;
import com.app.onlinebookstore.model.ShoppingCart;
import com.app.onlinebookstore.model.User;
import com.app.onlinebookstore.repository.order.OrderItemRepository;
import com.app.onlinebookstore.repository.order.OrderRepository;
import com.app.onlinebookstore.service.OrderService;
import com.app.onlinebookstore.service.ShoppingCartService;
import com.app.onlinebookstore.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderDto placeOrder(OrderPlaceRequestDto request) {
        User authenticatedUser = userService.getAuthenticatedUser();
        ShoppingCart shoppingCart = shoppingCartService
                .findShoppingCartByAuthenticatedUser()
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find shopping cart for username: "
                                        + authenticatedUser.getUsername())
                );
        Order order = orderMapper.toOrder(shoppingCart, request);
        shoppingCartService.clear(shoppingCart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getHistory(Pageable pageable) {
        return orderRepository.findAllWithUserAndOrderItems(pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto updateStatus(Long orderId, OrderUpdateRequestDto request) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId
                ));
        order.setStatus(request.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getAllOrderItems(Long orderId, Pageable pageable) {
        return orderItemRepository.findAllByOrderId(orderId, pageable)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId) {
        return orderItemRepository.findByIdAndOrderId(orderId, itemId)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find Order Item by id: " + itemId
                ));
    }
}
