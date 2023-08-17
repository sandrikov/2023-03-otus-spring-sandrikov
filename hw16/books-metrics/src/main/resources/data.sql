insert into genres (id, `name`)
values (default, 'Adventure fiction'),    -- 1
       (default, 'Autobiography'),        -- 2
       (default, 'Detective'),            -- 3
       (default, 'Erotic romance novel'); -- 4

insert into authors (id, `name`)
values (default, 'Mark Twain'),      -- 1
       (default, 'Agatha Christie'), -- 2
       (default, 'Leo Tolstoy'),     -- 3
       (default, 'Erika Leonard');   -- 4

insert into books (id, title, author_id, genre_id, age_limit)
values (default, 'The Murder on the Links', 2, 3, 16),        -- 1
       (default, 'The Secret of Chimneys', 2, 3, 12),         -- 2
       (default, 'Adventures of Huckleberry Finn', 1, 1, 12), -- 3
       (default, 'The Adventures of Tom Sawyer', 1, 1, 6),    -- 4
       (default, 'Tom Sawyer Abroad', 1, 1, 0),               -- 5
       (default, 'Tom Sawyer, Detective', 1, 3, 12),          -- 6
       (default, 'Fifty Shades of Grey', 4, 4, 18);           -- 7

insert into comments (id, book_id, text)
values (default, 1, 'Comment №1 The Murder on the Links'),
       (default, 1, 'Comment №2 The Murder on the Links'),
       --(default, 2, 'The Secret of Chimneys'),  -- 2 w/o comment
       (default, 3, 'Comment №1 Adventures of Huckleberry Finn'),
       (default, 4, 'Comment №1 The Adventures of Tom Sawyer'),
       (default, 5, 'Comment №1 Tom Sawyer Abroad'),
       (default, 6, 'Comment №1 Tom Sawyer, Detective ')
;

INSERT INTO users (username, password, enabled)
values ('child', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', 1),
       ('adult', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', 1),
       ('admin', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', 1),
       ('librarian', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', 1),
       ('guest', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', 0)
;

INSERT INTO authorities (id, username, authority)
values (default, 'adult', 'ROLE_USER'),
       (default, 'child', 'ROLE_USER'),
       (default, 'child', 'ROLE_CHILD'),
       (default, 'librarian', 'ROLE_EDITOR'),
       (default, 'admin', 'ROLE_ADMIN'),
       (default, 'admin', 'ROLE_EDITOR'),
       (default, 'guest', 'ROLE_USER')
;
