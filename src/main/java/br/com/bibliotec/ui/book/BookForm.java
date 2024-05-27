package br.com.bibliotec.ui.book;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.CustomUpload;
import br.com.bibliotec.ui.componets.ErrorDialog;
import br.com.bibliotec.ui.componets.GenericForm;
import br.com.bibliotec.ui.componets.UploadPT;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@PermitAll
@PageTitle("Livro")
@Route(value = "/livro", layout = MainView.class)
public class BookForm extends GenericForm<Book, BookController, Long> {

    @Bind("code")
    private final TextField txtCode;

    @Bind("title")
    private final TextField txtTitle;

    @Bind("author")
    private final TextField txtAuthor;

    @Bind("synopsis")
    private final TextArea txtSynopsis;

    private CustomUpload upload;
    
    public BookForm(@Autowired BookController controller) {
        super(controller, Book.class);
        
        setTitleParameter("LIVRO");

        createImageInput();
        
        txtCode = new TextField("Código");
        txtCode.setAllowedCharPattern("[0-9/\\-.]");

        txtTitle = new TextField("Título");
        txtAuthor = new TextField("Autor");
        txtSynopsis = new TextArea("Sinopse");
        
        setDefaultRoute("/livro");
        
        getFormLayout().add(upload, txtCode, txtTitle, txtAuthor, txtSynopsis);
    }

    private void createImageInput() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        upload = new CustomUpload(buffer);
        UploadPT i18n = new UploadPT();
        int maxFileSizeInBytes = 1024 * 1024;

        Span label = new Span("Adicione a imagem do livro.");
        label.getStyle().set("vertical-align", "bottom");
        upload.setDropLabel(label);

        upload.setI18n(i18n);
        upload.setUploadButton(new Button("Adicionar imagem"));
        upload.setAcceptedFileTypes(".png", ".jpg", ".jpeg");
        upload.setMaxFiles(1);
        upload.addFileRemoveListener(this::removeImage);
        upload.addSucceededListener(event -> insertImage(event, buffer));
        upload.setMaxFileSize(maxFileSizeInBytes);
        upload.addFileRejectedListener(event -> {
            ErrorDialog.show("Ops!", "A imagem que você está tentando carregar é muito grande. Por favor, selecione uma imagem com tamanho menor que 1MB.");
        });
    }

    private void insertImage(SucceededEvent event, MultiFileMemoryBuffer buffer) {
        String fileName = event.getFileName();
        InputStream inputStream = buffer.getInputStream(fileName);
        try {
            getBinder().getValue().setImage(inputStream.readAllBytes());
            renderImage();
        } catch (IOException error) {
            ErrorDialog.show("Ops!", "Ocorreu um problema ao adicionar a imagem. Por favor, tente novamente.");
            error.printStackTrace();
        }
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
        upload.setDropLabel(new Span("Adicione a imagem do livro."));
        getBinder().getValue().setImage(null);
    }
}
