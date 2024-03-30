package br.com.bibliotec.ui.book;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.List;

@Route(value = "", layout = MainView.class)
public class BookGrid extends VerticalLayout {
    
    private final BookController bookController;
    
    private Grid<Book> grid;
    
    private Button addButton; 
    
    private Button editButton;
    
    private final H1 title;
    
    private TextField searchField;
    
    private HorizontalLayout actionButtons;
    
    private final BookForm bookFormDialog;
    
    private List<Book> listAllBooks;
    
    public BookGrid(@Autowired BookController bookController) {
        
        this.bookController = bookController;
        bookFormDialog = new BookForm(bookController);
        
        title = new H1("Livros");

        actionButtons = createActionButtons();

        searchField = createSearchField();

        creatGrid();

        add(title, actionButtons, searchField, grid);
    }

    private HorizontalLayout createActionButtons() {
        
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
                Notification.show("Selecione um item.").addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        });
        
        actionButtons.add(addButton, editButton);
        return actionButtons;
    }

    private TextField createSearchField() {
       
        searchField = new TextField("Pesquisa");
        searchField.setWidth("60%");
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(event -> {
            if (!searchField.isEmpty()) {
                List<Book> filteredBooks = listAllBooks.stream()
                        .filter(book -> book.getTitle().toLowerCase().contains(searchField.getValue().toLowerCase()))
                        .toList();
                
                refreshGrid(filteredBooks);
            } else {
                refreshGrid();
            }
        });
        
        return searchField;
    }

    private void creatGrid() {
      
        
        grid = new Grid<>();
        grid.addComponentColumn(book -> {
            if(book.getImage().length > 1 ) {
                StreamResource resource = new StreamResource(book.getTitle(), () -> new ByteArrayInputStream(book.getImage()));
                Image image = new Image(resource, "Book image");
                image.setWidth("30px");
                image.setHeight("45px");
                
                return image;
            }
            return createEmptyImage();
        }).setHeader("Capa").setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);
        
        grid.addColumn(Book::getTitle).setHeader("Título").setWidth("20%");
        grid.addColumn(Book::getAuthor).setHeader("Autor").setWidth("20%");
        grid.addColumn(Book::getSynopsis).setHeader("Sinopse").setWidth("50%");

        refreshGrid();
        addRefreshOnForm();
    }

    private static Div createEmptyImage() {
        Div emptyImage = new Div();
        emptyImage.setWidth("30px");
        emptyImage.setHeight("45px");
        return emptyImage;
    }

    protected void refreshGrid() {
        listAllBooks = bookController.list().reversed();
        grid.setDataProvider(DataProvider.ofCollection(listAllBooks));
    }

    protected void refreshGrid(List<Book> listBooks) {
        grid.setDataProvider(DataProvider.ofCollection(listBooks));
    }

    private void addRefreshOnForm() {
        bookFormDialog.getConfirmButton().addClickListener(click -> refreshGrid());
        bookFormDialog.getSaveButton().addClickListener(click -> refreshGrid());
    }
}
