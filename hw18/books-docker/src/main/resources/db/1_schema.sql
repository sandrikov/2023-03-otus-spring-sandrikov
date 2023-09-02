create table if not exists genres (
    id   bigserial primary key,
    name varchar(128) not null,
    constraint "UK_genre_name" unique (name)
);

create table if not exists authors (
    id   bigserial primary key,
    name varchar(128) not null,
    constraint "uk_author_name" unique (name)
);

create table if not exists books (
    id        bigserial primary key,
    title     varchar(128) not null,
    author_id bigint       not null,
    genre_id  bigint       not null,
    age_limit integer      not null,
    constraint "UK_book_name_author" unique (title, author_id),
    constraint "FK_book_author" foreign key (author_id) references authors(id),
    constraint "FK_book_genre" foreign key (genre_id) references genres(id)
);

create table if not exists comments (
    id      bigserial primary key,
    book_id bigint        not null,
    text    varchar(1024) not null,
    constraint "FK_comment_book" foreign key (book_id) references books(id)
);

create table if not exists users (
    username varchar(50)  not null primary key,
    password varchar(500) not null,
    enabled  boolean                 not null
);

create table if not exists authorities (
    id        bigserial primary key,
    username  varchar(50) not null,
    authority varchar(50) not null,
    constraint "UK_auth_username" unique (username, authority),
    constraint FK_authorities_users foreign key (username) references users (username)
);
