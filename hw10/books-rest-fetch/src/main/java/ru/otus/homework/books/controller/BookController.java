package ru.otus.homework.books.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Log4j2
public class BookController {

    @GetMapping("/")
    public String listPage(Model model) {
        model.addAttribute("keywords", "list books, book catalog");
        log.debug("PAGE 'List of books'");
        return "bookList";

    }

    @GetMapping("/book/{id}/edit")
    public String editPage(@PathVariable("id") long id, Model model) {
        log.debug("PAGE 'Edit book' : {}", id);
        return "bookEdit";
    }

    @GetMapping("/book/{id}/view")
    public String viewPage(@PathVariable("id") long id, Model model) {
        log.debug("PAGE 'View book' : {}", id);
        return "bookView";
    }

    @GetMapping("/book/add")
    public String addBookPage(Model model) {
        log.debug("PAGE 'Add book'");
        return "bookEdit";
    }

    @GetMapping("/book/{id}/delete")
    public String deletePage(@PathVariable("id") long id, Model model) {
        log.debug("PAGE 'Delete book'");
        return "bookView";
    }

}
