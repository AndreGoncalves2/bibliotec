package br.com.bibliotec.controller;

import br.com.bibliotec.builder.BookLoanBuilder;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.BookLoan;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookLoanControllerTest {

    private BookLoan bookLoanExample;

    @Autowired
    private BookLoanController controller;
    
    @Autowired
    private BookController bookController;
    
    @Autowired
    private StudentController studentController;

    @BeforeAll
    void buildBean() throws BibliotecException {
        bookLoanExample = BookLoanBuilder.build().addBook(bookController).addStudent(studentController).now();
    }

    @Test
    @Order(1)
    void testSave() throws BibliotecException {
        assertNotNull(controller.save(bookLoanExample));
    }

    @Test
    @Order(2)
    void testList() {
        assertNotNull(controller.list());
    }

    @Test
    @Order(3)
    void testLoad() throws BibliotecException {
        BookLoan testLoad = controller.save(bookLoanExample);
        assertNotNull(controller.load(testLoad.getId()));
    }

    @Test
    @Order(4)
    void testUpdate() throws BibliotecException {
        BookLoan testUpdate = controller.save(bookLoanExample);
        LocalDate oldDate = bookLoanExample.getBookingDate();
        testUpdate.setBookingDate(LocalDate.now().plusMonths(2));
        testUpdate = controller.update(testUpdate);

        assertNotEquals(oldDate, testUpdate.getBookingDate());
    }

    @Test
    @Order(5)
    void testDelete() throws BibliotecException {
        BookLoan testDelete = controller.save(bookLoanExample);
        controller.delete(testDelete);
        
        assertThrows(BibliotecException.class, () -> controller.load(testDelete.getId()));
    }
    
    @Test
    @Order(6)
    void testSetReturned() throws BibliotecException {
        String bookCode = "1234567";
        
        bookLoanExample.getBook().setCode(bookCode);
        bookLoanExample.setReturned(false);
        
        controller.save(bookLoanExample);
        controller.setReturned(bookLoanExample, bookCode);
        BookLoan bookLoanBookReturned = controller.load(bookLoanExample.getId());
        
        assertEquals(bookLoanBookReturned.getReturned(), true);
    }
}
