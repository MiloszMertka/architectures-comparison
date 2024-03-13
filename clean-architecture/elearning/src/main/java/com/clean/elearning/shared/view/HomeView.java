package com.clean.elearning.shared.view;

import com.clean.elearning.shared.service.SecurityService;
import com.clean.elearning.user.domain.Role;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "", layout = MainLayout.class)
@PermitAll
public class HomeView extends VerticalLayout implements BeforeEnterObserver {

    private final SecurityService securityService;

    public HomeView(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (securityService.getCurrentUser().getRole().equals(Role.ADMIN)) {
            beforeEnterEvent.forwardTo("/courses");
            return;
        }
        
        beforeEnterEvent.forwardTo("/my-courses");
    }

}
