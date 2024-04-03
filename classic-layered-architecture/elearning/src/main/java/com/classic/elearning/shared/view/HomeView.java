package com.classic.elearning.shared.view;

import com.classic.elearning.user.domain.Role;
import com.classic.elearning.user.domain.User;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class HomeView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticationContext authenticationContext;

    public HomeView(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        final var user = authenticationContext.getAuthenticatedUser(User.class).orElseThrow();
        if (user.getRole().equals(Role.ADMIN)) {
            beforeEnterEvent.forwardTo("/courses");
            return;
        }

        beforeEnterEvent.forwardTo("/my-courses");
    }

}
