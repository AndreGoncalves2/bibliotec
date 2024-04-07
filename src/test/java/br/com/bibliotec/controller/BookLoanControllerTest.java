package br.com.bibliotec.controller;

import br.com.bibliotec.builder.BookLoanBuilder;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.BookLoan;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @BeforeAll
    void buildBean() throws BibliotecException {
        bookLoanExample = BookLoanBuilder.build().addBook(bookController).now();
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
        Boolean oldDescription = bookLoanExample.getReturned();
        testUpdate.setReturned(true);
        testUpdate = controller.save(testUpdate);

        assertNotEquals(oldDescription, testUpdate.getReturned());
    }

    @Test
    @Order(5)
    void testDelete() throws BibliotecException {
        BookLoan testDelete = controller.save(bookLoanExample);
        controller.delete(testDelete);
        
        assertThrows(BibliotecException.class, () -> controller.load(testDelete.getId()));
    }
}
