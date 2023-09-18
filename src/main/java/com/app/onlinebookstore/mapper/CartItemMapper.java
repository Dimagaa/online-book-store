package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.cartitem.CartItemDto;
import com.app.onlinebookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {

    @Mapping(target = "bookId", source = "item.book.id")
    @Mapping(target = "bookTitle", source = "item.book.title")
    CartItemDto toDto(CartItem item);
}
