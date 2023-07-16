package ru.otus.homework.books.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@Log4j2
public class BookController {

    @GetMapping("/")
    public Mono<Rendering> listPage(Model model) {
        log.debug("PAGE 'List of books'");
        return Mono.just(Rendering.view("bookList").build());
    }

    @GetMapping("/book/{id}/edit")
    public Mono<Rendering> editPage(@PathVariable("id") long id, Model model) {
        log.debug("PAGE 'Edit book' : {}", id);
        return Mono.just(Rendering.view("bookEdit").build());
    }

    @GetMapping("/book/{id}/view")
    public Mono<Rendering> viewPage(@PathVariable("id") long id, Model model) {
        log.debug("PAGE 'View book' : {}", id);
        return Mono.just(Rendering.view("bookView").build());
    }

    @GetMapping("/book/add")
    public Mono<Rendering> addBookPage(Model model) {
        log.debug("PAGE 'Add book'");
        return Mono.just(Rendering.view("bookEdit").build());
    }

    @GetMapping("/book/{id}/delete")
    public Mono<Rendering> deletePage(@PathVariable("id") long id, Model model) {
        log.debug("PAGE 'Delete book'");
        return Mono.just(Rendering.view("bookView").build());
    }

}
