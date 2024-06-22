package br.com.bibliotec.ui.book;

import br.com.bibliotec.config.GlobalProperties;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.ErrorDialog;
import br.com.bibliotec.ui.componets.GenericGrid;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@PermitAll
@PageTitle("Livros")
@Route(value = "/livro", layout = MainView.class)
public class BookGrid extends GenericGrid<Book, BookController> {

    public BookGrid(@Autowired BookController bookController) {
        super("LIVROS", bookController);
        creatGrid();
    }

    private void creatGrid() {
        Grid<Book> grid = getGrid();

        grid.addComponentColumn(BookGrid::imageRender).setHeader("Capa").setFlexGrow(0).setTextAlign(ColumnTextAlign.CENTER);
        grid.addColumn(Book::getCode).setHeader("Código").setWidth("5%").setSortable(true);
        grid.addColumn(Book::getTitle).setHeader("Título").setWidth("15%").setSortable(true);
        grid.addColumn(Book::getAuthor).setHeader("Autor").setWidth("15%");
        grid.addColumn(Book::getSynopsis).setHeader("Sinopse").setWidth("40%");
        grid.addColumn(book -> book.getUser() != null ? book.getUser().getName() : "").setHeader("Criado Por");

        refresh();
    }

    private static HtmlContainer imageRender(Book book) {
        String newFileName = book.getStringImage();
        File file = new File(GlobalProperties.getFileDirectory()+ newFileName);
        try {
            if (file.exists() && !file.isDirectory()) {
                return constructImage(newFileName, file);
            } else {
                return createEmptyImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorDialog.show("Ops!", " Erro ao carregar imagem.");
            return createEmptyImage();
        }
    }

    private static Image constructImage(String newFileName, File file) {
        StreamResource resource = new StreamResource(
                newFileName,
                () -> {
                    try {
                        return new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
        Image image = new Image(resource, "alt text");
        image.setWidth("30px");
        image.setHeight("45px");
        return image;
    }

    private static Div createEmptyImage() {
        Div emptyImage = new Div();
        emptyImage.setWidth("30px");
        emptyImage.setHeight("45px");
        return emptyImage;
    }
}
