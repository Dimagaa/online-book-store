package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.order.OrderDto;
import com.app.onlinebookstore.dto.order.OrderPlaceRequestDto;
import com.app.onlinebookstore.exception.EntityProcessingException;
import com.app.onlinebookstore.model.Order;
import com.app.onlinebookstore.model.OrderItem;
import com.app.onlinebookstore.model.ShoppingCart;
import java.math.BigDecimal;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "orderDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "orderItems", source = "shoppingCart.cartItems")
    Order toOrder(ShoppingCart shoppingCart, OrderPlaceRequestDto request);

    @AfterMapping
    default void setOrderAndTotal(@MappingTarget Order order) {
        order.getOrderItems().stream()
                .peek(item -> item.setOrder(order))
                .map(OrderMapper::calculateAmountForItem)
                .reduce(BigDecimal::add)
                .ifPresentOrElse(order::setTotal,
                        () -> {
                            throw new EntityProcessingException(
                                    "Invalid order items were provided");
                        });
    }

    private static BigDecimal calculateAmountForItem(OrderItem item) {
        BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
        return item.getPrice().multiply(quantity);
    }
}
