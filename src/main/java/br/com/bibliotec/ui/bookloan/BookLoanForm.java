package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.GenericForm;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "/emprestimo", layout = MainView.class)
public class BookLoanForm  extends GenericForm<BookLoan, BookLoanController, Long> {

    @Bind("book")
    private final ComboBox<Book> bookComboBox;

    @Bind("bookingDate")
    private final DatePicker bookingDate;

    @Bind("dueDate")
    private final DatePicker dueDate;

    @Bind("student")
    private final ComboBox<Student> studentComboBox;
    
    public BookLoanForm(@Autowired BookLoanController controller,
                        @Autowired BookController bookController,
                        @Autowired StudentController studentController) {
        
        super(controller, BookLoan.class);
        
        setTitleParameter("EMPRÉSTIMO");

        bookComboBox = new ComboBox<>("Livro");
        bookComboBox.setItemLabelGenerator(Book::getTitle);
        bookComboBox.setItems(bookController.list());

        bookingDate = new DatePicker("Data do empréstimo");
        dueDate = new DatePicker("Data do vencimento");

        bookingDate.addValueChangeListener(event -> dueDate.setMin(event.getValue()));

        studentComboBox = new ComboBox<>("Aluno");
        studentComboBox.setItemLabelGenerator(student -> student.getName() + " " + student.getStudentClass());
        studentComboBox.setItems(studentController.list());
        
        setDefaultRoute("/emprestimo");
        
        getFormLayout().add(studentComboBox, bookComboBox, bookingDate, dueDate);
    }
}
