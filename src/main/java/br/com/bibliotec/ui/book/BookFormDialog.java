package br.com.bibliotec.ui.book;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.ui.componets.CustomUpload;
import br.com.bibliotec.ui.componets.GenericFormDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BookFormDialog extends GenericFormDialog<Book, BookController, Long> {
    
    @Bind("code")
    private final TextField txtCode; 
    
    @Bind("title")
    private final TextField txtTitle;

    @Bind("author")
    private final TextField txtAuthor;

    @Bind("synopsis")
    private final TextArea txtsynopsis;
    
    private CustomUpload upload;
    private MultiFileMemoryBuffer buffer;

    public BookFormDialog(@Autowired BookController controller) throws BibliotecException {
        super(controller, Book.class);
        
        txtCode = new TextField("Código");
        txtCode.setAllowedCharPattern("[0-9/\\-.]");

        txtTitle = new TextField("Título");
        txtAuthor = new TextField("Autor");
        txtsynopsis = new TextArea("Sinopse");
        
        createBinder();
        
        createImageInput();
        
        getFormLayout().add(upload, txtCode, txtTitle, txtAuthor, txtsynopsis);
    }

    private void createImageInput() {
        buffer = new MultiFileMemoryBuffer();
        upload = new CustomUpload(buffer);
        
        upload.setUploadButton(new Button("Adicionar imagem"));
        upload.setAcceptedFileTypes(".png", ".jpg", ".jpeg");
        upload.setDropLabel(new Span("Adicione a imagem do livro aqui."));
        upload.setMaxFiles(1);
        
        upload.addFileRemoveListener(this::removeImage);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            try {
                getBinder().getValue().setImage(inputStream.readAllBytes());
                renderImage();
            } catch (IOException error) {
                Notification.show("Erro ao adicionar imagem.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                error.printStackTrace();
            }
        });
    }
    
    private void renderImage() {
        byte[] bytes = getBinder().getValue().getImage();

        StreamResource resource = new StreamResource(
                "image.png",
                () -> new ByteArrayInputStream(bytes)
        );
        Image image = new Image(resource, "Imagem");
        image.setMaxWidth("5rem");
        image.setMaxHeight("5rem");
        image.getStyle().set("border-radius", ".3rem");

        image.getStyle().set("vertical-align", "bottom");
        upload.setDropLabel(image);
    }
    
    private void removeImage() {
        upload.setDropLabel(new Span("Adicione a imagem do livro aqui."));
        getBinder().getValue().setImage(null);
    }
}
