package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.componets.DatePickerPT;
import br.com.bibliotec.ui.componets.GenericFormDialog;
import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class BookLoanFormDialog extends GenericFormDialog<BookLoan, BookLoanController, Long> {
    
    @Bind("book")
    private final ComboBox<Book> bookComboBox;
    
    @Bind("bookingDate")
    private final DatePickerPT bookingDate;

    @Bind("dueDate")
    private final DatePickerPT dueDate;

    @Bind("student")
    private final ComboBox<Student> studentComboBox;
    
    private final UserController userController;

    public BookLoanFormDialog(BookLoanController controller,
                              BookController bookController,
                              StudentController studentController,
                              UserController userController) throws BibliotecException {
        super(controller, BookLoan.class);
        this.userController = userController;
        

        bookComboBox = new ComboBox<>("Livro");
        bookComboBox.setItemLabelGenerator(Book::getTitle);
        bookComboBox.setItems(bookController.list());
        
        bookingDate = new DatePickerPT("Data do empréstimo");
        dueDate = new DatePickerPT("Data do vencimento");
        
        bookingDate.setValue(LocalDate.now());
        bookingDate.addValueChangeListener(event -> dueDate.setMin(event.getValue()));

        studentComboBox = new ComboBox<>("Aluno");
        studentComboBox.setItemLabelGenerator(student -> student.getName() + " " + student.getStudentClass());
        studentComboBox.setItems(studentController.list());

        createBinder();

        getFormLayout().add(studentComboBox, bookComboBox, bookingDate, dueDate);
    }

    @Override
    protected void beforeSave(BookLoan currentEntity) {
        UserService userService = new UserService(userController);
        User userLogged = userService.getLoggedUser();

        currentEntity.setUser(userLogged);
    }
}

