package com.app.onlinebookstore.repository.shoppingcart;

import com.app.onlinebookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("""
            FROM ShoppingCart sc
            LEFT JOIN FETCH sc.cartItems i
            LEFT JOIN FETCH i.book
            WHERE sc.user.id = ?#{ principal?.id }
            """)
    Optional<ShoppingCart> findForCurrentUserWithCartItemsAndBooks();
}
