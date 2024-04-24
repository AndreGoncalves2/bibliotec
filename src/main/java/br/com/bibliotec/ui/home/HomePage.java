package br.com.bibliotec.ui.home;

import br.com.bibliotec.controller.BookController;
import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.controller.StudentController;
import br.com.bibliotec.ui.MainView;
import br.com.bibliotec.ui.book.BookForm;
import br.com.bibliotec.ui.book.BookGrid;
import br.com.bibliotec.ui.bookloan.BookLoanForm;
import br.com.bibliotec.ui.bookloan.BookLoanGrid;
import br.com.bibliotec.ui.student.StudentForm;
import br.com.bibliotec.ui.student.StudentGrid;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "/", layout = MainView.class)
public class HomePage extends VerticalLayout {
    
    private BookController bookController;
    private BookLoanController bookLoanController;
    private StudentController studentController;

    public HomePage(@Autowired BookController bookController,
                    @Autowired BookLoanController bookLoanController,
                    @Autowired StudentController studentController) throws IllegalAccessException {
        this.bookController = bookController;
        this.bookLoanController = bookLoanController;
        this.studentController = studentController;
        
        
        createHeader();
        createCards();
        createFastActionsText();
        createFastActions();
    }
    
    private void createFastActionsText() {
        HorizontalLayout fastActionsLayout = new HorizontalLayout();
        fastActionsLayout.addClassName("fast-actions-message-layout");

        H2 fast = new H2("AÇÕES");
        H2 action = new H2("RÁPIDAS:");

        fastActionsLayout.add(fast, action);
        add(fastActionsLayout);
    }
    
    private void createFastActions() throws IllegalAccessException {
        BookForm bookForm = new BookForm(bookController, null);
        BookLoanForm bookLoanForm = new BookLoanForm(bookLoanController, bookController, studentController, null);
        StudentForm studentForm = new StudentForm(studentController, null);
        
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
        
        actionBookIconDiv.addClickListener(click -> bookForm.open());
        actionStudentIconDiv.addClickListener(click -> studentForm.open());
        actionBookLoanIconDiv.addClickListener(click -> bookLoanForm.open());
        
        actionBookDiv.add(actionBook, actionBookIconDiv);
        actionStudentDiv.add(actionStudent, actionStudentIconDiv);
        actionBookLoanDiv.add(actionBookLoan, actionBookLoanIconDiv);

        fastActionsWrapper.add(actionBookDiv, actionStudentDiv, actionBookLoanDiv);
        add(fastActionsWrapper);
    }
    
    private void createHeader() {
        H5 welcomeMessage = new H5("[MENSAGEM DE BOAS-VINDAS E BREVE DESCRIÇÃO DA BIBLIOTECA]");
        H4 message = new H4("CONFIRA O STATUS ATUAL DA BIBLIOTECA :");

        Div messageWrapper = new Div();
        
        messageWrapper.addClassName("message-wrapper");
        messageWrapper.add(message);
        
        add(welcomeMessage, messageWrapper);
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
