insert into genres (id, `name`)
values (1, 'Adventure fiction'),
       (2, 'Autobiography'),
       (3, 'Detective');

insert into authors (id, `name`)
values (1, 'Mark Twain'),
       (2, 'Agatha Christie'),
       (3, 'Leo Tolstoy');

insert into books (id, `name`, author_id, genre_id)
values (1, 'The Murder on the Links', 2, 3),
       (2, 'The Secret of Chimneys', 2, 3),
       (3, 'Adventures of Huckleberry Finn', 1, 1),
       (4, 'The Adventures of Tom Sawyer', 1, 1),
       (5, 'Tom Sawyer Abroad', 1, 1),
       (6, 'Tom Sawyer, Detective ', 1, 3);
