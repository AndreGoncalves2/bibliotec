package br.com.bibliotec.ui.book;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.Book;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;

public class BookForm extends Dialog {
    
    private final TextField txtTitle;

    private final TextField txtAuthor;

    private final TextField txtsynopsis;
    
    private final BookController controller;
    
    private FormLayout formLayout;

    private boolean isNew;
    
    private  Button confirmButton;
    
    private Button saveButton;
    
    private Book currentBook;

    private Dialog deleteDialog;

    private  Button deleteButton;

    private final BeanValidationBinder<Book> binder;
    
    public BookForm(@Autowired BookController controller) {
        this.controller = controller;
        this.formLayout = new FormLayout();
        this.currentBook = new Book();
        
        binder = new BeanValidationBinder<>(Book.class);
        
        txtTitle = new TextField("Título");
        txtAuthor = new TextField("Autor");
        txtsynopsis = new TextField("Sinopse");
        
        binder.forField(txtTitle).bind("title");
        binder.forField(txtAuthor).bind("author");
        binder.forField(txtsynopsis).bind("synopsis");

        HorizontalLayout displayButtons = new HorizontalLayout();
        addOpenedChangeListener(dialog -> {
            if (isNew) {
                saveButton.setText("Salvar");
                setHeaderTitle("Novo livro");
                displayButtons.remove(deleteButton);
            } else {
                saveButton.setText("Atualizar");
                setHeaderTitle("Editar livro");
                displayButtons.add(deleteButton);
            }
        });
        createDialogButtons();
        
        displayButtons.add(saveButton);

        formLayout.add(txtTitle, txtAuthor, txtsynopsis);
        createDeleteDialog();
        add(formLayout);

        getFooter().add(displayButtons);
        setNewBean();
    }

    private void createDialogButtons() {

        saveButton= new Button();
        saveButton.addClickListener(click -> {
            try {
                handleSaveButton();
            } catch (BibliotecException e) {
                throw new RuntimeException(e);
            }
        });

        this.confirmButton = new Button("Confirmar");
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        this.deleteButton = new Button("Excluir");
        deleteButton.addClickListener(click -> deleteDialog.open());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        Button exitButton = new Button(new Icon(VaadinIcon.CLOSE_BIG));
        exitButton.addClickListener(click -> close());
        exitButton.addClassName("close-button");
        getHeader().add(exitButton);
    }

    private void createDeleteDialog() {
        deleteDialog = new Dialog();

        confirmButton.addClickListener(click -> deleteAndClose());

        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(click -> deleteDialog.close());

        deleteDialog.setHeaderTitle("Excluir item ?");
        deleteDialog.add("Tem certeza que deja excluir ?");

        deleteDialog.getFooter().add(cancelButton, confirmButton);
    }

    private void deleteAndClose() {
        try {
            controller.delete(currentBook);
            deleteDialog.close();
            close();
        } catch (BibliotecException e){
            Notification.show("Erro ao deletar item.");
        }
    }

    public void handleSaveButton() throws BibliotecException {
        try {
            binder.writeBean(currentBook);

            if (binder.isValid()) {
                if (currentBook.getId() == null) {
                    controller.save(currentBook);
                } else {
                    controller.update(currentBook);
                }
                resetBinder();
                close();
            }

        } catch (ValidationException e) {
            Notification.show("Verifique os campos e tente novamente");
            throw new BibliotecException("Erro ao salvar os dados");
        }
    }

    public void resetBinder() {
        binder.readBean(null);
        setNewBean();
    }

    public void setBinder(Book entity) {
        this.currentBook = entity;
        binder.readBean(entity);
        this.isNew = false;
    }

    public void setNewBean() {
        this.currentBook = new Book();
        binder.readBean(null);
        this.isNew = true;
    }
    
}
