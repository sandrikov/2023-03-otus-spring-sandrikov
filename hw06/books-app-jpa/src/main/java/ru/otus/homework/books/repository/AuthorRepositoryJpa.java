package ru.otus.homework.books.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.otus.homework.books.domain.Author;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

@Repository
public class AuthorRepositoryJpa implements AuthorRepository {

    private final EntityManager em;

    public AuthorRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Author save(Author author) {
        if (author.getId() <= 0) {
            em.persist(author);
            return author;
        } else {
            return em.merge(author);
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        val author = em.find(Author.class, id);
        return ofNullable(author);
    }

    @Override
    public Optional<Author> findByName(String name) {
        try {
            val author = em.createQuery("select a from Author a where a.name = :name", Author.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(author);
        } catch (NoResultException e) {
            return empty();
        }
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery("select a from Author a", Author.class)
                .getResultList();
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public void delete(Author author) {
        val existingAuthor = em.find(Author.class, author.getId());
        if (existingAuthor != null) {
            em.remove(em.contains(author) ? author : em.merge(author));
        }
    }

    @Override
    public long count() {
        return em.createQuery("select count(*) from Author", Long.class)
                .getSingleResult();
    }
}
