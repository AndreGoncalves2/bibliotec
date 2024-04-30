package br.com.bibliotec.ui.student;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.ui.componets.GenericFormDialog;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

public class StudentFormDialog extends GenericFormDialog<Student, StudentController, Long> {
    
    @Bind("ra")
    public TextField txtRa;
    
    @Bind("name")
    public TextField txtName;

    @Bind("studentClass")
    public TextField txtStudentClass;
    
    public StudentFormDialog(@Autowired StudentController controller) throws BibliotecException {
        super(controller, Student.class);

        txtRa = new TextField("RA");
        txtRa.setAllowedCharPattern("[0-9]");
        txtRa.setMinLength(10);
        txtRa.setMaxLength(10);
        
        txtName = new TextField("Nome");
        txtStudentClass = new TextField("Turma");
        
        createBinder();

        getFormLayout().add(txtRa, txtName, txtStudentClass);
    }
}
