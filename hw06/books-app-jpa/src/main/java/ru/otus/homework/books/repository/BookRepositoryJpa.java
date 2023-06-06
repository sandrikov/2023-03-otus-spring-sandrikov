package ru.otus.homework.books.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
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

@Component
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
            val entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
            val book = getQuery(spec)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph)
                    .getSingleResult();
            return Optional.of(book);
        } catch (NoResultException e) {
            return empty();
        }
    }

    @Override
    public List<Book> findAll() {
        val qls = "select b from Book b join fetch b.author join fetch b.genre";
        return em.createQuery(qls, Book.class).getResultList();
    }

    @Override
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
    public List<BookProjection> findAllWithStatByAuthorAndGenreAndTitle(Author author, Genre genre, String title) {
        val spec = getSpecification(author, genre, title);
        val entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        val books = getQuery(spec)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();

        val qls = "select c.book.id, count(c) from Comment c group by c.book.id";
        val commentCounts = em.createQuery(qls, Object[].class).getResultList();

        Map<Long, Long> stat = commentCounts.stream().collect(Collectors.toMap(t -> (Long)t[0], t -> (Long)t[1]));

        return books.stream()
                .map(b -> new Object[] {b, stat.getOrDefault(b.getId(), 0L)})
                .map(bookProjectionMapper::toDto).toList();
    }

    public List<Book> findAllByAuthorAndGenreAndTitle(Author author, Genre genre, String title) {
        val spec = getSpecification(author, genre, title);
        val entityGraph = em.getEntityGraph("book-author-genre-entity-graph");
        return getQuery(spec)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
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
    public int deleteAllInBatch(List<Book> booksToDelete) {
        val query = em.createQuery("delete from Book b where b in (:books)");
        query.setParameter("books", booksToDelete);
        return query.executeUpdate();
    }

    @Override
    public int deleteAllInBatch() {
        return em.createQuery("delete from Book").executeUpdate();
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

    private static Specification<Book> getSpecification(Author author, Genre genre, String title) {
        return Specification.where(titleEquals(title))
                .and(genreEquals(genre))
                .and(authorEquals(author));
    }

    private TypedQuery<Book> getQuery(Specification<Book> spec) {
        val builder = em.getCriteriaBuilder();
        val criteriaQuery = builder.createQuery(Book.class);
        val root = criteriaQuery.from(Book.class);
        val predicate = spec.toPredicate(root, criteriaQuery, builder);
        return em.createQuery(criteriaQuery.where(predicate).select(root));
    }

}
