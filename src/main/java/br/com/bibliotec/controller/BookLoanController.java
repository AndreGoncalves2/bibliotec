package br.com.bibliotec.controller;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exception.CodeIncorrectException;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.repository.BookLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookLoanController extends GenericController<BookLoan, Long, BookLoanRepository> {
    
    public BookLoanController(@Autowired BookLoanRepository repository) {
        this.repository = repository;
    }
    
    public void setReturned(BookLoan entity, String bookCode) throws CodeIncorrectException {
        if(entity.getBook().getCode().equals(bookCode)) {
            repository.updateReturned(entity.getId(), true);
        } else {
            throw new CodeIncorrectException("O código informado não corresponde ao livro do empréstimo selecionado.");
        }
    }
    
    public boolean verifyBookIsBorrowed(Book book) {
        return repository.existsByBookAndReturned(book, true);
    }
    
    @Override
    protected void validate(BookLoan entity, Mode mode) {
        if (mode.equals(Mode.SAVE)) {
            entity.setReturned(false);
        }
    }
}
