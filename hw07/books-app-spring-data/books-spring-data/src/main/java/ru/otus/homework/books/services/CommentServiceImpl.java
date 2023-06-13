package ru.otus.homework.books.services;

import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Book;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.dto.CommentDto;
import ru.otus.homework.books.mappers.CommentMapper;
import ru.otus.homework.books.repository.BookRepository;
import ru.otus.homework.books.repository.CommentRepository;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.ServiceUtils;

import static ru.otus.homework.books.services.BookServiceImpl.BOOK_NOT_FOUND;
import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@Service
public class CommentServiceImpl implements CommentService {

    public static final String COMMENT_NOT_FOUND = "Comment is not found: %s";

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.commentMapper = commentMapper;
    }


    @Transactional
    @Override
    public ServiceResponse<CommentDto> addComment(long bookId, CommentDto commentDto) {
        try {
            val book = findBook(bookId);
            var comment = commentMapper.toEntity(commentDto, book);
            book.addComment(comment);
            comment = commentRepository.save(comment);
            return done(commentMapper.toDto(comment));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<CommentDto> modifyComment(CommentDto commentDto) {
        try {
            var comment = findComment(commentDto.getId());
            commentMapper.partialUpdate(commentDto, comment);
            comment = commentRepository.save(comment);
            return done(commentMapper.toDto(comment));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Transactional
    @Override
    public ServiceResponse<CommentDto> deleteComment(long commentId) {
        try {
            var comment = findComment(commentId);
            comment.getBook().removeComment(comment);
            commentRepository.delete(comment);
            return done(commentMapper.toDto(comment));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    private Comment findComment(Long commentId) throws EntityNotFoundException {
        return ServiceUtils.findById(commentId, commentRepository::findById, COMMENT_NOT_FOUND);
    }

    private Book findBook(Long bookId) throws EntityNotFoundException {
        return ServiceUtils.findById(bookId, bookRepository::findById, BOOK_NOT_FOUND);
    }

}
