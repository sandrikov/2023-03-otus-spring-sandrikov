create table genres(
  id bigserial,
  name varchar(255) not null,
  primary key (id),
  unique (name)
);

create table authors(
  id bigserial,
  name varchar(255) not null,
  primary key (id),
  unique (name)
);

create table books(
  id bigserial,
  name varchar(255) not null,
  author_id bigint not null references authors (id),
  genre_id bigint not null references genres (id),
  primary key (id),
  unique ( name, author_id )
);
