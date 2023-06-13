package ru.otus.homework.books.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.REFRESH;
import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
@NamedEntityGraph(name = "book-author-genre-entity-graph",
        attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("genre")})
public class Book {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    private String title;

    @ManyToOne(cascade = REFRESH)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(cascade = REFRESH)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @OneToMany(mappedBy = "book", cascade = ALL, orphanRemoval = true)
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
    }

    public void addComment(String text) {
        val comment = new Comment(text, this);
        addComment(comment);
    }

    public void removeComment(Comment comment) {
        if (comments != null) {
            for (var it = comments.iterator(); it.hasNext();) {
                val existingComment = it.next();
                if (existingComment.getId() == comment.getId()) {
                    it.remove();
                    break;
                }
            }
        }
        comment.setBook(null);
    }

    public void removeAllComments() {
        if (comments != null) {
            comments.clear();
        }
    }
}
