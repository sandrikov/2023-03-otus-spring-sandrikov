package ru.otus.homework.books.repository;

import jakarta.persistence.EntityManager;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Component
public class CommentRepositoryJpa implements CommentRepository {

    private final EntityManager em;

    public CommentRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() <= 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public Optional<Comment> findById(long id){
        val comment = em.find(Comment.class, id);
        return ofNullable(comment);
    }

    @Override
    public void delete(Comment comment) {
        val existingComment = em.find(Comment.class, comment.getId());
        if (existingComment != null) {
            em.remove(em.contains(comment) ? comment : em.merge(comment));
        }
    }

    @Override
    public void deleteAllByBooksInBatch(List<Book> books) {
        val query = em.createQuery("delete from Comment c where c.book in (:books)");
        query.setParameter("books", books);
        query.executeUpdate();
    }

    @Override
    public void deleteAllInBatch() {
        em.createQuery("delete from Comment").executeUpdate();
    }
}
