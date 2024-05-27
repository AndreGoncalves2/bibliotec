package br.com.bibliotec.ui.book;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.ui.MainView;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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

        refresh();
    }

    private static HtmlContainer imageRender(Book book) {
        try {
            BufferedImage imageCheck = null;
            if (book.getImage() != null) {
                imageCheck = ImageIO.read(new ByteArrayInputStream(book.getImage()));
            }

            if (imageCheck != null) {
                StreamResource resource = new StreamResource(book.getTitle(), () -> new ByteArrayInputStream(book.getImage()));
                Image image = new Image(resource, "Book image");
                image.setWidth("30px");
                image.setHeight("45px");

                return image;
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
        return createEmptyImage();
    }

    private static Div createEmptyImage() {
        Div emptyImage = new Div();
        emptyImage.setWidth("30px");
        emptyImage.setHeight("45px");
        return emptyImage;
    }
}
