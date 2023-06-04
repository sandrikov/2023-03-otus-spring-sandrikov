package ru.otus.homework.books.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books",
        uniqueConstraints = @UniqueConstraint(columnNames = {"title", "author_id"}))
@NamedEntityGraph(name = "book-author-genre-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")}
)
public class Book {

    public static final int MAX_TITLE_LENGTH = 128;

    public static final String UK_BOOK_TITLE_AUTHOR = "UK_book_title_author";

    public static final String FK_BOOK_AUTHOR = "FK_book_author";

    public static final String FK_BOOK_GENRE = "FK_book_genre";

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = MAX_TITLE_LENGTH)
    private String title;

    @ManyToOne(cascade = REFRESH, fetch = LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(cascade = REFRESH, fetch = LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @OneToMany(mappedBy = "book", cascade = ALL, fetch = LAZY, orphanRemoval = true)
    private List<Comment> comments;

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public void addComment(Comment comment) {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
        comment.setBook(this);
    }

    public void addComment(String text) {
        addComment(new Comment(text, this));
    }

    public void removeComment(Comment comment) {
        if (comments != null) {
            for (Iterator<Comment> it = comments.iterator(); it.hasNext();) {
                val existingComment = it.next();
                if (existingComment.getId() == comment.getId()) {
                    it.remove();
                    break;
                }
            }
        }
        comment.setBook(null);
    }
}
