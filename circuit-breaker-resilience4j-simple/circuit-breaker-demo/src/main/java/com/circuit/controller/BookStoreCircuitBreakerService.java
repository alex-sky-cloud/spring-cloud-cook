package com.circuit.controller;

import com.circuit.dto.Book;
import com.circuit.manager.BookCircuitBreakerManager;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.function.Supplier;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookStoreCircuitBreakerService {

    public final BookCircuitBreakerManager bookCircuitBreakerManager;

    private final CircuitBreaker timeCircuitBreaker;

    @GetMapping(value = "/home")
    public String home(HttpServletRequest request, Model model) {
        return "home";
    }

    @GetMapping(value = "/books")
    public String books(HttpServletRequest request, Model model) {

        /*создаем декоратор, который запустит вызовы адресованные удаленному серверу Books,
        * через timeCircuitBreaker (прокси-сервер, который и есть CircuitBreaker)*/
        Supplier<List<Book>> booksSupplier =
                timeCircuitBreaker.decorateSupplier(bookCircuitBreakerManager::getAllBooksFromLibrary);

        log.info(" Начинаем вызов удаленного REST service Books через `Circuit Breaker`");

        List<Book> books = generateCallsToRemoteBookService(booksSupplier);
        model.addAttribute("books", books);

        return "books";
    }

    /** данный метод будет генерировать запросы на удаленный сервис Book,
     * вызовы будут оборачиваться через сircuit breaker*/
    private List<Book> generateCallsToRemoteBookService(Supplier<List<Book>> booksSupplier){

        List<Book> books = null;

        for (int i = 0; i < 15; i++) {

            try {
                log.info("Получаем books от удаленного сервис Book, " +
                        "через прокси, то есть через сircuit breaker");
                books = booksSupplier.get();
            } catch (Exception e) {
                log.error("Ошибка, возникла при попытке получения books" +
                        ", от удаленного сервиса Books, через `сircuit breaker`", e);
            }
        }
        return books;
    }
}
