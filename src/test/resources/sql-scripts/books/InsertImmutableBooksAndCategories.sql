INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'To Kill a Mockingbird', 'Harper Lee', '9780061120084', 9.99, 'A classic novel', '/book1.jpg', false),
       (2, '1984', 'George Orwell', '9780451524935', 10.54, 'A dystopian novel', '/book2.jpg', false),
       (3, 'Pride and Prejudice', 'Jane Austen', '9780486284736', 7.99, 'A romantic novel', '/images/book3.jpg', false);
INSERT INTO categories (id, name, description, is_deleted)
VALUES (1, 'Classic novel', 'A classic novel', false),
       (2, 'Dystopian novel', 'A dystopian novel', false),
       (3, 'Romantic novel', 'A romantic novel', false),
       (4, 'Additional', 'Additional category', false);
INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1),
       (2, 2),
       (2, 4),
       (3, 3),
       (3, 4);
