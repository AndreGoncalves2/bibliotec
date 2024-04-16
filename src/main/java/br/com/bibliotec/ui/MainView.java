package br.com.bibliotec.ui;

import br.com.bibliotec.ui.book.BookGrid;
import br.com.bibliotec.ui.bookloan.BookLoanGrid;
import br.com.bibliotec.ui.student.StudentGrid;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

@Theme(value = "bibliotec", variant = Lumo.DARK)
public class MainView extends AppLayout implements AppShellConfigurator, BeforeEnterObserver {
    
    private Div bookDiv;
    private Div studentDiv;
    private Div loanDiv;
    private Div configDiv;
    private Div startDiv;
    
    public MainView() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("header-container");
        
        HorizontalLayout tileContainer = new HorizontalLayout();
        tileContainer.setClassName("title-container");
        H4 title = new H4("BIBLIOTECA ESCOLA MUNICIPAL JOSÉ AUGUSTO GAMA DE SOUZA");
        tileContainer.add(title);
        
        HorizontalLayout layoutButtons = createHeader();
        
        HorizontalLayout pageTitle = createPageTitle();
        
        container.add(tileContainer, layoutButtons, pageTitle);
        addToNavbar(container);
    }
    
    private HorizontalLayout createHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("buttons-container");

        startDiv = new Div();
        startDiv.setClassName("div-links-start");
        RouterLink startLink = new RouterLink("INÍCIO", BookLoanGrid.class);
        startDiv.add(startLink);
        
        HorizontalLayout endDiv = new HorizontalLayout();
        endDiv.setClassName("div-links-container-end");

        bookDiv = new Div();
        bookDiv.setClassName("div-links-right");
        RouterLink bookLink = new RouterLink("LIVROS", BookGrid.class);
        bookDiv.add(bookLink);
        endDiv.add(bookDiv);

        studentDiv = new Div();
        studentDiv.setClassName("div-links-right");
        RouterLink studentLink = new RouterLink("ALUNOS", StudentGrid.class);
        studentDiv.add(studentLink);
        endDiv.add(studentDiv);

        loanDiv = new Div();
        loanDiv.setClassName("div-links-right");
        RouterLink loanLink = new RouterLink("EMPRÉSTIMOS", BookLoanGrid.class);
        loanDiv.add(loanLink);
        endDiv.add(loanDiv);

        configDiv = new Div();
        configDiv.setClassName("div-link-config");
        RouterLink configLink = new RouterLink("CONFIGURAÇÕES", BookLoanGrid.class);
        configDiv.add(configLink);
        endDiv.add(configDiv);
        
        layout.add(startDiv, endDiv);
        return layout;
    }
    
    private HorizontalLayout createPageTitle() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("school-name-container");
        
        H1 start = new H1("BIBLIOTECA DIGITAL DA");
        start.addClassName("title-start");
        
        H1 end = new H1("ESCOLA MUNICIPAL JAGS");
        end.addClassName("title-end");
        
        layout.add(start, end);
        return layout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String route = event.getLocation().getPath();
        
        bookDiv.getStyle().remove("background-color");
        studentDiv.getStyle().remove("background-color");
        loanDiv.getStyle().remove("background-color");
        
        switch (route) {
            case "livro" -> bookDiv.getStyle().set("background-color", "var(--blue-color)");
            case "aluno" -> studentDiv.getStyle().set("background-color", "var(--blue-color)");
            case "emprestimo" -> loanDiv.getStyle().set("background-color", "var(--blue-color)");
        }
    }
}
