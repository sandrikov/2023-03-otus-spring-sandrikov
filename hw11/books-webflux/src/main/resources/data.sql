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
