package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.category.CategoryDto;
import com.app.onlinebookstore.dto.category.CategoryRequestDto;
import com.app.onlinebookstore.exception.EntityAlreadyExistsException;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.CategoryMapper;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.repository.book.CategoryRepository;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static final Pageable PAGEABLE = Pageable.ofSize(10);
    private static Map<Long, CategoryRequestDto> requestDtos;
    private static Map<Long, CategoryDto> categoryDtos;
    private static Map<Long, Category> categories;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeAll
    static void beforeAll() {

        CategoryRequestDto requestDto1 = new CategoryRequestDto(
                "Fantasy Adventure",
                "Fantasy adventure books"
        );

        CategoryRequestDto requestDto2 = new CategoryRequestDto(
                "Dystopian Fiction",
                "Novels in a dystopian setting"
        );

        CategoryRequestDto requestDto3 = new CategoryRequestDto(
                "Post-Apocalyptic",
                "Fiction, Post-apocalyptic novels"
        );

        requestDtos = Map.of(1L, requestDto1, 2L, requestDto2, 3L, requestDto3);

        CategoryDto categoryDto1 = new CategoryDto(
                1L,
                "Fantasy Adventure",
                "Fantasy adventure books"
        );
        CategoryDto categoryDto2 = new CategoryDto(
                2L,
                "Dystopian Fiction",
                "Novels in a dystopian setting"
        );
        CategoryDto categoryDto3 = new CategoryDto(
                3L,
                "Post-Apocalyptic",
                "Fiction, Post-apocalyptic novels"
        );
        categoryDtos = Map.of(1L, categoryDto1, 2L, categoryDto2, 3L, categoryDto3);

        Category category1 = new Category(
                1L,
                "Fantasy Adventure",
                "Fantasy adventure books",
                false
        );
        Category category2 = new Category(
                2L,
                "Dystopian Fiction",
                "Novels in a dystopian setting",
                false
        );
        Category category3 = new Category(
                3L,
                "Post-Apocalyptic",
                "Fiction, Post-apocalyptic novels",
                false
        );
        categories = Map.of(1L, category1, 2L, category2, 3L, category3);
    }

    @Test
    @DisplayName("findAll: When Categories Exist, Return List of CategoryDtos")
    void findAll_WhenCategoriesExist_ReturnListOfCategoryDtos() {
        List<CategoryDto> expected = categoryDtos.values().stream()
                .sorted(Comparator.comparingLong(CategoryDto::id))
                .toList();

        Mockito.when(categoryRepository.findAll())
                .thenReturn(categories.values().stream().toList());
        categories.values().forEach(category -> Mockito.when(categoryMapper.toDto(category))
                .thenReturn(categoryDtos.get(category.getId())));

        List<CategoryDto> actual = categoryService.findAll(PAGEABLE)
                .stream()
                .sorted(Comparator.comparingLong(CategoryDto::id))
                .toList();

        Assertions.assertIterableEquals(expected, actual);
    }

    @TestFactory
    @DisplayName("getById: When Category Exists, Return CategoryDto")
    Stream<DynamicTest> getById_WhenCategoryExists_ReturnCategoryDto() {
        return categoryDtos.values().stream()
                .map(expected -> DynamicTest.dynamicTest(
                        "Get category by id: " + expected.id(),
                        () -> {
                            Category category = categories.get(expected.id());
                            Mockito.when(categoryRepository.findById(expected.id()))
                                    .thenReturn(Optional.of(category));
                            Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);

                            CategoryDto actual = categoryService.getById(expected.id());

                            Assertions.assertEquals(expected, actual);
                        }
                ));
    }

    @Test
    @DisplayName("getById: When Category Does Not Exist, Throw EntityNotFoundException")
    void getById_WhenCategoryDoesNotExist_ThrowEntityNotFoundException() {
        long id = -999;

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(id)
        );

        Assertions.assertEquals("Can't find entity by id: " + id, exception.getMessage());
    }

    @TestFactory
    @DisplayName("save: When Valid Category Request, Return Created CategoryDto")
    Stream<DynamicTest> save_WhenValidCategoryRequest_ReturnCreatedCategoryDto() {
        return requestDtos.entrySet().stream()
                .map(entry -> DynamicTest.dynamicTest(
                        "Save category: " + entry.getValue(),
                        () -> {
                            final CategoryDto expected = categoryDtos.get(entry.getKey());
                            Category category = categories.get(entry.getKey());
                            CategoryRequestDto request = entry.getValue();

                            Mockito.when(categoryRepository.findByName(category.getName()))
                                    .thenReturn(Optional.empty());
                            Mockito.when(categoryMapper.toEntity(request)).thenReturn(category);
                            Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);
                            Mockito.when(categoryRepository.save(category)).thenReturn(category);

                            CategoryDto actual = categoryService.save(request);

                            Assertions.assertEquals(expected, actual);
                        }
                ));
    }

    @Test
    @DisplayName("save: When Category with Same Name Exists, Throw EntityAlreadyExistsException")
    void save_WhenCategoryWithSameNameExists_ThrowEntityAlreadyExistsException() {
        CategoryRequestDto request = requestDtos.get(1L);
        Category category = categories.get(1L);

        Mockito.when(categoryRepository.findByName(request.name()))
                .thenReturn(Optional.of(category));

        EntityAlreadyExistsException exception = Assertions.assertThrows(
                EntityAlreadyExistsException.class,
                () -> categoryService.save(request));

        Assertions.assertEquals("Category already exists by name: " + category.getName()
                        + ", category id: " + category.getId(),
                exception.getMessage());

    }

    @TestFactory
    @DisplayName("update: When Category Exists, Return Updated CategoryDto")
    Stream<DynamicTest> update_WhenCategoryRequestAndCategoryExists_ReturnUpdatedCategoryDto() {
        return requestDtos.entrySet().stream()
                .map(entry -> DynamicTest.dynamicTest(
                        "Update by id:" + entry.getKey(),
                        () -> {
                            CategoryRequestDto requestDto = entry.getValue();
                            Category category = categories.get(entry.getKey());
                            CategoryDto expected = categoryDtos.get(entry.getKey());

                            Mockito.when(categoryRepository.findById(category.getId()))
                                    .thenReturn(Optional.of(category));
                            Mockito.when(categoryMapper.toEntity(requestDto)).thenReturn(category);
                            Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);
                            Mockito.when(categoryRepository.save(category)).thenReturn(category);

                            CategoryDto actual = categoryService.update(entry.getKey(), requestDto);
                            Assertions.assertEquals(expected, actual);
                        }
                ));

    }

    @Test
    @DisplayName("update: When Category Does Not Exist, Throw EntityNotFoundException")
    void update_WhenCategoryDoesNotExist_ThrowEntityNotFoundException() {
        long id = -999L;
        CategoryRequestDto requestDto = requestDtos.get(1L);

        Mockito.when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.update(id, requestDto)
        );
        Assertions.assertEquals(
                "Can't find entity by id: " + id,
                exception.getMessage()
        );
    }

    @TestFactory
    @DisplayName("deleteById: When Category Exists, Delete Category")
    Stream<DynamicTest> deleteById_WhenCategoryExists_DeleteCategory() {
        return categories.entrySet().stream()
                .map(entry -> DynamicTest.dynamicTest(
                        "Delete by id: " + entry.getKey(),
                        () -> {
                            Long id = entry.getKey();
                            Category category = entry.getValue();
                            Mockito.when(categoryRepository.findById(id))
                                    .thenReturn(Optional.ofNullable(category));

                            categoryService.deleteById(id);

                            Mockito.verify(categoryRepository, Mockito.times(1))
                                    .deleteById(id);
                            Mockito.verifyNoMoreInteractions(categoryRepository);
                        }
                ));
    }

    @Test
    @DisplayName("deleteById: When Category Does Not Exist, Throw EntityNotFoundException")
    void deleteById_WhenCategoryDoesNotExist_ThrowEntityNotFoundException() {
        long id = -999L;
        Mockito.when(categoryRepository.findById(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.deleteById(id)
        );

        Assertions.assertEquals(
                "Not found category by id: " + id,
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("findAllById: When Valid Category IDs, Return Set of Categories")
    void findAllById_WhenValidCategoryIDs_ReturnSetOfCategories() {
        Set<Category> expected = new HashSet<>(categories.values());
        Mockito.when(categoryRepository.findAllById(categories.keySet()))
                .thenReturn(categories.values().stream().toList());

        Set<Category> actual = categoryService.findAllById(categories.keySet());

        Assertions.assertIterableEquals(expected, actual);
    }
}
