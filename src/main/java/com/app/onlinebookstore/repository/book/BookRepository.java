package com.app.onlinebookstore.repository.book;

import com.app.onlinebookstore.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query("FROM Book b LEFT JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId, Pageable pageable);

    @Query("FROM Book b LEFT JOIN FETCH b.categories" )
    List<Book> findAllWithCategories(Pageable pageable);

    @Query("FROM Book b LEFT JOIN FETCH b.categories WHERE b.id = :id")
    Optional<Book> findByIdWithCategories(Long id);

    Optional<Book> findByIsbn(String isbn);
}
