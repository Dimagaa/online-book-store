package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.category.CategoryCreateRequestDto;
import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.CategoryMapper;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.repository.book.CategoryRepository;
import com.app.onlinebookstore.service.CategoryService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find entity by id: " + id
                ));
    }

    @Override
    public CategoryDto save(CategoryCreateRequestDto request) {
        Category savedCategory = categoryRepository.save(categoryMapper.toEntity(request));
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto update(Long id, CategoryCreateRequestDto categoryDto) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find entity by id: " + id
                ));
        Category category = categoryMapper.toEntity(categoryDto);
        category.setId(id);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }


}
