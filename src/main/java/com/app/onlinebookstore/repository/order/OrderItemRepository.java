package com.app.onlinebookstore.repository.order;

import com.app.onlinebookstore.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("""
            FROM OrderItem i
            WHERE i.order.id = :orderId
            AND i.order.user.id = ?#{ principal?.id }
            """)
    List<OrderItem> findAllByOrderIdForCurrentUser(Long orderId, Pageable pageable);

    @Query("""
            FROM OrderItem i
            WHERE i.id = :id
            AND i.order.id = :orderId
            AND i.order.user.id = ?#{ principal?.id }
            """)
    Optional<OrderItem> findByIdAndOrderIdForCurrentUser(Long orderId, Long id);
}
