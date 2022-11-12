package com.circuit.book.controller;

import com.circuit.book.model.Book;
import com.circuit.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*@CrossOrigin("https://localhost:8089") это нужно, если к данному сервису
* будут подключаться с другого домена, с указанного адреса*/
@RestController
@RequestMapping("/v1/library")
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(required = false) String bookTitle) {

        try {
            List<Book> listOfBooks = new ArrayList<>();
            if (bookTitle == null || bookTitle.isEmpty()) {
                listOfBooks.addAll(bookRepository.findAll());
            } else {
                listOfBooks.addAll(bookRepository.findByTitleContaining(bookTitle));
            }

            if (listOfBooks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(listOfBooks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable("id") long id) {

       return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addABookToLibrary(@RequestBody Book book) {
        try {

            Book bookNew = new Book(
                    book.getTitle(),
                    book.getAuthor(),
                    book.getIsbn()
            );

            Book createdBook = bookRepository
                    .save(bookNew);

            return new ResponseEntity<>(createdBook, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateABook(@PathVariable("id") long id, @RequestBody Book book) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        if (bookOptional.isPresent()) {

            Book updatedBook = bookOptional.get();
            updatedBook.setTitle(book.getTitle());
            updatedBook.setAuthor(book.getAuthor());
            updatedBook.setIsbn(book.getIsbn());
            return new ResponseEntity<>(bookRepository.save(updatedBook), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteABook(@PathVariable("id") long id) {
        try {
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
