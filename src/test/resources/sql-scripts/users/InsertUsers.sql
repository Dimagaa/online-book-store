INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (1, 'harry@example.com', '$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK', 'Harry', 'Potter',
        '4 Privet Drive, Little Whinging, England');

INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (2, 'frodo@example.com', '$2a$10$eNDxAZLdjj7A42/3t6t/lu9YigNljFK2bk9HSabaM3wGEYMv.6dYK', 'Frodo', 'Baggins',
        'Bag End, Hobbiton, The Shire');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, (SELECT id FROM roles WHERE name = 'ROLE_USER')),
       (2, (SELECT id FROM roles WHERE name = 'ROLE_USER'));
