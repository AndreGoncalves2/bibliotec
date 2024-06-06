package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.authentication.SecurityService;
import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.DatePickerPT;
import br.com.bibliotec.ui.componets.ErrorDialog;
import br.com.bibliotec.ui.componets.GenericForm;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@PageTitle("Empréstimo")
@Route(value = "/emprestimo", layout = MainView.class)
public class BookLoanForm  extends GenericForm<BookLoan, BookLoanController, Long> {

    @Bind("book")
    private final ComboBox<Book> bookComboBox;

    @Bind("bookingDate")
    private final DatePickerPT bookingDate;

    @Bind("dueDate")
    private final DatePickerPT dueDate;

    @Bind("student")
    private final ComboBox<Student> studentComboBox;
    
    private final BookLoanController bookLoanController;
    private final UserController userController;

    public BookLoanForm(@Autowired BookLoanController controller,
                        @Autowired BookController bookController,
                        @Autowired StudentController studentController,
                        @Autowired BookLoanController bookLoanController,
                        @Autowired UserController userController) {
        
        super(controller, BookLoan.class);
        this.bookLoanController = bookLoanController;
        this.userController = userController;
        
        setTitleParameter("EMPRÉSTIMO");

        bookComboBox = new ComboBox<>("Livro");
        bookComboBox.setItemLabelGenerator(book -> book.getCode() + " - " + book.getTitle());
        bookComboBox.setRenderer(createRenderer());
        bookComboBox.setItems(bookController.list());
        bookComboBox.addValueChangeListener(event -> {
            if (event.isFromClient()) checkBookIsBorrowed(event.getValue());
        });

        bookingDate = new DatePickerPT("Data do empréstimo");
        dueDate = new DatePickerPT("Data do vencimento");

        bookingDate.addValueChangeListener(event -> dueDate.setMin(event.getValue()));

        studentComboBox = new ComboBox<>("Aluno");
        studentComboBox.setItemLabelGenerator(student -> student.getName() + " " + student.getStudentClass());
        studentComboBox.setItems(studentController.list());
        
        setDefaultRoute("/emprestimo");
        
        getFormLayout().add(studentComboBox, bookComboBox, bookingDate, dueDate);
    }

    private Renderer<Book> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex; align-items: center;\">");
        tpl.append("  ${item.getCode} - ${item.getTitle}");
        tpl.append("  <span style=\"flex-grow: 1;\"></span>");
        tpl.append("  ${item.isBlocked ? '⛔' : ''}");
        tpl.append("</div>");

        return LitRenderer.<Book>of(tpl.toString())
                .withProperty("isBlocked", bookLoanController::verifyBookIsBorrowed)
                .withProperty("getCode", Book::getCode)
                .withProperty("getTitle", Book::getTitle);
    }
    
    private void checkBookIsBorrowed(Book book) {
        if (bookLoanController.verifyBookIsBorrowed(book)) {
            ErrorDialog.show("Ops!", "Este livro está emprestado no momento.");
            bookComboBox.clear();
        }
    }
    
    @Override
    protected void beforeSave(BookLoan currentEntity) {
        UserService userService = new UserService(userController);
        User userLogged = userService.getLoggedUser();
        
        currentEntity.setUser(userLogged);
    }
}
