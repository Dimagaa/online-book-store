package com.app.onlinebookstore.repository.order;

import com.app.onlinebookstore.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
            FROM Order o
            LEFT JOIN FETCH o.user u
            LEFT JOIN FETCH o.orderItems i
            LEFT JOIN FETCH i.book
            WHERE u.id = ?#{ principal?.id }
            """)
    List<Order> findAllForCurrentUserWithUserAndOrderItems(Pageable pageable);

    @Query("""
            FROM Order o
            LEFT JOIN FETCH o.user
            LEFT JOIN FETCH o.orderItems i
            LEFT JOIN FETCH i.book
            WHERE o.id = :orderId
            """)
    Optional<Order> findByIdWithItems(Long orderId);
}
