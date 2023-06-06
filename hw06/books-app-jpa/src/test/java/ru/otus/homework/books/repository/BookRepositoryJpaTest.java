package ru.otus.homework.books.repository;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.domain.Genre;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.mappers.AuthorMapperImpl;
import ru.otus.homework.books.mappers.BookProjectionMapperImpl;
import ru.otus.homework.books.mappers.CommentMapperImpl;
import ru.otus.homework.books.mappers.GenreMapperImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.otus.homework.books.repository.AuthorRepositoryJpaTest.MARK_TWAIN;
import static ru.otus.homework.books.repository.AuthorRepositoryJpaTest.MARK_TWAIN_ID;
import static ru.otus.homework.books.repository.AuthorRepositoryJpaTest.UNUSED_AUTHOR_ID;
import static ru.otus.homework.books.repository.GenreRepositoryJpaTest.DETECTIVE;
import static ru.otus.homework.books.repository.GenreRepositoryJpaTest.DETECTIVE_GENRE_ID;
import static ru.otus.homework.books.repository.GenreRepositoryJpaTest.HISTORICAL_FICTION;
import static ru.otus.homework.books.repository.GenreRepositoryJpaTest.HISTORICAL_FICTION_ID;
import static ru.otus.homework.books.repository.GenreRepositoryJpaTest.UNUSED_GENRE_ID;

@DisplayName("Репозиторий для работы с книгами")
@DataJpaTest
@Import({BookRepositoryJpa.class, AuthorRepositoryJpa.class, GenreRepositoryJpa.class, CommentRepositoryJpa.class,
        GenreMapperImpl.class, AuthorMapperImpl.class, CommentMapperImpl.class, BookProjectionMapperImpl.class})
public class BookRepositoryJpaTest {

    public static final int NUMBER_OF_BOOKS = 8;
    public static final int NUMBER_OF_DETECTIVE_BOOKS = 3;
    public static final int NUMBER_OF_TWAIN_BOOKS = 5;
    public static final int NUMBER_OF_JEANNE_D_ARC_BOOKS = 2;
    public static final int NUMBER_OF_TWAIN_DETECTIVES = 1;
    public static final long TWAIN_D_ARK_BOOK_ID = 5;
    public static final int NUMBER_OF_TWAIN_D_ARK_COMMENTS = 2;
    public static final int TWAIN_D_ARK_1ST_COMMENT_ID = 6;
    public static final int TWAIN_D_ARK_2ND_COMMENT_ID = 7;
    public static final String JEANNE_D_ARC = "Жанна д'Арк";
    public static final String THREE_MUSKETEERS = "Три мушкетёра";
    public static final String ADVENTURES_OF_TOM_SAWYER = "Приключения Тома Сойера";
    public static final String TOM_SAWYER_DETECTIVES = "Том Сойер, детектив";

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("Кол-во книг")
    @Test
    void count() {
        val count = bookRepository.count();
        assertEquals(count, NUMBER_OF_BOOKS);
    }

    @DisplayName("Поиск всех книг")
    @Test
    void findAll() {
        val books = bookRepository.findAll();
        assertThat(books).isNotNull().hasSize(NUMBER_OF_BOOKS)
                .allMatch(s -> s.getTitle() != null && !s.getTitle().isBlank(), "Name is not blank")
                .allMatch(s -> s.getAuthor() != null, "Author is presented")
                .allMatch(s -> s.getGenre() != null, "Genre is presented");
    }

    @DisplayName("Поиск книг со статист. информацией")
    @Test
    void findAllWithStat() {
        val books = bookRepository.findAllWithStat();
        assertThat(books).isNotNull().hasSize(NUMBER_OF_BOOKS);
        books.stream().map(BookProjection::toString).forEach(System.out::println);
    }

    @DisplayName("Поиск по ID")
    @Test
    void findById() {
        val book = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertTrue(book.isPresent());
        assertEquals(JEANNE_D_ARC, book.get().getTitle(), "Book title");
        assertEquals(MARK_TWAIN, book.get().getAuthor().getName(), "Author name");
        assertEquals(HISTORICAL_FICTION, book.get().getGenre().getName(), "Genre name");
    }

