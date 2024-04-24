package br.com.bibliotec.ui.student;

import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.GenericGrid;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "/aluno", layout = MainView.class)
public class StudentGrid extends GenericGrid<Student, StudentController> {

    public StudentGrid(@Autowired StudentController studentController) throws IllegalAccessException {
        super("ALUNOS", studentController);
        creatGrid();

        StudentForm studentFormDialog = new StudentForm(studentController, this);
        setForm(studentFormDialog);
    }

    private void creatGrid() {

        Grid<Student> grid = getGrid();

        grid.addColumn(Student::getRa).setHeader("RA").setFlexGrow(1).setSortable(true);
        grid.addColumn(Student::getName).setHeader("Nome").setFlexGrow(4).setSortable(true);
        grid.addColumn(Student::getStudentClass).setHeader("Classe").setFlexGrow(4).setSortable(true);

        refresh();
    }
}
