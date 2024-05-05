package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.GenericGrid;
import br.com.bibliotec.ui.componets.ReturnBookDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@PermitAll
@Route(value = "/emprestimo", layout = MainView.class)
public class BookLoanGrid extends GenericGrid<BookLoan, BookLoanController> {
    
    private final Button retunButton;
    
    private ReturnBookDialog returnBookDialog;
    
    private final BookLoanController bookLoanController;
    

    public BookLoanGrid(@Autowired BookLoanController bookLoanController) {
        super("EMPRÉSTIMOS", bookLoanController);
        
        this.bookLoanController = bookLoanController;
        
        retunButton = new Button("DEVOLVER");
        retunButton.addClassName("button-form");
        retunButton.addClickListener(click -> clickReturnBook());
        creatGrid();
        
        actionButtons.add(retunButton);
    }
    
    private void clickReturnBook() {
        BookLoan currentBookLoan = getGrid().getSelectedItems().stream().findFirst().orElse(null);
        returnBookDialog = new ReturnBookDialog(currentBookLoan, bookLoanController, this);
    }

    private void creatGrid() {
        Grid<BookLoan> grid = getGrid();
        grid.addClassName("grid");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        grid.addColumn(bookLoan -> bookLoan.getBook().getTitle()).setHeader("Título").setFlexGrow(3).setSortable(true);
        grid.addColumn(bookLoan -> bookLoan.getStudent().getName()).setHeader("Aluno").setFlexGrow(3).setSortable(true);
        grid.addColumn(bookLoan -> bookLoan.getStudent().getStudentClass()).setHeader("Turma").setFlexGrow(1).setSortable(true);
        grid.addColumn(bookLoan -> formatter.format(bookLoan.getBookingDate())).setHeader("Data Reserva").setFlexGrow(1).setSortable(true);
        grid.addColumn(bookLoan -> formatter.format(bookLoan.getDueDate())).setHeader("Data Vencimento").setFlexGrow(1).setSortable(true);
        grid.addComponentColumn(bookLoan -> createStatusIcon(bookLoan.getReturned())).setHeader("Devolvido").setWidth("1rem").setComparator(BookLoan::getReturned);
        
        grid.setPartNameGenerator(bookLoan -> {
            if (LocalDate.now().isAfter(bookLoan.getDueDate()) && !bookLoan.getReturned()) {
                return "late";
            }
            return null;
        });
        
        refresh();
    }
    
    private Icon createStatusIcon(Boolean returned) {
        Icon icon;
        if (returned) {
            icon = VaadinIcon.CHECK.create();
            icon.getElement().getThemeList().add("badge success");
        } else {
            icon = VaadinIcon.CLOSE_SMALL.create();
            icon.getElement().getThemeList().add("badge error");
        }
        return icon;
    }
}
