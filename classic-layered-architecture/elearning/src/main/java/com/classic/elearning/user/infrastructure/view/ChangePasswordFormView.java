package com.classic.elearning.user.infrastructure.view;

import com.classic.elearning.shared.view.HomeView;
import com.classic.elearning.shared.view.MainLayout;
import com.classic.elearning.user.adapter.dto.ChangePasswordRequest;
import com.classic.elearning.user.adapter.ui.ChangePasswordFormUI;
import com.classic.elearning.user.adapter.ui.presenter.ChangePasswordFormPresenter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.lang.NonNull;

@Route(value = "change-password", layout = MainLayout.class)
@PageTitle("Change password")
@PermitAll
public class ChangePasswordFormView extends VerticalLayout implements ChangePasswordFormUI {

    private final ChangePasswordFormPresenter changePasswordFormPresenter;
    private final BeanValidationBinder<ChangePasswordRequest> binder = new BeanValidationBinder<>(ChangePasswordRequest.class);
    private final TextField currentPassword = new TextField("Current password");
    private final TextField newPassword = new TextField("New password");
    private final Button changePasswordButton = new Button("Change password");

    public ChangePasswordFormView(ChangePasswordFormPresenter changePasswordFormPresenter) {
        this.changePasswordFormPresenter = changePasswordFormPresenter;
        changePasswordFormPresenter.setChangePasswordFormUI(this);
        binder.bindInstanceFields(this);
        binder.setBean(new ChangePasswordRequest());
        final var content = createContent();
        add(content);
    }

    @Override
    public boolean isFormValid() {
        return binder.validate().isOk();
    }

    @Override
    public void navigateToHomeView() {
        getUI().ifPresent(ui -> ui.navigate(HomeView.class));
    }

    @Override
    public void showErrorMessage(@NonNull String message) {
        final var errorNotification = new Notification(message, 3000);
        errorNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorNotification.open();
    }

    private Component createContent() {
        final var heading = new H1("Change password");
        final var fieldsLayout = createFieldsLayout();
        final var buttonsLayout = createButtonsLayout();
        return new VerticalLayout(heading, fieldsLayout, buttonsLayout);
    }

    private Component createFieldsLayout() {
        return new FormLayout(currentPassword, newPassword);
    }

    private Component createButtonsLayout() {
        changePasswordButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        changePasswordButton.addClickShortcut(Key.ENTER);
        changePasswordButton.addClickListener(click -> changePasswordFormPresenter.handleChangePasswordButtonClick(binder.getBean()));
        return new HorizontalLayout(changePasswordButton);
    }

}
