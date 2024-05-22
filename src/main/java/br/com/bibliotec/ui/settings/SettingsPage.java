package br.com.bibliotec.ui.settings;

import br.com.bibliotec.authentication.SecurityService;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.PageTitleName;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        
        SecurityService securityService = new SecurityService();
        
        HorizontalLayout containerAccount = new HorizontalLayout();
        HorizontalLayout containerLogout = new HorizontalLayout();
        
        PageTitleName pageTitle = new PageTitleName("CONFIGURAÇÕES");
        Span account = new Span("CONTA");
        Span logout = new Span("SAIR");
        
        account.addClickListener(click -> UI.getCurrent().navigate("/configuracoes/" + getCurrentUserId()));
        logout.addClickListener(click -> securityService.logout());
        
        containerAccount.addClassName("link-container");
        containerLogout.addClassName("link-container");
        
        Icon userIcon = LumoIcon.USER.create();
        Icon logoutIcon = VaadinIcon.SIGN_OUT.create();

        containerLogout.add(logoutIcon, logout);
        containerAccount.add(userIcon, account);
        add(pageTitle, containerAccount, containerLogout);
    }
    
    private Long getCurrentUserId() {
        SecurityService securityService = new SecurityService();
        User currentUser = userController.loadBYUsername(securityService.getAuthenticatedUser().getUsername());
        
        return currentUser.getId();
    }
}
