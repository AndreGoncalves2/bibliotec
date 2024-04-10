package br.com.bibliotec.controller;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookController extends GenericController<Book, Long, BookRepository> {
    
    public BookController(@Autowired BookRepository bookRepository) {
        this.repository = bookRepository;
    }
}
