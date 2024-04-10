package br.com.bibliotec.ui.book;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.listener.RefreshListener;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.ui.MainView;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
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
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Route(value = "/livro", layout = MainView.class)
public class BookGrid extends VerticalLayout implements RefreshListener {
    
    private final BookController bookController;
    
    private Grid<Book> grid;
    
    private Button addButton; 
    
    private Button editButton;
    
    private final H1 title;
    
    private TextField searchField;
    
    private HorizontalLayout actionButtons;
    
    private final BookForm bookFormDialog;
    
    private List<Book> listAllBooks;
    
    public BookGrid(@Autowired BookController bookController) throws IllegalAccessException {
        
        this.bookController = bookController;
        bookFormDialog = new BookForm(bookController, this);
        
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
            try {
                bookFormDialog.setBinder(getSelectedItem());
                bookFormDialog.open(); 
            } catch (BibliotecException e) {
                e.printStackTrace();
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
                List<Book> filteredBooks = listAllBooks.stream()
                        .filter(book -> book.toString().toLowerCase().contains(searchField.getValue().toLowerCase()))
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
        
        grid.addComponentColumn(BookGrid::imageRender).setHeader("Capa").setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(Book::getCode).setHeader("Código").setWidth("5%");
        grid.addColumn(Book::getTitle).setHeader("Título").setWidth("15%");
        grid.addColumn(Book::getAuthor).setHeader("Autor").setWidth("15%");
        grid.addColumn(Book::getSynopsis).setHeader("Sinopse").setWidth("40%");

        refresh();
    }

    private static HtmlContainer imageRender(Book book) {
        try {
            BufferedImage imageCheck = ImageIO.read(new ByteArrayInputStream(book.getImage()));
            
            if (imageCheck != null) {
                StreamResource resource = new StreamResource(book.getTitle(), () -> new ByteArrayInputStream(book.getImage()));
                Image image = new Image(resource, "Book image");
                image.setWidth("30px");
                image.setHeight("45px");
    
                return image;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return createEmptyImage();
    }

    private static Div createEmptyImage() {
        Div emptyImage = new Div();
        emptyImage.setWidth("30px");
        emptyImage.setHeight("45px");
        return emptyImage;
    }

    public Book getSelectedItem() throws BibliotecException {
        if (grid.getSelectedItems().stream().findFirst().isEmpty()) {
            Notification.show("Nenhum item selecionado").addThemeVariants(NotificationVariant.LUMO_WARNING);
            throw new BibliotecException("Nenhum item selecionado");
        }
        return grid.getSelectedItems().stream().findFirst().get();
    }
    
    protected void refreshGrid(List<Book> listBooks) {
        grid.setDataProvider(DataProvider.ofCollection(listBooks));
    }

    @Override
    public void refresh() {
        listAllBooks = bookController.list().reversed();
        grid.setDataProvider(DataProvider.ofCollection(listAllBooks));
    }
}
