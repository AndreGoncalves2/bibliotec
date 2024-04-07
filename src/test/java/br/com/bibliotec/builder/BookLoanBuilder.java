package br.com.bibliotec.builder;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.BookLoan;

import java.time.LocalDate;

public class BookLoanBuilder {
    
    private BookLoan bookLoan;
    
    public static BookLoanBuilder build() {
        BookLoanBuilder builder = new BookLoanBuilder();
        builder.bookLoan = new BookLoan();
        
        builder.bookLoan.setBookingDate(LocalDate.now());
        builder.bookLoan.setDueDate(LocalDate.now());
        builder.bookLoan.setReturned(false);
//        builder.bookLoan.setStudentClass("Teste classe");
//        builder.bookLoan.setStudent("Teste Aluno");
        
        return builder;
    }
    
    public BookLoanBuilder addBook(BookController bookController) throws BibliotecException {
        Book book = bookController.save(BookBuilder.build().now());
        this.bookLoan.setBook(book);
        return this;
    }
    
    public BookLoan now() {
        return bookLoan;
    }
}
