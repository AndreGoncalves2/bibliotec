package br.com.bibliotec.ui.componets;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ErrorDialog extends Div {

    public static void show(String title, String message) {
        Dialog dialog = new Dialog();
        H2 headline = new H2(title);
        
        dialog.addClassName("error-dialog");

        Paragraph paragraph = new Paragraph(message);

        Button closeButton = new Button("Fechar");
        closeButton.addClickListener(e -> dialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(headline, paragraph);
        dialog.add(dialogLayout);
        dialog.getFooter().add(closeButton);
        dialog.open();
    }
}
