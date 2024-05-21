package br.com.bibliotec.ui.componets;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.interfaces.HasId;
import br.com.bibliotec.ui.helper.Binder;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.ValidationException;

import java.lang.reflect.InvocationTargetException;

public class GenericFormDialog<T extends HasId<I>, C extends GenericController<T, I, ?>, I> extends Dialog {
    
    private T currentEntity;
    private final C controller;
    
    protected Class<T> beanType;
    private final Binder<T, I, C> binder;
    
    private Button saveButton;
    private Button cancelButton;

    private final FormLayout formLayout;

    
    public GenericFormDialog(C controller, Class<T> beanType) {
        this.controller = controller;
        this.beanType = beanType;
        
        setHeaderTitle("Novo item");

        binder = new Binder<>(beanType, this, controller);
        this.formLayout = new FormLayout();
        formLayout.getStyle().set("padding", "3rem");
        formLayout.setMaxWidth("30rem");
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        
        createButtons();
        
        add(formLayout);
    }
    
    public FormLayout getFormLayout() {
        return formLayout;
    }
    
    public void createBinder() throws BibliotecException {
        try {
            binder.createBinder();
            setNewBean();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new BibliotecException(e.getMessage());
        }
    }
    
    public Binder<T, I, C> getBinder() {
        return binder;
    }

    private void createButtons() {
        HorizontalLayout displayButtons = new HorizontalLayout();
        
        Button exitButton = new Button(new Icon(VaadinIcon.CLOSE_BIG));
        
        saveButton = new Button("SALVAR");
        cancelButton = new Button("CANCELAR");
        
        saveButton.addClassName("button-form-confirm");
        cancelButton.addClassName("button-form-delete");
        
        cancelButton.addClickListener(click -> close());
        exitButton.addClickListener(click -> close());
        saveButton.addClickListener(click -> {
            try {
                handleSaveButton();
            } catch (ValidationException error) {
                ErrorDialog.show("Ops!", "Ocorreu um problema ao salvar o item. Por favor, tente novamente.");
                error.printStackTrace();
            }
        });

        exitButton.addClassName("close-button");
        
        getHeader().add(exitButton);
        displayButtons.add(cancelButton, saveButton);
        getFooter().add(displayButtons);
    }

    public void handleSaveButton() throws ValidationException {
        binder.writeBean(currentEntity);

        if (binder.isValid()) {
            try {
                if (currentEntity.getId() == null) {
                    controller.save(currentEntity);
                } else {
                    controller.update(currentEntity);
                }

                resetBinder();
                close();
            } catch (BibliotecException e) {
                ErrorDialog.show("Ops!", e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public void resetBinder() throws BibliotecException {
        binder.readBean(null);
        setNewBean();
    }

    public void setNewBean() throws BibliotecException {
        try {
            this.currentEntity = beanType.getDeclaredConstructor().newInstance();
            binder.readBean(currentEntity);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BibliotecException(e.getMessage());
        }
    }
}
