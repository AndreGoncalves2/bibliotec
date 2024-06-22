package br.com.bibliotec.ui.componets;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.interfaces.HasId;
import br.com.bibliotec.ui.helper.Binder;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
    private final FormLayout formLayout;
    private final Div divFooter;
    
    private PageTitleName title;
    
    private final Button deleteButton ;
    private Button confirmButton;
    private Button cancelButton;
    private Dialog deleteDialog;

    protected Class<T> beanType;
    private final Binder<T, I, C> binder;
    
    private String defaultRoute;
    private String titleParameter;
    
    public GenericForm(C controller, Class<T> beanType) {
        this.controller = controller;
        this.beanType = beanType;
        binder = new Binder<>(beanType, this, controller);

        deleteButton = new Button("EXCLUIR");
        VerticalLayout mainContainer = new VerticalLayout();

        Button returnButton = createReturnButton();

        divTop = new Div();
        formLayout = new FormLayout();
        divFooter = new Div();
        
        divTop.addClassName("div-top-form");
        formLayout.addClassName("div-content-form");
        divFooter.addClassName("div-footer-form");
        
        mainContainer.addClassName("generic-form");
        formLayout.addClassName("div-content");
        
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
         
        mainContainer.add(returnButton, divTop, formLayout, divFooter);
        
        add(mainContainer);
    }

    private Button createReturnButton() {
        Icon returnIcon = VaadinIcon.ARROW_BACKWARD.create();
        Button button = new Button(returnIcon);
        
        button.addClassName("return-button");
        
        button.addClickListener(click -> UI.getCurrent().navigate(defaultRoute));

        return button;
    }

    private void creteButtons(String urlParameter) {
        confirmButton = new Button("SALVAR");
        cancelButton = new Button("CANCELAR");

        confirmButton.addClassName("button-form-confirm");
        cancelButton.addClassName("button-form-delete");
        deleteButton.addClassName("button-form-delete");

        confirmButton.addClickListener(click -> handleSaveButton());
        cancelButton.addClickListener(click -> UI.getCurrent().navigate(defaultRoute));
        deleteButton.addClickListener(click -> deleteDialog.open());
        
        if (urlParameter.equals("novo")) {
            divFooter.addComponentAsFirst(cancelButton);
        } else {
            divFooter.addComponentAsFirst(deleteButton);
        }
        
        divFooter.add(confirmButton);
        createDeleteDialog();
    }

    private void createDeleteDialog() {
        deleteDialog = new Dialog();
        
        Button confirmDeleteButton = new Button("Excluir");
        Button cancelButton = new Button("Cancelar");
        
        confirmDeleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        
        confirmDeleteButton.addClickListener(click -> deleteAndClose());
        cancelButton.addClickListener(click -> deleteDialog.close());

        deleteDialog.setHeaderTitle("Excluir item ?");
        deleteDialog.add("Tem certeza que deseja excluir ?");

        deleteDialog.getFooter().add(cancelButton, confirmDeleteButton);
    }

    private void deleteAndClose() {
        try {
            controller.delete(currentEntity);
            deleteDialog.close();
            UI.getCurrent().navigate(defaultRoute);
        } catch (BibliotecException error){
            ErrorDialog.show("Ops!", error.getMessage());
            error.printStackTrace();
        }
    }

    protected void handleSaveButton() {
        beforeSave(currentEntity);
        try {
            binder.writeBean(currentEntity);
            try {
                if (currentEntity.getId() == null) {
                    controller.save(currentEntity);
                } else {
                    controller.update(currentEntity);
                }

                UI.getCurrent().navigate(defaultRoute);
            } catch (BibliotecException e) {
                ErrorDialog.show("Ops!", e.getMessage());
                e.printStackTrace();
            }
        } catch (ValidationException e) {
            e.printStackTrace();
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
        
        processBinderCreation(parameter);
        configureTitlePage(parameter);
        creteButtons(parameter);
    }

    private void configureTitlePage(String parameter) {
        if (parameter.equals("novo")) {
            title = new PageTitleName("ADICIONAR " + titleParameter);
        } else {
            title = new PageTitleName("EDITAR " + titleParameter);
        }
        
        title.enablePageTitle(false);
        
        addComponentAsFirst(title);
    }

    private void processBinderCreation(String parameter) {
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
                afterRead();
            } catch (BibliotecException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setTitleParameter(String titleParameter) {
        this.titleParameter = titleParameter;
    }
    
    protected void beforeSave(T currentEntity) {}
    protected void afterRead() {}

    public Binder<T, I, C> getBinder() {
        return binder;
    }
    
    public FormLayout getFormLayout() {
        return formLayout;
    }
    
    public Div getDivFooter() {
        return divFooter;
    }
    
    public Div getDivTop() {
        return divTop;
    }
    
    public void setButtonDeleteVisible(boolean deleteVisible) {
        deleteButton.setVisible(deleteVisible);
    }
}
