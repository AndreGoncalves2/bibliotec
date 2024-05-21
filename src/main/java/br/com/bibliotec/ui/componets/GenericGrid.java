package br.com.bibliotec.ui.componets;

import br.com.bibliotec.controller.helper.GenericController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.interfaces.FormDefinition;
import br.com.bibliotec.interfaces.HasId;
import br.com.bibliotec.interfaces.RefreshListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.RouteConfiguration;

import java.util.List;
import java.util.Optional;

public class GenericGrid<T extends HasId<?>, C extends GenericController<T,?,?>> extends VerticalLayout implements RefreshListener {
    
    C controller;

    private final Grid<T> currentGrid;

    private FormDefinition currentForm;
    
    public HorizontalLayout actionButtons;

    private Button editButton;

    private TextField searchField;

    private List<T> listAllEntity;
    
    private final String currentParameter;
    
    
    public GenericGrid(String title, C controller) {
        this.controller = controller;
        
        currentParameter = RouteConfiguration.forSessionScope().getUrl(getClass());
        currentGrid = new Grid<>();
        PageTitleName pageTitle = new PageTitleName(title);

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
        addButton.addClickListener(click -> UI.getCurrent().navigate(currentParameter + "/novo"));

        editButton = new Button("EDITAR");
        editButton.addClassName("button-form");
        editButton.addClickListener(click -> {
            try {
                UI.getCurrent().navigate(currentParameter + "/" + getSelectedItem().getId());
            } catch (BibliotecException e) {
                throw new RuntimeException(e);
            }
        });

        actionButtons.add(addButton, editButton);
        return actionButtons;
    }

    public T getSelectedItem() throws BibliotecException {
        Optional<T> selectedItem = currentGrid.getSelectedItems().stream().findFirst();
        if (selectedItem.isEmpty()) {
            ErrorDialog.show("Ops!", "Nenhum item foi selecionado. Por favor, selecione um item para continuar.");
            throw new BibliotecException("Nenhum item selecionado");
        }
        return selectedItem.get();
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
