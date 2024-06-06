package br.com.bibliotec.ui.book;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.config.GlobalProperties;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.CustomUpload;
import br.com.bibliotec.ui.componets.ErrorDialog;
import br.com.bibliotec.ui.componets.GenericForm;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.PermitAll;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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
    
    private final Icon clearIcon;
    private Image image;
    private CustomUpload upload;
    private byte[] fileContentBytes;
    
    private final UserController userController;

    public BookForm(@Autowired BookController controller,
                    @Autowired UserController userController) {
        super(controller, Book.class);
        this.userController = userController;
        
        setTitleParameter("LIVRO");

        createImageInput();
        
        txtCode = new TextField("Código");
        txtCode.setAllowedCharPattern("[0-9/\\-.]");

        txtTitle = new TextField("Título");
        txtAuthor = new TextField("Autor");
        txtSynopsis = new TextArea("Sinopse");
        
        Div uploadDiv = new Div(upload);
        
        clearIcon = VaadinIcon.CLOSE_BIG.create();
        
        uploadDiv.setClassName("upload-div");
        setDefaultRoute("/livro");

        clearIcon.addClickListener(event -> removeImage());
        clearIcon.setVisible(false);
        
        uploadDiv.add(clearIcon);
        getFormLayout().add(uploadDiv, txtCode, txtTitle, txtAuthor, txtSynopsis);
    }

    private void createImageInput() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        upload = new CustomUpload(buffer);
        UploadPT i18n = new UploadPT();
        int maxFileSizeInBytes = 1024 * 1024;

        Span label = new Span("Adicione uma imagem.");
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

    private void removeImage() {
        upload.setDropLabel(new Span("Adicione uma imagem."));
        upload.setFileContentStream(null);
        upload.clearFileList();
        clearIcon.setVisible(false);
    }

    @Override
    protected void beforeSave(Book currentEntity) {
        deleteOldFile();

        setNewFile(currentEntity);

        setUser(currentEntity);
    }

    private void setUser(Book currentEntity) {
        UserService userService = new UserService(userController);
        User userLogged = userService.getLoggedUser();

        currentEntity.setUser(userLogged);
    }

    private void setNewFile(Book currentEntity) {
        if (upload.getFileContentStream() != null) {
            try {
                File directory = GlobalProperties.getDirectory();
                String extension = FilenameUtils.getExtension(upload.getFileName());
                String newFileName = UUID.randomUUID() + "." + extension;
                
                File outputFile = new File(directory + File.separator + newFileName);
                Files.copy(new ByteArrayInputStream(fileContentBytes), outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                currentEntity.setStringImage(newFileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void afterRead() {
        String newFileName = getBinder().getValue().getStringImage();
        File file = new File(GlobalProperties.getFileDirectory()+ newFileName);
        
        if(file.exists() && !file.isDirectory()) {
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
            
            createImage(resource);
        } else {
            clearIcon.setVisible(false);
        }
    }

    private void deleteOldFile() {
        Book currentBook = getBinder().getValue();
        
        File existingFile = new File(GlobalProperties.getDirectory() + File.separator + currentBook.getStringImage());
        if (existingFile.exists()) {
            existingFile.delete();
        }
    }
}
