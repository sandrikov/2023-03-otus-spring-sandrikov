package ru.otus.homework.books.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.homework.books.domain.Genre;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Component
public class GenreRepositoryJpa implements GenreRepository {

    private final EntityManager em;

    public GenreRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() <= 0) {
            em.persist(genre);
            return genre;
        } else {
            return em.merge(genre);
        }
    }

    @Override
    public Optional<Genre> findById(long id) {
        return ofNullable(em.find(Genre.class, id));
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try {
            val genre = em.createQuery("select a from Genre a where a.name = :name", Genre.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(genre);
        } catch (NoResultException e) {
            return empty();
        }
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery("select a from Genre a", Genre.class)
                .getResultList();
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public void delete(Genre genre) {
        val existingGenre = em.find(Genre.class, genre.getId());
        if (existingGenre != null) {
            em.remove(em.contains(genre) ? genre : em.merge(genre));
        }
    }

    @Override
    public long count() {
        return em.createQuery("select count(*) from Genre", Long.class)
                .getSingleResult();
    }
}
