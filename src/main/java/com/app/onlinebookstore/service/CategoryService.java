package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.category.CategoryCreateRequestDto;
import com.app.onlinebookstore.dto.category.CategoryDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryCreateRequestDto request);

    CategoryDto update(Long id, CategoryCreateRequestDto categoryDto);

    void deleteById(Long id);
}
