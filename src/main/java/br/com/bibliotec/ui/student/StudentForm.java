package br.com.bibliotec.ui.student;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.GenericForm;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@PageTitle("Aluno")
@Route(value = "aluno", layout = MainView.class)
public class StudentForm extends GenericForm<Student, StudentController, Long> {

    @Bind("ra")
    public TextField txtRa;

    @Bind("name")
    public TextField txtName;

    @Bind("studentClass")
    public TextField txtStudentClass;
    
    private final UserController userController;

    public StudentForm(@Autowired StudentController controller,
                       @Autowired UserController userController) {
        super(controller, Student.class);
        this.userController = userController;

        setTitleParameter("ALUNO");
        
        txtRa = new TextField("RA");
        txtRa.setAllowedCharPattern("[0-9]");
        txtRa.setMaxLength(10);

        txtName = new TextField("Nome");
        txtStudentClass = new TextField("Turma");
        
        setDefaultRoute("aluno");
        
        getFormLayout().add(txtRa, txtName, txtStudentClass);
    }

    @Override
    protected void beforeSave(Student currentEntity) {
        UserService userService = new UserService(userController);
        User userLogged = userService.getLoggedUser();

        currentEntity.setUser(userLogged);
    }
}
