package ru.otus.homework.books.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.homework.books.dto.BookDto;
import ru.otus.homework.books.services.AuthorService;
import ru.otus.homework.books.services.BookService;
import ru.otus.homework.books.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listPage(Model model) {
        var response = bookService.listBooks(null, null, null);
        model.addAttribute("books", response.getData());
        return "bookList";
    }

    @GetMapping("/book/{id}/edit")
    public String editPage(@PathVariable("id") long id, Model model) {
        val book = bookService.getBookProjection(id).orElseThrow(BookAppException::new);
        val authors = authorService.listAuthors().orElseThrow(BookAppException::new);
        val genres = genreService.listGenres().orElseThrow(BookAppException::new);
        model.addAttribute("book", book);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
        return "bookEdit";
    }

    @PostMapping("/book/{id}/edit")
    public String saveBook(@Valid @ModelAttribute("book") BookDto bookDto,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            fillDropDowns(model);
            return "bookEdit";
        }
        bookService.modifyBook(bookDto).orElseThrow(BookAppException::new);
        return "redirect:/";
    }

    @GetMapping("/book/add")
    public String addBookPage(Model model) {
        model.addAttribute("book", new BookDto());
        fillDropDowns(model);
        return "bookAdd";
    }

    @PostMapping("/book/add")
    public String addBook(@Valid @ModelAttribute("book") BookDto bookDto,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            fillDropDowns(model);
            return "bookAdd";
        }
        bookService.createBook(bookDto.getTitle(), bookDto.getAuthor().getId(), bookDto.getGenre().getId())
                .orElseThrow(BookAppException::new);
        return "redirect:/";
    }

    @GetMapping("/book/{id}/delete")
    public String deletePage(@PathVariable("id") long id, Model model) {
        val book = bookService.getBook(id).orElseThrow(BookAppException::new);
        model.addAttribute("book", book);
        return "bookDelete";
    }

    @DeleteMapping(value = "/book/{id}/delete")
    public String deleteBook(@PathVariable("id") long id) {
        bookService.deleteBook(id).orElseThrow(BookAppException::new);
        return "redirect:/";
    }


    private void fillDropDowns(Model model) {
        val authors = authorService.listAuthors().orElseThrow(BookAppException::new);
        val genres = genreService.listGenres().orElseThrow(BookAppException::new);
        model.addAttribute("authors", authors);
        model.addAttribute("genres", genres);
    }
}
