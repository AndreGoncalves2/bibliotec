package br.com.bibliotec.ui.componets;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.listener.FormDefinition;
import br.com.bibliotec.listener.RefreshListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;

public class GenericGrid<T, C extends GenericController<T,?,?>> extends VerticalLayout implements RefreshListener {
    
    C controller;

    private final Grid<T> currentGrid;

    private FormDefinition currentForm;
    
    private HorizontalLayout actionButtons;

    private Button editButton;

    private TextField searchField;

    private List<T> listAllEntity;
    
    
    public GenericGrid(String title, C controller) {
        this.controller = controller;
        currentGrid = new Grid<>();
        PageTitle pageTitle = new PageTitle(title);

        actionButtons = createActionButtons();

        searchField = createSearchField();
        
        add(pageTitle, actionButtons, searchField, currentGrid);
    }
    
    public Grid<T> getGrid() {
        return currentGrid;
    }
    
    public void setForm(FormDefinition form) {
        currentForm = form;
    }

    private HorizontalLayout createActionButtons() {

        actionButtons = new HorizontalLayout();
        
        Button addButton = new Button("ADICIONAR");
        addButton.setClassName("button-form");
        addButton.addClickListener(click -> {
            currentForm.setNewBean();
            currentForm.open();
        });

        editButton = new Button("EDITAR");
        editButton.addClassName("button-form");
        editButton.addClickListener(click -> {
            try {
                currentForm.setBinder(getSelectedItem());
                currentForm.open();
            } catch (BibliotecException e) {
                e.printStackTrace();
            }
        });

        actionButtons.add(addButton, editButton);
        return actionButtons;
    }

    public T getSelectedItem() throws BibliotecException {
        if (currentGrid.getSelectedItems().stream().findFirst().isEmpty()) {
            Notification.show("Nenhum item selecionado").addThemeVariants(NotificationVariant.LUMO_WARNING);
            throw new BibliotecException("Nenhum item selecionado");
        }
        return currentGrid.getSelectedItems().stream().findFirst().get();
    }

    private TextField createSearchField() {

        searchField = new TextField();
        searchField.addClassName("search-field");
        searchField.setPlaceholder("Busca rápida");
        searchField.setWidth("60%");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> {
            if (!searchField.isEmpty()) {
                List<T> filteredlist = listAllEntity.stream()
                        .filter(entity -> entity.toString().toLowerCase().contains(searchField.getValue().toLowerCase()))
                        .toList();

                refreshGrid(filteredlist);
            } else {
                refresh();
            }
        });

        return searchField;
    }






    protected void refreshGrid(List<T> listEntity) {
        currentGrid.setDataProvider(DataProvider.ofCollection(listEntity));
    }

    @Override
    public void refresh() {
        listAllEntity = controller.list().reversed();
        currentGrid.setDataProvider(DataProvider.ofCollection(listAllEntity));
    }
}
