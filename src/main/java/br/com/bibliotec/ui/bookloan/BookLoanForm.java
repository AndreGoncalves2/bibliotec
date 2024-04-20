package br.com.bibliotec.ui.bookloan;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.listener.FormDefinition;
import br.com.bibliotec.listener.RefreshListener;
import br.com.bibliotec.model.Book;
import br.com.bibliotec.model.BookLoan;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.ui.helper.Binder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.ValidationException;

import java.time.LocalDate;

public class BookLoanForm extends Dialog implements FormDefinition {
    
    @Bind("book")
    private final ComboBox<Book> bookComboBox;
    
    @Bind("bookingDate")
    private final DatePicker bookingDate;

    @Bind("dueDate")
    private final DatePicker dueDate;

    @Bind("student")
    private final ComboBox<Student> studentComboBox;
    
    private final BookLoanController controller;

    private final FormLayout formLayout;

    private boolean isNew;

    private  Button confirmButton;

    private Button saveButton;

    private BookLoan currentBookLoan;

    private Dialog deleteDialog;

    private  Button deleteButton;


    private final Binder<BookLoan> binder;

    private final RefreshListener refreshListener;

    public BookLoanForm(BookLoanController controller, BookController bookController, StudentController studentController, RefreshListener refreshListener) throws IllegalAccessException {
        this.refreshListener = refreshListener;
        this.controller = controller;
        this.formLayout = new FormLayout();
        formLayout.setMaxWidth("500px");
        this.currentBookLoan = new BookLoan();

        binder = new Binder<>(BookLoan.class, this);

        bookComboBox = new ComboBox<>("Empréstimo");
        bookComboBox.setItems(bookController.list());
        
        bookingDate = new DatePicker("Data do empréstimo");
        dueDate = new DatePicker("Data do vencimento");
        
        bookingDate.setValue(LocalDate.now());
        bookingDate.addValueChangeListener(event -> dueDate.setMin(event.getValue()));

        studentComboBox = new ComboBox<>("Aluno");
        studentComboBox.setItems(studentController.list());
        
        binder.createBean();
        setNewBean();

        createButtons();

        formLayout.add(bookComboBox, bookingDate, dueDate, studentComboBox);
        createDeleteDialog();
        add(formLayout);
    }

    private void createButtons() {
        HorizontalLayout displayButtons = new HorizontalLayout();
        addOpenedChangeListener(dialog -> {
            if (isNew) {
                saveButton.setText("Salvar");
                setHeaderTitle("Novo Empréstimo");
                displayButtons.remove(deleteButton);
            } else {
                saveButton.setText("Atualizar");
                setHeaderTitle("Editar Empréstimo");
                displayButtons.add(deleteButton);
            }
        });
        createDialogButtons();

        displayButtons.add(saveButton);
        getFooter().add(displayButtons);
    }
    

    private void createDialogButtons() {

        saveButton= new Button();
        saveButton.addClickListener(click -> {
            try {
                handleSaveButton();
            } catch (ValidationException error) {
                Notification.show("Erro ao salver item.").addThemeVariants(NotificationVariant.LUMO_ERROR);
                error.printStackTrace();
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
            controller.delete(currentBookLoan);
            deleteDialog.close();
            if(refreshListener != null) {
                refreshListener.refresh();
            }
            close();
        } catch (BibliotecException error){
            Notification.show("Erro ao deletar item.");
            error.printStackTrace();
        }
    }

    public void handleSaveButton() throws ValidationException {
        binder.writeBean(currentBookLoan);

        if (binder.isValid()) {
            try {
                if (currentBookLoan.getId() == null) {
                    controller.save(currentBookLoan);
                } else {
                    controller.update(currentBookLoan);
                }

                resetBinder();
                if(refreshListener != null) {
                    refreshListener.refresh();
                }
                close();
            } catch (BibliotecException e) {
                Notification.show(e.getMessage()).addThemeVariants(NotificationVariant.LUMO_WARNING);
                e.printStackTrace();
            }
        }
    }

    public void resetBinder() {
        binder.readBean(null);
        setNewBean();
    }

    public void setNewBean() {
        this.currentBookLoan = new BookLoan();
        binder.readBean(null);
        this.isNew = true;
    }

    @Override
    public <T> void setBinder(T entity) {
        this.currentBookLoan = (BookLoan) entity;
        binder.readBean((BookLoan) entity);
        this.isNew = false;
    }
}

