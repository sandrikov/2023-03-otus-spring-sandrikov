package ru.otus.homework.books.mappers;

import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.dto.BookProjection;
import ru.otus.homework.books.dto.CommentDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BookMapperImpl implements BookMapper {
    private final GenreMapper genreMapper;

    private final AuthorMapper authorMapper;

    private final CommentMapper commentMapper;

    public BookMapperImpl(GenreMapper genreMapper, AuthorMapper authorMapper, CommentMapper commentMapper) {
        this.genreMapper = genreMapper;
        this.authorMapper = authorMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public Book toEntity(BookDto dto) {
        val comments = dto.getComments();
        val book = new Book(dto.getId(), dto.getTitle(), authorMapper.toEntity(dto.getAuthor()),
                genreMapper.toEntity(dto.getGenre()), null);
        if (comments != null) {
            comments.stream().map(c -> commentMapper.toEntity(c, book)).forEach(book::addComment);
        }
        return book;
    }

    @Override
    public BookDto toDto(Book book) {
        val comments = book.getComments() != null ?
                book.getComments().stream().map(commentMapper::toDto).toList() : null;
        return new BookDto(book.getId(), book.getTitle(), authorMapper.toDto(book.getAuthor()),
                genreMapper.toDto(book.getGenre()), comments);
    }

    @Override
    public BookProjection toBookProjection(Book book, long numberOfComments) {
        return new BookProjection(book.getId(), book.getTitle(), authorMapper.toDto(book.getAuthor()),
                genreMapper.toDto(book.getGenre()), numberOfComments);
    }

    @Override
    public void partialUpdate(BookDto dto, Book book) {
        if (dto == null) {
            return;
        }
        if (dto.getTitle() != null) {
            book.setTitle(dto.getTitle());
        }
        if (dto.getAuthor() != null) {
            book.setAuthor(authorMapper.toEntity(dto.getAuthor()));
        }
        if (dto.getGenre() != null) {
            book.setGenre(genreMapper.toEntity(dto.getGenre()));
        }
        List<CommentDto> comments = dto.getComments();
        if (comments != null) {
            mergeComments(book, comments);
        }
    }

    private void mergeComments(Book book, List<CommentDto> comments) {
        List<Comment> toAdd = new ArrayList<>();
        Map<Long, CommentDto> toMerge = new HashMap<>();
        for (val commentDto : comments) {
            if (commentDto.getId() < 1) {
                toAdd.add(commentMapper.toEntity(commentDto, book));
            } else {
                toMerge.put(commentDto.getId(), commentDto);
            }
        }
        for (var comment : book.getComments().toArray(Comment[]::new)) {
            val commentDto = toMerge.get(comment.getId());
            if (commentDto == null) {
                book.removeComment(comment);
            } else {
                commentMapper.partialUpdate(commentDto, comment);
            }
        }
        toAdd.forEach(book::addComment);
    }
}
