-- Books to Categories Mapping
INSERT INTO books_categories (book_id, category_id)
VALUES
    -- Harry Potter and the Sorcerer's Stone
    ((SELECT id FROM books WHERE isbn = '9780590353427'), (SELECT id FROM categories WHERE name = 'Fantasy Adventure')),
    -- The Lord of the Rings
    ((SELECT id FROM books WHERE isbn = '9780544003415'), (SELECT id FROM categories WHERE name = 'Fantasy Adventure')),
    -- The Hunger Games
    ((SELECT id FROM books WHERE isbn = '9780439023481'), (SELECT id FROM categories WHERE name = 'Dystopian Fiction')),
    -- The Road
    ((SELECT id FROM books WHERE isbn = '9780307387899'), (SELECT id FROM categories WHERE name = 'Post-Apocalyptic Fiction')),
    -- Gone Girl
    ((SELECT id FROM books WHERE isbn = '9780307588371'), (SELECT id FROM categories WHERE name = 'Mystery Thriller')),
    -- The Girl with the Dragon Tattoo
    ((SELECT id FROM books WHERE isbn = '9780307269751'), (SELECT id FROM categories WHERE name = 'Mystery Thriller')),
    -- The Road Less Traveled
    ((SELECT id FROM books WHERE isbn = '9780743243155'), (SELECT id FROM categories WHERE name = 'Self-Help')),
    -- The Alchemist
    ((SELECT id FROM books WHERE isbn = '9780061122415'), (SELECT id FROM categories WHERE name = 'Philosophical Fiction')),
    -- The Art of War
    ((SELECT id FROM books WHERE isbn = '9781590302255'), (SELECT id FROM categories WHERE name = 'Military Strategy')),
    -- The Catcher in the Rye
    ((SELECT id FROM books WHERE isbn = '9780316769174'), (SELECT id FROM categories WHERE name = 'Coming-of-Age')),
    -- To the Lighthouse
    ((SELECT id FROM books WHERE isbn = '9780156907393'), (SELECT id FROM categories WHERE name = 'Modernist Fiction')),
    -- The Road to Character
    ((SELECT id FROM books WHERE isbn = '9780812993257'), (SELECT id FROM categories WHERE name = 'Personal Development')),
    -- The Goldfinch
    ((SELECT id FROM books WHERE isbn = '9780316055437'), (SELECT id FROM categories WHERE name = 'Contemporary Fiction')),
    -- The Help
    ((SELECT id FROM books WHERE isbn = '9780425232200'), (SELECT id FROM categories WHERE name = 'Historical Fiction')),
    -- The Girl on the Train
    ((SELECT id FROM books WHERE isbn = '9780735212169'), (SELECT id FROM categories WHERE name = 'Psychological Thriller')),
    -- The Martian
    ((SELECT id FROM books WHERE isbn = '9780553418026'), (SELECT id FROM categories WHERE name = 'Science Fiction')),
    -- Educated
    ((SELECT id FROM books WHERE isbn = '9780399590504'), (SELECT id FROM categories WHERE name = 'Memoir')),
    -- The Nightingale
    ((SELECT id FROM books WHERE isbn = '9780312577223'), (SELECT id FROM categories WHERE name = 'Historical Fiction')),
    -- The Silent Patient
    ((SELECT id FROM books WHERE isbn = '9781250301697'), (SELECT id FROM categories WHERE name = 'Psychological Thriller')),
    -- The Water Dancer
    ((SELECT id FROM books WHERE isbn = '9780399590597'), (SELECT id FROM categories WHERE name = 'Historical Fiction')),
    -- The Testaments
    ((SELECT id FROM books WHERE isbn = '9780385543781'), (SELECT id FROM categories WHERE name = 'Dystopian Fiction')),
    -- Becoming
    ((SELECT id FROM books WHERE isbn = '9781524763138'), (SELECT id FROM categories WHERE name = 'Memoir')),
    -- To Kill a Mockingbird
    ((SELECT id FROM books WHERE isbn = '9780061120084'), (SELECT id FROM categories WHERE name = 'Classic Literature')),
    -- 1984
    ((SELECT id FROM books WHERE isbn = '9780451524935'), (SELECT id FROM categories WHERE name = 'Dystopian Fiction')),
    -- Pride and Prejudice
    ((SELECT id FROM books WHERE isbn = '9780486284736'), (SELECT id FROM categories WHERE name = 'Romantic Fiction')),
    -- The Great Gatsby
    ((SELECT id FROM books WHERE isbn = '9780743273565'), (SELECT id FROM categories WHERE name = 'Roaring Twenties')),
    -- Brave New World
    ((SELECT id FROM books WHERE isbn = '9780060850524'), (SELECT id FROM categories WHERE name = 'Science Fiction')),
    -- The Catcher in the Rye
    ((SELECT id FROM books WHERE isbn = '9780316769488'), (SELECT id FROM categories WHERE name = 'Coming-of-Age')),
    -- The Hobbit
    ((SELECT id FROM books WHERE isbn = '9780345339683'), (SELECT id FROM categories WHERE name = 'Fantasy Adventure')),
    -- The Da Vinci Code
    ((SELECT id FROM books WHERE isbn = '9780307474278'), (SELECT id FROM categories WHERE name = 'Mystery Thriller')),
    -- To Kill a Mockingbird (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780061120084'), (SELECT id FROM categories WHERE name = 'Historical Fiction')),
    ((SELECT id FROM books WHERE isbn = '9780061120084'), (SELECT id FROM categories WHERE name = 'Social Justice')),
    -- 1984 (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780451524935'), (SELECT id FROM categories WHERE name = 'Political Fiction')),
    ((SELECT id FROM books WHERE isbn = '9780451524935'), (SELECT id FROM categories WHERE name = 'Totalitarianism')),
    -- Pride and Prejudice (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780486284736'), (SELECT id FROM categories WHERE name = 'Historical Romance')),
    -- The Great Gatsby (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780743273565'), (SELECT id FROM categories WHERE name = 'Literary Fiction')),
    -- To the Lighthouse (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780156907392'), (SELECT id FROM categories WHERE name = 'Feminist Literature')),
    -- Brave New World (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780060850524'), (SELECT id FROM categories WHERE name = 'Social Commentary')),
    -- The Catcher in the Rye (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780316769488'), (SELECT id FROM categories WHERE name = 'Teenage Angst')),
    -- The Hobbit (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780345339683'), (SELECT id FROM categories WHERE name = 'Epic Fantasy')),
    -- The Da Vinci Code (Additional Categories)
    ((SELECT id FROM books WHERE isbn = '9780307474278'), (SELECT id FROM categories WHERE name = 'Religious Conspiracy'));
