package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.book.BookWithoutCategoryIdsDto;
import com.app.onlinebookstore.dto.category.CategoryCreateRequestDto;
import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.service.BookService;
import com.app.onlinebookstore.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category API", description = "Operation related to categories")
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @Operation(
            summary = "Create a new category",
            description = "Create a new category with the provided data"
    )
    @PostMapping
    public CategoryDto create(@RequestBody @Valid CategoryCreateRequestDto request) {
        return categoryService.save(request);
    }

    @Operation(
            summary = "Get all categories",
            description = "Retrieve a list of all categories"
    )
    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.findAll();
    }

    @Operation(
            summary = "Get a category by ID",
            description = "Retrieve a category by its unique identifier"
    )
    @GetMapping("/{id}")
    public CategoryDto getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(
            summary = "Update a category by ID",
            description = "Update an existing category with the provided data"
    )
    @PutMapping("/{id}")
    public CategoryDto update(@ PathVariable Long id,
                              @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @Operation(
            summary = "Delete a category by ID",
            description = "Delete a category by its unique identifier"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(
            summary = "Get books by category ID",
            description = "Retrieve a list of books belonging to"
            + " a category by its unique identifier"
    )
    @GetMapping("/{id}/books")
    public List<BookWithoutCategoryIdsDto> getBooksByCategoryId(@PathVariable Long id, Pageable pageable) {
        return bookService.getByCategoryId(id, pageable);
    }
}
