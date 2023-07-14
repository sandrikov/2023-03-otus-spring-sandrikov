insert into genres (id, `name`)
values (default, 'Приключенческая литература'), -- 1
       (default, 'Автобиография'),              -- 2 жанр без книг в каталоге
       (default, 'Детектив'),                   -- 3
       (default, 'Исторические хроники'),       -- 4
       (default, 'Исторический роман'); -- 5

insert into authors (id, `name`)
values (default, 'Марк Твен'),    -- 1
       (default, 'Агата Кристи'), -- 2
       (default, 'Лев Толстой'),  -- 3 автор без книг в каталоге
       (default, 'Александр Дюма'); -- 4

insert into books (id, `title`, author_id, genre_id)
values (default, 'Приключения Тома Сойера', 1, 1),      -- 1
       (default, 'Приключения Гекльберри Финна', 1, 1), -- 2
       (default, 'Том Сойер за границей', 1, 1),        -- 3
       (default, 'Том Сойер, детектив', 1, 3),          -- 4
       (default, 'Жанна д''Арк', 1, 5),                 -- 5
       (default, 'Жанна д''Арк', 4, 4),                 -- 6 одноимённая книга другого автора в другом жанре
       (default, 'Убийство на перепутье', 2, 3),        -- 7
       (default, 'Тайна дымоходов', 2, 3); -- 8

insert into comments (id, book_id, text)
values (default, 1, 'Comment #1 Приключения Тома Сойера'),      -- 1
       (default, 1, 'Comment #2 Приключения Тома Сойера'),      -- 2
       (default, 1, 'Comment #3 Приключения Тома Сойера'),      -- 3
       --(default, 2, 'Comment #1 Приключения Гекльберри Финна'), -- книга без комментария
       (default, 3, 'Comment #1 Том Сойер за границей'),        -- 4
       (default, 4, 'Comment #1 Том Сойер, детектив'),          -- 5
       (default, 5, 'Comment #1 Жанна д''Арк Марка Твена'),     -- 6
       (default, 5, 'Comment #2 Жанна д''Арк Марка Твена'),     -- 7
       (default, 6, 'Comment #1 Жанна д''Арк Александра Дюма'), -- 8
       (default, 7, 'Comment #1 Убийство на перепутье'),        -- 9
       (default, 8, 'Comment #1 Тайна дымоходов'); -- 10

INSERT INTO users (username, password, enabled)
values ('userOneAuthority', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', 1),
       ('userTwoAuthorities', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', 1),
       ('userNoAuthority', '$2a$10$bltA1SzOChKZIYJe47s0IuboUDNJLY6KgLj76orBDSsgjUbd3NCla', 0)
;

INSERT INTO authorities (id, username, authority)
values (default, 'userOneAuthority', 'ROLE_USER'),
       (default, 'userTwoAuthorities', 'ROLE_USER'),
       (default, 'userTwoAuthorities', 'ROLE_ADMIN')
