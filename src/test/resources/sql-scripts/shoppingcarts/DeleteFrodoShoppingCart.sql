DELETE
FROM cart_items
WHERE cart_items.shopping_cart_id = (SELECT id FROM shopping_carts WHERE user_id = 2);
DELETE
FROM shopping_carts
WHERE user_id = 2;
