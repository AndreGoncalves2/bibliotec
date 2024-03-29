package br.com.bibliotec.ui.book;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainView.class)
public class BookView extends VerticalLayout {
    
    private final BookController bookController;
    
    private Grid<Book> grid;
    
    private Button addButton; 
    
    private Button editButton;
    
    private final H1 title;
    
    private final TextField searchField;
    
    private final HorizontalLayout actionButtons;
    
    private BookForm bookFormDialog;
    
    public BookView(@Autowired BookController bookController) {
        
        this.bookController = bookController;
        bookFormDialog = new BookForm(bookController);
        
        title = new H1("Livros");

        actionButtons = new HorizontalLayout();
        addButton = new Button("ADD");
        addButton.addClickListener(click -> {
            bookFormDialog.setNewBean();
            bookFormDialog.open();
        });

        editButton = new Button("EDIT");
        editButton.addClickListener(click -> {
            if (grid.getSelectedItems().stream().findFirst().isPresent()) {
                bookFormDialog.setBinder(grid.getSelectedItems().stream().findFirst().get());
                bookFormDialog.open(); 
            } else {
                Notification.show("Selecione um item.");
            }
        });
        actionButtons.add(addButton, editButton);
        

        searchField = new TextField("Pesquisa");
        searchField.setWidth("60%");

        creatGrid();

        add(title, actionButtons, searchField, grid);
    }

    private void creatGrid() {
        grid = new Grid<>();

        grid.addColumn(Book::getTitle).setHeader("Título").setWidth("20%");
        grid.addColumn(Book::getAuthor).setHeader("Autor").setWidth("20%");
        grid.addColumn(Book::getSynopsis).setHeader("Sinopse").setWidth("60%");

        grid.setItems(bookController.list());
    }
}
