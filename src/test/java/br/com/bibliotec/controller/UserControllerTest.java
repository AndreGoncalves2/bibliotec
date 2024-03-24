package br.com.bibliotec.controller;

import br.com.bibliotec.builder.UserBuilder;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    
    private User userExample;
    
    @Autowired
    private UserController controller;

    @BeforeAll
    void buildBean() {
        userExample = UserBuilder.build().now();
    }

    @Test
    @Order(1)
    void testSave() throws BibliotecException {
        assertNotNull(controller.save(userExample));
    }

    @Test
    @Order(2)
    void testList() {
        assertNotNull(controller.list());
    }

    @Test
    @Order(3)
    void testLoad() throws BibliotecException {
        User testLoad = controller.save(userExample);
        assertNotNull(controller.load(testLoad.getId()));
    }

    @Test
    @Order(4)
    void testUpdate() throws BibliotecException {
        User testUpdate = controller.save(userExample);
        String oldDescription = userExample.getName();
        testUpdate.setName("Update");
        testUpdate = controller.save(testUpdate);

        assertNotEquals(oldDescription, testUpdate.getUsername());
    }

    @Test
    @Order(5)
    void testDelete() throws BibliotecException {
        User testDelete = controller.save(userExample);
        controller.delete(testDelete);

        assertThrows(BibliotecException.class, () -> controller.load(testDelete.getId()));
    }
}
