package br.com.bibliotec.ui.settings;

import br.com.bibliotec.anotation.Bind;
import br.com.bibliotec.authentication.SecurityService;
import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.componets.ErrorDialog;
import br.com.bibliotec.ui.componets.GenericForm;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "configuracoes", layout = MainView.class)
public class UserSettings extends GenericForm<User, UserController, Long> {
    
    @Bind("username")
    private final TextField txtUserame;

    @Bind("email")
    private final TextField txtEmail;
    
    private final PasswordField txtNewPassword;
    private final PasswordField txtConfirmNewPassword;
    private final PasswordField txtOldPassword;
    
    private final UserController userController;

    public UserSettings(@Autowired UserController userController) {
        super(userController, User.class);
        this.userController = userController;

        setTitleParameter("USUÁRIO");
        
        addClassName("user-settings");
        setButtonDeleteVisible(false);

        VerticalLayout cardLayout = new VerticalLayout();
        VerticalLayout formContent = new VerticalLayout();
        
        H2 settingsAccount = new H2("EDITAR INFORMAÇÕES");
        txtUserame = new TextField();
        txtEmail = new TextField();
        txtNewPassword = new PasswordField();
        txtConfirmNewPassword = new PasswordField();
        txtOldPassword = new PasswordField();

        txtUserame.setPlaceholder("nome");
        txtEmail.setPlaceholder("e-mail");
        txtNewPassword.setPlaceholder("nova senha");
        txtConfirmNewPassword.setPlaceholder("confirme sua nova senha");
        txtOldPassword.setPlaceholder("Senha atual");

        txtUserame.setPrefixComponent(LumoIcon.USER.create());
        txtEmail.setPrefixComponent(VaadinIcon.ENVELOPE.create());
        txtNewPassword.setPrefixComponent(VaadinIcon.LOCK.create());
        txtConfirmNewPassword.setPrefixComponent(VaadinIcon.LOCK.create());
        txtOldPassword.setPrefixComponent(VaadinIcon.LOCK.create());
        
        txtOldPassword.setRequiredIndicatorVisible(true);
        txtConfirmNewPassword.setRequiredIndicatorVisible(true);
        
        cardLayout.addClassName("card-default-form");
        formContent.addClassName("form-layout");

        txtUserame.setReadOnly(true);
        txtConfirmNewPassword.addBlurListener(event -> checkPasswordIsEqual());
        
        cardLayout.add(settingsAccount);
        getDivTop().add(cardLayout);

        formContent.add(txtUserame, txtEmail, txtOldPassword, txtNewPassword, txtConfirmNewPassword);
        getFormLayout().add(formContent);
        
        setDefaultRoute("/configuracoes");
    }

    private boolean checkPasswordIsEqual() {
        if (!txtNewPassword.getValue().equals(txtConfirmNewPassword.getValue())) {
            txtConfirmNewPassword.setInvalid(true);
            txtConfirmNewPassword.setErrorMessage("As senhas não conferem");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void handleSaveButton()  {
        SecurityService securityService = new SecurityService();
        User currentUser = userController.loadBYUsername(securityService.getAuthenticatedUser().getUsername());
        
        if (!checkPasswordIsEqual()) {
            try {
                throw new BibliotecException("Por favor, confirme que a nova senha e sua confirmação coincidem.");
            } catch (BibliotecException e) {
                ErrorDialog.show("Ops!", e.getMessage());
                return;
            }
        }
        
        if (UserService.matches(txtOldPassword.getValue(), currentUser.getPassword())) {
            String encodePassword = UserService.encodePassword(txtNewPassword.getValue());
            getBinder().getValue().setPassword(encodePassword);
            
            super.handleSaveButton();
        } else {
            try {
                throw new BibliotecException("Senha atual incorreta.");
            } catch (BibliotecException e) {
                ErrorDialog.show("Ops!", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
