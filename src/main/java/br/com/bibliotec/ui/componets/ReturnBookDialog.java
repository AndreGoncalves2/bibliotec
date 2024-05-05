package br.com.bibliotec.ui.componets;

import br.com.bibliotec.controller.BookLoanController;
import br.com.bibliotec.exception.CodeIncorrectException;
import br.com.bibliotec.interfaces.RefreshListener;
import br.com.bibliotec.model.BookLoan;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;

public class ReturnBookDialog extends Dialog {
    
    private final TextField txtBookCode;
    private final Button confirmButton;
    private final Button cancelButton;
    
    public ReturnBookDialog(BookLoan bookLoan, BookLoanController bookLoanController, RefreshListener refreshListener) {
        
        setHeaderTitle("Devolução de livro");
        txtBookCode = new TextField("Código do livro");
        
        confirmButton = new Button("SALVAR");
        confirmButton.addClickListener(click -> {
            try {
                bookLoanController.setReturned(bookLoan, txtBookCode.getValue());
                refreshListener.refresh();
                close();
            } catch (CodeIncorrectException e) {
                Notification.show(e.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
                e.printStackTrace();
            }
        });

        cancelButton= new Button("Cancelar");
        cancelButton.addClickListener(click -> close());
        
        add(txtBookCode);
        getFooter().add(cancelButton, confirmButton);
        open();
    }
}
