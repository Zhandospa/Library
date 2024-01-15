package com.example.technoparkpa;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Controller
public class BooksController {

    private final BookService books;
    private final PersonService people;

    public BooksController(@Autowired BookService books,@Autowired PersonService people) {
        this.books = books;
        this.people = people;
    }

    @GetMapping("/books")
    public String showBook(Model model) {
        model.addAttribute("books", books.getBooks());
        return "books";
    }

    @GetMapping("/books/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "createBook";
    }

    @PostMapping("/books/new")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult result) {

        if (result.hasErrors()) {
            return "createBook";
        }
        books.addBook(book);

        return "redirect:/books";
    }


    @GetMapping("/books/{id}")
    public String showBook(Model model, @PathVariable int id) {
        var b = books.getBook(id);
        model.addAttribute("book", b);
        model.addAttribute("people", people.getPeople());
        return "book";
    }

    @PostMapping("/books/{id}")
    public String selBook(@PathVariable int id, @RequestParam("selectedPerson") long personId) {
        var b = books.getBook(id);
        var p = people.getPerson(personId);
        p.addBook(b);
        b.setPerson(p);
        people.upDatePerson( p);
        books.upDateBook(b, personId);
        return "redirect:/books/" + id;
    }

    @PostMapping("/books/{id}/osvo")
    public String ocvo(@PathVariable long id) {
        var b = books.getBook(id);

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Tec", "postgres", "Zhandosika")) {
            b.delPerson();
            books.upDateBook(b, -456);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/books/" + id;
    }



    @GetMapping("/books/{id}/edit")
    public String showEditBook(Model model, @PathVariable Long id) {
        Book book = books.getBook(id);
        model.addAttribute("book", book);
        return "editBook";
    }

    @PostMapping("/books/{id}/edit")
    public String editBook(@PathVariable Long id, @Valid @ModelAttribute("book") Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editBook";
        }

        book.setId(id);
        books.upDateBook(book);
        return "redirect:/books";
    }


    @PostMapping("/books/{id}/del")
    public String delBook(@PathVariable int id) {
        books.deleteBook(id);
        return "redirect:/books";
    }
}
