package br.com.bibliotec.ui.student;

import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.listener.RefreshListener;
import br.com.bibliotec.model.Student;
import br.com.bibliotec.ui.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "/aluno", layout = MainView.class)
public class StudentGrid extends VerticalLayout implements RefreshListener {

    private final StudentController studentController;

    private Grid<Student> grid;

    private Button addButton;

    private Button editButton;

    private final H1 title;

    private TextField searchField;

    private HorizontalLayout actionButtons;

    private final StudentForm studentFormDialog;

    private List<Student> listAllStudents;

    public StudentGrid(@Autowired StudentController studentController) throws IllegalAccessException {

        this.studentController = studentController;
        studentFormDialog = new StudentForm(studentController, this);

        title = new H1("Alunos");

        actionButtons = createActionButtons();

        searchField = createSearchField();

        creatGrid();

        add(title, actionButtons, searchField, grid);
    }

    private HorizontalLayout createActionButtons() {

        actionButtons = new HorizontalLayout();
        addButton = new Button("ADD");
        addButton.addClickListener(click -> {
            studentFormDialog.setNewBean();
            studentFormDialog.open();
        });

        editButton = new Button("EDIT");
        editButton.addClickListener(click -> {
            if (grid.getSelectedItems().stream().findFirst().isPresent()) {
                studentFormDialog.setBinder(grid.getSelectedItems().stream().findFirst().get());
                studentFormDialog.open();
            } else {
                Notification.show("Selecione um item.").addThemeVariants(NotificationVariant.LUMO_WARNING);
            }
        });

        actionButtons.add(addButton, editButton);
        return actionButtons;
    }

    private TextField createSearchField() {

        searchField = new TextField("Pesquisa");
        searchField.setWidth("60%");
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> {
            if (!searchField.isEmpty()) {
                List<Student> filteredStudent = listAllStudents.stream()
                        .filter(book -> book.toString().toLowerCase().contains(searchField.getValue().toLowerCase()))
                        .toList();

                refreshGrid(filteredStudent);
            } else {
                refresh();
            }
        });

        return searchField;
    }

    private void creatGrid() {


        grid = new Grid<>();

        grid.addColumn(Student::getRa).setHeader("RA").setFlexGrow(0);
        grid.addColumn(Student::getName).setHeader("Nome").setFlexGrow(1);
        grid.addColumn(Student::getStudentClass).setHeader("Classe").setFlexGrow(1);

        refresh();
    }

    protected void refreshGrid(List<Student> listBooks) {
        grid.setDataProvider(DataProvider.ofCollection(listBooks));
    }

    @Override
    public void refresh() {
        listAllStudents = studentController.list().reversed();
        grid.setDataProvider(DataProvider.ofCollection(listAllStudents));
    }
}
