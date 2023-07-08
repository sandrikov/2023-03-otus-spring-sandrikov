create table genres
(
    id   bigint auto_increment,
    name varchar(128) not null,
    primary key (id)
);
alter table genres
    add constraint "UK_genre_name" unique (name);

create table authors
(
    id   bigint auto_increment,
    name varchar(128) not null,
    primary key (id)
);
alter table authors
    add constraint "UK_author_name" unique (name);

create table books
(
    id        bigint auto_increment,
    title     varchar(128) not null,
    author_id bigint       not null,
    genre_id  bigint       not null,
    primary key (id)
);
alter table books
    add constraint "UK_book_name_author" unique (title, author_id);
alter table books
    add constraint "FK_book_author"
        foreign key (author_id) references authors;
alter table books
    add constraint "FK_book_genre"
        foreign key (genre_id) references genres;

create table comments
(
    id      bigint auto_increment,
    book_id bigint        not null,
    text    varchar(1024) not null,
    primary key (id)
);
alter table comments
    add constraint "FK_comment_book"
        foreign key (book_id) references books;

create table users
(
    username varchar_ignorecase(50)  not null primary key,
    password varchar_ignorecase(500) not null,
    enabled  boolean                 not null
);
create table authorities
(
    id        bigint auto_increment,
    username  varchar_ignorecase(50) not null,
    authority varchar_ignorecase(50) not null,
    primary key (id) ,
    constraint fk_authorities_users foreign key (username) references users (username)
);
create unique index ix_auth_username on authorities (username, authority);
