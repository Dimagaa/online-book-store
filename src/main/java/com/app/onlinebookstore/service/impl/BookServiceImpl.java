package com.app.onlinebookstore.service.impl;

import com.app.onlinebookstore.dto.book.BookDto;
import com.app.onlinebookstore.dto.book.BookSearchParameters;
import com.app.onlinebookstore.dto.book.BookWithoutCategoryIdsDto;
import com.app.onlinebookstore.dto.book.CreateBookRequestDto;
import com.app.onlinebookstore.exception.BookProcessingException;
import com.app.onlinebookstore.exception.EntityNotFoundException;
import com.app.onlinebookstore.mapper.BookMapper;
import com.app.onlinebookstore.model.Book;
import com.app.onlinebookstore.model.Category;
import com.app.onlinebookstore.repository.book.BookRepository;
import com.app.onlinebookstore.repository.book.BookSpecificationBuilder;
import com.app.onlinebookstore.service.BookService;
import com.app.onlinebookstore.service.CategoryService;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder specificationBuilder;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        if (bookRepository.findByIsbn(bookRequestDto.isbn()).isPresent()) {
            throw new BookProcessingException(
                    "Book creation failed. A book with ISBN already exists: "
                            + bookRequestDto.isbn());
        }
        Book book = bookMapper.toModel(bookRequestDto);
        book.setCategories(findCategoriesByIds(bookRequestDto.categories()));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAllWithCategories(pageable)
                .stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getById(Long id) {
        return bookRepository.findByIdWithCategories(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id: " + id));
    }

    @Override
    @Transactional
    public BookDto update(Long id, CreateBookRequestDto bookRequestDto) {
        bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't update: Not found book with id: " + id));
        if (bookRepository.findByIsbn(bookRequestDto.isbn()).isPresent()) {
            throw new BookProcessingException(
                    "Book updating failed. A book with ISBN already exists: "
                            + bookRequestDto.isbn());
        }
        Book book = bookMapper.toModel(bookRequestDto);
        book.setId(id);
        book.setCategories(findCategoriesByIds(bookRequestDto.categories()));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookWithoutCategoryIdsDto> search(BookSearchParameters searchParameters,
                                                  Pageable pageable) {
        Specification<Book> bookSpecification = specificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .stream()
                .map(bookMapper::toDtoWithoutIds)
                .toList();
    }

    @Override
    public List<BookWithoutCategoryIdsDto> getByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoryId(id, pageable)
                .stream()
                .map(bookMapper::toDtoWithoutIds)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    private Set<Category> findCategoriesByIds(Set<Long> ids) {
        Set<Category> categories = categoryService.findAllById(ids);
        if (categories.size() != ids.size()) {
            String missingIds = ids.stream()
                    .filter(id -> categories.stream()
                            .noneMatch(category -> category.getId().equals(id)))
                    .map(Objects::toString)
                    .collect(Collectors.joining(", "));
            throw new BookProcessingException(
                    "Book creation was failed. There are not categories with id: "
                            + missingIds
            );
        }
        return categories;
    }
}
