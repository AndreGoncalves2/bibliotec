package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.GenericGrid;
import br.com.bibliotec.ui.componets.ReturnBookDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;

@PermitAll
@Route(value = "/emprestimo", layout = MainView.class)
public class BookLoanGrid extends GenericGrid<BookLoan, BookLoanController> {
    
    private final Button retunButton;
    
    private ReturnBookDialog returnBookDialog;
    
    private final BookLoanController bookLoanController;
    

    public BookLoanGrid(@Autowired BookLoanController bookLoanController,
                        @Autowired BookController bookController,
                        @Autowired StudentController studentController) throws IllegalAccessException, BibliotecException {
        super("EMPRÉSTIMOS", bookLoanController);
        
        this.bookLoanController = bookLoanController;
        
        retunButton = new Button("DEVOLVER");
        retunButton.addClassName("button-form");
        retunButton.addClickListener(click -> clickReturnBook());
        creatGrid();

        BookLoanForm bookLoanFormDialog = new BookLoanForm(bookLoanController, bookController, studentController, this);
        setForm(bookLoanFormDialog);
        actionButtons.add(retunButton);
        
    }
    
    private void clickReturnBook() {
        BookLoan currentBookLoan = getGrid().getSelectedItems().stream().findFirst().get();
        returnBookDialog = new ReturnBookDialog(currentBookLoan, bookLoanController, this);
    }

    private void creatGrid() {
        Grid<BookLoan> grid = getGrid();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        grid.addColumn(bookLoan -> bookLoan.getBook().getTitle()).setHeader("Título").setFlexGrow(3).setSortable(true);
        grid.addColumn(bookLoan -> bookLoan.getStudent().getName()).setHeader("Aluno").setFlexGrow(3).setSortable(true);
        grid.addColumn(bookLoan -> bookLoan.getStudent().getStudentClass()).setHeader("Turma").setFlexGrow(1).setSortable(true);
        grid.addColumn(bookLoan -> formatter.format(bookLoan.getBookingDate())).setHeader("Data Reserva").setFlexGrow(1).setSortable(true);
        grid.addColumn(bookLoan -> formatter.format(bookLoan.getDueDate())).setHeader("Data Vencimento").setFlexGrow(1).setSortable(true);
        grid.addColumn(BookLoan::getReturned).setHeader("Retornado").setFlexGrow(1).setSortable(true);
        
        refresh();
        
    }
}
