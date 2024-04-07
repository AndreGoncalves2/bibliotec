package br.com.bibliotec.ui.student;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.listener.RefreshListener;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.ui.helper.Binder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;

public class StudentForm extends Dialog {
    
    @Bind("ra")
    public TextField txtRa;
    
    @Bind("name")
    public TextField txtName;

    @Bind("studentClass")
    public TextField txtStudentClass;

    private final StudentController controller;

    private final FormLayout formLayout;

    private boolean isNew;

    private Button confirmButton;

    private Button saveButton;

    private Student currentStudent;

    private Dialog deleteDialog;

    private  Button deleteButton;

    private final Binder<Student> binder;

    private final RefreshListener refreshListener;

    public StudentForm(StudentController controller, RefreshListener refreshListener) throws IllegalAccessException {
        this.refreshListener = refreshListener;
        this.controller = controller;
        this.formLayout = new FormLayout();
        
        binder = new Binder<>(Student.class, this);
        
        formLayout.setMaxWidth("500px");

        txtRa = new TextField("RA");
        txtRa.setAllowedCharPattern("[0-9]");
        txtRa.setMinLength(10);
        txtRa.setMaxLength(10);
        
        txtName = new TextField("Nome");
        txtStudentClass = new TextField("Turma");

        
        createButtons();

        formLayout.add(txtRa, txtName, txtStudentClass);
        createDeleteDialog();
        add(formLayout);

        setNewBean();

        addOpenedChangeListener(event -> txtRa.setReadOnly(!isNew));
        binder.createBean();
        
    }
    
    private void createButtons() {
        HorizontalLayout displayButtons = new HorizontalLayout();
        addOpenedChangeListener(dialog -> {
            if (isNew) {
                saveButton.setText("Salvar");
                setHeaderTitle("Novo Aluno");
                displayButtons.remove(deleteButton);
            } else {
                saveButton.setText("Atualizar");
                setHeaderTitle("Editar Aluno");
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
            } catch (BibliotecException | ValidationException error) {
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
            controller.delete(currentStudent);
            deleteDialog.close();
            refreshListener.refresh();
            close();
        } catch (BibliotecException error){
            Notification.show("Erro ao deletar item.");
            error.printStackTrace();
        }
    }

    public void handleSaveButton() throws BibliotecException, ValidationException {

        binder.writeBean(currentStudent);

        if (binder.isValid()) {
            try {
                if (currentStudent.getId() == null) {
                    controller.save(currentStudent);
                } else {
                    controller.update(currentStudent);
                }
                
                resetBinder();
                refreshListener.refresh();
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

    public void setBinder(Student entity) {
        this.currentStudent = entity;
        binder.readBean(entity);
        this.isNew = false;
    }

    public void setNewBean() {
        this.currentStudent = new Student();
        binder.readBean(null);
        this.isNew = true;
    }
}
