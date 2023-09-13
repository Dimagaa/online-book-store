package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.category.CategoryCreateRequestDto;
import com.app.onlinebookstore.dto.category.CategoryDto;
import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CategoryCreateRequestDto request);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
