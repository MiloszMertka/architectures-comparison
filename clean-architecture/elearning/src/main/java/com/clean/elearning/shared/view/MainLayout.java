package com.clean.elearning.shared.view;

import com.clean.elearning.course.infrastructure.view.AdminCourseListView;
import com.clean.elearning.course.infrastructure.view.CourseListView;
import com.clean.elearning.shared.service.SecurityService;
import com.clean.elearning.user.domain.Role;
import com.clean.elearning.user.infrastructure.view.ChangePasswordFormView;
import com.clean.elearning.user.infrastructure.view.UserListView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

import static com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import static com.vaadin.flow.theme.lumo.LumoUtility.*;

public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        final var drawerToggle = new DrawerToggle();
        final var logo = createLogo();
        final var changePasswordButton = createChangePasswordButton();
        final var logoutButton = createLogoutButton();

        final var header = new HorizontalLayout(drawerToggle, logo, changePasswordButton, logoutButton);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(Padding.Vertical.NONE, Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private Component createLogo() {
        final var logo = new H1("Elearning");
        logo.addClassNames(FontSize.LARGE, Margin.MEDIUM);
        return logo;
    }

    private Component createChangePasswordButton() {
        final var changePasswordButton = new Button("Change password");
        changePasswordButton.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate(ChangePasswordFormView.class)));
        return changePasswordButton;
    }

    private Component createLogoutButton() {
        final var logoutButton = new Button("Logout");
        logoutButton.addClickListener(event -> securityService.logout());
        return logoutButton;
    }

    private void createDrawer() {
        final var links = createLinks();
        addToDrawer(links);
    }

    private Component createLinks() {
        final var links = new VerticalLayout();

        if (securityService.getCurrentUser().getRole().equals(Role.ADMIN)) {
            links.add(
                    new RouterLink("Courses", AdminCourseListView.class),
                    new RouterLink("Users", UserListView.class)
            );
        } else {
            links.add(
                    new RouterLink("My courses", CourseListView.class)
            );
        }

        return links;
    }

}
