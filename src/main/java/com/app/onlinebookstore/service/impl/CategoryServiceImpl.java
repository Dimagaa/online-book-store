package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CategoryRequestDto;
import com.app.onlinebookstore.exception.EntityAlreadyExistsException;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.CategoryMapper;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.repository.book.CategoryRepository;
import com.app.onlinebookstore.service.CategoryService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public CategoryDto save(CategoryRequestDto request) {
        Optional<Category> category = categoryRepository.findByName(request.name());
        if (category.isPresent()) {
            throw new EntityAlreadyExistsException(
                    "Category already exists by name: " + category.get().getName()
                            + ", category id: " + category.get().getId()
            );
        }

        Category savedCategory = categoryRepository.save(categoryMapper.toEntity(request));
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto categoryDto) {
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
        categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Not found category by id: " + id
                ));
        categoryRepository.deleteById(id);
    }

    @Override
    public Set<Category> findAllById(Set<Long> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        return new HashSet<>(categories);
    }
}
