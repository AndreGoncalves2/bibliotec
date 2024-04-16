package br.com.bibliotec.ui.componets;


import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;

@StyleSheet("components/pageTitle.css")
public class PageTitle extends Div {

    public PageTitle(String title) {
        addClassName("div-page-title");
        
        H2 pageTitle = new H2(title);
        add(pageTitle);
    }
}
