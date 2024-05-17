package br.com.bibliotec.ui.settings;

import br.com.bibliotec.authentication.SecurityService;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.PageTitleName;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "configuracoes", layout = MainView.class)
public class SettingsPage extends VerticalLayout {

    private final UserController userController;

    public SettingsPage(@Autowired UserController userController) {
        this.userController = userController;
        
        HorizontalLayout container = new HorizontalLayout();
        PageTitleName pageTitle = new PageTitleName("CONFIGURAÇÕES");
        Span account = new Span("CONTA");
        
        account.addClickListener(click -> UI.getCurrent().navigate("/configuracoes/" + getCurrentUserId()));
        container.addClassName("link-container");
        
        Icon icon = LumoIcon.USER.create();

        container.add(icon, account);
        add(pageTitle, container);
    }
    
    private Long getCurrentUserId() {
        SecurityService securityService = new SecurityService();
        User currentUser = userController.loadBYUsername(securityService.getAuthenticatedUser().getUsername());
        
        return currentUser.getId();
    }
}