    @DisplayName("Поиск по альтернативному ключу: название + автор")
    @Test
    void findByNameAndAuthor() {
        val author = new Author(MARK_TWAIN_ID, MARK_TWAIN);
        val book = bookRepository.findByTitleAndAuthor(JEANNE_D_ARC, author);
        assertTrue(book.isPresent());
        assertEquals(JEANNE_D_ARC, book.get().getTitle(), "Book title");
        assertEquals(author.getId(), book.get().getAuthor().getId(), "Author ID");
        assertEquals(author.getName(), book.get().getAuthor().getName(), "Author name");
        assertEquals(HISTORICAL_FICTION, book.get().getGenre().getName(), "Genre name");
    }

    @DisplayName("Каскадное удаление комментариев. Lazy данные не загружены. Авторы и жанры не удалены")
    @Test
    void deleteById() {
        bookRepository.deleteById(TWAIN_D_ARK_BOOK_ID);
        assertEquals(NUMBER_OF_BOOKS - 1, bookRepository.count());
        val optionalBook = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertThat(optionalBook).isNotPresent();
        val commentIds = new long[]{6, 7};
        for (long commentId : commentIds) {
            assertThat(em.find(Comment.class, commentId)).isNull();
        }
        val authorById = em.find(Author.class, MARK_TWAIN_ID);
        assertThat(authorById).isNotNull();
        val genreById = em.find(Genre.class, HISTORICAL_FICTION_ID);
        assertThat(genreById).isNotNull();
    }

    @DisplayName("Каскадное удаление комментариев. Lazy данные в контексте. Авторы и жанры не удалены")
    @Test
    void delete() {
        val optionalBook = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertThat(optionalBook).isPresent();
        val book = optionalBook.get();
        val comments = book.getComments();
        assertThat(comments).isNotNull()
                .hasSize(NUMBER_OF_TWAIN_D_ARK_COMMENTS);
        val commentIds = comments.stream().map(Comment::getId).toArray(Long[]::new);
        bookRepository.delete(book);
        assertEquals(NUMBER_OF_BOOKS - 1, bookRepository.count());
        val optionalBook1 = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertThat(optionalBook1).isNotPresent();
        for (Long commentId : commentIds) {
            assertThat(em.find(Comment.class, commentId)).isNull();
        }
        val authorById = em.find(Author.class, book.getAuthor().getId());
        assertThat(authorById).isNotNull();
        val genreById = em.find(Genre.class, book.getGenre().getId());
        assertThat(genreById).isNotNull();
    }

    @DisplayName("Создание книги и каскадное сохранение комментариев")
    @Test
    void saveInsert() {
        val author = em.find(Author.class, MARK_TWAIN_ID);
        val genre = em.find(Genre.class, DETECTIVE_GENRE_ID);
        val bookToSave = new Book(THREE_MUSKETEERS, author, genre);
        bookToSave.addComment("Comment #1 " + THREE_MUSKETEERS);
        bookToSave.addComment("Comment #2 " + THREE_MUSKETEERS);
        val saved = bookRepository.save(bookToSave);

        val bookOptional = bookRepository.findById(saved.getId());
        assertThat(bookOptional).isPresent();
        val book = bookOptional.get();
        assertThat(book)
                .matches(s -> s.getAuthor() != null, "Author is presented")
                .matches(s -> s.getGenre() != null, "Genre is presented")
                .isSameAs(saved);
        assertEquals(THREE_MUSKETEERS, book.getTitle(), "Book title");
        assertEquals(MARK_TWAIN, book.getAuthor().getName(), "Author");
        assertEquals(DETECTIVE, book.getGenre().getName(), "Genre");
        assertThat(book.getComments()).isNotNull().hasSize(2);
    }

