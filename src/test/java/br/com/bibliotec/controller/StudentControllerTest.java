package br.com.bibliotec.controller;

import br.com.bibliotec.builder.StudentBuilder;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentControllerTest {
    private Student studentExample;

    @Autowired
    private StudentController controller;

    @BeforeAll
    void buildBean() {
        studentExample = StudentBuilder.build().now();
    }

    @Test
    @Order(1)
    void testSave() throws BibliotecException {
        assertNotNull(controller.save(studentExample));
    }

    @Test
    @Order(2)
    void testList() {
        assertNotNull(controller.list());
    }

    @Test
    @Order(3)
    void testLoad() throws BibliotecException {
        Student testLoad = controller.save(studentExample);
        assertNotNull(controller.load(testLoad.getRa()));
    }

    @Test
    @Order(4)
    void testUpdate() throws BibliotecException {
        Student testUpdate = controller.save(studentExample);
        String oldDescription = studentExample.getName();
        testUpdate.setName("name Update");
        testUpdate = controller.save(testUpdate);

        assertNotEquals(oldDescription, testUpdate.getName());
    }

    @Test
    @Order(5)
    void testDelete() throws BibliotecException {
        Student testDelete = controller.save(studentExample);
        controller.delete(testDelete);

        assertThrows(BibliotecException.class, () -> controller.load(testDelete.getRa()));
    }
}
