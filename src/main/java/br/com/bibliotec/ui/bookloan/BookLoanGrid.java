package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.GenericGrid;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;

@Route(value = "/emprestimo", layout = MainView.class)
public class BookLoanGrid extends GenericGrid<BookLoan, BookLoanController> {

    public BookLoanGrid(@Autowired BookLoanController bookLoanController,
                        @Autowired BookController bookController,
                        @Autowired StudentController studentController) throws IllegalAccessException, BibliotecException {
        super("EMPRÉSTIMOS", bookLoanController);

        creatGrid();

        BookLoanForm bookLoanFormDialog = new BookLoanForm(bookLoanController, bookController, studentController, this);
        setForm(bookLoanFormDialog);
    }

    private void creatGrid() {
        Grid<BookLoan> grid = getGrid();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        grid.addColumn(bookLoan -> bookLoan.getBook().getTitle()).setHeader("Título").setFlexGrow(3);
        grid.addColumn(bookLoan -> bookLoan.getStudent().getName()).setHeader("Aluno").setFlexGrow(3);
        grid.addColumn(bookLoan -> bookLoan.getStudent().getStudentClass()).setHeader("Turma").setFlexGrow(1);
        grid.addColumn(bookLoan -> formatter.format(bookLoan.getBookingDate())).setHeader("Data Reserva").setFlexGrow(1);
        grid.addColumn(bookLoan -> formatter.format(bookLoan.getDueDate())).setHeader("Data Vencimento").setFlexGrow(1);
        grid.addColumn(BookLoan::getReturned).setHeader("Retornado").setFlexGrow(0);
        
        refresh();
    }
}
