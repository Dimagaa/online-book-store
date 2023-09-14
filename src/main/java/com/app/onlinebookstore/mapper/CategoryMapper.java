package com.app.onlinebookstore.mapper;

import com.app.onlinebookstore.config.MapperConfig;
import com.app.onlinebookstore.dto.category.CategoryCreateRequestDto;
import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toEntity(CategoryCreateRequestDto request);
}
