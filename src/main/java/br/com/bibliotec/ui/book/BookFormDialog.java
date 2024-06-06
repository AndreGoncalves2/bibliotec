package br.com.bibliotec.ui.book;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.config.GlobalProperties;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.componets.CustomUpload;
import br.com.bibliotec.ui.componets.ErrorDialog;
import br.com.bibliotec.ui.componets.GenericFormDialog;
import br.com.bibliotec.ui.componets.UploadPT;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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
    private byte[] fileContentBytes;
    private Image image;
    private final Icon clearIcon;
    
    private final UserController userController;

    public BookFormDialog(BookController controller,
                          UserController userController) throws BibliotecException {
        super(controller, Book.class);
        this.userController = userController;
        
        setTitle("Novo livro");
        
        txtCode = new TextField("Código");
        txtCode.setAllowedCharPattern("[0-9/\\-.]");

        txtTitle = new TextField("Título");
        txtAuthor = new TextField("Autor");
        txtsynopsis = new TextArea("Sinopse");
        
        clearIcon = VaadinIcon.CLOSE_BIG.create();
        clearIcon.setVisible(false);
        
        createBinder();
        
        createImageInput();

        Div uploadDiv = new Div(upload);
        uploadDiv.setClassName("upload-div");

        clearIcon.addClickListener(event -> removeImage());
        
        uploadDiv.add(clearIcon);
        getFormLayout().add(uploadDiv, txtCode, txtTitle, txtAuthor, txtsynopsis);
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
        upload.addSucceededListener(event -> renderImage());
        upload.setMaxFileSize(maxFileSizeInBytes);
        upload.addFileRejectedListener(event -> {
            ErrorDialog.show("Ops!", "A imagem que você está tentando carregar é muito grande. Por favor, selecione uma imagem com tamanho menor que 1MB.");
        });
    }
    

    private void renderImage() {
        String fileName = upload.getFileName();
        try {
            fileContentBytes = upload.getFileContentStream().readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        StreamResource resource = new StreamResource(fileName, () -> new ByteArrayInputStream(fileContentBytes));
        createImage(resource);
    }

    private void removeImage() {
        upload.setDropLabel(new Span("Adicione uma imagem."));
        upload.setFileContentStream(null);
        upload.clearFileList();
        clearIcon.setVisible(false);
    }
    private void createImage(StreamResource resource) {
        try {
            image = new Image(resource, "Imagem");

            image.setMaxWidth("5rem");
            image.setMaxHeight("5rem");
            image.getStyle().set("border-radius", ".2rem");

            image.getStyle().set("vertical-align", "bottom");
            upload.setDropLabel(image);
            clearIcon.setVisible(true);
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    protected void beforeSave(Book currentEntity){
        setNewFile();

        setUser(currentEntity);
    }
    
    private void setUser(Book currentEntity) {
        UserService userService = new UserService(userController);
        User userLogged = userService.getLoggedUser();

        currentEntity.setUser(userLogged);
    }

    private void setNewFile() {
        if (upload.getFileContentStream() != null) {
            try {
                File directory = GlobalProperties.getDirectory();
                String extension = FilenameUtils.getExtension(upload.getFileName());
                String newFileName = UUID.randomUUID() + "." + extension;
                
                File outputFile = new File(directory + File.separator + newFileName);
                Files.copy(new ByteArrayInputStream(fileContentBytes), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                getBinder().getValue().setStringImage(newFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    ;
}
