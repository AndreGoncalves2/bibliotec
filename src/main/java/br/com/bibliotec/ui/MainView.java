package br.com.bibliotec.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainView extends AppLayout {
    
    public MainView() {

        DrawerToggle toggle = new DrawerToggle();

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.addClassName("main-view-scroller-1");
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle);
        addClassName("main-view-app-layout-1");
    }

    private SideNav getSideNav() {
        SideNav sideNav = new SideNav();
        sideNav.addItem(
                new SideNavItem("Livro", "/livro",
                        VaadinIcon.BOOK.create()),
                new SideNavItem("Empréstimo", "/emprestimo",
                        VaadinIcon.EXCHANGE.create()),
                new SideNavItem("Aluno", "/aluno",
                        VaadinIcon.NURSE.create())
        );
        
        return sideNav;
    }
}
