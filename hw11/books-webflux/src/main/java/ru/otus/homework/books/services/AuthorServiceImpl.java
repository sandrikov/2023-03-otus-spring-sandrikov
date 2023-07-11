package ru.otus.homework.books.services;

import lombok.val;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.rest.misc.BookAppException;
import ru.otus.homework.books.services.misc.ServiceErrorMessages;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.services.misc.EntityNotFoundException;
import ru.otus.homework.books.services.misc.Reply;
import ru.otus.homework.books.services.misc.ServiceUtils;

import java.util.Objects;
import java.util.Optional;

import static ru.otus.homework.books.rest.misc.RestErrorMessages.getInvalidIdMessage;
import static ru.otus.homework.books.services.misc.Reply.done;
import static ru.otus.homework.books.services.misc.Reply.error;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getAuthorAlreadyExistsMessage;
import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getAuthorNotFoundMessage;

@Service
public class AuthorServiceImpl implements AuthorService {

    public static final String INTEGRITY_VIOLATION_ERROR = """
            Deletion is not possible: there are author's books.
            First call: delete books --author-id %s
            """;

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    private final BookService bookService;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper,
                             @Lazy BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.authorMapper = authorMapper;
    }

    @Override
    public Flux<AuthorDto> listAuthors() {
        return authorRepository.findAll().map(authorMapper::toDto);
    }

    @Override
    public Mono<AuthorDto> getAuthor(Long id) {
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    @Override
    public Mono<AuthorDto> findAuthor(String name) {
        return authorRepository.findByName(name).map(authorMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<AuthorDto> createAuthor(Mono<AuthorDto> authorDto) {
        Mono<Author> authorToCreate = authorDto
                .handle((dto, sink) -> {
                    if (authorRepository.existsByName(dto.getName()).block()) {
                        sink.error(new BookAppException(getAuthorAlreadyExistsMessage(dto.getName())));
                    } else {
                        sink.next(authorMapper.toEntity(dto));
                    }
                });
        return authorRepository.save(authorToCreate).map(authorMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<AuthorDto> updateAuthor(Long id, Mono<AuthorDto> authorDto) {
        Mono<Author> authorToUpdate = authorDto
                .handle((dto, sink) -> {
                    if (!Objects.equals(id, dto.getId())) {
                        sink.error(new BookAppException(getInvalidIdMessage(id, dto.getId())));
                    } else if (authorRepository.findByName(dto.getName())
                                .filter(author -> !Objects.equals(id, author.getId()))
                                .blockOptional().isPresent()) {
                        sink.error(new BookAppException(getAuthorAlreadyExistsMessage(dto.getName())));
                    } else if (authorRepository.findById(id).blockOptional().isEmpty()) {
                        sink.next(Mono.<Author>empty());
                    } else {
                        sink.next(authorMapper.toEntity(dto));
                    }
                });
        authorToUpdate.
        mapNotNull

        Author author;
        try {
            author = findAuthor(id);
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
        if (!Objects.equals(name, author.getName()) &&
                authorRepository.findByName(name).isPresent()) {
            return error(getAuthorAlreadyExistsMessage(name));
        }
        author.setName(name);
        authorRepository.save(author);
        return done(authorMapper.toDto(author));
    }


    @Transactional
    @Override
    public Reply<AuthorDto> deleteAuthor(Long id) {
        if (bookService.existsByAuthorId(id)) {
            return error(String.format(INTEGRITY_VIOLATION_ERROR, id));
        }
        try {
            val author = findAuthor(id);
            authorRepository.delete(author);
            return done(authorMapper.toDto(author));
        } catch (EntityNotFoundException e) {
            return error(e.getMessage());
        }
    }

    @Override
    public Mono<Author> findAuthor(Long authorId) {
        return ServiceUtils.findById(authorId, authorRepository::findById, ServiceErrorMessages.AUTHOR_NOT_FOUND);
    }

}
