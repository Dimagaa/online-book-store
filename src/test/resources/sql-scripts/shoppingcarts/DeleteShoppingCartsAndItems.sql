DELETE
FROM cart_items
WHERE shopping_cart_id IN (1, 2);
DELETE
FROM shopping_carts
WHERE id IN (1, 2);
