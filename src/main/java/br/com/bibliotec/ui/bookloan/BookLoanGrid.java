package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.listener.RefreshListener;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.ui.MainView;
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

import java.util.List;

@Route(value = "/emprestimo", layout = MainView.class)
public class BookLoanGrid extends VerticalLayout implements RefreshListener {
    private final BookLoanController bookLoanController;

    private Grid<BookLoan> grid;

    private Button addButton;

    private Button editButton;

    private final H1 title;

    private TextField searchField;

    private HorizontalLayout actionButtons;

    private final BookLoanForm bookLoanFormDialog;

    private List<BookLoan> listAllBookLoan;

    public BookLoanGrid(@Autowired BookLoanController bookLoanController,
                        @Autowired BookController bookController,
                        @Autowired StudentController studentController) throws IllegalAccessException {

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
            if (grid.getSelectedItems().stream().findFirst().isPresent()) {
                bookLoanFormDialog.setBinder(grid.getSelectedItems().stream().findFirst().get());
                bookLoanFormDialog.open();
            } else {
                Notification.show("Selecione um item.").addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        });

        actionButtons.add(addButton, editButton);
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
        
        grid.addColumn(bookLoan -> bookLoan.getBook().getTitle()).setFlexGrow(1);
        grid.addColumn(BookLoan::getStudent).setHeader("Aluno").setFlexGrow(1);
//        grid.addColumn(BookLoan::getStudentClass).setHeader("Turma").setFlexGrow(1);
        grid.addColumn(BookLoan::getBookingDate).setHeader("Data Reserva").setWidth("20%");
        grid.addColumn(BookLoan::getDueDate).setHeader("Data Vencimento").setWidth("20%");
        grid.addColumn(BookLoan::getReturned).setHeader("Retornado").setWidth("20%");
        
        refresh();
    }

    protected void refreshGrid(List<BookLoan> listBookLoan) {
        grid.setDataProvider(DataProvider.ofCollection(listBookLoan));
    }

    @Override
    public void refresh() {
        listAllBookLoan = bookLoanController.list().reversed();
        grid.setDataProvider(DataProvider.ofCollection(listAllBookLoan));
    }
}
