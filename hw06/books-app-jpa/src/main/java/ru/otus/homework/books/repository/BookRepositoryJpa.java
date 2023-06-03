package ru.otus.homework.books.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.mappers.BookProjectionMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static ru.otus.homework.books.repository.BookSpecifications.authorEquals;
import static ru.otus.homework.books.repository.BookSpecifications.genreEquals;
import static ru.otus.homework.books.repository.BookSpecifications.titleEquals;

@Repository
public class BookRepositoryJpa implements BookRepository {

    private final BookProjectionMapper bookProjectionMapper;

    private final EntityManager em;

    public BookRepositoryJpa(EntityManager em, @Lazy BookProjectionMapper bookProjectionMapper) {
        this.bookProjectionMapper = bookProjectionMapper;
        this.em = em;
    }

    @Override
    public long count() {
        return em.createQuery("select count(*) from Book", Long.class)
                .getSingleResult();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() <= 0) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        return ofNullable(em.find(Book.class, id));
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, Author author) {
        val spec = Specification.where(titleEquals(title)).and(authorEquals(author));
        try {
            val book = getQuery(spec).getSingleResult();
            return Optional.of(book);
        } catch (NoResultException e) {
            return empty();
        }
    }

    @Override
    public List<Book> findAll() {
        val entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        return em.createQuery("select g from Book g", Book.class)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    public List<BookProjection> findAllWithStat() {
        val qls = """
                select b, count(c)
                  from Book b
                        join fetch b.author
                        join fetch b.genre
                        left join Comment c on b = c.book
                  group by b
                """;
        val query = em.createQuery(qls, Object[].class);
        return query.getResultList().stream().map(bookProjectionMapper::toDto).toList();
    }

    @Override
    public List<BookProjection> findAllWithStatByAuthorAndGenreTitle(Author author, Genre genre, String title) {
        val entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        val spec = getSpecification(author, genre, title);
        val query = getQuery(spec).setHint("jakarta.persistence.fetchgraph", entityGraph);

        List<Book> books = query.getResultList();

        val qls = "select c.book.id, count(c) from Comment c group by c.book.id";
        val commentCounts = em.createQuery(qls, Object[].class).getResultList();
        Map<Long, Long> stat = commentCounts.stream().collect(Collectors.toMap(t -> (Long)t[0], t -> (Long)t[1]));

        return books.stream()
                .map(b -> new Object[] {b, stat.getOrDefault(b.getId(), 0L)})
                .map(bookProjectionMapper::toDto).toList();
    }

    @Override
    public void deleteById(long id) {
        findById(id).ifPresent(em::remove);
    }

    @Override
    public void delete(Book book) {
        val existingBook = em.find(Book.class, book.getId());
        if (existingBook != null) {
            em.remove(em.contains(book) ? book : em.merge(book));
        }
    }

    @Override
    public int deleteAllByAuthor(Author author) {
        return deleteAll(findAllByAuthorGenreTitle(author, null, null));
    }

    @Override
    public int deleteAllByAuthorAndGenre(Author author, Genre genre) {
        return deleteAll(findAllByAuthorGenreTitle(author, genre, null));
    }

    @Override
    public int deleteAllByGenre(Genre genre) {
        return deleteAll(findAllByAuthorGenreTitle(null, genre, null));
    }

    @Override
    public int deleteAll() {
        return deleteAll(findAll());
    }

    @Override
    public long countByAuthorAndGenreAndTitle(Author author, Genre genre, String title) {
        if (author == null && genre == null && title == null) {
            return count();
        }
        val builder = em.getCriteriaBuilder();
        val cq = builder.createQuery(Long.class);
        val root = cq.from(Book.class);
        val predicate = getSpecification(author, genre, title)
                .toPredicate(root, cq, builder);
        cq.select(builder.count(root)).where(predicate);
        return em.createQuery(cq).getSingleResult();
    }

    @Override
    public Optional<Comment> findCommentById(long id) {
        return ofNullable(em.find(Comment.class, id));
    }

    @Override
    public Comment saveComment(Book book, Comment comment) {
        if (comment.getId() <= 0) {
            book.addComment(comment);
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public void deleteComment(Book book, Comment comment) {
        val existingComment = em.find(Comment.class, comment.getId());
        if (existingComment != null) {
            book.removeComment(comment);
            em.remove(em.contains(comment) ? comment : em.merge(comment));
        }
    }

    private static Specification<Book> getSpecification(Author author, Genre genre, String title) {
        return Specification.where(titleEquals(title))
                .and(genreEquals(genre))
                .and(authorEquals(author));
    }

    private List<Book> findAllByAuthorGenreTitle(Author author, Genre genre, String title) {
        val spec = getSpecification(author, genre, title);
        return getQuery(spec).getResultList();
    }


    private TypedQuery<Book> getQuery(Specification<Book> spec) {
        val builder = em.getCriteriaBuilder();
        val criteriaQuery = builder.createQuery(Book.class);
        val root = criteriaQuery.from(Book.class);
        val predicate = spec.toPredicate(root, criteriaQuery, builder);
        return em.createQuery(criteriaQuery.where(predicate).select(root));
    }

    private int deleteAll(List<Book> booksToDelete) {
        booksToDelete.forEach(em::remove);
        return booksToDelete.size();
    }

}
