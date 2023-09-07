package com.app.onlinebookstore.controller;

import com.app.onlinebookstore.dto.BookDto;
import com.app.onlinebookstore.dto.BookSearchParameters;
import com.app.onlinebookstore.dto.CreateBookRequestDto;
import com.app.onlinebookstore.service.BookService;
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

@Tag(name = "Books API", description = "Look at and search for books")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookService bookService;

    @Operation(
            summary = "Get all books",
            description = "Retrieve a list of all books using pagination"
    )
    @GetMapping
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Operation(
            summary = "Get book by ID",
            description = "Retrieve a book by its unique identifier"
    )
    @GetMapping("/{id}")
    public BookDto getById(@PathVariable Long id) {
        return bookService.getById(id);
    }

    @Operation(
            summary = "Create a new book",
            description = "Add a new book to the database"
    )
    @PostMapping
    public BookDto create(@RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }

    @Operation(
            summary = "Update book by ID",
            description = "Modify an existing book using its unique identifier"
    )
    @PutMapping("/{id}")
    public BookDto update(@PathVariable Long id,
                              @RequestBody CreateBookRequestDto bookRequestDto) {
        return bookService.update(id, bookRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete book by ID",
            description = "Remove a book from the collection by its unique identifier"
    )
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search books",
            description = "Search for books based on specific criteria using pagination"
    )
    public List<BookDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }
}
