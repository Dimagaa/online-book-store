package com.app.onlinebookstore.service;

import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CategoryRequestDto;
import com.app.onlinebookstore.model.Category;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto request);

    CategoryDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);

    Set<Category> findAllById(Set<Long> categories);
}
