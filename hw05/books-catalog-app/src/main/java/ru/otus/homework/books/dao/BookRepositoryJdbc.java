package ru.otus.homework.books.dao;


import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.homework.books.model.Author;
import ru.otus.homework.books.model.Book;
import ru.otus.homework.books.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Repository
public class BookRepositoryJdbc implements BookRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public BookRepositoryJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate, AuthorRepository authorRepository,
                              GenreRepository genreRepository) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public int count() {
        var count = getJdbcOperations().queryForObject("select count(*) from books", Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public void save(Book book) {
        if (book.getId() == null) {
            var id = getJdbcOperations().queryForObject("select max(id) from books", Long.class);
            book.setId(id == null ? 1 : id + 1);
        }
        var sql = "merge into books (id, name, author_id, genre_id) values (:id, :name, :author_id, :genre_id)";
        var params = Map.of("id", book.getId(),
                "name", book.getName(),
                "author_id", book.getAuthor().getId(),
                "genre_id", book.getGenre().getId());
        namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public List<Book> findAll() {
        var authorMap = authorRepository.findAllUsed().stream().collect(toMap(Author::getId, identity()));
        var genreMap = genreRepository.findAllUsed().stream().collect(toMap(Genre::getId, identity()));
        return getJdbcOperations().query("select id, name, author_id, genre_id from books",
                new BookMapper(authorMap::get, genreMap::get));
    }

    @Override
    public Book findById(long id) {
        var sql = "select id, name, author_id, genre_id from books where id = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id),
                new BookMapper(authorRepository::findById, genreRepository::findById));
    }

    @Override
    public Optional<Book> findByNameAndAuthor(String name, Author author) {
        var sql = "select id, name, author_id, genre_id from books where name = :name and author_id = :author_id";
        var params = Map.of("name", name,"author_id", author.getId());
        try {
            var book = namedParameterJdbcTemplate.queryForObject(sql, params,
                    new BookMapper(t -> author, genreRepository::findById));
            return ofNullable(book);
        } catch (EmptyResultDataAccessException ex) {
            return empty();
        }
    }

    @Override
    public List<Book> findByName(String name) {
        var sql = "select id, name, author_id, genre_id from books where name = :name";
        return namedParameterJdbcTemplate.query(sql, Map.of("name", name),
                new BookMapper(authorRepository::findById, genreRepository::findById));
    }

    @Override
    public List<Book> findByAuthor(Author author) {
        var sql = "select id, name, author_id, genre_id from books where author_id = :author_id";
        return namedParameterJdbcTemplate.query(sql, Map.of("author_id", author.getId()),
                    new BookMapper(t -> author, genreRepository::findById));
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        var sql = "select id, name, author_id, genre_id from books where genre_id = :genre_id";
        return namedParameterJdbcTemplate.query(sql, Map.of("genre_id", genre.getId()),
                new BookMapper(authorRepository::findById, t -> genre));
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcTemplate.update("delete from books where id = :id", Map.of("id", id));
    }

    @Override
    public void delete(Book book) {
        deleteById(book.getId());
    }

    @Override
    public int deleteAll() {
        return getJdbcOperations().update("delete from books");
    }

    @Override
    public int deleteAllByAuthor(Author author) {
        var sql = "delete from books where author_id = :author_id";
        return namedParameterJdbcTemplate.update(sql, Map.of("author_id", author.getId()));
    }

    @Override
    public int deleteAllByGenre(Genre genre) {
        var sql = "delete from books where genre_id = :genre_id";
        return namedParameterJdbcTemplate.update(sql, Map.of("genre_id", genre.getId()));
    }

    @Override
    public int deleteAllByAuthorAndGenre(Author author, Genre genre) {
        var sql = "delete from books where genre_id = :genre_id and author_id = :author_id";
        return namedParameterJdbcTemplate.update(sql, Map.of("genre_id", genre.getId(), "author_id", author.getId()));
    }

    private JdbcOperations getJdbcOperations() {
        return namedParameterJdbcTemplate.getJdbcOperations();
    }

    private record BookMapper(Function<Long, Author> getAuthor,
                              Function<Long, Genre> getGenre) implements RowMapper<Book> {

        @Override
            public Book mapRow(ResultSet rs, int i) throws SQLException {
                var id = rs.getLong("id");
                var name = rs.getString("name");
                var authorId = rs.getLong("author_id");
                var genreId = rs.getLong("genre_id");
                return new Book(id, name, getAuthor.apply(authorId), getGenre.apply(genreId));
            }
        }
}
