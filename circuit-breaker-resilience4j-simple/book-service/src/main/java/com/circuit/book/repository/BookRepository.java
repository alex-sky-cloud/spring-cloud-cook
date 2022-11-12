package com.circuit.book.repository;

import com.circuit.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {

    Set<Book> findByTitleContaining (String bookTitle);
}