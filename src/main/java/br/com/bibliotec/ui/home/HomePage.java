package br.com.bibliotec.ui.home;

import br.com.bibliotec.authentication.SecurityService;
import br.com.bibliotec.authentication.UserService;
import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.controller.UserController;
import br.com.bibliotec.exeption.BibliotecException;
import br.com.bibliotec.model.User;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.book.BookFormDialog;
import br.com.bibliotec.ui.book.BookGrid;
import br.com.bibliotec.ui.bookloan.BookLoanFormDialog;
import br.com.bibliotec.ui.bookloan.BookLoanGrid;
import br.com.bibliotec.ui.componets.PageTitleName;
import br.com.bibliotec.ui.student.StudentFormDialog;
import br.com.bibliotec.ui.student.StudentGrid;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@PageTitle("Início")
@Route(value = "/home", layout = MainView.class)
public class HomePage extends VerticalLayout {
    
    private final BookController bookController;
    private final BookLoanController bookLoanController;
    private final StudentController studentController;
    private final UserController userController;

    public HomePage(@Autowired BookController bookController,
                    @Autowired BookLoanController bookLoanController,
                    @Autowired StudentController studentController,
                    @Autowired UserController userController) throws BibliotecException {
        this.bookController = bookController;
        this.bookLoanController = bookLoanController;
        this.studentController = studentController;
        this.userController = userController;
        
        PageTitleName pageTitle = new PageTitleName("");
        
        
        createHeader();
        createCards();
        createFastActionsText();
        createFastActions();
        
        pageTitle.enableTitleContainer(false);
        addComponentAsFirst(pageTitle);
    }
    
    private void createFastActionsText() {
        HorizontalLayout fastActionsLayout = new HorizontalLayout();
        fastActionsLayout.addClassName("fast-actions-message-layout");

        H2 fast = new H2("AÇÕES");
        H2 action = new H2("RÁPIDAS:");

        fastActionsLayout.add(fast, action);
        add(fastActionsLayout);
    }
    
    private void createFastActions() throws  BibliotecException {
        BookFormDialog bookForm = new BookFormDialog(bookController, userController);
        BookLoanFormDialog bookLoanForm = new BookLoanFormDialog(bookLoanController, bookController, studentController, userController);
        StudentFormDialog studentForm = new StudentFormDialog(studentController, userController);
        
        VerticalLayout fastActionsWrapper = new VerticalLayout();
        fastActionsWrapper.addClassName("fast-actions-layout");
              
        HorizontalLayout actionBookDiv = new HorizontalLayout();
        HorizontalLayout actionStudentDiv = new HorizontalLayout();
        HorizontalLayout actionBookLoanDiv = new HorizontalLayout();

        actionBookDiv.addClassName("fast-action");
        actionStudentDiv.addClassName("fast-action");
        actionBookLoanDiv.addClassName("fast-action");
        
        H4 actionBook = new H4("ADICIONAR LIVRO");
        H4 actionStudent = new H4("REGISTRAR ALUNO");
        H4 actionBookLoan = new H4("REGISTRAR EMPRÉSTIMO");
        
        Div actionBookIconDiv = new Div(LumoIcon.PLUS.create());
        Div actionStudentIconDiv = new Div(LumoIcon.PLUS.create());
        Div actionBookLoanIconDiv = new Div(LumoIcon.PLUS.create());

        actionBookDiv.addClickListener(click -> bookForm.open());
        actionStudentDiv.addClickListener(click -> studentForm.open());
        actionBookLoanDiv.addClickListener(click -> bookLoanForm.open());
        
        actionBookDiv.add(actionBook, actionBookIconDiv);
        actionStudentDiv.add(actionStudent, actionStudentIconDiv);
        actionBookLoanDiv.add(actionBookLoan, actionBookLoanIconDiv);

        fastActionsWrapper.add(actionBookDiv, actionStudentDiv, actionBookLoanDiv);
        add(fastActionsWrapper);
    }
    
    private void createHeader() {
        Div messageWrapper = new Div();
        
        H5 welcomeMessage = new H5("Bem-vindo, " + getUsername());
        H4 message = new H4("CONFIRA O STATUS ATUAL DA BIBLIOTECA :");
        
        messageWrapper.addClassName("message-wrapper");
        messageWrapper.add(message);
        
        add(welcomeMessage, messageWrapper);
    }

    private String getUsername() {
        UserService userService = new UserService(userController);
        SecurityService securityService = new SecurityService();
        
        User userLogged = userService.getLoggedUser();
        String username = "";
        
        if (userLogged == null) {
            securityService.logout();
            UI.getCurrent().navigate("/login");
        } else{
            username = userLogged.getName();
        }
        
        return username;
    }

    private void createCards() {
        UI currentUI = UI.getCurrent();
        
        HorizontalLayout cardsWrapper = new HorizontalLayout();
        cardsWrapper.addClassName("cards-wrapper");
        
        Div bookCard = new Div();
        Div studentCard = new Div();
        Div bookLoanCard = new Div();
        
        bookCard.addClickListener(click -> currentUI.navigate(BookGrid.class));
        studentCard.addClickListener(click -> currentUI.navigate(StudentGrid.class));
        bookLoanCard.addClickListener(click -> currentUI.navigate(BookLoanGrid.class));
        
        bookCard.addClassName("home-cards");
        studentCard.addClassName("home-cards");
        bookLoanCard.addClassName("home-cards");
        
        Div bookIcon = new Div(VaadinIcon.OPEN_BOOK.create());
        Div studentIcon = new Div(VaadinIcon.NURSE.create());
        Div bookLoanIcon = new Div(VaadinIcon.DOWNLOAD_ALT.create());
        
        H4 bookMessage = new H4("LIVROS REGISTRADOS");
        H4 studentMessage = new H4("ALUNOS LEITORES");
        H4 bookLoanMessage = new H4("LIVROS EMPRESTADOS");
        
        bookCard.add(bookIcon, bookMessage);
        studentCard.add(studentIcon, studentMessage);
        bookLoanCard.add(bookLoanIcon, bookLoanMessage);

        cardsWrapper.add(bookCard, studentCard, bookLoanCard);
        add(cardsWrapper);
    }
}
