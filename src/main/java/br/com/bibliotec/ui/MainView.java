package br.com.bibliotec.ui;

import br.com.bibliotec.ui.book.BookGrid;
import br.com.bibliotec.ui.bookloan.BookLoanGrid;
import br.com.bibliotec.ui.home.HomePage;
import br.com.bibliotec.ui.settings.SettingsPage;
import br.com.bibliotec.ui.student.StudentGrid;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.HashMap;
import java.util.Map;

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
        HorizontalLayout layoutButtons = createHeader();
        
        tileContainer.setClassName("title-container");
        
        H4 title = new H4("BIBLIOTECA ESCOLA MUNICIPAL JOSÉ AUGUSTO GAMA DE SOUZA");
        
        tileContainer.add(title);
        
        container.add(tileContainer, layoutButtons);
        addToNavbar(container);
    }
    
    private HorizontalLayout createHeader() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("buttons-container");
        
        startDiv = new Div();
        bookDiv = new Div();
        studentDiv = new Div();
        loanDiv = new Div();
        configDiv = new Div();

        startDiv.setClassName("div-links-start");
        bookDiv.setClassName("div-links-right");
        studentDiv.setClassName("div-links-right");
        loanDiv.setClassName("div-links-right");
        configDiv.setClassName("div-link-config");
        
        RouterLink startLink = new RouterLink("INÍCIO", HomePage.class);
        RouterLink bookLink = new RouterLink("LIVROS", BookGrid.class);
        RouterLink studentLink = new RouterLink("ALUNOS", StudentGrid.class);
        RouterLink loanLink = new RouterLink("EMPRÉSTIMOS", BookLoanGrid.class);
        RouterLink configLink = new RouterLink("CONFIGURAÇÕES", SettingsPage.class);
        
        startDiv.add(startLink);
        bookDiv.add(bookLink);
        studentDiv.add(studentLink);
        loanDiv.add(loanLink);
        configDiv.add(configLink);
        
        HorizontalLayout endDiv = new HorizontalLayout();
        endDiv.setClassName("div-links-container-end");

        endDiv.add(bookDiv);
        endDiv.add(studentDiv);
        endDiv.add(loanDiv);
        endDiv.add(configDiv);

        layout.add(startDiv, endDiv);
        
        return layout;
    }
    
   

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String route = event.getLocation().getPath();
        
        Map<String, Div> routeToDivMap = new HashMap<>();
        routeToDivMap.put("livro", bookDiv);
        routeToDivMap.put("aluno", studentDiv);
        routeToDivMap.put("emprestimo", loanDiv);
        routeToDivMap.put("home", startDiv);
        routeToDivMap.put("configuracoes", configDiv);
        
        routeToDivMap.values().forEach(div -> div.removeClassName("div-selected"));

        routeToDivMap.entrySet().stream()
                .filter(entry -> route.contains(entry.getKey()))
                .findFirst()
                .ifPresent(entry -> entry.getValue().addClassName("div-selected"));
    }
}
