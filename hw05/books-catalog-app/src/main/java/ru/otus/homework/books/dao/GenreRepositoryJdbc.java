package ru.otus.homework.books.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.homework.books.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Repository
public class GenreRepositoryJdbc implements GenreRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GenreRepositoryJdbc(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int count() {
        var count = getJdbcOperations().queryForObject("select count(*) from genres", Integer.class);
        return count == null ? 0 : count;
    }

    @Override
    public void save(Genre genre) {
        if (genre.getId() == null) {
            var id = getJdbcOperations().queryForObject("select max(id) from genres", Long.class);
            genre.setId(id == null ? 1 : id + 1);
        }
        var params = Map.of("id", genre.getId(), "name", genre.getName());
        namedParameterJdbcTemplate.update("merge into genres (id, name) values (:id, :name)", params);
    }

    @Override
    public Genre findById(long id) {
        var sql = "select id, name from genres where id = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, Map.of("id", id), new GenreMapper());
    }

    @Override
    public Optional<Genre> findByName(String name) {
        var sql = "select id, name from genres where name = :name";
        try {
            return ofNullable(namedParameterJdbcTemplate.queryForObject(sql, Map.of("name", name), new GenreMapper()));
        } catch (EmptyResultDataAccessException ex) {
            return empty();
        }
    }

    @Override
    public List<Genre> findAllUsed() {
        var sql = """
                select id, name
                  from genres
                  where id in (select distinct genre_id
                                 from books)
                """;
        return getJdbcOperations().query(sql, new GenreMapper());
    }

    @Override
    public List<Genre> findAll() {
        return getJdbcOperations().query("select id, name from genres", new GenreMapper());
    }

    @Override
    public void deleteById(long id) {
        namedParameterJdbcTemplate.update("delete from genres where id = :id", Map.of("id", id));
    }

    @Override
    public void delete(Genre genre) {
        deleteById(genre.getId());
    }

    private JdbcOperations getJdbcOperations() {
        return namedParameterJdbcTemplate.getJdbcOperations();
    }

    private static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
