package br.com.bibliotec.controller;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.repository.BookLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookLoanController extends GenericController<BookLoan, Long, BookLoanRepository> {
    
    public BookLoanController(@Autowired BookLoanRepository repository) {
        this.repository = repository;
    }
    
    @Override
    protected void validate(BookLoan entity, Mode mode) {
        if (mode.equals(Mode.SAVE)) {
            entity.setReturned(false);
        }
    }
}
