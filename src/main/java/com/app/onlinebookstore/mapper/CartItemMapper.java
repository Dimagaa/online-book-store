package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.cartitem.CartItemCreateRequestDto;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.model.CartItem;
import com.app.onlinebookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "item.book.id")
    @Mapping(target = "bookTitle", source = "item.book.title")
    CartItemDto toDto(CartItem item);

    @Mapping(target = "shoppingCart", source = "shoppingCart")
    @Mapping(target = "book", source = "requestDto.bookId", qualifiedByName = "bookFromId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    CartItem toModel(CartItemCreateRequestDto requestDto, ShoppingCart shoppingCart);
}
