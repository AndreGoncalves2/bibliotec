package br.com.bibliotec.controller;

import br.com.bibliotec.builder.ReviewBuilder;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Review;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReviewControllerTest {
    
    private Review reviewExample;
    
    @Autowired
    private ReviewController controller;
    
    @Autowired
    private BookController bookController;
    
    @Autowired
    private UserController userController;

    @BeforeAll
    void buildBean() throws BibliotecException {
        reviewExample = ReviewBuilder.build().addBook(bookController).addUser(userController).now();
    }

    @Test
    @Order(1)
    void testSave() throws BibliotecException {
        assertNotNull(controller.save(reviewExample));
    }

    @Test
    @Order(2)
    void testList() {
        assertNotNull(controller.list());
    }

    @Test
    @Order(3)
    void testLoad() throws BibliotecException {
        Review testLoad = controller.save(reviewExample);
        assertNotNull(controller.load(testLoad.getId()));
    }

    @Test
    @Order(4)
    void testUpdate() throws BibliotecException {
        Review testUpdate = controller.save(reviewExample);
        String oldDescription = reviewExample.getTitle();
        testUpdate.setTitle("title Update");
        testUpdate = controller.save(testUpdate);

        assertNotEquals(oldDescription, testUpdate.getTitle());
    }

    @Test
    @Order(5)
    void testDelete() throws BibliotecException {
        Review testDelete = controller.save(reviewExample);
        controller.delete(testDelete);

        assertThrows(BibliotecException.class, () -> controller.load(testDelete.getId()));
    }
    
}
