create table genres
(
    id   serial primary key,
    name varchar(128) not null
);
alter table genres
    add constraint "UK_genre_name" unique (name);

create table authors
(
    id   serial primary key,
    name varchar(128) not null
);
alter table authors
    add constraint "UK_author_name" unique (name);

create table books
(
    id        serial primary key,
    title     varchar(128) not null,
    author_id bigint       not null,
    genre_id  bigint       not null
);
alter table books
    add constraint "UK_book_name_author" unique (title, author_id);
alter table books
    add constraint "FK_book_author"
        foreign key (author_id) references authors;
alter table books
    add constraint "FK_book_genre"
        foreign key (genre_id) references genres;

insert into genres (id, name)
values (default, 'Adventure fiction'), -- 1
       (default, 'Autobiography'),     -- 2
       (default, 'Detective'); -- 3

insert into authors (id, name)
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