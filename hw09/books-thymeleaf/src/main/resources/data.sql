insert into genres (id, `name`)
values (default, 'Adventure fiction'), -- 1
       (default, 'Autobiography'),     -- 2
       (default, 'Detective'); -- 3

insert into authors (id, `name`)
values (default, 'Mark Twain'),      -- 1
       (default, 'Agatha Christie'), -- 2
       (default, 'Leo Tolstoy'); -- 3

insert into books (id, title, author_id, genre_id)
values (default, 'The Murder on the Links', 2, 3),        -- 1
       (default, 'The Secret of Chimneys', 2, 3),         -- 2
       (default, 'Adventures of Huckleberry Finn', 1, 1), -- 3
       (default, 'The Adventures of Tom Sawyer', 1, 1),   -- 4
       (default, 'Tom Sawyer Abroad', 1, 1),              -- 5
       (default, 'Tom Sawyer, Detective ', 1, 3); -- 6

insert into comments (id, book_id, text)
values (default, 1, 'Comment №1 The Murder on the Links'),
       (default, 1, 'Comment №2 The Murder on the Links'),
       --(default, 2, 'The Secret of Chimneys'),  -- 2 w/o comment
       (default, 3, 'Comment №1 Adventures of Huckleberry Finn'),
       (default, 4, 'Comment №1 The Adventures of Tom Sawyer'),
       (default, 5, 'Comment №1 Tom Sawyer Abroad'),
       (default, 6, 'Comment №1 Tom Sawyer, Detective ');
