package br.com.bibliotec.ui.user;

import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.ErrorDialog;
import br.com.bibliotec.ui.componets.PageTitleName;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
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
        this.controller = controller;

        setSizeFull();

        H2 loginTitle = new H2("FAÇA LOGIN");
        Button loginButton = new Button("Entrar");
        Button registerButton = new Button("Criar conta");
        PageTitleName pageTitleName = new PageTitleName("");

        VerticalLayout form = new VerticalLayout();
        VerticalLayout cardLayout = new VerticalLayout();
        
        txtUser = new TextField();
        txtPassword = new PasswordField();
        
        txtUser.setPrefixComponent(LumoIcon.USER.create());
        txtPassword.setPrefixComponent(VaadinIcon.LOCK.create());
        pageTitleName.enableTitleContainer(false);
        
        txtUser.setPlaceholder("E-mail");
        txtPassword.setPlaceholder("Senha");
        
        loginButton.addClassName("btn-login");
        
        loginButton.addClickListener(click -> handleClickLogin());
        loginButton.addClickShortcut(Key.ENTER);
        
        registerButton.addClickListener(click -> UI.getCurrent().navigate("/registrar"));

        addClassName("default-form-layout");
        form.addClassName("form-layout");
        cardLayout.addClassName("card-default-form");
        
        form.add(txtUser, txtPassword, loginButton, registerButton);
        cardLayout.add(loginTitle, form);
        add(pageTitleName, cardLayout);

        Html formHtml = new Html(
                "<form name=\"f\" th:action=\"@{/login}\" method=\"post\">"
                + "<input type=\"hidden\" id=\"username\" name=\"username\"/>  "
                + "<input type=\"hidden\" id=\"password\" name=\"password\"/>"
                + "</form>");

        add(formHtml);
    }

    private void handleClickLogin() {

        try {
            User user = controller.loadByEmail(txtUser.getValue());

            if (UserService.matches(txtPassword.getValue(), user.getPassword())) {
                UI.getCurrent().getPage().executeJs("document.getElementById(\"username\").value = \""+user.getEmail()+"\"; "
                        + "document.getElementById(\"password\").value = \""+user.getPassword()+"\"; "
                        + "document.f.submit();");
                UI.getCurrent().navigate("/home");
            } else {
                throw new UsernameNotFoundException("Usuário ou senha incorreta.");
            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            ErrorDialog.show("Ops!", "Parece que houve um erro com seu usuário ou senha. Por favor, confira e tente de novo.");
            txtUser.clear();
            txtPassword.clear();
        }
    }
}
