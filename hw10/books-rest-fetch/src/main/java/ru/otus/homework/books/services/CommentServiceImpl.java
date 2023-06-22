package ru.otus.homework.books.services;

import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.books.domain.Comment;
import ru.otus.homework.books.mappers.CommentMapper;
import ru.otus.homework.books.repository.CommentRepository;
import ru.otus.homework.books.rest.dto.CommentDto;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.ServiceErrorMessages;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.otus.homework.books.services.ServiceResponse.done;
import static ru.otus.homework.books.services.ServiceResponse.error;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookService bookService;

    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository, CommentMapper commentMapper,
                              @Lazy BookService bookService) {
        this.commentRepository = commentRepository;
        this.bookService = bookService;
        this.commentMapper = commentMapper;
    }


    @Transactional
    @Override
    public ServiceResponse<CommentDto> addComment(long bookId, CommentDto commentDto) {
        try {
            val book = bookService.findBook(bookId);
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

    @Override
    public void deleteComments(Collection<Long> bookIds) {
        val commentsToDelete = commentRepository.findAllByBookIdIn(bookIds);
        commentRepository.deleteAllInBatch(commentsToDelete);
    }

    @Override
    public long countByBookId(Long bookId) {
        return commentRepository.countByBookId(bookId);
    }

    @Override
    public Map<Long, Long> countGroupByBookId(Collection<Long> bookIds) {
        return commentRepository.countGroupByBookId(bookIds)
                .collect(Collectors.toMap(t -> (Long) t[0], t -> (Long) t[1]));
    }

    private Comment findComment(Long commentId) throws EntityNotFoundException {
        return ServiceUtils.findById(commentId, commentRepository::findById, ServiceErrorMessages.COMMENT_NOT_FOUND);
    }

}
