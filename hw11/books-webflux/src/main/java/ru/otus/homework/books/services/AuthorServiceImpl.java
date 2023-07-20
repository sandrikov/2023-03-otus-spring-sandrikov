package ru.otus.homework.books.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.homework.books.domain.Author;
import ru.otus.homework.books.mappers.AuthorMapper;
import ru.otus.homework.books.repository.AuthorRepository;
import ru.otus.homework.books.rest.dto.AuthorDto;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

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

    @Override
    public Mono<AuthorDto> createAuthor(Mono<AuthorDto> authorDto) {
        return authorDto.map(authorMapper::toEntity)
                .flatMap(author -> authorRepository.save(author).map(authorMapper::toDto));
    }

    @Override
    public Mono<AuthorDto> updateAuthor(Long id, Mono<AuthorDto> authorDto) {
        return authorRepository.findById(id)
                .zipWith(authorDto)
                .map(t -> authorMapper.partialUpdate(t.getT2(), t.getT1()))
                .flatMap(author -> authorRepository.save(author).map(authorMapper::toDto));
    }

    @Override
    public Mono<AuthorDto> deleteAuthor(Long id) {
        return authorRepository.findById(id)
                .flatMap(s -> authorRepository.delete(s)
                        .then(Mono.just(authorMapper.toDto(s))));
    }

    @Override
    public Mono<Author> findAuthor(Long id) {
        return authorRepository.findById(id);
    }

}
