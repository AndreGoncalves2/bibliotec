package br.com.bibliotec.controller;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookController extends GenericController<Book, Long, BookRepository> {
    
    public BookController(@Autowired BookRepository bookRepository) {
        this.repository = bookRepository;
    }

    @Override
    protected void validate(Book entity, Mode mode) throws BibliotecException {
        if (mode.equals(Mode.SAVE)) {
            Book book = repository.findBookByCode(entity.getCode()).orElse(null);
            if (book != null) {
                throw  new BibliotecException("Código do livro já cadastrado");
            }
        }
    }
}
