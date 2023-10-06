INSERT INTO shopping_carts (id, user_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 3),
       (2, 1, 3, 2),
       (3, 2, 1, 1),
       (4, 2, 2, 5),
       (5, 2, 3, 3);
