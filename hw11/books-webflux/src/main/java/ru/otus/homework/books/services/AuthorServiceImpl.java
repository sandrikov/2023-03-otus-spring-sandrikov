package ru.otus.homework.books.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.rest.dto.AuthorDto;
import ru.otus.homework.books.rest.misc.BookAppException;

import static ru.otus.homework.books.services.misc.ServiceErrorMessages.getAuthorAlreadyExistsMessage;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    public static final String INTEGRITY_VIOLATION_ERROR = """
            Deletion is not possible: there are author's books.
            First call: delete books --author-id %s
            """;

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public Flux<AuthorDto> listAuthors() {
        return authorRepository.findAll().map(authorMapper::toDto);
    }

    @Override
    public Mono<AuthorDto> getAuthor(Long id) {
        return authorRepository.findById(id).map(authorMapper::toDto);
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
        return authorRepository.findById(id)
                .flatMap(s -> authorRepository
                        .save(authorDto.map(dto -> authorMapper.partialUpdate(dto, s)))
                        .map(authorMapper::toDto));
    }

    @Transactional
    @Override
    public Mono<AuthorDto> deleteAuthor(Long id) {
        return authorRepository.findById(id)
                .flatMap(s -> authorRepository.delete(s)
                        .then(Mono.just(authorMapper.toDto(s))));
    }

}
