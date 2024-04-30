package br.com.bibliotec.ui.book;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.GenericForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;

@PermitAll
@Route(value = "/livro", layout = MainView.class)
public class BookForm extends GenericForm<Book, BookController, Long> {

    @Bind("code")
    private final TextField txtCode;

    @Bind("title")
    private final TextField txtTitle;

    @Bind("author")
    private final TextField txtAuthor;

    @Bind("synopsis")
    private final TextArea txtsynopsis;

    private Upload upload;
    
    public BookForm(@Autowired BookController controller) {
        super(controller, Book.class);

        createImageInput();
        
        txtCode = new TextField("Código");
        txtCode.setAllowedCharPattern("[0-9/\\-.]");

        txtTitle = new TextField("Título");
        txtAuthor = new TextField("Autor");
        txtsynopsis = new TextArea("Sinopse");
        
        setDefaultRoute("/livro");
        
        getDivContent().add(upload, txtCode, txtTitle, txtAuthor, txtsynopsis);
    }

    private void createImageInput() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        upload = new Upload(buffer);

        Span label = new Span("Adicione a imagem do livro aqui.");
        label.getStyle().set("vertical-align", "bottom");
        upload.setDropLabel(label);

        upload.setUploadButton(new Button("Adicionar imagem"));
        upload.setAcceptedFileTypes(".png", ".jpg", ".jpeg");
        upload.setMaxFiles(1);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            try {
                getBinder().getValue().setImage(inputStream.readAllBytes());
            } catch (IOException error) {
                Notification.show("Erro ao adicionar imagem.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                error.printStackTrace();
            }
        });
    }
}
