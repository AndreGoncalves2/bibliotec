package br.com.bibliotec.ui.config;

import br.com.bibliotec.ui.home.HomePage;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.NavigationTrigger;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "/")
@AnonymousAllowed
public class RouteConfig extends VerticalLayout implements BeforeEnterObserver {

    public RouteConfig() {
        add("Você será redirecionado para a página inicial...");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getTrigger() == NavigationTrigger.PAGE_LOAD) {
            beforeEnterEvent.rerouteTo(HomePage.class);
        }
    }
}