package br.com.bibliotec.controller;

import br.com.bibliotec.builder.BookBuilder;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookControllerTest {
    
    private Book bookExample;
    
    @Autowired
    private BookController controller;
    
    @BeforeAll
    void buildBean() {
        bookExample = BookBuilder.build().now();
    }

    @Test
    @Order(1)
    void testSave() throws BibliotecException {
        assertNotNull(controller.save(bookExample));
    }

    @Test
    @Order(2)
    void testList() {
        assertNotNull(controller.list());
    }

    @Test
    @Order(3)
    void testLoad() throws BibliotecException {
        Book testLoad = controller.save(bookExample);
        assertNotNull(controller.load(testLoad.getId()));
    }

    @Test
    @Order(4)
    void testUpdate() throws BibliotecException {
        Book testUpdate = controller.save(bookExample);
        String oldDescription = bookExample.getTitle();
        testUpdate.setTitle("title Update");
        testUpdate = controller.update(testUpdate);

        assertNotEquals(oldDescription, testUpdate.getTitle());
    }

    @Test
    @Order(5)
    void testDelete() throws BibliotecException {
        Book testDelete = controller.save(bookExample);
        controller.delete(testDelete);

        assertThrows(BibliotecException.class, () -> controller.load(testDelete.getId()));
    }
}
