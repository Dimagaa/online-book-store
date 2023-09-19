package com.app.onlinebookstore.repository.shoppingcart;

import com.app.onlinebookstore.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("""
            FROM CartItem i
            WHERE i.id = :cartItemId
            AND i.shoppingCart.user.id = ?#{ principal?.id }
            """)
    Optional<CartItem> findByIdForCurrentUser(Long cartItemId);
}
