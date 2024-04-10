package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.listener.RefreshListener;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.ReturnBookDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "/emprestimo", layout = MainView.class)
public class BookLoanGrid extends VerticalLayout implements RefreshListener {
    private final BookLoanController bookLoanController;

    private Grid<BookLoan> grid;

    private Button addButton;

    private Button editButton;
    
    private Button returnButton;

    private final H1 title;

    private TextField searchField;

    private HorizontalLayout actionButtons;

    private final BookLoanForm bookLoanFormDialog;

    private List<BookLoan> listAllBookLoan;
    
    private ReturnBookDialog returnBookDialog;

    public BookLoanGrid(@Autowired BookLoanController bookLoanController,
                        @Autowired BookController bookController,
                        @Autowired StudentController studentController) throws IllegalAccessException, BibliotecException {

        this.bookLoanController = bookLoanController;
        bookLoanFormDialog = new BookLoanForm(bookLoanController, bookController, studentController, this);

        title = new H1("Empréstimos");

        actionButtons = createActionButtons();

        searchField = createSearchField();

        creatGrid();

        add(title, actionButtons, searchField, grid);
    }

    private HorizontalLayout createActionButtons() {

        actionButtons = new HorizontalLayout();
        addButton = new Button("ADD");
        addButton.addClickListener(click -> {
            bookLoanFormDialog.setNewBean();
            bookLoanFormDialog.open();
        });

        editButton = new Button("EDIT");
        editButton.addClickListener(click -> {
            try {
                bookLoanFormDialog.setBinder(getSelectedItem());
                bookLoanFormDialog.open();
            } catch (BibliotecException e) {
                e.printStackTrace();
            }
        });

        returnButton = new Button("Devolver");
        returnButton.addClickListener(click -> {
            try {
                returnBookDialog = new ReturnBookDialog(getSelectedItem(), bookLoanController, this);
            } catch (BibliotecException e) {
                throw new RuntimeException(e);
            }
        });
        
        
        

        actionButtons.add(addButton, editButton,returnButton);
        return actionButtons;
    }

    private TextField createSearchField() {

        searchField = new TextField("Pesquisa");
        searchField.setWidth("60%");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> {
            if (!searchField.isEmpty()) {
                List<BookLoan> filteredBooks = listAllBookLoan.stream()
                        .filter(bookLoan -> bookLoan.toString().toLowerCase().contains(searchField.getValue().toLowerCase()))
                        .toList();

                refreshGrid(filteredBooks);
            } else {
                refresh();
            }
        });

        return searchField;
    }

    private void creatGrid() {
        grid = new Grid<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        grid.addColumn(bookLoan -> bookLoan.getBook().getTitle()).setHeader("Título").setFlexGrow(3);
        grid.addColumn(bookLoan -> bookLoan.getStudent().getName()).setHeader("Aluno").setFlexGrow(3);
        grid.addColumn(bookLoan -> bookLoan.getStudent().getStudentClass()).setHeader("Turma").setFlexGrow(1);
        grid.addColumn(bookLoan -> formatter.format(bookLoan.getBookingDate())).setHeader("Data Reserva").setFlexGrow(1);
        grid.addColumn(bookLoan -> formatter.format(bookLoan.getDueDate())).setHeader("Data Vencimento").setFlexGrow(1);
        grid.addColumn(BookLoan::getReturned).setHeader("Retornado").setFlexGrow(0);
        
        refresh();
    }

    protected void refreshGrid(List<BookLoan> listBookLoan) {
        grid.setDataProvider(DataProvider.ofCollection(listBookLoan));
    }
    
    public BookLoan getSelectedItem() throws BibliotecException {
        if (grid.getSelectedItems().stream().findFirst().isEmpty()) {
            Notification.show("Nenhum item selecionado").addThemeVariants(NotificationVariant.LUMO_WARNING);
            throw new BibliotecException("Nenhum item selecionado");
        }
        return grid.getSelectedItems().stream().findFirst().get();
    }

    @Override
    public void refresh() {
        listAllBookLoan = bookLoanController.list().reversed();
        grid.setDataProvider(DataProvider.ofCollection(listAllBookLoan));
    }
}
