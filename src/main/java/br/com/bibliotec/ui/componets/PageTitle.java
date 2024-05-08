package br.com.bibliotec.ui.componets;


import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@StyleSheet("components/pageTitle.css")
public class PageTitle extends VerticalLayout {

    private Div pageTitleContainer;

    public PageTitle(String title) {
        setAlignItems(Alignment.CENTER);
        pageTitleContainer = new Div();
        H2 pageTitle = new H2(title);

        pageTitleContainer.add(pageTitle);
        pageTitleContainer.addClassName("div-page-title");
        
        HorizontalLayout headerTitle = createPageTitle();
        add(headerTitle, pageTitleContainer);
    }

    private HorizontalLayout createPageTitle() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addClassName("school-name-container");

        H1 start = new H1("BIBLIOTECA DIGITAL DA");
        H1 end = new H1("ESCOLA MUNICIPAL JAGS");

        start.addClassName("title-start");
        end.addClassName("title-end");

        layout.add(start, end);
        return layout;
    }
    
    public void enableTitleContainer(Boolean enableTitleContainer) {
        pageTitleContainer.setVisible(enableTitleContainer);
    }
}
