package br.com.bibliotec.ui.student;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.GenericForm;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "aluno", layout = MainView.class)
public class StudentForm extends GenericForm<Student, StudentController, Long> {

    @Bind("ra")
    public TextField txtRa;

    @Bind("name")
    public TextField txtName;

    @Bind("studentClass")
    public TextField txtStudentClass;

    public StudentForm(StudentController controller) {
        super(controller, Student.class);

        setTitleParameter("ALUNO");
        
        txtRa = new TextField("RA");
        txtRa.setAllowedCharPattern("[0-9]");
        txtRa.setMaxLength(10);

        txtName = new TextField("Nome");
        txtStudentClass = new TextField("Turma");
        
        setDefaultRoute("aluno");
        
        getFormLayout().add(txtRa, txtName, txtStudentClass);
    }
}
