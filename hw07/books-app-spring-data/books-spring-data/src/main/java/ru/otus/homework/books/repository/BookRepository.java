package ru.otus.homework.books.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.ListQueryByExampleExecutor;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, ListQueryByExampleExecutor<Book>,
        BookRepositoryCustom {

    /**
     * Find by alternative key
     */
    @EntityGraph("book-author-genre-entity-graph")
    Optional<Book> findByTitleAndAuthor(String title, Author author);

    @Query("""
            select b, count(c)
              from Book b
                    join fetch b.author
                    join fetch b.genre
                    left join Comment c on b = c.book
              group by b""")
    List<Object[]> findAllBookWithCommentCount();

    @EntityGraph("book-author-genre-entity-graph")
    @Override
    <S extends Book> List<S> findAll(Example<S> example);

}