    @DisplayName("Изменение всех аттрибутов книги и каскадное сохранение/удаление комментариев")
    @Test
    void saveUpdate() {
        val optionalBook = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertThat(optionalBook).isPresent();
        val book2save = optionalBook.get();
        val comments = book2save.getComments();
        assertThat(comments).isNotNull().hasSize(NUMBER_OF_TWAIN_D_ARK_COMMENTS);

        val newAuthor = em.find(Author.class, UNUSED_AUTHOR_ID);
        val newGenre = em.find(Genre.class, UNUSED_GENRE_ID);
        val newTitle = "Новое название";
        val comment1NewText = "Новый текст 1-го комментария";
        val newComment2Text = "Текст нового 2-го комментария";
        val newComment3Text = "Текст нового 3-го комментария";
        book2save.setTitle(newTitle);
        book2save.setAuthor(newAuthor);
        book2save.setGenre(newGenre);
        comments.get(0).setText(comment1NewText);
        book2save.removeComment(comments.get(1));
        book2save.addComment(newComment2Text);
        book2save.addComment(newComment3Text);

        bookRepository.save(book2save);

        val bookWithStats = bookRepository.findAllWithStat();
        assertThat(bookWithStats).isNotNull().hasSize(NUMBER_OF_BOOKS);
        val bookWithStat = bookWithStats.stream()
                .filter(t -> t.id() == TWAIN_D_ARK_BOOK_ID).findFirst();
        assertTrue(bookWithStat.isPresent());
        val book = bookWithStat.get();
        assertEquals(3, book.commentCount(), "Comments Count");
        assertEquals(newTitle, book.title(), "Book title");
        assertEquals(newAuthor.getName(), book.author().getName(), "Author");
        assertEquals(newGenre.getName(), book.genre().getName(), "Genre");
        em.flush();
        em.clear();
        val bookAfter = bookRepository.findById(TWAIN_D_ARK_BOOK_ID);
        assertThat(bookAfter).isPresent();
        assertThat(bookAfter.get().getComments()).isNotNull().hasSize(3)
                .extracting(Comment::getText).contains(comment1NewText, newComment2Text, newComment3Text);
    }

    @DisplayName("Поиск по альтернативному ключу")
    @Test
    void findByTitleAndAuthor() {
        val author = new Author(MARK_TWAIN_ID, MARK_TWAIN);
        val book = bookRepository.findByTitleAndAuthor(JEANNE_D_ARC, author);
        assertTrue(book.isPresent());
        assertEquals(JEANNE_D_ARC, book.get().getTitle(), "Book title");
        assertEquals(author.getId(), book.get().getAuthor().getId(), "Author id");
        assertEquals(author.getName(), book.get().getAuthor().getName(), "Author name");
        assertEquals(HISTORICAL_FICTION, book.get().getGenre().getName(), "Genre name");
    }

    @DisplayName("Удаление книги по автору")
    @Test
    void deleteBooksByAuthor() {
        val author = new Author(MARK_TWAIN_ID, MARK_TWAIN);
        val books = bookRepository.findAllByAuthorAndGenreAndTitle(author, null, null);
        assertEquals(NUMBER_OF_TWAIN_BOOKS, books.size(), "Number of books");

        commentRepository.deleteAllByBooksInBatch(books);
        int deleted = bookRepository.deleteAllInBatch(books);
        assertEquals(NUMBER_OF_TWAIN_BOOKS, deleted, "Number of deleted books");
        assertEquals(NUMBER_OF_BOOKS - NUMBER_OF_TWAIN_BOOKS, bookRepository.count(), "Number of rest books");
    }

    @DisplayName("Посчитать книги о разным критериям")
    @Test
    void countByAuthorAndGenreAndTitle() {
        val author = new Author(MARK_TWAIN_ID, MARK_TWAIN);
        val genre = new Genre(DETECTIVE_GENRE_ID, DETECTIVE);
        long count;
        count = bookRepository.countByAuthorAndGenreAndTitle(null, null, null);
        assertEquals(NUMBER_OF_BOOKS, count, "All books");
        count = bookRepository.countByAuthorAndGenreAndTitle(author, null, null);
        assertEquals(NUMBER_OF_TWAIN_BOOKS, count, "Number of Twain's books");
        count = bookRepository.countByAuthorAndGenreAndTitle(null, genre, null);
        assertEquals(NUMBER_OF_DETECTIVE_BOOKS, count, "Number of Twain's books");
        count = bookRepository.countByAuthorAndGenreAndTitle(author, genre, null);
        assertEquals(NUMBER_OF_TWAIN_DETECTIVES, count, "Number of Twain's detectives");
    }

}