package ru.otus.homework.books.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.homework.books.model.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;


@Repository
public class AuthorRepositoryJdbc implements AuthorRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public AuthorRepositoryJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Author> findAllUsed() {
        var sql = """
                select id, name
                  from authors
                  where id in (select distinct author_id
                                 from books)
                """;
        return getJdbcOperations().query(sql, new AuthorMapper());
    }

    @Override
    public List<Author> findAll() {
        return getJdbcOperations().query("select id, name from authors", new AuthorMapper());
    }

    @Override
    public int count() {
        var count = getJdbcOperations().queryForObject("select count(*) from authors", Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public void save(Author author) {
        if (author.getId() == null) {
            var id = getJdbcOperations().queryForObject("select max(id) from authors", Long.class);
            author.setId(id == null ? 1 : id + 1);
        }
        var params = Map.of("id", author.getId(), "name", author.getName());
        namedParameterJdbcTemplate.update("merge into authors (id, name) values (:id, :name)", params);
    }

    @Override
    public Author findById(long id) {
        var sql = "select id, name from authors where id = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id), new AuthorMapper());

    }

    @Override
    public Optional<Author> findByName(String name) {
        var sql = "select id, name from authors where name = :name";
        try {
            return ofNullable(namedParameterJdbcTemplate.queryForObject(sql, Map.of("name", name), new AuthorMapper()));
        } catch (EmptyResultDataAccessException ex) {
            return empty();
        }
    }

    @Override
    public void deleteById(long id) {
        int rowCount = namedParameterJdbcTemplate.update("delete from authors where id = :id", Map.of("id", id));
        if (rowCount == 0) {
            throw new EmptyResultDataAccessException(1);
        }
    }

    @Override
    public void delete(Author author) {
        deleteById(author.getId());
    }

    private JdbcOperations getJdbcOperations() {
        return namedParameterJdbcTemplate.getJdbcOperations();
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            return new Author(id, name);
        }
    }
}
