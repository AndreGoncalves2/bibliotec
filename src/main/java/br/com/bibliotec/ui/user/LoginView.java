package br.com.bibliotec.ui.user;

import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@PageTitle("Login")
@AnonymousAllowed
@Route(value = "login", layout = MainView.class)
public class LoginView extends VerticalLayout {

    private final TextField txtUser;

    private final PasswordField txtPassword;

    private final UserController controller;

    public LoginView(@Autowired UserController controller) {

        addClassName("default-form-layout");

        this.controller = controller;
        setSizeFull();

        H2 loginTitle = new H2("FAÇA LOGIN");
        Button loginButton = new Button("entrar");
        Button registerButton = new Button("criar conta");

        VerticalLayout form = new VerticalLayout();
        VerticalLayout cardLayout = new VerticalLayout();
        
        txtUser = new TextField();
        txtPassword = new PasswordField();
        
        txtUser.setPrefixComponent(LumoIcon.USER.create());
        txtPassword.setPrefixComponent(VaadinIcon.LOCK.create());
        
        txtUser.setPlaceholder("Usuário");
        txtPassword.setPlaceholder("Senha");
        
        loginButton.addClassName("btn-login");
        loginButton.addClickListener(click -> handleClickLogin());

        form.addClassName("form-layout");
        form.add(txtUser, txtPassword, loginButton, registerButton);

        cardLayout.addClassName("card-default-form");

        cardLayout.add(loginTitle, form);
        add(cardLayout);

        Html formHtml = new Html(
                "<form name=\"f\" th:action=\"@{/login}\" method=\"post\">"
                + "<input type=\"hidden\" id=\"username\" name=\"username\"/>  "
                + "<input type=\"hidden\" id=\"password\" name=\"password\"/>"
                + "</form>");

        add(formHtml);
    }

    private void handleClickLogin() {

        try {
            User user = controller.loadBYUsername(txtUser.getValue());

            if (UserService.matches(txtPassword.getValue(), user.getPassword())) {
                UI.getCurrent().getPage().executeJs("document.getElementById(\"username\").value = \""+user.getUsername()+"\"; "
                        + "document.getElementById(\"password\").value = \""+user.getPassword()+"\"; "
                        + "document.f.submit();");
                UI.getCurrent().navigate("/home");
            } else {
                Notification.show("Usuário ou senha incorreta.").addThemeVariants(NotificationVariant.LUMO_WARNING);
                txtUser.clear();
                txtPassword.clear();
            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            Notification.show("Usuário ou senha incorreta.").addThemeVariants(NotificationVariant.LUMO_WARNING);
        }
    }
}
