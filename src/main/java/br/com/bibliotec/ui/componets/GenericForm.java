package br.com.bibliotec.ui.componets;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.interfaces.HasId;
import br.com.bibliotec.ui.helper.Binder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;

import java.lang.reflect.InvocationTargetException;

public class GenericForm<T extends HasId<I>, C extends GenericController<T, I, ?>, I> extends VerticalLayout 
        implements HasUrlParameter<String> {

    private T currentEntity;
    private final C controller;
    
    private final Div divTop;
    private final Div divContent;
    private final Div divFooter;
    
    private final H1 title;
    
    private final Button confirmButton;
    private final Button deleteButton;
    private Dialog deleteDialog;

    protected Class<T> beanType;
    private final Binder<T, I, C> binder;
    
    private String defaultRoute;
    
    
    public GenericForm(C controller, Class<T> beanType) {
        this.controller = controller;
        this.beanType = beanType;

        binder = new Binder<>(beanType, this, controller);
        
        divTop = new Div();
        divContent = new Div();
        divFooter = new Div();
        title = new H1();
        
        confirmButton = new Button("Confirmar");
        confirmButton.addClickListener(click -> {
            try {
                handleSaveButton();
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });

        createDeleteDialog();
        
        deleteButton = new Button("Excluir");
        deleteButton.addClickListener(click -> deleteDialog.open());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        
        
        divFooter.add(confirmButton, deleteButton);
        add(title, divTop, divContent, divFooter);
    }

    private void createDeleteDialog() {
        deleteDialog = new Dialog();
        Button confirmDeleteButton = new Button("Excluir");
        Button cancelButton = new Button("Cancelar");
        
        confirmDeleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        
        confirmDeleteButton.addClickListener(click -> deleteAndClose());
        cancelButton.addClickListener(click -> deleteDialog.close());

        deleteDialog.setHeaderTitle("Excluir item ?");
        deleteDialog.add("Tem certeza que deja excluir ?");

        deleteDialog.getFooter().add(cancelButton, confirmDeleteButton);
    }

    private void deleteAndClose() {
        try {
            controller.delete(currentEntity);
            deleteDialog.close();
            UI.getCurrent().navigate(defaultRoute);
        } catch (BibliotecException error){
            Notification.show(error.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            error.printStackTrace();
        }
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

                UI.getCurrent().navigate(defaultRoute);
            } catch (BibliotecException e) {
                Notification.show(e.getMessage()).addThemeVariants(NotificationVariant.LUMO_WARNING);
                e.printStackTrace();
            }
        }
    }

    public void setDefaultRoute(String route) {
        defaultRoute = route;
    }

    public void setNewBean() throws BibliotecException {
        try {
            this.currentEntity = beanType.getDeclaredConstructor().newInstance();
            binder.readBean(currentEntity);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new BibliotecException(e.getMessage());
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        if (parameter.equals("novo")) {
            try {
                binder.createBinder();
                setNewBean();
            } catch (BibliotecException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);

            }
        } else {
            try {
                binder.createBinder();
                binder.loadBean((I) Long.valueOf(parameter));
                currentEntity = binder.getValue();
            } catch (BibliotecException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setTitle(String title) {
       this.title.setText(title);
    }

    public Binder<T, I, C> getBinder() {
        return binder;
    }
    
    public Div getDivContent() {
        return divContent;
    }
    
    public Div getDivFooter() {
        return divFooter;
    }
    
    public Div getDivTop() {
        return divTop;
    }
}
