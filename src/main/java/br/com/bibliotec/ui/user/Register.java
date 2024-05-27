package br.com.bibliotec.ui.user;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.ErrorDialog;
import br.com.bibliotec.ui.componets.PageTitleName;
import br.com.bibliotec.ui.helper.Binder;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoIcon;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@PageTitle("Registrar")
@Route(value = "registrar", layout = MainView.class)
public class Register extends VerticalLayout {
    
    @Bind("name")
    private final TextField txtName;
    
    @Bind("email")
    private final TextField txtEmail;
    
    @Bind("password")
    private final PasswordField txtPassword;
    
    private final PasswordField txtConfirmPassword;

    private final Binder<User, Long, UserController> binder;
    private final UserController controller;

    public Register(@Autowired UserController controller) throws IllegalAccessException {
        binder = new Binder<>(User.class, this, controller);
        this.controller = controller;
        
        PageTitleName pageTitle = new PageTitleName("");
        pageTitle.enableTitleContainer(false);
        
        H2 registerTitle = new H2("CRIE SUA CONTA");
        Button registerButton = new Button("Criar conta");
        VerticalLayout form = new VerticalLayout();
        VerticalLayout cardLayout = new VerticalLayout();
        
        txtName = new TextField();
        txtEmail = new TextField();
        txtPassword = new PasswordField();
        txtConfirmPassword  = new PasswordField();
        
        txtName.setPlaceholder("Nome *");
        txtEmail.setPlaceholder("E-mail *");
        txtPassword.setPlaceholder("Senha *");
        txtConfirmPassword.setPlaceholder("Confirme sua senha *");

        txtName.setPrefixComponent(LumoIcon.USER.create());
        txtEmail.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        txtPassword.setPrefixComponent(VaadinIcon.LOCK.create());
        txtConfirmPassword.setPrefixComponent(VaadinIcon.LOCK.create());
        
        txtName.setRequiredIndicatorVisible(true);
        txtName.setRequired(true);
        
        txtConfirmPassword.setRequiredIndicatorVisible(true);
        
        binder.createBinder();
        
        txtConfirmPassword.addBlurListener(event -> checkPasswordIsEqual());
        registerButton.addClickListener(click -> clickRegister());
        registerButton.addClickShortcut(Key.ENTER);
        
        addClassName("default-form-layout");
        form.addClassName("form-layout");
        cardLayout.addClassName("card-default-form");
        
        configurePasswordFields();

        form.add(txtName, txtEmail, txtPassword, txtConfirmPassword, registerButton);
        cardLayout.add(registerTitle, form);
        add(pageTitle,cardLayout);
    }
    
    private void configurePasswordFields() {
        txtConfirmPassword.setErrorMessage("As senhas não conferem");
    }

    private boolean checkPasswordIsEqual() {
        if (!txtPassword.getValue().equals(txtConfirmPassword.getValue())) {
            txtConfirmPassword.setInvalid(true);
            return false;
        } else {
            return true;
        }
    }

    private void clickRegister() {
        User user= new User();

        try {
            binder.writeBean(user);
        } catch (ValidationException e) {
            ErrorDialog.show("Ops!", "Por favor, verifique os campos preenchidos e tente novamente.");
            e.printStackTrace();
        }
        
        if (binder.isValid() && checkPasswordIsEqual()) {
            String encodePassword = UserService.encodePassword(user.getPassword());
            user.setPassword(encodePassword);

            try {
                controller.save(user);
                UI.getCurrent().navigate("/login");
            } catch (BibliotecException e) {
                ErrorDialog.show("Ops!", e.getMessage());
            }
        }
    }
}