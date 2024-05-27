package br.com.bibliotec.ui.settings;

import br.com.bibliotec.authentication.SecurityService;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.PageTitleName;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@PageTitle("Configurações")
@Route(value = "configuracoes", layout = MainView.class)
public class SettingsPage extends VerticalLayout {

    private final UserController userController;
    
    private Dialog logoutDialog;

    public SettingsPage(@Autowired UserController userController) {
        this.userController = userController;
        

        
        HorizontalLayout containerAccount = new HorizontalLayout();
        HorizontalLayout containerLogout = new HorizontalLayout();
        
        PageTitleName pageTitle = new PageTitleName("CONFIGURAÇÕES");
        Span account = new Span("CONTA");
        Span logout = new Span("SAIR");
        
        account.addClickListener(click -> UI.getCurrent().navigate("/configuracoes/" + getCurrentUserId()));
        logout.addClickListener(click -> logoutDialog.open());
        
        containerAccount.addClassName("link-container");
        containerLogout.addClassName("link-container");
        
        Icon userIcon = LumoIcon.USER.create();
        Icon logoutIcon = VaadinIcon.SIGN_OUT.create();

        containerLogout.add(logoutIcon, logout);
        containerAccount.add(userIcon, account);

        createLogoutDialog();
        
        add(pageTitle, containerAccount, containerLogout);
    }
    
    private Long getCurrentUserId() {
        SecurityService securityService = new SecurityService();
        User currentUser = userController.loadByEmail(securityService.getAuthenticatedUser().getUsername());

        return currentUser.getId();
    }

    private void createLogoutDialog() {
        SecurityService securityService = new SecurityService();
        logoutDialog = new Dialog();

        Button confirmDeleteButton = new Button("Sair");
        Button cancelButton = new Button("Cancelar");

        confirmDeleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        confirmDeleteButton.addClickListener(click -> securityService.logout());
        cancelButton.addClickListener(click -> logoutDialog.close());

        logoutDialog.setHeaderTitle("Confirmar Saída");
        logoutDialog.add("Tem certeza de que deseja sair da sua conta?");

        logoutDialog.getFooter().add(cancelButton, confirmDeleteButton);
    }
}
